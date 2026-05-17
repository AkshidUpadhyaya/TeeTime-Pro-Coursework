package com.golfstore.model;

/**
 * UserModel.java
 * represents a single row in the 'users' table.
 * Follows strict MVC rules: data container only.
 *
 * Field names map directly to the 'users' table columns:
 *   id       -> users.id
 *   username -> users.username
 *   email    -> users.email
 *   password -> users.password
 *   role     -> users.role
 */
public class UserModel {

    // Private fields — encapsulation enforced
    private int id;
    private String username;
    private String email;
    private String password;
    private String role;
    private int failedAttempts;
    private boolean accountLocked;

    // No-argument constructor (required by JavaBean standard)
    public UserModel() {}

    // Full constructor
    public UserModel(int id, String username, String email,
                     String password, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
    
    public int getFailedAttempts() { return failedAttempts; }
    public void setFailedAttempts(int failedAttempts) { 
        this.failedAttempts = failedAttempts; 
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public boolean isAccountLocked() { return accountLocked; }
    public void setAccountLocked(boolean accountLocked) { 
        this.accountLocked = accountLocked; 
    }
}