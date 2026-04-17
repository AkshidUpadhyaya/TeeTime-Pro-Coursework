<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register - TeeTime Pro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="auth-card">
        <h2>Staff Registration</h2>
        
        <%-- 
           JSP SCRIPTLET BLOCK:
           Checks if the Controller (RegisterServlet) sent an "errorMessage" attribute.
           If it did (e.g., username already exists in MySQL), it renders this error div. 
        --%>
        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="error-message">
                <%= request.getAttribute("errorMessage") %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/RegisterServlet" method="POST">
            
            <div class="input-group">
                <label>Username</label>
                <input type="text" name="username" required>
            </div>
            
            <div class="input-group">
                <label>Email</label>
                <input type="email" name="email" required>
            </div>
            
            <div class="input-group">
                <label>Password</label>
                <input type="password" name="password" required>
            </div>
            
            <button type="submit">Register</button>
            
            <p class="auth-footer-link">
            	Already have an account? <a href="${pageContext.request.contextPath}/LoginServlet">Login here</a>
        	</p>
        </form>
    </div>
</body>
</html>