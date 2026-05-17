package com.golfstore.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ContactServlet.java
 * Handles GET (show form) and POST (form submission).
 * Public route — no session required.
 */
@SuppressWarnings("serial")
@WebServlet("/contact")
public class ContactServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/pages/contact.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Extract form fields
        String name    = request.getParameter("name");
        String email   = request.getParameter("email");
        String message = request.getParameter("message");

        // Basic validation
        if (name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            message == null || message.trim().isEmpty()) {

            request.setAttribute("errorMessage",
                "All fields are required.");
            request.getRequestDispatcher("/WEB-INF/pages/contact.jsp")
                   .forward(request, response);
            return;
        }

        // In a real system this would send an email.
        // For now we confirm submission with a success message.
        request.setAttribute("successMessage",
            "Thank you " + name + "! Your message has been received.");
        request.getRequestDispatcher("/WEB-INF/pages/contact.jsp")
               .forward(request, response);
    }
}