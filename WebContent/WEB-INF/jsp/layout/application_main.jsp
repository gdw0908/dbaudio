<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld" %>
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
  <div id="container">
  	<div class="sub">
  		<decorator:body />
  	</div>
  </div>
</div>
<div>
&nbsp;<br/>
&nbsp;<br/>
&nbsp;<br/>
&nbsp;<br/>
</div>
</body>
</html>