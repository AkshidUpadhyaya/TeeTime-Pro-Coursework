package com.golfstore.controllers;

import com.golfstore.config.DBConnection;
import com.golfstore.model.ProductModel;
import com.golfstore.model.CategoryModel;

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
 * StaffProductServlet.java
 * Read-only product view for staff members.
 * Mapped to URL pattern /staff/products
 *
 * Features:
 *   - View all products (read-only, no edit/delete buttons)
 *   - Search products by name or category
 */
@SuppressWarnings("serial")
@WebServlet("/staff/products")
public class StaffProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Session security guard
        HttpSession session = request.getSession(false);
        if (session == null ||
            session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(
                request.getContextPath() + "/login");
            return;
        }

        // Get search keyword if provided
        String search = request.getParameter("search");
        if (search != null) {
            search = search.trim();
        }

        List<ProductModel> productList = new ArrayList<>();
        List<CategoryModel> categoryList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            // Build query — with or without search
            String sql;
            if (search != null && !search.isEmpty()) {
                // Search by product name or category name
                sql = "SELECT p.id, p.name, p.description, p.price, " +
                      "p.stock_quantity, p.category_id, p.image_path, " +
                      "c.name AS category_name " +
                      "FROM products p " +
                      "LEFT JOIN categories c ON p.category_id = c.id " +
                      "WHERE p.name LIKE ? OR c.name LIKE ? " +
                      "ORDER BY p.id DESC";
                ps = conn.prepareStatement(sql);
                ps.setString(1, "%" + search + "%");
                ps.setString(2, "%" + search + "%");
            } else {
                sql = "SELECT p.id, p.name, p.description, p.price, " +
                      "p.stock_quantity, p.category_id, p.image_path, " +
                      "c.name AS category_name " +
                      "FROM products p " +
                      "LEFT JOIN categories c ON p.category_id = c.id " +
                      "ORDER BY p.id DESC";
                ps = conn.prepareStatement(sql);
            }

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
                productList.add(product);
            }

            rs.close();
            ps.close();

            // Load categories for filter dropdown
            ps = conn.prepareStatement(
                "SELECT id, name FROM categories ORDER BY name ASC");
            rs = ps.executeQuery();
            while (rs.next()) {
                categoryList.add(new CategoryModel(
                    rs.getInt("id"),
                    rs.getString("name")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage",
                "Could not load products. Please try again.");
        } finally {
            try { if (rs   != null) rs.close();  } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps   != null) ps.close();  } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        request.setAttribute("productList", productList);
        request.setAttribute("categoryList", categoryList);
        request.setAttribute("search", search);

        RequestDispatcher dispatcher = request.getRequestDispatcher(
            "/WEB-INF/pages/staffProducts.jsp");
        dispatcher.forward(request, response);
    }
}