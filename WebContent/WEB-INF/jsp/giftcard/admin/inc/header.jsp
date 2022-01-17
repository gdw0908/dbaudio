<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>DBSOUND 관리시스템</title>
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/xeicon@2.3.3/xeicon.min.css">
<link href="/lib/css/cmsbase.css" rel="stylesheet" type="text/css" />
<link href="/lib/css/cmsadmin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
function logout(){
	window.top.location.href = "/giftcard/admin/login.do?mode=logout";
}
</script>
</head>
<body>
<div id="headerwrap">

  <h1><a href="javascript:window.top.location.reload();"><img src="/images/admin/header/logo.svg" alt="DBSOUND 홈페이지 관리시스템" /></a><span>상품관리&nbsp;<span>통합관리시스템</span></span></h1>
  
	  <div class="admin_profile">
	<%--   <c:if test = "${sessionScope.member.group_seq ne '8' }">
	    <span><a href = "javascript:goBody('/admin/goods/spell/index.do?tab=2');">결재완료건수:<span>${data.cnt1 }</span>&nbsp;건</a></span>
	    <span><a href = "javascript:goBody('/admin/goods/spell/index.do?tab=3');">취소요청건수:<span>${data.cnt2 }</span>&nbsp;건</a></span>
	    <span><a href = "javascript:goBody('/admin/goods/spell/index.do?tab=6');">반품요청건수:<span>${data.cnt3 }</span>&nbsp;건</a></span>
	    <span><a href = "javascript:goBody('/admin/goods/spell/index.do?tab=8');">환불요청건수:<span>${data.cnt4 }</span>&nbsp;건</a></span>
	    <c:if test = "${sessionScope.member.group_seq eq '1'}">
	    <!-- <span><a href = "javascript:goBody('/admin/system/board/mantoman/index.do');">1:1문의건수:<span>${data.cnt5 }</span>&nbsp;건</a></span> -->
	    </c:if>
	    <span><a href = "javascript:goBody('/admin/system/board/application/index.do');">국산차 부품문의건수:<span>${data.cnt6 }</span>&nbsp;건</a></span>
	    <span><a href = "javascript:goBody('/admin/system/board/application_import/index.do');">수입차 부품문의건수:<span>${data.cnt7 }</span>&nbsp;건</a></span>
	    </c:if> --%>
	    <p class="profileico">
	    	<i class="xi-user"></i>
	    	<span>${sessionScope.member.member_nm }</span>님 이 로그인 하셨습니다.
	    </p>
<!--    <p class="profileicoa"><a href="#"><img src="/images/admin/header/heard_20.gif" alt="정보수정" /></a></p> -->
	    <p class="profileicob"><a href="javascript:logout()">로그아웃</a></p>
	  </div>
	
</div>
</body>
</html>
