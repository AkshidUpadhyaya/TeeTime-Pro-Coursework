package com.golfstore.controllers;

import com.golfstore.config.DBConnection;
import com.golfstore.model.UserModel;

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
 * UserManagementServlet.java
 * Admin-only controller for managing user accounts.
 * Mapped to URL pattern /users
 *
 * Features:
 *   - View all registered users
 *   - Unlock locked accounts
 *   - Reset failed attempts
 */
@SuppressWarnings("serial")
@WebServlet("/users")
public class UserManagementServlet extends HttpServlet {

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

        // Admin only
        String role = (String) session.getAttribute("userRole");
        if (!"admin".equals(role)) {
            response.sendRedirect(
                request.getContextPath() + "/dashboard");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "unlock":
                unlockUser(request, response);
                break;
            default:
                listUsers(request, response);
                break;
        }
    }

    // PRIVATE HELPER METHODS

    /**
     * Lists all registered users with their status.
     */
    private void listUsers(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        List<UserModel> userList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT id, username, email, role, " +
                         "failed_attempts, account_locked " +
                         "FROM users ORDER BY id ASC";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                UserModel user = new UserModel();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setFailedAttempts(rs.getInt("failed_attempts"));
                user.setAccountLocked(rs.getInt("account_locked") == 1);
                userList.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage",
                "Could not load users. Please try again.");
        } finally {
            closeResources(conn, ps, rs);
        }

        request.setAttribute("userList", userList);

        RequestDispatcher dispatcher = request.getRequestDispatcher(
            "/WEB-INF/pages/userList.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Unlocks a user account and resets failed attempts.
     */
    private void unlockUser(HttpServletRequest request,
                             HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");

        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(
                request.getContextPath() + "/users");
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(
                request.getContextPath() + "/users");
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnection.getConnection();

            String sql = "UPDATE users SET " +
                         "failed_attempts = 0, " +
                         "account_locked = 0 " +
                         "WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.executeUpdate();

            response.sendRedirect(request.getContextPath() +
                "/users?success=Account+unlocked+successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() +
                "/users?error=Could+not+unlock+account");
        } finally {
            closeResources(conn, ps, null);
        }
    }

    /**
     * Closes JDBC resources safely.
     */
    private void closeResources(Connection conn,
                                 PreparedStatement ps,
                                 ResultSet rs) {
        try { if (rs   != null) rs.close();  } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps   != null) ps.close();  } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }
}