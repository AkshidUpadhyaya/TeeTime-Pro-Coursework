<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard | TeeTime Pro</title>
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
            <a href="${pageContext.request.contextPath}/dashboard" class="active">Dashboard</a>
            <a href="${pageContext.request.contextPath}/products?action=list">Products</a>
            <a href="${pageContext.request.contextPath}/categories">Categories</a>
            <a href="${pageContext.request.contextPath}/profile">My Profile</a>
            <a href="${pageContext.request.contextPath}/users">Users</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </nav>
    </header>

    <main class="main-content">

        <%-- Welcome Banner --%>
        <div class="dashboard-welcome">
            <h2>Welcome back, <c:out value="${sessionScope.loggedInUser}"/></h2>
            <p>Here is your inventory overview for today.</p>
        </div>

        <%-- Error Message --%>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <%-- Stats Cards Row --%>
        <div class="stats-grid">

            <div class="stat-card">
                <div class="stat-icon stat-icon-blue">&#9635;</div>
                <div class="stat-info">
                    <span class="stat-number">${totalProducts}</span>
                    <span class="stat-label">Total Products</span>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon stat-icon-green">&#9638;</div>
                <div class="stat-info">
                    <span class="stat-number">${totalCategories}</span>
                    <span class="stat-label">Categories</span>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon stat-icon-red">&#9888;</div>
                <div class="stat-info">
                    <span class="stat-number">${lowStockCount}</span>
                    <span class="stat-label">Low Stock Items</span>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon stat-icon-purple">&#9787;</div>
                <div class="stat-info">
                    <span class="stat-number">${totalUsers}</span>
                    <span class="stat-label">Registered Users</span>
                </div>
            </div>

        </div>

        <%-- Low Stock Alert Section --%>
        <c:if test="${lowStockCount gt 0}">
            <div class="dashboard-section">
                <h3 class="section-title alert-title">
                    &#9888; Low Stock Alerts
                </h3>
                <div class="table-container">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Product Name</th>
                                <th>Stock Remaining</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="product" items="${lowStockProducts}">
                                <tr>
                                    <td>${product.name}</td>
                                    <td>
                                        <span class="badge badge-warning">
                                            ${product.stockQuantity} left
                                        </span>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/products?action=edit&id=${product.id}"
                                           class="btn-edit">Update Stock</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:if>

        <%-- Recently Added Products Section --%>
        <div class="dashboard-section">
            <div class="section-header-row">
                <h3 class="section-title">Recently Added Products</h3>
                <a href="${pageContext.request.contextPath}/products?action=list"
                   class="btn-secondary">View All</a>
            </div>
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Product Name</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty recentProducts}">
                                <tr>
                                    <td colspan="4" class="table-empty">
                                        No products yet.
                                        <a href="${pageContext.request.contextPath}/products?action=add">
                                            Add your first product.
                                        </a>
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="product" items="${recentProducts}">
                                    <tr>
                                        <td>${product.name}</td>
                                        <td>
                                            <fmt:formatNumber
                                                value="${product.price}"
                                                type="currency"
                                                currencySymbol="£"/>
                                        </td>
                                        <td>
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
        </div>

        <%-- Quick Actions Row --%>
        <div class="dashboard-section">
            <h3 class="section-title">Quick Actions</h3>
            <div class="quick-actions">
                <a href="${pageContext.request.contextPath}/products?action=add"
                   class="quick-action-card">
                    <span class="quick-action-icon">+</span>
                    <span>Add Product</span>
                </a>
                <a href="${pageContext.request.contextPath}/categories?action=add"
                   class="quick-action-card">
                    <span class="quick-action-icon">+</span>
                    <span>Add Category</span>
                </a>
                <a href="${pageContext.request.contextPath}/products?action=list"
                   class="quick-action-card">
                    <span class="quick-action-icon">&#9635;</span>
                    <span>View Inventory</span>
                </a>
                <a href="${pageContext.request.contextPath}/about"
                   class="quick-action-card">
                    <span class="quick-action-icon">&#9432;</span>
                    <span>About</span>
                </a>
            </div>
        </div>

    </main>

    <footer class="main-footer">
        <p>TeeTime Pro &copy; 2025 | Golf Equipment Management System</p>
    </footer>

</body>
</html>