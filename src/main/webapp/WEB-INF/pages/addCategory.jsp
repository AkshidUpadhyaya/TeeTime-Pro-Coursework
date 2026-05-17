<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Category | TeeTime Pro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <header class="main-header">
        <div class="header-brand">
            <h1>TeeTime Pro</h1>
            <span class="header-subtitle">Golf Equipment Management</span>
        </div>
        <nav class="header-nav">
            <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/products?action=list">Products</a>
            <a href="${pageContext.request.contextPath}/categories" class="active">Categories</a>
            <a href="${pageContext.request.contextPath}/profile">My Profile</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </nav>
    </header>

    <main class="main-content">

        <div class="page-title-row">
            <h2>Add New Category</h2>
            <a href="${pageContext.request.contextPath}/categories"
               class="btn-secondary">
                &larr; Back to Categories
            </a>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <div class="form-container">
            <form action="${pageContext.request.contextPath}/categories" method="post">

                <input type="hidden" name="action" value="add">

                <div class="form-group">
                    <label for="name">Category Name <span class="required">*</span></label>
                    <input type="text" id="name" name="name"
                           placeholder="e.g. Golf Clubs"
                           required maxlength="50">
                    <small class="form-hint">
                        Maximum 50 characters. Must be unique.
                    </small>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn-primary">Add Category</button>
                    <a href="${pageContext.request.contextPath}/categories"
                       class="btn-secondary">Cancel</a>
                </div>

            </form>
        </div>

    </main>

    <footer class="main-footer">
        <p>TeeTime Pro &copy; 2025 | Golf Equipment Management System</p>
    </footer>

</body>
</html>