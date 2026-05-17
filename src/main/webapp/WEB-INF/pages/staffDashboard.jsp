<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Staff Portal | TeeTime Pro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <header class="main-header">
        <div class="header-brand">
            <h1>TeeTime Pro</h1>
            <span class="header-subtitle">Staff Portal</span>
        </div>
        <nav class="header-nav">
            <a href="${pageContext.request.contextPath}/dashboard" class="active">Home</a>
            <a href="${pageContext.request.contextPath}/staff/products">View Inventory</a>
            <a href="${pageContext.request.contextPath}/profile">My Profile</a>
            <a href="${pageContext.request.contextPath}/about">About</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </nav>
    </header>

    <main class="main-content">

        <div class="dashboard-welcome">
            <h2>Welcome, <c:out value="${sessionScope.loggedInUser}"/></h2>
            <p>You are logged in as Staff. View the inventory below.</p>
        </div>

        <%-- Stats Cards — read only --%>
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
        </div>

        <%-- Recently Added Products --%>
        <div class="dashboard-section">
            <div class="section-header-row">
                <h3 class="section-title">Recently Added Products</h3>
                <a href="${pageContext.request.contextPath}/staff/products"
                   class="btn-secondary">View All Inventory</a>
            </div>
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Product Name</th>
                            <th>Price</th>
                            <th>Stock</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty recentProducts}">
                                <tr>
                                    <td colspan="3" class="table-empty">
                                        No products available yet.
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="product"
                                           items="${recentProducts}">
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
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

        <%-- Quick Actions for Staff --%>
        <div class="dashboard-section">
            <h3 class="section-title">Quick Actions</h3>
            <div class="quick-actions">
                <a href="${pageContext.request.contextPath}/staff/products"
                   class="quick-action-card">
                    <span class="quick-action-icon">&#9635;</span>
                    <span>View Inventory</span>
                </a>
                <a href="${pageContext.request.contextPath}/profile"
                   class="quick-action-card">
                    <span class="quick-action-icon">&#9787;</span>
                    <span>My Profile</span>
                </a>
                <a href="${pageContext.request.contextPath}/about"
                   class="quick-action-card">
                    <span class="quick-action-icon">&#9432;</span>
                    <span>About</span>
                </a>
                <a href="${pageContext.request.contextPath}/contact"
                   class="quick-action-card">
                    <span class="quick-action-icon">&#9993;</span>
                    <span>Contact</span>
                </a>
            </div>
        </div>

    </main>

    <footer class="main-footer">
        <p>TeeTime Pro &copy; 2025 | Golf Equipment Management System</p>
    </footer>

</body>
</html>