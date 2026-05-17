<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About | TeeTime Pro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <header class="main-header">
        <div class="header-brand">
            <h1>TeeTime Pro</h1>
            <span class="header-subtitle">Golf Equipment Management</span>
        </div>
        <nav class="header-nav">
            <c:choose>
                <c:when test="${not empty sessionScope.loggedInUser}">
                    <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                    <a href="${pageContext.request.contextPath}/products?action=list">Products</a>
                    <a href="${pageContext.request.contextPath}/categories">Categories</a>
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login">Login</a>
                    <a href="${pageContext.request.contextPath}/register">Register</a>
                </c:otherwise>
            </c:choose>
            <a href="${pageContext.request.contextPath}/about" class="active">About</a>
            <a href="${pageContext.request.contextPath}/contact">Contact</a>
        </nav>
    </header>

    <main class="main-content">

        <div class="page-title-row">
            <h2>About TeeTime Pro</h2>
        </div>

        <div class="about-grid">

            <div class="about-card">
                <div class="about-icon">&#9971;</div>
                <h3>Our Mission</h3>
                <p>
                    TeeTime Pro is a web-based inventory management system built
                    specifically for golf pro-shops. Our mission is to replace
                    outdated spreadsheet-based systems with a secure, efficient,
                    and centralised platform that staff can access from any device.
                </p>
            </div>

            <div class="about-card">
                <div class="about-icon">&#9875;</div>
                <h3>What We Do</h3>
                <p>
                    We provide a complete inventory management solution for golf
                    equipment. From tracking clubs, balls, and accessories to
                    managing stock levels and categories — TeeTime Pro keeps
                    your pro-shop running smoothly.
                </p>
            </div>

            <div class="about-card">
                <div class="about-icon">&#128274;</div>
                <h3>Security First</h3>
                <p>
                    Built with Java EE and MySQL, TeeTime Pro uses SHA-256
                    password encryption, session-based authentication, and
                    role-based access control to ensure your inventory data
                    is always protected.
                </p>
            </div>

            <div class="about-card">
                <div class="about-icon">&#128200;</div>
                <h3>Technology</h3>
                <p>
                    TeeTime Pro is built using Java EE, JSP, JSTL, MySQL,
                    and Apache Tomcat following strict MVC architecture.
                    The system is designed to be scalable, maintainable,
                    and deployable across multiple devices.
                </p>
            </div>

        </div>

        <div class="about-mission">
            <h3>Built for Golf Pro-Shops</h3>
            <p>
                Traditional golf pro-shops rely on paper ledgers or Excel
                spreadsheets to manage their inventory. These manual systems
                suffer from concurrency issues, lack of security, and poor
                scalability. TeeTime Pro solves all of these problems by
                providing a centralised, web-based platform that multiple
                staff members can use simultaneously with full data integrity.
            </p>
            <div class="about-actions">
                <a href="${pageContext.request.contextPath}/login"
                   class="btn-primary">Get Started</a>
                <a href="${pageContext.request.contextPath}/contact"
                   class="btn-secondary">Contact Us</a>
            </div>
        </div>

    </main>

    <footer class="main-footer">
        <p>TeeTime Pro &copy; 2025 | Golf Equipment Management System</p>
    </footer>

</body>
</html>