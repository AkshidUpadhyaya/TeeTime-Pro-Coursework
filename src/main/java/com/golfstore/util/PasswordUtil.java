package com.golfstore.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * PasswordUtil.java
 * Utility class for password hashing using SHA-256.
 * Used by RegisterServlet to hash passwords before storing
 * and by LoginServlet to hash input before comparing.
 */
public class PasswordUtil {

    /**
     * Hashes a plain text password using SHA-256 algorithm.
     * Returns a 64-character hex string.
     */
    public static String hashPassword(String plainText) {
        try {
            // Load SHA-256 MessageDigest instance
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Convert password string to bytes and hash it
            byte[] hashedBytes = md.digest(plainText.getBytes());

            // Convert hashed bytes to hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}