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
	var obj = {
		resultCode : 1,
		data : {
			/*payment_info : {
				returncode: "0000",			//결과코드
				returnmsg: '성공',				//결과메세지
				tid: "345353546456436"						//다날 거래 키
				
			}*/
			payment_info : {
				returncode: "${RES_DATA.RETURNCODE}",			//결과코드
				returnmsg: '${RES_DATA.RETURNMSG}',				//결과메세지
				tid: "${RES_DATA.TID}",							//다날 거래 키
				
				isbill: "${RES_DATA.ISBILL}",					//초기인증 시 결제여부 Y/N
				billkey: "${RES_DATA.BILLKEY}",					//자동결제용 Key
				
				orderid: "${RES_DATA.ORDERID}",					//가맹점 주문번호
				itemname: "${RES_DATA.ITEMNAME}",				//상품명
				amount: "${RES_DATA.AMOUNT}",					//승인금액(총 금액)
				
				discountamount: "${RES_DATA.DISCOUNTAMOUNT}",	//할인금액
				trxamount: "${RES_DATA.TRXAMOUNT}",				//CP가 요청한 금액에서 할인금액을 제외한 실제 승인 된 금액
				
				trandate: "${RES_DATA.TRANDATE}",				//승인 시 매출발생일자 (yyyyMMdd)
				trantime: "${RES_DATA.TRANTIME}"				//승인 시 매출발생시간 (HHmmss)
			},
			card_info: {
				cardcode: "${RES_DATA.CARDCODE}",				//카드사 코드
				cardname: "${RES_DATA.CARDNAME}",				//카드사 명
				cardno: "${RES_DATA.CARDNO}",					//카드번호 (1111-1111-****-1111 마스킹 처리)
				quota: "${RES_DATA.QUOTA}",						//할부 개월 수 (일시불: "00", 할부 : "02"~"36")
				cardauthno: "${RES_DATA.CARDAUTHNO}"			//거래 승인 번호
			},
			user_info: {
				username: "${RES_DATA.USERNAME}",				//구매자 이름
				userid: "${RES_DATA.USERID}",					//구매자 ID
				userphone: "${RES_DATA.USERPHONE}"				//구매자 전화번호
			},
			basic_info: {
				bypassvalue: "${RES_DATA.BYPASSVALUE}"			//AUTH에서 보낸 값을 그대로 적용
			}
		}
	}

	window.opener.postMessage(obj, '*' );
	window.close();
}

</script>
</head>
<body>

<form name="form" >
	<!-- popSize 530x430  -->
	<div class="popWrap">
		<h1 class="logo"><img src="/danal/img/logo.gif" alt="Danal 신용카드" /></h1>
		<div class="tit_area">
			<p class="tit"><img src="/danal/img/tit05.gif" alt="결제완료 Complete" /></p>
		</div>
		<div class="box">
			<div class="boxTop">
				<div class="boxBtm" style="height:136px;">
					<p class="txt_info"><img src="/danal/img/txt_com.gif" width="202" height="17" alt="귀하의 발급이 완료되었습니다." /></p>
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