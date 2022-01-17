<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript">
<c:if test="${message != null }">
alert("${fn:replace(message, '<br>', '\\n')}");
</c:if>
<c:if test="${message2 != null }">
alert("${fn:replace(message2, '<br>', '\\n')}");
</c:if>
<c:choose>
	<c:when test="${!(top == null || top == '')}">
	window.top.location.href = "${top }";
	</c:when>
	<c:when test="${!(opener == null || opener == '')}">
	window.opener.location.href = "${opener }";
	window.close();
	</c:when>
	<c:when test="${redirect == null || redirect == ''}">
		history.back();
	</c:when>
	
	<c:when test="${redirect == 'close'}">
	window.close();
	</c:when>

	<c:otherwise>
		document.location.href = "${redirect }";
	</c:otherwise>
</c:choose>
</script>