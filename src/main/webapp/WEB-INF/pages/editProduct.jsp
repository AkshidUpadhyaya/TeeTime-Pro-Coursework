<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Product | TeeTime Pro</title>
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

        <div class="page-title-row">
            <h2>Edit Product</h2>
            <a href="${pageContext.request.contextPath}/products?action=list"
               class="btn-secondary">
                &larr; Back to Products
            </a>
        </div>

        <%-- Error Message --%>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <%-- Edit Product Form --%>
        <div class="form-container">
            <form action="${pageContext.request.contextPath}/products" method="post">

                <%-- Hidden fields to identify which product to update --%>
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="id" value="${product.id}">

                <div class="form-group">
                    <label for="name">Product Name <span class="required">*</span></label>
                    <input type="text" id="name" name="name"
                           value="${product.name}"
                           required maxlength="100">
                </div>

                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" name="description"
                              rows="3">${product.description}</textarea>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="price">Price (£) <span class="required">*</span></label>
                        <input type="number" id="price" name="price"
                               value="${product.price}"
                               step="0.01" min="0" required>
                    </div>

                    <div class="form-group">
                        <label for="stock_quantity">Stock Quantity <span class="required">*</span></label>
                        <input type="number" id="stock_quantity" name="stock_quantity"
                               value="${product.stockQuantity}"
                               min="0" required>
                    </div>
                </div>

                <div class="form-group">
                    <label for="category_id">Category <span class="required">*</span></label>
                    <select id="category_id" name="category_id" required>
                        <option value="">-- Select a Category --</option>
                        <c:forEach var="category" items="${categoryList}">
                            <%-- Mark the product's current category as selected --%>
                            <c:choose>
                                <c:when test="${category.id == product.categoryId}">
                                    <option value="${category.id}" selected>
                                        ${category.name}
                                    </option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${category.id}">
                                        ${category.name}
                                    </option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="image_path">Image Path</label>
                    <input type="text" id="image_path" name="image_path"
                           value="${product.imagePath}"
                           placeholder="e.g. images/driver.jpg">
                    <small class="form-hint">Optional. Enter the relative path to the product image.</small>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn-primary">Save Changes</button>
                    <a href="${pageContext.request.contextPath}/products?action=list"
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