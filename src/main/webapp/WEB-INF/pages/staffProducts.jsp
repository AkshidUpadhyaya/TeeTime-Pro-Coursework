<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inventory | TeeTime Pro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <header class="main-header">
        <div class="header-brand">
            <h1>TeeTime Pro</h1>
            <span class="header-subtitle">Staff Portal</span>
        </div>
        <nav class="header-nav">
            <a href="${pageContext.request.contextPath}/dashboard">Home</a>
            <a href="${pageContext.request.contextPath}/staff/products" class="active">View Inventory</a>
            <a href="${pageContext.request.contextPath}/profile">My Profile</a>
            <a href="${pageContext.request.contextPath}/about">About</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </nav>
    </header>

    <main class="main-content">

        <div class="page-title-row">
            <h2>Product Inventory</h2>
        </div>

        <%-- Search Bar --%>
        <div class="search-container">
            <form action="${pageContext.request.contextPath}/staff/products"
                  method="GET">
                <div class="search-bar">
                    <input type="text" name="search"
                           placeholder="Search by product name or category..."
                           value="${search}">
                    <button type="submit" class="btn-primary">Search</button>
                    <c:if test="${not empty search}">
                        <a href="${pageContext.request.contextPath}/staff/products"
                           class="btn-secondary">Clear</a>
                    </c:if>
                </div>
            </form>
        </div>

        <%-- Search Results Info --%>
        <c:if test="${not empty search}">
            <div class="alert alert-success">
                Showing results for: <strong>${search}</strong>
                (${productList.size()} found)
            </div>
        </c:if>

        <%-- Error Message --%>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <%-- Products Table — Read Only --%>
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
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty productList}">
                            <tr>
                                <td colspan="6" class="table-empty">
                                    No products found.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="product"
                                       items="${productList}"
                                       varStatus="status">
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
                                    <td>${product.categoryId}</td>
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