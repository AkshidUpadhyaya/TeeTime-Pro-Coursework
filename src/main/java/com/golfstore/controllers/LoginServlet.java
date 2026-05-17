package com.golfstore.controllers;

import com.golfstore.config.DBConnection;
import com.golfstore.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * LoginServlet.java
 * -----------------
 * MVC Controller for user authentication.
 * Mapped to URL pattern /login
 *
 * Security features:
 *   - SHA-256 password hashing via PasswordUtil
 *   - Account lockout after 3 failed attempts
 *   - Session creation on successful login
 *   - Role stored in session for role-based routing
 */
@SuppressWarnings("serial")
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // Maximum failed attempts before account is locked
    private static final int MAX_ATTEMPTS = 3;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/pages/login.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username       = request.getParameter("username");
        String password       = request.getParameter("password");
        String hashedPassword = PasswordUtil.hashPassword(password);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            // STEP 1: Check if account exists and is locked
            String checkSql = "SELECT id, password, role, " +
                              "failed_attempts, account_locked " +
                              "FROM users WHERE username = ?";
            ps = conn.prepareStatement(checkSql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (!rs.next()) {
                // Username not found
                request.setAttribute("errorMessage",
                    "Invalid credentials. Please try again.");
                request.getRequestDispatcher("/WEB-INF/pages/login.jsp")
                       .forward(request, response);
                return;
            }

            // STEP 2: Check if account is locked
            int accountLocked = rs.getInt("account_locked");
            if (accountLocked == 1) {
                request.setAttribute("errorMessage",
                    "Your account has been locked after too many " +
                    "failed attempts. Please contact an administrator.");
                request.getRequestDispatcher("/WEB-INF/pages/login.jsp")
                       .forward(request, response);
                return;
            }

            // STEP 3: Check password
            String storedPassword = rs.getString("password");
            String role           = rs.getString("role");
            int failedAttempts    = rs.getInt("failed_attempts");
            int userId            = rs.getInt("id");

            // Close first query
            rs.close();
            ps.close();

            if (storedPassword.equals(hashedPassword)) {
                // SUCCESS — reset failed attempts counter
                ps = conn.prepareStatement(
                    "UPDATE users SET failed_attempts = 0 WHERE id = ?");
                ps.setInt(1, userId);
                ps.executeUpdate();
                ps.close();

                // Create session and store user info
                HttpSession session = request.getSession();
                session.setAttribute("loggedInUser", username);
                session.setAttribute("userRole", role);
                session.setAttribute("userId", userId);

                // Route based on role
                response.sendRedirect(
                    request.getContextPath() + "/dashboard");

            } else {
                // FAILED LOGIN — increment failed attempts
                failedAttempts++;

                if (failedAttempts >= MAX_ATTEMPTS) {
                    // Lock the account
                    ps = conn.prepareStatement(
                        "UPDATE users SET failed_attempts = ?, " +
                        "account_locked = 1 WHERE id = ?");
                    ps.setInt(1, failedAttempts);
                    ps.setInt(2, userId);
                    ps.executeUpdate();

                    request.setAttribute("errorMessage",
                        "Your account has been locked after " +
                        MAX_ATTEMPTS + " failed attempts. " +
                        "Please contact an administrator.");
                } else {
                    // Increment counter
                    ps = conn.prepareStatement(
                        "UPDATE users SET failed_attempts = ? " +
                        "WHERE id = ?");
                    ps.setInt(1, failedAttempts);
                    ps.setInt(2, userId);
                    ps.executeUpdate();

                    int remaining = MAX_ATTEMPTS - failedAttempts;
                    request.setAttribute("errorMessage",
                        "Invalid credentials. " + remaining +
                        " attempt(s) remaining before lockout.");
                }

                request.getRequestDispatcher("/WEB-INF/pages/login.jsp")
                       .forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage",
                "System error. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp")
                   .forward(request, response);
        } finally {
            try { if (rs   != null) rs.close();   } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps   != null) ps.close();   } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close();  } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}