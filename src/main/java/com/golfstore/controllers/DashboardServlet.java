package com.golfstore.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Check if the user is actually logged in (Security check)
        HttpSession session = request.getSession(false);
        
        if (session != null && session.getAttribute("loggedInUser") != null) {
            // 2. They are logged in! Send them to the secure dashboard view
            request.getRequestDispatcher("/WEB-INF/pages/dashboard.jsp").forward(request, response);
        } else {
            // 3. They are NOT logged in! Kick them back to the login page
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
        }
    }
}