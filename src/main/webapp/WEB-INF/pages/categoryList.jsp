<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Categories | TeeTime Pro</title>
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
            <h2>Equipment Categories</h2>
            <a href="${pageContext.request.contextPath}/categories?action=add"
               class="btn-primary">
                + Add New Category
            </a>
        </div>

        <%-- Success / Error Messages --%>
        <c:if test="${not empty param.success}">
            <div class="alert alert-success">${param.success}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-error">${param.error}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <div class="table-container">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Category Name</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty categoryList}">
                            <tr>
                                <td colspan="3" class="table-empty">
                                    No categories found. Add your first category!
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="category" items="${categoryList}"
                                       varStatus="status">
                                <tr>
                                    <td>${status.count}</td>
                                    <td>${category.name}</td>
                                    <td class="action-buttons">
                                        <a href="${pageContext.request.contextPath}/categories?action=delete&id=${category.id}"
                                           class="btn-delete"
                                           onclick="return confirm('Are you sure? This will fail if products are assigned to this category.')">
                                           Delete
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>

    </main>

    <footer class="main-footer">
        <p>TeeTime Pro &copy; 2025 | Golf Equipment Management System</p>
    </footer>

</body>
</html>