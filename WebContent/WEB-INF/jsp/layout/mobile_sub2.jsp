<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld" %>
<c:set var="requestURI" value="${pageContext.request.requestURI }"/>
<c:set var="seller_mode" value="N"/>
<c:set var="menuHide" value="Y"/>
<c:if test="${!(sessionScope.member eq null) && (sessionScope.member.group_seq eq '3' || sessionScope.member.group_seq eq '8') }"><%-- 협력사 or 관리자 or 자원관리--%>
	<c:set var="menuHide" value="N"/>		
</c:if>
<c:if test="${requestURI eq '/mobile/seller/seller_modify.do' || requestURI eq '/mobile/seller/seller_insert.do' || requestURI eq '/mobile/seller/resources_insert.do' || requestURI eq '/mobile/seller/resources_modify.do'}">
	<c:set var="seller_mode" value="Y"/>
</c:if>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><decorator:title default="Parts MOA" /></title>
<link rel="shortcut icon" href="/images/favicon/favicon.ico">
<link rel="stylesheet" href="/lib/css/mobile.css" type="text/css">
<script type="text/javascript" src="/lib/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="/lib/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/lib/js/jcarousellite_1.0.1.js"></script>
<script type="text/javascript" src="/lib/js/article.js"></script>
<script type="text/javascript" src="/lib/js/mc.js"></script>
<script type="text/javascript" src="/lib/js/gnb.js"></script>
<script type="text/javascript" src="/lib/js/roll.js"></script>
<script type="text/javascript">

</script>
<decorator:head />
</head>
<body>
<div class="wrap">
  <c:if test="${seller_mode eq 'N' }">
  <page:applyDecorator name="mobile.header" />
  </c:if>
  <div id="container">
	<c:if test="${seller_mode eq 'N' }">
  	<!-- gnb메뉴 -->
	<page:applyDecorator name="mobile.gnb" />
  	<!-- //gnb메뉴 -->
  	</c:if>
  	
  	<div class="sub">
  		<decorator:body />
	  	<!-- fixed_btn -->
	  	<c:if test="${seller_mode eq 'N' }">
	    <span class="my_menu"><a href="#"><img src="/images/mobile/common/btn_l.png" alt="my menu"></a></span>
<%--		    <c:if test="${menuHide eq 'N' }">
		    	<c:if test="${sessionScope.member.group_seq ne '8'}">
	      			<span class="btn_regi"><a href="/mobile/seller/seller_insert.do"><img src="/images/mobile/common/btn_r.png" alt="상품등록"></a></span>
	      			<span class="btn_regi_2"><a href="/mobile/seller/resources_insert.do"><img src="/images/mobile/common/btn_resources.png" alt="자원등록"></a></span>
	      		</c:if>
	      		<c:if test="${sessionScope.member.group_seq eq '8'}">
	      			<span class="btn_regi"><a href="/mobile/seller/resources_insert.do"><img src="/images/mobile/common/btn_resources.png" alt="자원등록"></a></span>
	      		</c:if>
	      	</c:if> --%>
      	</c:if>
	    <!-- //fixed_btn -->
  	</div>
  </div>
  <page:applyDecorator name="mobile.footer" />
</div>
	<c:if test="${seller_mode eq 'N' }">
	<page:applyDecorator name="mobile.mymenu" />
	</c:if>
</body>
</html>