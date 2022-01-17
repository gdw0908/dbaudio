<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:choose>
	<c:when test="${empty sessionScope.member}">
	<c:redirect url="/giftcard/admin/login.do"/>
	</c:when>
	<c:when test="${!(sessionScope.member.group_seq eq '1' || sessionScope.member.group_seq eq '3' || sessionScope.member.group_seq eq '8') || sessionScope.member.login_yn eq 'N'}">
	<c:redirect url="/"/>
	</c:when>
</c:choose>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<title>티켓모아 관리시스템</title>

<link rel="shortcut icon" href="/images/favicon/favicon.ico">
<link href="/lib/css/cmsbase.css" rel="stylesheet" type="text/css" />
<link href="/lib/css/cmsadmin.css" rel="stylesheet" type="text/css" />
</head>
<frameset rows="45,*" frameborder="no" framespacing="0">
  <frame src="/giftcard/admin/inc/header.do"  noresize="noresize" />
  <frameset cols="281,*" >
    <frame src="/giftcard/admin/inc/left.do" style="width: 281px;" scrolling="no" noresize="noresize" name="leftFrame"/>
    <frame src="/giftcard/admin/login/login_img.do"  noresize="noresize" name="bodyFrame"/>
  </frameset>
</frameset><noframes></noframes>
<body>

<!-- 헤더부분 -->

</body>
</html>