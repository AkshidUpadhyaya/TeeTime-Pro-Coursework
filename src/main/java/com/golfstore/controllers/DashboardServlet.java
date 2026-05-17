package com.golfstore.controllers;

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

import com.golfstore.model.ProductModel;

/**
 * DashboardServlet.java
 * MVC Controller for the main admin dashboard.
 * Mapped to URL pattern /dashboard
 *
 * Responsibilities:
 *   1. Session security guard — redirects unauthenticated users
 *   2. Queries live statistics from the database:
 *        - Total product count
 *        - Total category count
 *        - Low stock product count (stock <= 5)
 *        - 5 most recently added products
 *   3. Binds all stats as request attributes
 *   4. Forwards to dashboard.jsp for display
 */
@SuppressWarnings("serial")
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    // Low stock threshold — products at or below this are flagged
    private static final int LOW_STOCK_THRESHOLD = 5;

    // doGet() — loads and forwards to the dashboard view
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- Session Security Guard ---
        // getSession(false) returns null if no session exists
        // Prevents unauthenticated direct URL access
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // --- Load all dashboard statistics from database ---
        loadDashboardStats(request);

        // --- Route based on role ---
        String userRole = (String) session.getAttribute("userRole");

        if ("admin".equals(userRole)) {
            // Admin sees full dashboard with CRUD
            RequestDispatcher dispatcher = request.getRequestDispatcher(
                "/WEB-INF/pages/dashboard.jsp");
            dispatcher.forward(request, response);
        } else {
            // Staff sees read-only staff portal
            RequestDispatcher dispatcher = request.getRequestDispatcher(
                "/WEB-INF/pages/staffDashboard.jsp");
            dispatcher.forward(request, response);
        }
    }

    // PRIVATE HELPER METHODS

    /**
     * Queries all dashboard statistics in a single method.
     * Each stat uses its own PreparedStatement for clarity.
     * All results are bound to request scope for the JSP.
     */
    private void loadDashboardStats(HttpServletRequest request) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // JDBC Step 2: Get single connection — reuse for all queries
            conn = DBConnection.getConnection();

            // STAT 1: Total number of products
            String countProductsSql = "SELECT COUNT(*) AS total FROM products";
            ps = conn.prepareStatement(countProductsSql);
            rs = ps.executeQuery();
            int totalProducts = 0;
            if (rs.next()) {
                totalProducts = rs.getInt("total");
            }
            request.setAttribute("totalProducts", totalProducts);
            rs.close();
            ps.close();

            // STAT 2: Total number of categories
            String countCatSql = "SELECT COUNT(*) AS total FROM categories";
            ps = conn.prepareStatement(countCatSql);
            rs = ps.executeQuery();
            int totalCategories = 0;
            if (rs.next()) {
                totalCategories = rs.getInt("total");
            }
            request.setAttribute("totalCategories", totalCategories);
            rs.close();
            ps.close();

            // STAT 3: Number of low stock products
            // Products with stock_quantity <= LOW_STOCK_THRESHOLD
            String lowStockCountSql = "SELECT COUNT(*) AS total FROM products " +
                                      "WHERE stock_quantity <= ?";
            ps = conn.prepareStatement(lowStockCountSql);
            ps.setInt(1, LOW_STOCK_THRESHOLD);
            rs = ps.executeQuery();
            int lowStockCount = 0;
            if (rs.next()) {
                lowStockCount = rs.getInt("total");
            }
            request.setAttribute("lowStockCount", lowStockCount);
            rs.close();
            ps.close();

            // STAT 4: Total number of registered users
            String countUsersSql = "SELECT COUNT(*) AS total FROM users";
            ps = conn.prepareStatement(countUsersSql);
            rs = ps.executeQuery();
            int totalUsers = 0;
            if (rs.next()) {
                totalUsers = rs.getInt("total");
            }
            request.setAttribute("totalUsers", totalUsers);
            rs.close();
            ps.close();

            // STAT 5: 5 most recently added products
            // Ordered by id DESC — highest id = most recent
            List<ProductModel> recentProducts = new ArrayList<>();
            String recentSql = "SELECT p.id, p.name, p.price, " +
                               "p.stock_quantity, p.category_id, " +
                               "p.description, p.image_path, " +
                               "c.name AS category_name " +
                               "FROM products p " +
                               "LEFT JOIN categories c ON p.category_id = c.id " +
                               "ORDER BY p.id DESC LIMIT 5";
            ps = conn.prepareStatement(recentSql);
            rs = ps.executeQuery();
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
                recentProducts.add(product);
            }
            request.setAttribute("recentProducts", recentProducts);
            rs.close();
            ps.close();

            // STAT 6: Low stock product list (for alert table)
            List<ProductModel> lowStockProducts = new ArrayList<>();
            String lowStockSql = "SELECT p.id, p.name, p.price, " +
                                 "p.stock_quantity, p.category_id, " +
                                 "p.description, p.image_path, " +
                                 "c.name AS category_name " +
                                 "FROM products p " +
                                 "LEFT JOIN categories c ON p.category_id = c.id " +
                                 "WHERE p.stock_quantity <= ? " +
                                 "ORDER BY p.stock_quantity ASC";
            ps = conn.prepareStatement(lowStockSql);
            ps.setInt(1, LOW_STOCK_THRESHOLD);
            rs = ps.executeQuery();
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
                lowStockProducts.add(product);
            }
            request.setAttribute("lowStockProducts", lowStockProducts);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage",
                "Could not load dashboard data. Please try again.");
        } finally {
            // JDBC Step 6: Close all resources
            closeResources(conn, ps, rs);
        }
    }

    /**
     * Closes JDBC resources safely in the correct order.
     */
    private void closeResources(Connection conn,
                                PreparedStatement ps,
                                ResultSet rs) {
        try { if (rs   != null) rs.close();  } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps   != null) ps.close();  } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }
}