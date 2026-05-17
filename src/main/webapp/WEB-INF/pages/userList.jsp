<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Management | TeeTime Pro</title>
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
            <a href="${pageContext.request.contextPath}/users" class="active">Users</a>
            <a href="${pageContext.request.contextPath}/profile">My Profile</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </nav>
    </header>

    <main class="main-content">

        <div class="page-title-row">
            <h2>User Management</h2>
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
                        <th>Username</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Failed Attempts</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty userList}">
                            <tr>
                                <td colspan="7" class="table-empty">
                                    No users found.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="user" items="${userList}"
                                       varStatus="status">
                                <tr>
                                    <td>${status.count}</td>
                                    <td>${user.username}</td>
                                    <td>${user.email}</td>
                                    <td>
                                        <span class="role-badge">
                                            ${user.role}
                                        </span>
                                    </td>
                                    <td>${user.failedAttempts}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${user.accountLocked}">
                                                <span class="badge badge-warning">
                                                    Locked
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-success">
                                                    Active
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="action-buttons">
                                        <c:if test="${user.accountLocked}">
                                            <a href="${pageContext.request.contextPath}/users?action=unlock&id=${user.id}"
                                               class="btn-edit"
                                               onclick="return confirm('Unlock this account?')">
                                               Unlock
                                            </a>
                                        </c:if>
                                        <c:if test="${!user.accountLocked}">
                                            <span class="text-muted">—</span>
                                        </c:if>
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
<p>TEST LINE - JSP IS WORKING - Users count: ${userList.size()}</p>
</html>