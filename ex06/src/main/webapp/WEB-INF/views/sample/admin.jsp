<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>admin page</title>
</head>
<body>
<!-- admin -->
<h1>/sample/admin page</h1>

<p>principal : <sec:authentication property="principal"/></p>
<%-- <p>MemberVO : <sec:authentication property="principal.member"/></p> --%>
<%-- <p>사용자이름 : <sec:authentication property="pricipal.member.username"/></p> --%>
<p>사용자아이디 : <sec:authentication property="principal.username"/> </p>
<%-- <p>사용자 권한 리스트  : <sec:authentication property="principal.member.authList"/></p> --%>

<a href="/customLogout">로그아웃</a>
</body>
</html>