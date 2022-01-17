<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="servlet_path" value="${requestScope['javax.servlet.forward.servlet_path']}"/>
<fmt:parseNumber var="totalpage" value="${param.totalpage }" integerOnly="true" type="number"/>
<c:set var="rows" value="${empty param.rows ? '10' : param.rows }"/>
<c:set var="blocksize" value="${empty param.blocksize ? '10' : param.blocksize }"/>
<c:set var="cpage" value="${empty param.cpage ? '1' : param.cpage }"/>
<fmt:parseNumber var="start" value="${cpage - ((cpage - 1) % blocksize) }" integerOnly="true" type="number"/>
<fmt:parseNumber var="end" value="${start + (blocksize-1) > totalpage ? totalpage : start + (blocksize-1) }" integerOnly="true" type="number"/>
<fmt:parseNumber var="nextblock" value="${(start+blocksize) > totalpage ? totalpage : (start+blocksize) }" integerOnly="true" type="number"/>
<fmt:parseNumber var="prevblock" value="${(end-blocksize) < 1 ? 1 : (start-blocksize)}" integerOnly="true" type="number"/>

<!-- 파라미터 -->
<c:set var="paramset" value=""/>
<c:forEach var="item" items="${pageContext.request.parameterNames}">
	<c:if test="${!(item eq 'totalpage' || item eq 'cpage' || item eq 'rows')}">
	<c:choose>
		<c:when test="${paramset eq '' }">
			<c:set var="paramset" value="${paramset }?${item }=${param[item] }"/>
		</c:when>
		<c:otherwise>
			<c:set var="paramset" value="${paramset }&${item }=${param[item] }"/>
		</c:otherwise>
	</c:choose>
	</c:if>
</c:forEach>
<c:choose>
	<c:when test="${paramset eq '' }">
		<c:set var="paramset" value="?"/>
	</c:when>
	<c:otherwise>
		<c:set var="paramset" value="${paramset }&"/>
	</c:otherwise>
</c:choose>
<!-- //파라미터 -->

<div class="paging">
	<span id="pagingWrap"> 
	<a href="${servlet_path }${paramset }rows=${rows }&cpage=1">처음</a>
	
	<c:if test="${cpage > 1 }">
		<a href="${servlet_path }${paramset }rows=${rows }&cpage=${prevblock }">이전</a>
	</c:if>
	
	<c:forEach var="i" begin="${start }" end="${end}">
	<c:choose>
		<c:when test="${cpage eq i }">
			<b><a href="javascript:void(0);">${i }</a></b>
		</c:when>
		<c:otherwise>
			<a href="${servlet_path }${paramset }rows=${rows }&cpage=${i}">${i}</a>
		</c:otherwise>
	</c:choose>
	</c:forEach>
	
	<c:if test="${cpage < totalpage }">
		<a href="${servlet_path }${paramset }rows=${rows }&cpage=${nextblock }">다음</a>
	</c:if>
	
	<a href="${servlet_path }${paramset }rows=${rows }&cpage=${totalpage }">끝</a>
	</span>
</div>