<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript">
<c:choose>
	<c:when test="${empty sessionScope.member }">
	alert("세션이 끊어졌습니다.\n로그인 후 다시 시도해 주세요.\n로그인페이지로 이동합니다.");
	window.top.location.href = "/giftcard/admin/login.do";
	</c:when>
	<c:otherwise>
	alert("접근권한이 없습니다.");
	history.back();
	</c:otherwise>
</c:choose>
</script>