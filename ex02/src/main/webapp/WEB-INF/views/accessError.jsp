<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Access denied page</title>
</head>
<body>
<h1>Access denied page</h1>
<h2><c:out value="${SPRING_SECURITY_403_EXCEPTION.getMessage()}" /></h2>
<h2><c:out value="${msg}" /></h2>
</body>
</html>