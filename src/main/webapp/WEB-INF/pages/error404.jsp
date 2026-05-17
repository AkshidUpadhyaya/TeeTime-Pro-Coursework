<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 | TeeTime Pro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <header class="main-header">
        <div class="header-brand">
            <h1>TeeTime Pro</h1>
            <span class="header-subtitle">Golf Equipment Management</span>
        </div>
    </header>
    <main class="main-content">
        <div class="error-page">
            <h1>404</h1>
            <h2>Page Not Found</h2>
            <p>The page you are looking for does not exist or has been moved.</p>
            <a href="${pageContext.request.contextPath}/login" class="btn-primary">
                Go to Login
            </a>
        </div>
    </main>
    <footer class="main-footer">
        <p>TeeTime Pro &copy; 2025 | Golf Equipment Management System</p>
    </footer>
</body>
</html>