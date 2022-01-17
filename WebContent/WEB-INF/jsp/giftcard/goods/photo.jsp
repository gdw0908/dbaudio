<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld" %>
<%@ taglib prefix="suf" uri="/WEB-INF/tlds/StringUtil_fn.tld" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<!doctype html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<style type="text/css">
img{width:500px;}
</style>
<title>사진목록</title>
</head>
<body>
	<c:forEach var="item" items="${data.list }">
		<img src="http://www.partsmoa.co.kr/upload/board/${item.yyyy }/${item.mm }/${item.uuid }" alt="${item.attach_nm }"/>
	</c:forEach>
</body>
</html>