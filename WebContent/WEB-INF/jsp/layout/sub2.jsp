<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld" %>

<!DOCTYPE HTML>
<html lang="ko">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8">
<title><decorator:title default="Parts MOA" /></title>
<link rel="shortcut icon" href="/images/favicon/favicon.ico">
<link rel="stylesheet" href="/lib/css/common.css" type="text/css">
<link rel="stylesheet" href="/lib/css/article.css" type="text/css">
<link rel="stylesheet" href="/lib/css/sub_2.css" type="text/css">
<link rel="stylesheet" href="/lib/css/redmond/jquery-ui-1.9.1.custom.min.css" type="text/css">
<script type="text/javascript" src="/lib/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/lib/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript" src="/lib/js/jcarousellite_1.0.1.js"></script>
<script type="text/javascript" src="/lib/js/article.js"></script>
<script type="text/javascript" src="/lib/js/mc.js"></script>
<script type="text/javascript" src="/lib/js/gnb.js"></script>
<script type="text/javascript" src="/lib/js/roll.js"></script>
<script type="text/javascript" src="/lib/js/common_sc.js"></script>
<script type="text/javascript" src="/smarteditor/js/HuskyEZCreator.js"></script>
<script type="text/javascript">
$(function(){
	$("img").on("error", function(){
		$(this).off("error").attr("src", "/images/common/no_image.gif");
	});
});
</script>
<decorator:head />
</head>
<body>
<div class="wrap">
	<page:applyDecorator name="header" />
  <div id="container">
    <div class="c_wrap">
    <c:if test="${((pageContext.request.requestURI) != '/company.do' && (pageContext.request.requestURI) != '/event.do')}" >
		<page:applyDecorator name="sub2.left" />
	</c:if>
		<decorator:body />
    </div>
  </div>
	<page:applyDecorator name="footer" />
</div>
</body>
</html>