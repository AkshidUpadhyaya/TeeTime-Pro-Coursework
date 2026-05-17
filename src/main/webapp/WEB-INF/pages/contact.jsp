<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact | TeeTime Pro</title>
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
            <a href="${pageContext.request.contextPath}/about">About</a>
            <a href="${pageContext.request.contextPath}/contact" class="active">Contact</a>
        </nav>
    </header>

    <main class="main-content">

        <div class="page-title-row">
            <h2>Contact Us</h2>
        </div>

        <div class="contact-grid">

            <div class="contact-info">
                <h3>Get In Touch</h3>
                <p>Have a question about TeeTime Pro? We are here to help.</p>

                <div class="contact-detail">
                    <span class="contact-icon">&#9993;</span>
                    <div>
                        <strong>Email</strong>
                        <p>support@teetimepro.com</p>
                    </div>
                </div>

                <div class="contact-detail">
                    <span class="contact-icon">&#128205;</span>
                    <div>
                        <strong>Address</strong>
                        <p>Islington College, Kathmandu, Nepal</p>
                    </div>
                </div>

                <div class="contact-detail">
                    <span class="contact-icon">&#128222;</span>
                    <div>
                        <strong>Phone</strong>
                        <p>+977 01 4444444</p>
                    </div>
                </div>

                <div class="contact-detail">
                    <span class="contact-icon">&#128337;</span>
                    <div>
                        <strong>Support Hours</strong>
                        <p>Monday to Friday, 9am to 5pm</p>
                    </div>
                </div>
            </div>

            <div class="form-container">
                <h3>Send a Message</h3>

                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success">${successMessage}</div>
                </c:if>
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-error">${errorMessage}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/contact"
                      method="POST">

                    <div class="form-group">
                        <label for="name">Full Name <span class="required">*</span></label>
                        <input type="text" id="name" name="name"
                               placeholder="Your full name" required>
                    </div>

                    <div class="form-group">
                        <label for="email">Email Address <span class="required">*</span></label>
                        <input type="email" id="email" name="email"
                               placeholder="Your email address" required>
                    </div>

                    <div class="form-group">
                        <label for="message">Message <span class="required">*</span></label>
                        <textarea id="message" name="message" rows="5"
                                  placeholder="How can we help you?"></textarea>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn-primary">Send Message</button>
                    </div>

                </form>
            </div>

        </div>

    </main>

    <footer class="main-footer">
        <p>TeeTime Pro &copy; 2025 | Golf Equipment Management System</p>
    </footer>

</body>
</html>