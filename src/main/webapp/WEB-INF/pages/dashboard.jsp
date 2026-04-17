<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard - TeeTime Pro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="dashboard-container">
        <div class="header-bar">
            <h1>TeeTime Pro Inventory Dashboard</h1>
            <div>
                <span>Welcome, <strong>${sessionScope.loggedInUser}</strong>!</span>
                <button class="logout-btn" onclick="window.location.href='${pageContext.request.contextPath}/LoginServlet'">Logout</button>
            </div>
        </div>
        
        <div class="content-section">
            <h3>Inventory Overview</h3>
            <p>Your Golf Equipment database connection is active. (Inventory tables will be populated in Milestone 2).</p>
        </div>
    </div>
</body>
</html>