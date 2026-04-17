<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - TeeTime Pro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="auth-card">
        <h2>Staff Login</h2>
        
        <%-- 
           JSP SCRIPTLET BLOCK:
           This block catches user-friendly error messages like "Invalid credentials" 
           sent back from the LoginServlet upon a failed database match.
        --%>
        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="error-message">
                <%= request.getAttribute("errorMessage") %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/LoginServlet" method="POST">
            
            <div class="input-group">
                <label>Username</label>
                <input type="text" name="username" required>
            </div>
            
            <div class="input-group">
                <label>Password</label>
                <input type="password" name="password" required>
            </div>
            
            <button type="submit">Login</button>
            
            <p class="auth-footer-link">
            	Don't have an account? <a href="${pageContext.request.contextPath}/RegisterServlet">Register here</a>
        	</p>
        </form>
    </div>
</body>
</html>