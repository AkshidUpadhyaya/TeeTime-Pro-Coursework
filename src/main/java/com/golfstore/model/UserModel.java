package com.golfstore.model;

/**
 * JavaBean representing a Staff User.
 * Following Week 5 conventions: Encapsulation and default constructor.
 */
public class UserModel {
    private String username;
    private String email;
    private String password;

    // Default Constructor
    public UserModel() {
    	
    }

    // Getters and Setters
    public String getUsername() { 
    	return username; 
    }
    
    public void setUsername(String username) { 
    	this.username = username; 
    }

    public String getEmail() { 
    	return email; 
    }
    
    public void setEmail(String email) { 
    	this.email = email; 
    }

    public String getPassword() { 
    	return password; 
    }
    
    public void setPassword(String password) { 
    	this.password = password; 
    }
}