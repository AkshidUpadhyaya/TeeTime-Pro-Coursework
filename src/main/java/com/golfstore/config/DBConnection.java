package com.golfstore.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class handles the database connection for the TeeTime Pro system.
 * By centralizing this logic, we prevent code duplication across our Servlets.
 */

public class DBConnection {
	// Database credentials for the local XAMPP MySQL server
    // The URL points specifically to our 'teetime_pro' database on port 3306
    private static final String URL = "jdbc:mysql://localhost:3306/teetime_pro";
    private static final String USER = "root"; // Default XAMPP username
    private static final String PASSWORD = ""; // Default XAMPP password is empty

    /**
     * Establishes and returns a connection to the database using the JDBC API.
     * @return Connection object if successful, null if it fails.
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // STEP 1: Load the JDBC Driver
            // This tells Java to use the external MySQL Connector JAR we added to the Build Path
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // STEP 2: Establish the Connection
            // We pass our URL, username, and password to the DriverManager
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection successful!");
            
        } catch (ClassNotFoundException e) {
            // This catches the error if the JAR file is missing from the Build Path
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            // This catches the error if XAMPP is off or credentials are wrong
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
        
        // Return the active connection back to whatever Servlet called this method
        return connection;
    }

}
