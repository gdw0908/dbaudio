<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html lang="ko">
<head>
<link href="/danal/css/style.css" type="text/css" rel="stylesheet" media="all" />
<script type="text/javascript" src="https://code.jquery.com/jquery-1.11.3.min.js"></script>

<script type="text/javascript">

$(document).ready(function(){
	window.resizeTo(550, 515);
})

function on_close(){
	window.opener.postMessage({resultCode : -1}, '*' );
	window.close();
}

</script>
</head>
<body>

<form name="form" >
	<!-- popSize 530x430  -->
	<div class="popWrap">
		<h1 class="logo"><img src="/danal/img/logo.gif" /></h1>
		<div class="tit_area">
			<p class="tit"><img src="/danal/img/tit05.gif" alt="결제취소 Complete" /></p>
		</div>
		<div class="box">
			<div class="boxTop">
				<div class="boxBtm" style="height:136px;">
					<p class="txt_info">[[${RETURNMSG}]]</p>
					<p class="txt_info">귀하의 결제가 취소되었습니다.</p>
				</div>
			</div>
		</div>
		<p class="btn">
			<a href="javascript:on_close();"><img src="/danal/img/btn_confirm.gif" width="91" height="28" alt="확인" /></a>
		</p>
		<div class="popFoot">
			<div class="foot_top">
				<div class="foot_btm">
					<div class="noti_area">
						 다날 신용카드결제를 이용해주셔서 고맙습니다. [Tel] 1566-3355
					</div>
				</div>
			</div>
		</div>
	</div>
</form>
</body>
</html>