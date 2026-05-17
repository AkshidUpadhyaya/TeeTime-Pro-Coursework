<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile | TeeTime Pro</title>
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
            <a href="${pageContext.request.contextPath}/categories">Categories</a>
            <a href="${pageContext.request.contextPath}/profile" class="active">My Profile</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </nav>
    </header>

    <main class="main-content">

        <div class="page-title-row">
            <h2>My Profile</h2>
            <a href="${pageContext.request.contextPath}/dashboard"
               class="btn-secondary">&larr; Back to Dashboard</a>
        </div>

        <%-- Success / Error Messages --%>
        <c:if test="${not empty param.success}">
            <div class="alert alert-success">${param.success}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <div class="profile-grid">

            <%-- Profile Info Card --%>
            <div class="profile-info-card">
                <div class="profile-avatar">
                    <span>${fn:substring(user.username, 0, 1)}</span>
                </div>
                <h3>${user.username}</h3>
                <span class="role-badge">${user.role}</span>
                <div class="profile-detail">
                    <span class="profile-detail-label">Email</span>
                    <span>${user.email}</span>
                </div>
                <div class="profile-detail">
                    <span class="profile-detail-label">Role</span>
                    <span>${user.role}</span>
                </div>
            </div>

            <%-- Right Column — Forms --%>
            <div class="profile-forms">

                <%-- Update Email Form --%>
                <div class="form-container">
                    <h3 class="form-section-title">Update Email</h3>
                    <form action="${pageContext.request.contextPath}/profile"
                          method="POST">
                        <input type="hidden" name="action" value="updateEmail">
                        <div class="form-group">
                            <label for="email">New Email Address</label>
                            <input type="email" id="email" name="email"
                                   value="${user.email}" required>
                        </div>
                        <div class="form-actions">
                            <button type="submit" class="btn-primary">
                                Update Email
                            </button>
                        </div>
                    </form>
                </div>

                <%-- Change Password Form --%>
                <div class="form-container">
                    <h3 class="form-section-title">Change Password</h3>
                    <form action="${pageContext.request.contextPath}/profile"
                          method="POST">
                        <input type="hidden" name="action" value="changePassword">
                        <div class="form-group">
                            <label for="currentPassword">Current Password</label>
                            <input type="password" id="currentPassword"
                                   name="currentPassword"
                                   placeholder="Enter current password"
                                   required>
                        </div>
                        <div class="form-group">
                            <label for="newPassword">New Password</label>
                            <input type="password" id="newPassword"
                                   name="newPassword"
                                   placeholder="Enter new password"
                                   required>
                        </div>
                        <div class="form-group">
                            <label for="confirmPassword">Confirm New Password</label>
                            <input type="password" id="confirmPassword"
                                   name="confirmPassword"
                                   placeholder="Confirm new password"
                                   required>
                        </div>
                        <div class="form-actions">
                            <button type="submit" class="btn-primary">
                                Change Password
                            </button>
                        </div>
                    </form>
                </div>

            </div>
        </div>

    </main>

    <footer class="main-footer">
        <p>TeeTime Pro &copy; 2025 | Golf Equipment Management System</p>
    </footer>

</body>
</html>