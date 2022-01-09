<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
</head>
<body>
	<c:if test="${! empty accessToken }">
	${email }로 로그인 되었습니다
	<form action="${pageContext.request.contextPath}/logout">
		<input type="hidden" name="accessToken" value="${accessToken }">
		<input type="submit" value="로그아웃">
	</form>
	</c:if>
	
	<c:if test="${empty accessToken }">
		<a href="${reqUrl }"><img src="resources/kakao_login.png"></a>
	</c:if>
	<br><br>
	<input type="button" onclick="document.location.href='${pageContext.request.contextPath}/summerNote/upload'" value="글 등록">
</body>
</html>