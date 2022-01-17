<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html lang="ko">
<head>
<link href="./css/style.css" type="text/css" rel="stylesheet" media="all" />
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

	<div class="popWrap">
		<h1 class="logo"><img src="/danal/img/logo.gif" alt="Danal 신용카드" /></h1>
		<div class="tit_area">
			<p class="tit"><img src="/danal/img/tit06.gif" width="198" height="20" alt="결제서비스 에러 Service Error" /></p>
		</div>
		<div class="box">
			<div class="boxTop">
				<div class="boxBtm">
					<div class="service_error">
						<dl class="error01">
							<dt><img src="/danal/img/error_txt01.gif" width="58" height="14" alt="에러 내용" /></dt>
							<dd id = "result_text">[[${RETURNCODE}]]&nbsp;[[${RETURNMSG}]]</dd>
						</dl>	
						<dl class="error02">
							<dt><img src="/danal/img/error_txt02.gif" width="58" height="14" alt="처리방법" /></dt>
							<dd>다날 고객센터 : 1566-3355 (전국공통)<br/>
							상담원 통화가능시간 : 평일 9시~ 18시 (토요일,일요일,공휴일 휴무)</dd>
						</dl>					
					</div>					
				</div>
			</div>
		</div>
		<p class="btn">
			<a href="javascript:on_close();"><img src="/danal/img/btn_confirm.gif" width="91" height="28" alt="íì¸" /></a>
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
	
</body>
</html>