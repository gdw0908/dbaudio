<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html lang="ko">
<head>
<link href="/danal/css/style.css" type="text/css" rel="stylesheet" media="all" />
<script type="text/javascript" src="https://code.jquery.com/jquery-1.11.3.min.js"></script>

<script type="text/javascript">

$(document).ready(function(){
	window.resizeTo(710, 560);
	$('#frm').submit();
})

</script>
</head>
<body>

<form id = "frm" name="frm" action="${STARTURL}" method="post">
	<input TYPE="hidden" id="STARTPARAMS" name="STARTPARAMS"  value="${STARTPARAMS}" /> 
</form>

</body>
</html>