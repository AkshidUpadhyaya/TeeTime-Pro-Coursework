<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register | TeeTime Pro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="auth-wrapper">
        <div class="auth-card">

            <div class="auth-logo">
                <h1>TeeTime Pro</h1>
                <p>Golf Equipment Management</p>
            </div>

            <h2>Staff Registration</h2>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">${errorMessage}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="POST">

                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username"
                           placeholder="Choose a username" required>
                </div>

                <div class="form-group">
                    <label for="email">Email Address</label>
                    <input type="email" id="email" name="email"
                           placeholder="Enter your email" required>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password"
                           placeholder="Choose a password" required>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn-primary">Create Account</button>
                </div>

            </form>

            <div class="auth-footer">
                Already have an account?
                <a href="${pageContext.request.contextPath}/login">Login here</a>
            </div>

        </div>
    </div>
</body>
</html>