<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Product | TeeTime Pro</title>
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
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </nav>
    </header>

    <%-- Main Content --%>
    <main class="main-content">

        <div class="page-title-row">
            <h2>Add New Product</h2>
            <a href="${pageContext.request.contextPath}/products?action=list"
               class="btn-secondary">
                &larr; Back to Products
            </a>
        </div>

        <%-- Error Message --%>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <%-- Add Product Form --%>
        <div class="form-container">
            <form action="${pageContext.request.contextPath}/products" method="post">

                <%-- Hidden field to tell doPost() which action to run --%>
                <input type="hidden" name="action" value="add">

                <div class="form-group">
                    <label for="name">Product Name <span class="required">*</span></label>
                    <input type="text" id="name" name="name"
                           placeholder="e.g. Callaway Rogue Driver"
                           required maxlength="100">
                </div>

                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" name="description"
                              placeholder="Brief product description..."
                              rows="3"></textarea>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="price">Price (£) <span class="required">*</span></label>
                        <input type="number" id="price" name="price"
                               placeholder="0.00" step="0.01" min="0"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="stock_quantity">Stock Quantity <span class="required">*</span></label>
                        <input type="number" id="stock_quantity" name="stock_quantity"
                               placeholder="0" min="0"
                               required>
                    </div>
                </div>

                <div class="form-group">
                    <label for="category_id">Category <span class="required">*</span></label>
                    <select id="category_id" name="category_id" required>
                        <option value="">-- Select a Category --</option>
                        <c:forEach var="category" items="${categoryList}">
                            <option value="${category.id}">${category.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="image_path">Image Path</label>
                    <input type="text" id="image_path" name="image_path"
                           placeholder="e.g. images/driver.jpg">
                    <small class="form-hint">Optional. Enter the relative path to the product image.</small>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn-primary">Add Product</button>
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