<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="shortcut icon" href="./img/favicon.ico" type="image/x-icon">
<link rel="icon" href="./img/favicon.ico" type="image/x-icon">
<link rel="stylesheet" href="./css/menu.css">
<link rel="stylesheet" href="./css/board.css">
</head>
<body>
<%@ include file="menu.jsp" %>
	<h1>보드</h1>
	<%-- 길이 검사 : ${fn:length(list) } --%>
	<c:choose>
		<c:when test="${fn:length(list) gt 0 }">
		<table>
		<tr>
			<th>번호</th>
			<th>제목</th>
			<th>글쓴이</th>
			<th>날짜</th>
			<th>읽음</th>
		</tr>
		<c:forEach items="${list }" var="row">
			<!-- onclick 자바스크립트 페이지 이동, URL?파라미터=값 -->
			<tr onclick="location.href='./detail?bno=${row.bno }'">
				<td class="td1">${row.bno }</td>
				<td class="title">${row.btitle }</td>
				<td class="td1">${row.m_name }</td>
				<td class="td2">${row.bdate }</td>
				<td class="td1">${row.blike }</td>
			</tr>
		</c:forEach>
	</table>
		</c:when>
		<c:otherwise><h1>출력할 데이터가 없습니다.</h1></c:otherwise>
	</c:choose>
	
	<!-- 로그인 했다면 글쓰기 버튼이 보여요 -->
	로그인한 이름 : ${sessionScope.mname } / ${sessionScope.mid } 
	<c:if test="${sessionScope.mname ne null }">	
		<button onclick="location.href='./write'">글쓰기</button>
	</c:if>
	
	
	
	
	
	
	
	
	
	
	
	
	
	
</body>
</html>