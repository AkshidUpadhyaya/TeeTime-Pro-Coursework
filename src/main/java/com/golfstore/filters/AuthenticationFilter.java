package com.golfstore.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * AuthenticationFilter.java
 * Servlet Filter that protects all secured routes.
 * Runs before every request and checks for a valid session.
 * If no session exists, redirects to login page.
 *
 * Public routes (no session required):
 *   /login, /register, /css/*, /images/*
 *
 * Protected routes (session required):
 *   /dashboard, /products, /categories, /logout
 */
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Filter initialisation — nothing needed here
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        // Cast to HTTP-specific request/response objects
        HttpServletRequest httpRequest   = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Get the current request URI
        String requestURI = httpRequest.getRequestURI();

        // Check if this is a public route that doesn't need a session
        boolean isPublicRoute =
            requestURI.contains("/login")    ||
            requestURI.contains("/register") ||
            requestURI.contains("/css/")     ||
            requestURI.contains("/images/")  ||
            requestURI.contains("/about")    ||
            requestURI.contains("/contact");

        if (isPublicRoute) {
            // Allow request to pass through without session check
            chain.doFilter(request, response);
            return;
        }

        // For all other routes — check session
        HttpSession session = httpRequest.getSession(false);
        boolean isLoggedIn  = (session != null &&
                               session.getAttribute("loggedInUser") != null);

        if (isLoggedIn) {
            // Role-based protection
            // Staff cannot access admin-only routes
            String userRole = (String) httpRequest.getSession(false)
                                                  .getAttribute("userRole");
            boolean isAdminRoute =
            	    (requestURI.contains("/products") && !requestURI.contains("/staff/products")) ||
            	    requestURI.contains("/categories");

            if (isAdminRoute && !"admin".equals(userRole)) {
                // Staff trying to access admin pages
                httpResponse.sendRedirect(
                    httpRequest.getContextPath() + "/dashboard");
                return;
            }

            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // Filter cleanup — nothing needed here
    }
}