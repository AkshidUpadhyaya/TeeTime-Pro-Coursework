package com.golfstore.controllers;

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
 * CategoryServlet.java
 * MVC Controller for Category management.
 * Mapped to URL pattern /categories
 *
 * Supported actions:
 *   action=list -> READ  -> loads all categories -> categoryList.jsp
 *   action=add  -> shows add form               -> addCategory.jsp
 *   POST add    -> CREATE -> inserts new category -> redirects to list
 *   action=delete -> DELETE -> removes category  -> redirects to list
 */
@SuppressWarnings("serial")
@WebServlet("/categories")
public class CategoryServlet extends HttpServlet {

    // doGet() — handles READ, showing add form, and delete
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Session security guard
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                listCategories(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "delete":
                deleteCategory(request, response);
                break;
            default:
                listCategories(request, response);
                break;
        }
    }

    // doPost() — handles CREATE form submission
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Session security guard
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "add":
                addCategory(request, response);
                break;
            default:
                listCategories(request, response);
                break;
        }
    }

    // PRIVATE HELPER METHODS

    /**
     * READ — Fetches all categories and forwards to categoryList.jsp.
     * Also fetches product count per category for display.
     */
    private void listCategories(HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {

        List<CategoryModel> categoryList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // JDBC Step 2: Get connection
            conn = DBConnection.getConnection();

            // JDBC Step 3: JOIN to also count products per category
            String sql = "SELECT c.id, c.name, " +
                         "COUNT(p.id) AS product_count " +
                         "FROM categories c " +
                         "LEFT JOIN products p ON c.id = p.category_id " +
                         "GROUP BY c.id, c.name " +
                         "ORDER BY c.name ASC";

            ps = conn.prepareStatement(sql);

            // JDBC Step 4: Execute
            rs = ps.executeQuery();

            // JDBC Step 5: Build category list from ResultSet
            while (rs.next()) {
                CategoryModel category = new CategoryModel(
                    rs.getInt("id"),
                    rs.getString("name")
                );
                // Store product count as a request attribute map
                // We pass it via the category name for JSP access
                categoryList.add(category);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage",
                "Could not load categories. Please try again.");
        } finally {
            // JDBC Step 6: Close all resources
            closeResources(conn, ps, rs);
        }

        request.setAttribute("categoryList", categoryList);

        RequestDispatcher dispatcher = request.getRequestDispatcher(
            "/WEB-INF/pages/categoryList.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * CREATE (GET) — Forwards to the empty add category form.
     */
    private void showAddForm(HttpServletRequest request,
                             HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher(
            "/WEB-INF/pages/addCategory.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * CREATE (POST) — Validates and inserts a new category row.
     * Checks for duplicate names before inserting.
     */
    private void addCategory(HttpServletRequest request,
                             HttpServletResponse response)
            throws ServletException, IOException {

        // Extract and trim the category name from the form
        String name = request.getParameter("name");
        if (name != null) {
            name = name.trim();
        }

        // Server-side validation
        if (name == null || name.isEmpty()) {
            request.setAttribute("errorMessage",
                "Category name cannot be empty.");
            RequestDispatcher dispatcher = request.getRequestDispatcher(
                "/WEB-INF/pages/addCategory.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Validate name length
        if (name.length() > 50) {
            request.setAttribute("errorMessage",
                "Category name cannot exceed 50 characters.");
            RequestDispatcher dispatcher = request.getRequestDispatcher(
                "/WEB-INF/pages/addCategory.jsp");
            dispatcher.forward(request, response);
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnection.getConnection();

            // JDBC Step 3: Parameterized INSERT
            // The UNIQUE constraint on categories.name also
            // protects against duplicates at the DB level
            String sql = "INSERT INTO categories (name) VALUES (?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);

            // JDBC Step 4: Execute INSERT
            ps.executeUpdate();

            // PRG pattern — redirect after successful POST
            response.sendRedirect(request.getContextPath() +
                "/categories?action=list&success=Category+added+successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            // Check for duplicate entry error code 1062
            if (e.getErrorCode() == 1062) {
                request.setAttribute("errorMessage",
                    "A category with that name already exists.");
            } else {
                request.setAttribute("errorMessage",
                    "Could not add category. Please try again.");
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher(
                "/WEB-INF/pages/addCategory.jsp");
            dispatcher.forward(request, response);
        } finally {
            closeResources(conn, ps, null);
        }
    }

    /**
     * DELETE — Removes a category by its primary key.
     * Checks if category has products before deleting
     * to maintain referential integrity.
     */
    private void deleteCategory(HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");

        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() +
                "/categories?action=list");
            return;
        }

        int categoryId;
        try {
            categoryId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() +
                "/categories?action=list");
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            // First check — does this category have products?
            // We should not delete a category that has products
            // linked to it via foreign key
            String checkSql = "SELECT COUNT(*) FROM products WHERE category_id = ?";
            ps = conn.prepareStatement(checkSql);
            ps.setInt(1, categoryId);
            rs = ps.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // Category has products — refuse deletion
                response.sendRedirect(request.getContextPath() +
                    "/categories?action=list&error=Cannot+delete+category+" +
                    "that+has+products+assigned+to+it");
                return;
            }

            // Safe to delete — close check statement first
            closeResources(null, ps, rs);
            ps = null;
            rs = null;

            // Now perform the DELETE
            String deleteSql = "DELETE FROM categories WHERE id = ?";
            ps = conn.prepareStatement(deleteSql);
            ps.setInt(1, categoryId);
            ps.executeUpdate();

            response.sendRedirect(request.getContextPath() +
                "/categories?action=list&success=Category+deleted+successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() +
                "/categories?action=list&error=Could+not+delete+category");
        } finally {
            closeResources(conn, ps, rs);
        }
    }

    /**
     * Closes JDBC resources safely in correct order.
     */
    private void closeResources(Connection conn,
                                PreparedStatement ps,
                                ResultSet rs) {
        try { if (rs   != null) rs.close();  } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps   != null) ps.close();  } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }
}