package com.golfstore.controllers;

import com.golfstore.config.DBConnection;
import com.golfstore.model.UserModel;
import com.golfstore.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Controller for handling Staff Registration.
 * Enforces MVC by processing business logic here, keeping JSPs clean.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Handles GET requests (e.g., when a user clicks a link to register)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Securely fetches the View from the hidden WEB-INF folder
        request.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(request, response);
    }

    // Handles POST requests (e.g., when the user submits the form)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Extract data from the HTML form using the 'name' attributes
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String hashedPassword = PasswordUtil.hashPassword(password);

        // 2. Encapsulate the data inside our JavaBean Model (Week 5 concept)
        UserModel newUser = new UserModel();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // 3. Establish Database Connection (JDBC Step 1 & 2)
            conn = DBConnection.getConnection();
            
            // 4. Create the SQL Query using PreparedStatement for security (Week 6 concept)
            // We force the role to be 'staff' for all new registrations
            String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, 'staff')";
            stmt = conn.prepareStatement(sql);
            
            // Bind the JavaBean data to the query parameters
            stmt.setString(1, newUser.getUsername());
            stmt.setString(2, newUser.getEmail());
            stmt.setString(3, newUser.getPassword()); 

            // 5. Execute the Query
            int rowsInserted = stmt.executeUpdate();

            // 6. Process the Result and Route the User
            if (rowsInserted > 0) {
                // Success! Use sendRedirect to change the URL and push them to the Login controller
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                // Failure! Forward them back to the registration page using the Dispatcher
                request.setAttribute("errorMessage", "Registration failed. Try again.");
                request.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database Error: User might already exist.");
            request.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(request, response);
        } finally {
            // Always close connections to free up resources
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}