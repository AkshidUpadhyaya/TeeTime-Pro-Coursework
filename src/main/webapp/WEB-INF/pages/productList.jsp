<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products | TeeTime Pro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <%-- Navigation Header --%>
    <header class="main-header">
        <div class="header-brand">
            <h1>TeeTime Pro</h1>
            <span class="header-subtitle">Golf Equipment Management</span>
        </div>
        <nav class="header-nav">
            <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/products?action=list" class="active">Products</a>
            <a href="${pageContext.request.contextPath}/categories">Categories</a>
            <a href="${pageContext.request.contextPath}/profile">My Profile</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </nav>
    </header>

    <%-- Main Content --%>
    <main class="main-content">

        <%-- Page Title Row --%>
        <div class="page-title-row">
            <h2>Product Inventory</h2>
            <a href="${pageContext.request.contextPath}/products?action=add" class="btn-primary">
                + Add New Product
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

        <%-- Products Table --%>
        <div class="table-container">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Product Name</th>
                        <th>Description</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Category</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty productList}">
                            <tr>
                                <td colspan="7" class="table-empty">
                                    No products found. Add your first product!
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="product" items="${productList}" varStatus="status">
                                <tr>
                                    <td>${status.count}</td>
                                    <td>${product.name}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty product.description}">
                                                ${product.description}
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted">—</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <fmt:formatNumber value="${product.price}"
                                            type="currency" currencySymbol="£"/>
                                    </td>
                                    <td>
                                        <%-- Low stock warning if quantity under 5 --%>
                                        <c:choose>
                                            <c:when test="${product.stockQuantity le 5}">
                                                <span class="badge badge-warning">
                                                    ${product.stockQuantity}
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-success">
                                                    ${product.stockQuantity}
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${product.categoryId}</td>
                                    <td class="action-buttons">
                                        <a href="${pageContext.request.contextPath}/products?action=edit&id=${product.id}"
                                           class="btn-edit">Edit</a>
                                        <a href="${pageContext.request.contextPath}/products?action=delete&id=${product.id}"
                                           class="btn-delete"
                                           onclick="return confirm('Are you sure you want to delete this product?')">
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

    <%-- Footer --%>
    <footer class="main-footer">
        <p>TeeTime Pro &copy; 2025 | Golf Equipment Management System</p>
    </footer>

</body>
</html>