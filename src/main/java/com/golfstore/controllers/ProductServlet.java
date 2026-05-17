package com.golfstore.controllers;

import com.golfstore.model.ProductModel;
import com.golfstore.model.CategoryModel;
import com.golfstore.config.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.RequestDispatcher;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductServlet.java
 * Central MVC Controller for all Product CRUD operations.
 * Mapped to URL pattern /products
 *
 * A 'action' request parameter drives which operation runs:
 *   action=list   -> READ   -> loads all products -> productList.jsp
 *   action=add    -> CREATE -> shows add form     -> addProduct.jsp
 *   action=edit   -> UPDATE -> shows edit form    -> editProduct.jsp
 *   action=delete -> DELETE -> removes product   -> redirects to list
 *
 * All database access uses the 6-step JDBC pattern:
 *   1. Load Driver (handled by DBConnection)
 *   2. Get Connection
 *   3. Create PreparedStatement
 *   4. Execute Query
 *   5. Process ResultSet
 *   6. Close resources (finally block)
 *
 * Security: Every method checks for a valid HttpSession first.
 * Unauthenticated users are redirected to /login immediately.
 */
@SuppressWarnings("serial")
@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    // doGet() — handles READ operations and loading form pages
    // Called when user navigates to a URL (not a form submit)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- STEP 1: Session Security Guard ---
        // getSession(false) never creates a new session — returns null
        // if no valid session exists. This prevents unauthenticated access.
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // --- STEP 2: Read the 'action' parameter from the URL ---
        // e.g. /products?action=list or /products?action=edit&id=3
        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; // default action
        }

        // --- STEP 3: Route to the correct operation ---
        switch (action) {
            case "list":
                listProducts(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteProduct(request, response);
                break;
            default:
                listProducts(request, response);
                break;
        }
    }

    // doPost() — handles CREATE and UPDATE form submissions
    // Called when a JSP form with method="post" is submitted
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- Session Security Guard ---
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // --- Route POST actions ---
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "add":
                addProduct(request, response);
                break;
            case "edit":
                updateProduct(request, response);
                break;
            default:
                listProducts(request, response);
                break;
        }
    }

    // PRIVATE HELPER METHODS — one per CRUD operation

    /**
     * READ — Fetches all products with their category names via
     * a JOIN query, builds a List of ProductModel objects,
     * sets it as a request attribute, and forwards to the JSP.
     */
    private void listProducts(HttpServletRequest request,
                              HttpServletResponse response)
            throws ServletException, IOException {

        // List to hold all product objects fetched from DB
        List<ProductModel> productList = new ArrayList<>();

        // JDBC resources declared outside try for finally-block closing
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // JDBC Step 2: Get connection from our centralised factory
            conn = DBConnection.getConnection();

            // JDBC Step 3: Prepare SQL — JOIN to get category name too
            // We use p.* to get all product columns
            String sql = "SELECT p.id, p.name, p.description, p.price, " +
                         "p.stock_quantity, p.category_id, p.image_path, " +
                         "c.name AS category_name " +
                         "FROM products p " +
                         "LEFT JOIN categories c ON p.category_id = c.id " +
                         "ORDER BY p.id DESC";

            ps = conn.prepareStatement(sql);

            // JDBC Step 4: Execute the SELECT query
            rs = ps.executeQuery();

            // JDBC Step 5: Process each row in the ResultSet
            while (rs.next()) {
                ProductModel product = new ProductModel(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("stock_quantity"),
                    rs.getInt("category_id"),
                    rs.getString("image_path")
                );
                productList.add(product);
            }

            // Bind the category name separately as a convenience
            // We store it in the request using a parallel approach
            // The JSP will use ${productList} to iterate

        } catch (SQLException e) {
            // Log the real error server-side, show friendly message to user
            e.printStackTrace();
            request.setAttribute("errorMessage", "Could not load products. Please try again.");
        } finally {
            // JDBC Step 6: Always close resources to prevent memory leaks
            closeResources(conn, ps, rs);
        }

        // Bind the list to the request scope so the JSP can access it
        request.setAttribute("productList", productList);

        // Also load categories for the filter dropdown on the list page
        loadCategories(request);

        // Forward to the View — never redirect here (we need request attributes)
        RequestDispatcher dispatcher = request.getRequestDispatcher(
            "/WEB-INF/pages/productList.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * CREATE (GET) — Loads the empty Add Product form.
     * Must load categories list so the dropdown is populated.
     */
    private void showAddForm(HttpServletRequest request,
                             HttpServletResponse response)
            throws ServletException, IOException {

        // Load categories into request scope for the <select> dropdown
        loadCategories(request);

        RequestDispatcher dispatcher = request.getRequestDispatcher(
            "/WEB-INF/pages/addProduct.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * CREATE (POST) — Receives form data from addProduct.jsp,
     * validates it, and inserts a new row into the products table.
     */
    private void addProduct(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException {

        // --- Extract and trim all form parameters ---
        String name        = request.getParameter("name").trim();
        String description = request.getParameter("description").trim();
        String priceStr    = request.getParameter("price").trim();
        String stockStr    = request.getParameter("stock_quantity").trim();
        String catIdStr    = request.getParameter("category_id").trim();
        String imagePath   = request.getParameter("image_path").trim();

        // --- Server-side validation ---
        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()
                || catIdStr.isEmpty()) {
            request.setAttribute("errorMessage",
                "Name, price, stock quantity, and category are required.");
            loadCategories(request);
            RequestDispatcher dispatcher = request.getRequestDispatcher(
                "/WEB-INF/pages/addProduct.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // --- Parse numeric values safely ---
        double price;
        int stockQuantity;
        int categoryId;

        try {
            price         = Double.parseDouble(priceStr);
            stockQuantity = Integer.parseInt(stockStr);
            categoryId    = Integer.parseInt(catIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage",
                "Price and stock quantity must be valid numbers.");
            loadCategories(request);
            RequestDispatcher dispatcher = request.getRequestDispatcher(
                "/WEB-INF/pages/addProduct.jsp");
            dispatcher.forward(request, response);
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // JDBC Step 2: Get connection
            conn = DBConnection.getConnection();

            // JDBC Step 3: Parameterized INSERT — never string concatenation
            String sql = "INSERT INTO products " +
                         "(name, description, price, stock_quantity, " +
                         "category_id, image_path) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";

            ps = conn.prepareStatement(sql);

            // JDBC Step 4: Bind parameters to the ? placeholders
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setDouble(3, price);
            ps.setInt(4, stockQuantity);
            ps.setInt(5, categoryId);
            ps.setString(6, imagePath.isEmpty() ? null : imagePath);

            // JDBC Step 5: Execute the INSERT
            ps.executeUpdate();

            // Success — redirect to list (PRG pattern prevents double submit)
            response.sendRedirect(request.getContextPath() +
                "/products?action=list&success=Product+added+successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage",
                "Could not add product. Please try again.");
            loadCategories(request);
            RequestDispatcher dispatcher = request.getRequestDispatcher(
                "/WEB-INF/pages/addProduct.jsp");
            dispatcher.forward(request, response);
        } finally {
            // JDBC Step 6: Close resources
            closeResources(conn, ps, null);
        }
    }

    /**
     * UPDATE (GET) — Loads the Edit form pre-filled with
     * the existing product data for the given id parameter.
     */
    private void showEditForm(HttpServletRequest request,
                              HttpServletResponse response)
            throws ServletException, IOException {

        // Get the product ID from the URL: /products?action=edit&id=5
        String idStr = request.getParameter("id");

        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() +
                "/products?action=list");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() +
                "/products?action=list");
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ProductModel product = null;

        try {
            conn = DBConnection.getConnection();

            // SELECT the single product row by its primary key
            String sql = "SELECT * FROM products WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);
            rs = ps.executeQuery();

            if (rs.next()) {
                product = new ProductModel(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("stock_quantity"),
                    rs.getInt("category_id"),
                    rs.getString("image_path")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        if (product == null) {
            // Product not found — go back to list with error
            response.sendRedirect(request.getContextPath() +
                "/products?action=list&error=Product+not+found");
            return;
        }

        // Bind product and categories to request scope for the JSP
        request.setAttribute("product", product);
        loadCategories(request);

        RequestDispatcher dispatcher = request.getRequestDispatcher(
            "/WEB-INF/pages/editProduct.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * UPDATE (POST) — Receives the edited form data and
     * runs a SQL UPDATE on the matching product row.
     */
    private void updateProduct(HttpServletRequest request,
                               HttpServletResponse response)
            throws ServletException, IOException {

        // Extract all form parameters
        String idStr       = request.getParameter("id").trim();
        String name        = request.getParameter("name").trim();
        String description = request.getParameter("description").trim();
        String priceStr    = request.getParameter("price").trim();
        String stockStr    = request.getParameter("stock_quantity").trim();
        String catIdStr    = request.getParameter("category_id").trim();
        String imagePath   = request.getParameter("image_path").trim();

        // Server-side validation
        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()
                || catIdStr.isEmpty()) {
            request.setAttribute("errorMessage",
                "Name, price, stock quantity, and category are required.");
            request.setAttribute("product", buildProductFromParams(request));
            loadCategories(request);
            RequestDispatcher dispatcher = request.getRequestDispatcher(
                "/WEB-INF/pages/editProduct.jsp");
            dispatcher.forward(request, response);
            return;
        }

        int productId;
        double price;
        int stockQuantity;
        int categoryId;

        try {
            productId     = Integer.parseInt(idStr);
            price         = Double.parseDouble(priceStr);
            stockQuantity = Integer.parseInt(stockStr);
            categoryId    = Integer.parseInt(catIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage",
                "Price and stock quantity must be valid numbers.");
            request.setAttribute("product", buildProductFromParams(request));
            loadCategories(request);
            RequestDispatcher dispatcher = request.getRequestDispatcher(
                "/WEB-INF/pages/editProduct.jsp");
            dispatcher.forward(request, response);
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnection.getConnection();

            // Parameterized UPDATE — WHERE clause uses primary key
            String sql = "UPDATE products SET name = ?, description = ?, " +
                         "price = ?, stock_quantity = ?, category_id = ?, " +
                         "image_path = ? WHERE id = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setDouble(3, price);
            ps.setInt(4, stockQuantity);
            ps.setInt(5, categoryId);
            ps.setString(6, imagePath.isEmpty() ? null : imagePath);
            ps.setInt(7, productId);

            ps.executeUpdate();

            // PRG redirect on success
            response.sendRedirect(request.getContextPath() +
                "/products?action=list&success=Product+updated+successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage",
                "Could not update product. Please try again.");
            request.setAttribute("product", buildProductFromParams(request));
            loadCategories(request);
            RequestDispatcher dispatcher = request.getRequestDispatcher(
                "/WEB-INF/pages/editProduct.jsp");
            dispatcher.forward(request, response);
        } finally {
            closeResources(conn, ps, null);
        }
    }

    /**
     * DELETE — Removes a product row by its primary key.
     * Triggered by /products?action=delete&id=5 (GET request).
     * Always redirects after deletion (no forward).
     */
    private void deleteProduct(HttpServletRequest request,
                               HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");

        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() +
                "/products?action=list");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() +
                "/products?action=list");
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnection.getConnection();

            // Parameterized DELETE — WHERE on primary key only
            String sql = "DELETE FROM products WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);
            ps.executeUpdate();

            response.sendRedirect(request.getContextPath() +
                "/products?action=list&success=Product+deleted+successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() +
                "/products?action=list&error=Could+not+delete+product");
        } finally {
            closeResources(conn, ps, null);
        }
    }

    // UTILITY METHODS

    /**
     * Loads all categories from the DB and binds them to the
     * request scope as "categoryList" for use in JSP dropdowns.
     * Called by any method that needs a category <select>.
     */
    private void loadCategories(HttpServletRequest request) {
        List<CategoryModel> categoryList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT id, name FROM categories ORDER BY name ASC";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                categoryList.add(new CategoryModel(
                    rs.getInt("id"),
                    rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        request.setAttribute("categoryList", categoryList);
    }

    /**
     * Rebuilds a ProductModel from request parameters.
     * Used to re-populate the edit form if validation fails,
     * so the user does not lose their entered data.
     */
    private ProductModel buildProductFromParams(HttpServletRequest request) {
        ProductModel p = new ProductModel();
        try {
            p.setId(Integer.parseInt(request.getParameter("id")));
            p.setName(request.getParameter("name"));
            p.setDescription(request.getParameter("description"));
            p.setPrice(Double.parseDouble(request.getParameter("price")));
            p.setStockQuantity(Integer.parseInt(
                request.getParameter("stock_quantity")));
            p.setCategoryId(Integer.parseInt(
                request.getParameter("category_id")));
            p.setImagePath(request.getParameter("image_path"));
        } catch (NumberFormatException e) {
            // Return partially filled object — JSP handles nulls gracefully
        }
        return p;
    }

    /**
     * Closes JDBC resources safely in the correct order:
     * ResultSet first, then PreparedStatement, then Connection.
     * Each is closed independently so one failure does not
     * prevent the others from closing.
     */
    private void closeResources(Connection conn,
                                PreparedStatement ps,
                                ResultSet rs) {
        try { if (rs   != null) rs.close();   } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps   != null) ps.close();   } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close();  } catch (SQLException e) { e.printStackTrace(); }
    }
}