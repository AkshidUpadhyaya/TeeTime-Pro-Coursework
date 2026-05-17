package com.golfstore.controllers;

import com.golfstore.config.DBConnection;
import com.golfstore.model.UserModel;
import com.golfstore.util.PasswordUtil;

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

/**
 * UserProfileServlet.java
 * MVC Controller for the Staff Member Portal.
 * Mapped to URL pattern /profile
 *
 * Allows logged-in staff to:
 *   - View their own profile details
 *   - Update their email address
 *   - Change their password
 */
@SuppressWarnings("serial")
@WebServlet("/profile")
public class UserProfileServlet extends HttpServlet {

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

        // Load current user profile from database
        loadUserProfile(request, response, session);
    }

    @Override
    protected void doPost(HttpServletRequest request,
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

        // Determine which form was submitted
        String action = request.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "updateEmail":
                updateEmail(request, response, session);
                break;
            case "changePassword":
                changePassword(request, response, session);
                break;
            default:
                response.sendRedirect(
                    request.getContextPath() + "/profile");
                break;
        }
    }

    // PRIVATE HELPER METHODS

    /**
     * Loads the current user's profile from the database
     * and forwards to the profile JSP.
     */
    private void loadUserProfile(HttpServletRequest request,
                                  HttpServletResponse response,
                                  HttpSession session)
            throws ServletException, IOException {

        String username = (String) session.getAttribute("loggedInUser");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserModel user = null;

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT id, username, email, role " +
                         "FROM users WHERE username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                user = new UserModel();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage",
                "Could not load profile. Please try again.");
        } finally {
            closeResources(conn, ps, rs);
        }

        request.setAttribute("user", user);

        RequestDispatcher dispatcher = request.getRequestDispatcher(
            "/WEB-INF/pages/profile.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Updates the user's email address.
     */
    private void updateEmail(HttpServletRequest request,
                              HttpServletResponse response,
                              HttpSession session)
            throws ServletException, IOException {

        String username = (String) session.getAttribute("loggedInUser");
        String newEmail = request.getParameter("email");

        if (newEmail == null || newEmail.trim().isEmpty()) {
            request.setAttribute("errorMessage",
                "Email address cannot be empty.");
            loadUserProfile(request, response, session);
            return;
        }

        newEmail = newEmail.trim();

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnection.getConnection();

            String sql = "UPDATE users SET email = ? WHERE username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, newEmail);
            ps.setString(2, username);
            ps.executeUpdate();

            response.sendRedirect(request.getContextPath() +
                "/profile?success=Email+updated+successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 1062) {
                request.setAttribute("errorMessage",
                    "That email is already in use.");
            } else {
                request.setAttribute("errorMessage",
                    "Could not update email. Please try again.");
            }
            loadUserProfile(request, response, session);
        } finally {
            closeResources(conn, ps, null);
        }
    }

    /**
     * Changes the user's password after verifying current password.
     */
    private void changePassword(HttpServletRequest request,
                                 HttpServletResponse response,
                                 HttpSession session)
            throws ServletException, IOException {

        String username    = (String) session.getAttribute("loggedInUser");
        String currentPass = request.getParameter("currentPassword");
        String newPass     = request.getParameter("newPassword");
        String confirmPass = request.getParameter("confirmPassword");

        // Validation
        if (currentPass == null || newPass == null || confirmPass == null ||
            currentPass.trim().isEmpty() || newPass.trim().isEmpty() ||
            confirmPass.trim().isEmpty()) {
            request.setAttribute("errorMessage",
                "All password fields are required.");
            loadUserProfile(request, response, session);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            request.setAttribute("errorMessage",
                "New passwords do not match.");
            loadUserProfile(request, response, session);
            return;
        }

        if (newPass.length() < 6) {
            request.setAttribute("errorMessage",
                "New password must be at least 6 characters.");
            loadUserProfile(request, response, session);
            return;
        }

        // Hash passwords for comparison and storage
        String hashedCurrent = PasswordUtil.hashPassword(currentPass);
        String hashedNew     = PasswordUtil.hashPassword(newPass);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            // Verify current password is correct
            String verifySql = "SELECT id FROM users " +
                               "WHERE username = ? AND password = ?";
            ps = conn.prepareStatement(verifySql);
            ps.setString(1, username);
            ps.setString(2, hashedCurrent);
            rs = ps.executeQuery();

            if (!rs.next()) {
                request.setAttribute("errorMessage",
                    "Current password is incorrect.");
                loadUserProfile(request, response, session);
                return;
            }

            // Close verify statement
            closeResources(null, ps, rs);
            ps = null;
            rs = null;

            // Update with new hashed password
            String updateSql = "UPDATE users SET password = ? " +
                               "WHERE username = ?";
            ps = conn.prepareStatement(updateSql);
            ps.setString(1, hashedNew);
            ps.setString(2, username);
            ps.executeUpdate();

            response.sendRedirect(request.getContextPath() +
                "/profile?success=Password+changed+successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage",
                "Could not change password. Please try again.");
            loadUserProfile(request, response, session);
        } finally {
            closeResources(conn, ps, rs);
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