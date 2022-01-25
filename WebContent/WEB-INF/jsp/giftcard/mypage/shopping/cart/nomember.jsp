<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="com.mc.common.util.*" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld" %>
<%@ taglib prefix="suf" uri="/WEB-INF/tlds/StringUtil_fn.tld" %>
<%@ page language="java" import="Kisinfo.Check.IPINClient" %>
<c:set var="orderno" value="${data.orderno }" scope="request"/>
<c:set var="memberInfo" value="${data.memberInfo }" />
<c:set var="memberBasongji" value="${data.memberBasongji }" />
<c:set var="user_price" value="0"/>
<c:set var="discount_price" value="0"/>
<c:set var="fee_price" value="0"/>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<meta http-equiv="X-UA-Compatible" content="ie=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta content="minimum-scale=1.0, width=device-width, maximum-scale=1, user-scalable=yes" name="viewport" />
<meta name="author" content="31system" />
<meta name="description" content="안녕하세요  페어링사운드 입니다." />
<meta name="Keywords" content="페어링사운드, 상품권, 백화점 상품권, 롯데 백화점, 롯데 상품권, 갤러리아 백화점, 갤러리아 상품권, 신세계 백화점, 신세계 상품권" />" />
<title>주문/결제</title>

<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/xeicon@2.3.3/xeicon.min.css">
<link rel="stylesheet" href="/lib/css/join.css" type="text/css">
<!-- ※ UTF8 언어 형식으로 페이지 제작 시 아래 경로의 js 파일을 사용할 것!! !! !! -->
<!-- <script language=javascript src="http://www.allthegate.com/plugin/AGSWallet_utf8.js"></script> -->
<!-- ※ SSL 보안을 이용할 경우 아래 경로의 js 파일을 사용할 것!! -->
<!-- <script language=javascript src="https://www.allthegate.com/plugin/AGSWallet_ssl.js"></script> -->
<!-- ※ SSL 보안 과 UTF8 언어 형식으로 페이지 제작 시 아래 경로의 js 파일을 사용할 사용할 것!! -->
<script language=javascript src="https://www.allthegate.com/plugin/AGSWallet_utf8_ssl.js"></script>
<script type="text/javascript" src="/lib/js/jquery.lck.util.js"></script>
<script type="text/javascript" src="/lib/js/common.js"></script>
<script type="text/javascript">
/* StartSmartUpdate();

$(function(){
	Enable_Flag(frmAGS_pay);
	
}); */

//<![CDATA[
function wrapWindowByMask(){
	//화면의 높이와 너비
	var maskHeight = $(document).height();
	var maskWidth = $(window).width();

	$('#mask').css({'width':maskWidth,'height':maskHeight});
	//애니메이션 효과 - 0초동안 까맣게 됐다가 60% 불투명도
	$('#mask').fadeIn(0);
	$('#mask').fadeTo("slow",0.6);
	//윈도우 띄움.
	$('.window').show();
}
$(document).ready(function(){
	//검은 막 띄우기
	$('.openMask').click(function(e){
	e.preventDefault();
	wrapWindowByMask();
	});
	//닫기 버튼을 눌렀을 때
	$('.window .close').click(function (e) {
	//링크 기본동작은 작동하지 않도록 한다.
	e.preventDefault();
	$('#mask, .window').hide();
	});
	//검은 막을 눌렀을 때
	//      $('#mask').click(function () {
	//          $(this).hide();
	//          $('.window').hide();
	//      });
});
//]]>
function checkReadName()
{
	window.open('', 'popupChk', 'width=500, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
	document.form_chk.action = "https://nice.checkplus.co.kr/CheckPlusSafeModel/checkplus.cb";
	document.form_chk.target = "popupChk";
	document.form_chk.submit();

}

function failCerti()
{
	alert("인증에 실패하였습니다. 다시 시도 하여주십시오.\n계속 문제가 발생할 경우 관리자에게 문의하시기 바랍니다.");
	return;
}
function go_next(str)
{
	alert("인증이 완료 되었습니다.");
	$("#m_member_nm").val(str);
	$("#OrdNm").val(str);
	
}

function Enable_Flag(form){
        form.Flag.value = "enable"
}

function Disable_Flag(form){
        form.Flag.value = "disable"
}

function openAddr(){
	window.open("/addr/road.jsp","addr","width=570,height=420");
}

function setAddr(roadAddrPart1, addrDetail, zipNo, jibunAddr) {
	//var zip = zipNo.split("-");
	$("#zip1").val(zipNo.substring(0,3));
	$("#zip2").val(zipNo.substring(3,5));
	$("#addr1").val(jibunAddr);
	$("#addr2").val(addrDetail);
}

function chk_email(value)
{
	var pattern = new RegExp(/^([\w]{1,})+[\w\.\-\_]+([\w]{1,})+@(?:[\w\-]{2,}\.)+[a-zA-Z]{2,}$/);
	
	return pattern.test(value);
}


function Check_Common(form){
	if(form.m_member_nm.value == ""){
		alert("주문자명을 입력하시기 바립니다.");
		form.m_member_nm.focus();
		return false;
	}
	if(form.m_email.value == ""){
		alert("이메일을 입력하시기 바립니다.");
		form.m_email.focus();
		return false;
	}
	if(!chk_email($("#m_email").val())) {
		alert("입력하신 이메일 형식이 올바르지 않습니다. 다시 입력하세요.");	
		jQuery("#m_email").val("");
		jQuery("#m_email").focus();
		return false;
	}
	if(form.m_cell1.value == "" || isNaN(form.m_cell1.value)){
		alert("주문자 휴대폰 번호를 정확히 입력해주세요");
		form.m_cell1.focus();
		return false;
	}
	if(form.m_cell2.value == "" || isNaN(form.m_cell2.value)){
		alert("주문자 휴대폰 번호를 정확히 입력해주세요");
		form.m_cell2.focus();
		return false;
	}
	if(form.m_cell3.value == "" || isNaN(form.m_cell3.value)){
		alert("주문자 휴대폰 번호를 정확히 해주세요");
		form.m_cell3.focus();
		return false;
	}
	if($("#passwd").val().length < 6){
		alert("주문비밀번호 6자 이상 입력해주시기 바랍니다.");
		$("#passwd").focus();
		return false;
	}
	if($("#passwd").val() != $("#passwd_confirm").val()){
		alert("주문비밀번호를 한번더 입력해주시기 바랍니다.");
		$("#passwd_confirm").focus();
		return false;
	}
	
	if(form.zip1.value == "" || isNaN(form.zip1.value)){
		alert("우편번호를 정확히 입력해주세요");
		form.zip1.focus();
		return false;
	}else{
		$("#m_zip_cd").val(form.zip1.value+"-"+form.zip2.value);
	}
	/*if(form.zip2.value == "" || isNaN(form.zip2.value)){
		alert("우편번호를 정확히 입력해주세요");
		form.zip2.focus();
		return false;
	}*/
	if(form.addr1.value == ""){
		alert("배송지 주소를 입력해주세요");
		form.addr1.focus();
		return false;
	}else{
		$("#m_addr1").val(form.addr1.value);
		$("#m_addr2").val(form.addr2.value);
	}
	if(form.receiver.value == ""){
		alert("수취인 이름을 입력해주세요");
		form.receiver.focus();
		return false;
	}
	if(form.cell1.value == "" || isNaN(form.cell1.value)){
		alert("휴대폰 번호를 정확히 입력해주세요");
		form.cell1.focus();
		return false;
	}
	if(form.cell2.value == "" || isNaN(form.cell2.value)){
		alert("휴대폰 번호를 정확히 입력해주세요");
		form.cell2.focus();
		return false;
	}
	if(form.cell3.value == "" || isNaN(form.cell3.value)){
		alert("휴대폰 번호를 정확히 해주세요");
		form.cell3.focus();
		return false;
	}
	$("#m_cell").val(form.m_cell1.value+"-"+form.m_cell2.value+"-"+form.m_cell3.value);
	/*if(form.tel1.value == "" || isNaN(form.tel1.value)){
		alert("휴대폰 번호를 정확히 입력해주세요");
		form.tel1.focus();
		return false;
	}
	if(form.tel2.value == "" || isNaN(form.tel2.value)){
		alert("휴대폰 번호를 정확히 입력해주세요");
		form.tel2.focus();
		return false;
	}
	if(form.tel3.value == "" || isNaN(form.tel3.value)){
		alert("휴대폰 번호를 정확히 해주세요");
		form.tel3.focus();
		return false;
	}
	
	if(form.StoreId.value == ""){
		alert("상점아이디를 입력하십시오.");
		return false;
	}
	else if(form.StoreNm.value == ""){
		alert("상점명을 입력하십시오.");
		return false;
	}
	else if(form.OrdNo.value == ""){
		alert("주문번호를 입력하십시오.");
		return false;
	}
	else if(form.ProdNm.value == ""){
		alert("상품명을 입력하십시오.");
		return false;
	}
	else if(form.Amt.value == ""){
		alert("금액을 입력하십시오.");
		return false;
	}
	else if(form.MallUrl.value == ""){
		alert("상점URL을 입력하십시오.");
		return false;
	} */
	return true;
}
/* function Pay(form){
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// MakePayMessage() 가 호출되면 올더게이트 플러그인이 화면에 나타나며 Hidden 필드
	// 에 리턴값들이 채워지게 됩니다.
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	if(form.Flag.value == "enable"){
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 입력된 데이타의 유효성을 검사합니다.
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		if(Check_Common(form) == true){
			
			form.UserEmail.value = $("#m_email").val();
			form.OrdPhone.value = $("#m_cell1").val()+"-"+$("#m_cell2").val()+"-"+$("#m_cell3").val();
			form.m_cell.value = $("#m_cell1").val()+"-"+$("#m_cell2").val()+"-"+$("#m_cell3").val();
			form.card_hp.value = $("#m_cell1").val()+"-"+$("#m_cell2").val()+"-"+$("#m_cell3").val();
			form.m_tel.value = $("#tel1").val()+"-"+$("#tel2").val()+"-"+$("#tel3").val();
			
			form.RcpNm.value = $("#receiver").val();
			form.UserId.value = $("#receiver").val();
			form.RcpPhone.value = $("#cell1").val()+"-"+$("#cell2").val()+"-"+$("#cell3").val();
			form.DlvAddr.value = $("#addr1").val()+" "+$("#addr2").val();
			
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// 올더게이트 플러그인 설치가 올바르게 되었는지 확인합니다.
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			if(document.AGSPay == null || document.AGSPay.object == null){
				alert("플러그인 설치 후 다시 시도 하십시오.");
			}else{
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// 올더게이트 플러그인 설정값을 동적으로 적용하기 JavaScript 코드를 사용하고 있습니다.
				// 상점설정에 맞게 JavaScript 코드를 수정하여 사용하십시오.
				//
				// [1] 일반/무이자 결제여부
				// [2] 일반결제시 할부개월수
				// [3] 무이자결제시 할부개월수 설정
				// [4] 인증여부
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// [1] 일반/무이자 결제여부를 설정합니다.
				//
				// 할부판매의 경우 구매자가 이자수수료를 부담하는 것이 기본입니다. 그러나,
				// 상점과 올더게이트간의 별도 계약을 통해서 할부이자를 상점측에서 부담할 수 있습니다.
				// 이경우 구매자는 무이자 할부거래가 가능합니다.
				//
				// 예제)
				// 	(1) 일반결제로 사용할 경우
				// 	form.DeviId.value = "9000400001";
				//
				// 	(2) 무이자결제로 사용할 경우
				// 	form.DeviId.value = "9000400002";
				//
				// 	(3) 만약 결제 금액이 100,000원 미만일 경우 일반할부로 100,000원 이상일 경우 무이자할부로 사용할 경우
				// 	if(parseInt(form.Amt.value) < 100000)
				//		form.DeviId.value = "9000400001";
				// 	else
				//		form.DeviId.value = "9000400002";
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				form.DeviId.value = "9000400001";
				
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// [2] 일반 할부기간을 설정합니다.
				// 
				// 일반 할부기간은 2 ~ 12개월까지 가능합니다.
				// 0:일시불, 2:2개월, 3:3개월, ... , 12:12개월
				// 
				// 예제)
				// 	(1) 할부기간을 일시불만 가능하도록 사용할 경우
				// 	form.QuotaInf.value = "0";
				//
				// 	(2) 할부기간을 일시불 ~ 12개월까지 사용할 경우
				//		form.QuotaInf.value = "0:3:4:5:6:7:8:9:10:11:12";
				//
				// 	(3) 결제금액이 일정범위안에 있을 경우에만 할부가 가능하게 할 경우
				// 	if((parseInt(form.Amt.value) >= 100000) || (parseInt(form.Amt.value) <= 200000))
				// 		form.QuotaInf.value = "0:2:3:4:5:6:7:8:9:10:11:12";
				// 	else
				// 		form.QuotaInf.value = "0";
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				//결제금액이 5만원 미만건을 할부결제로 요청할경우 결제실패
				if(parseInt(form.Amt.value) < 50000)
					form.QuotaInf.value = "0";
				else
					form.QuotaInf.value = "0:2:3:4:5:6:7:8:9:10:11:12";
				
				////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// [3] 무이자 할부기간을 설정합니다.
				// (일반결제인 경우에는 본 설정은 적용되지 않습니다.)
				// 
				// 무이자 할부기간은 2 ~ 12개월까지 가능하며, 
				// 올더게이트에서 제한한 할부 개월수까지만 설정해야 합니다.
				// 
				// 100:BC
				// 200:국민
				// 201:NH
				// 300:외환
				// 310:하나SK
				// 400:삼성
				// 500:신한
				// 800:현대
				// 900:롯데
				// 
				// 예제)
				// 	(1) 모든 할부거래를 무이자로 하고 싶을때에는 ALL로 설정
				// 	form.NointInf.value = "ALL";
				//
				// 	(2) 국민카드 특정개월수만 무이자를 하고 싶을경우 샘플(2:3:4:5:6개월)
				// 	form.NointInf.value = "200-2:3:4:5:6";
				//
				// 	(3) 외환카드 특정개월수만 무이자를 하고 싶을경우 샘플(2:3:4:5:6개월)
				// 	form.NointInf.value = "300-2:3:4:5:6";
				//
				// 	(4) 국민,외환카드 특정개월수만 무이자를 하고 싶을경우 샘플(2:3:4:5:6개월)
				// 	form.NointInf.value = "200-2:3:4:5:6,300-2:3:4:5:6";
				//	
				//	(5) 무이자 할부기간 설정을 하지 않을 경우에는 NONE로 설정
				//	form.NointInf.value = "NONE";
				//
				//	(6) 전카드사 특정개월수만 무이자를 하고 싶은경우(2:3:6개월)
				//	form.NointInf.value = "100-2:3:6,200-2:3:6,201-2:3:6,300-2:3:6,310-2:3:6,400-2:3:6,500-2:3:6,800-2:3:6,900-2:3:6";
				//
				////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				if(form.DeviId.value == "9000400002")
					form.NointInf.value = "ALL";
				   
				if(MakePayMessage(form) == true){
					Disable_Flag(form);
					
					var openwin = window.open("/agspay/AGS_progress.html","popup","width=300,height=160"); //"지불처리중"이라는 팝업창연결 부분
					
					form.submit();
				}else{
					alert("지불에 실패하였습니다.");// 취소시 이동페이지 설정부분
				}
			}
		}
	}
} */

//배송비 선결제 설정
function changeCod(idx, val){

	$.ajax({
		url : "/giftcard/mypage/shopping/cart/index.do?mode=changeCod", 
		type: "POST", 
		data : {cart_no : idx, cod_yn : val}, 
		dataType : "json", 
		async: false, 
		cache : false, 
		success : function(data){
			if(data.rst == "1"){
				location.reload();
			}else{
				alert("배송비 선결제/착불 변경에 실패하였습니다.");
			}
		},
		error : function(data){
			alert("배송비 선결제/착불 변경에 실패하였습니다.");
		}
	});
}

$(function(){
	var basong_msg = [
		"부재시 경비실에 맡겨주세요.",
		"빠른 배송 부탁드립니다.",
		"부재시 핸드폰으로 연락바랍니다.",
		"배송 전 연락바랍니다."
	];
	$(".message").autocomplete({
		source: basong_msg,
		minLength: 0
	}).focus(function(){
		$(this).autocomplete("search");
	});
});

function goStep3() {
	if(Check_Common(frm) == true){
		goDanalPayPop();
	}
}

function goDanalPayPop(){
	//window.open('/danal/Ready.do?orderid=PT130122000039&amount=70000&itemname=페어링마이크&useragent=PC&dt=20220113&username=구매자&userid=admin&useremail=gdw0908@nate.com&SERVICETYPE=DANALCARD','player','width=550, height=515, scrollbars=yes, resizable=no, top=1, left=1');
	var pop_title="다날카드결제시스템";
		
	if($("#username").val() == ""){
		$("#username").val($("#m_member_nm").val());	
	}
	if($("#userid").val() == ""){
		$("#userid").val("");	
	}
		 
	if(Number($("#totalQty").val()) > 1){
		var itemname = $("#itemname").val()+" 외"+$("#totalQty").val()+"개";
		$("#itemname").val(itemname);
	}
	var isMobile = navigator.userAgent.match(/Android/i) || navigator.userAgent.match(/webOS/i) || navigator.userAgent.match(/iPhone/i) || navigator.userAgent.match(/iPad/i) || navigator.userAgent.match(/iPod/i) || navigator.userAgent.match(/BlackBerry/i) || navigator.userAgent.match(/Windows Phone/i) ? true : false;
	console.log("isMobile==="+isMobile);
	$("#useragent").val("PC");
	if(isMobile){
		$("#useragent").val("MOBILE");	
	}
    var date = new Date();
    var year = date.getFullYear().toString();
    var month = date.getMonth() + 1;
    month = month < 10 ? '0' + month.toString() : month.toString();
    var day = date.getDate();
    day = day < 10 ? '0' + day.toString() : day.toString();    
	var dt = year+month+day;
	console.log("dt==="+dt);
	$("#dt").val(dt);
	
	var mobile='${memberInfo.cell }';
	if(mobile == ""){
		mobile = $("#m_cell1").val()+"-"+$("#m_cell2").val()+"-"+$("#m_cell3").val();
	}
	
	var actionUrl ="";
	if($("input[name='cardPayGubun']:checked").val() == "1"){
		$("#AuthTy").val("danal"); //인증결제
		actionUrl ='/danal/Ready.do?orderid='+$("#orderid").val()+'&amount='+$("#amount").val()+'&itemname='+$("#itemname").val()+'&useragent='+$("#useragent").val()+'&dt='+$("#dt").val()+'&username='+$("#username").val()+'&userid='+$("#userid").val()+'&useremail='+$("#useremail").val()+'&SERVICETYPE='+$("#SERVICETYPE").val();
	}else{
		$("#AuthTy").val("danalDr"); //수기결제
		actionUrl ='/danal/ManualPay.do?orderid='+$("#orderid").val()+'&amount='+$("#amount").val()+'&itemname='+$("#itemname").val()+'&useragent='+$("#useragent").val()+'&dt='+$("#dt").val()+'&username='+$("#username").val()+'&userid='+$("#userid").val()+'&useremail='+$("#useremail").val()+'&SERVICETYPE='+$("#SERVICETYPE").val()+'&mobile='+mobile;		
	}
	//get 방식 
	window.open(actionUrl,'player','width=550, height=515, scrollbars=yes, resizable=no, top=1, left=1');
	//post 방식 
	//window.open('',pop_title,'width=550, height=515, scrollbars=yes, resizable=no, top=1, left=1');	
	//$("#danalFrm").submit();
}

window.addEventListener('message', function(e) {
	  console.log(e.data); // { hello: 'parent' }	  
	  try{
		  var resObj = new Object();
		  resObj = JSON.stringify(e.data);		  
		  var result = JSON.parse(resObj);
		  console.log(result.data.payment_info.returncode);		  	  
		  
		  if(result.data.payment_info.returncode== "0000"){
				$("#rapprno").val(result.data.card_info.cardauthno); //승인번호
				$("#rinstmt").val(result.data.card_info.quota); //할부개월
				$("#rdealno").val(result.data.payment_info.tid); //거래번호
				$("#rapprtm").val(result.data.payment_info.trandate+result.data.payment_info.trantime); //승인시간				
			  	$("#frm").submit();
		  }
	  }catch{		  
	  }
});
</script>
</head>
<body>
<!-- <form name="frmAGS_pay" method="post" action="/giftcard/mypage/shopping/cart/non_member.do?mode=pay_ing" style="width: 100%;"> -->
<form name="frm"  id="frm" method="post" action="/giftcard/mypage/shopping/cart/non_member.do?mode=pay_ing" style="width: 100%;">
	<div class="title_rocation">
      <div class="tr_wrap">
        <h3>주문/결제</h3>
      </div>
    </div>

    <div class="j_wrap">
    
    <h5 class="no_mem_type">1. 주문제품</h5>

          <table class="cart_style_1">
          <caption>
          장바구니 리스트
          </caption>
          <colgroup>
          <col width="">
          <col width="7%">
          <col width="15%">
          <col width="20%">
          <col width="16%">
          </colgroup>
          <thead>
          <tr>
            <th scope="col">제품정보</th>
            <th scope="col">수량</th>
            <th scope="col">가격</th>
            <th scope="col">배송비</th>
            <th scope="col">합계</th>
          </tr>
          </thead>
          <tbody>
          <c:set var="user_price_l" 	value=""/>
		  <c:set var="productNm" 		value=""/>
          <c:forEach var="item" items="${data.list }" varStatus="status">
          	<c:set var="user_price_l" value="${item.user_price * item.qty }"/>
			<c:set var="prod_price" value="${prod_price + user_price_l }"/>
			<c:set var="discount_price_l" value="0"/>
			<c:set var="fee_price_l" value="0"/>
			<c:set var="qty" 	value="${item.qty}"/>
			<c:set var="productNm" value="${status.index == 0 ? item.PRODUCTNM : item.PRODUCTNM+'' }"/>
      		<%-- <c:if test="${item.discount_rate > 0}">
        		<c:set var="user_price" value="${user_price + (item.user_price * item.qty) }"/>
         		<c:set var="discount_price" value="${discount_price + ((item.user_price * item.qty) - (item.sale_price * item.qty)) }"/>
				<c:set var="user_price_l" value="${item.user_price * item.qty }"/>
				<c:set var="discount_price_l" value="${(item.user_price * item.qty) - (item.sale_price * item.qty) }"/>
           	</c:if>
			<c:if test="${item.discount_rate == 0 || empty item.discount_rate}">
        		<c:set var="user_price" value="${user_price + (item.user_price * item.qty) }"/>
				<c:set var="user_price_l" value="${item.user_price * item.qty }"/>
           	</c:if>
       		<c:if test="${item.cod_yn eq 'Y' }">
            	<c:set var="fee_price" value="${fee_price + item.fee_amt }"/>
            	<c:set var="fee_price_l" value="${item.fee_amt }"/>
            </c:if> --%>
          <tr>
            <td class="cart_main">
              <div class="product_box">
                <div class="pb_l"> <a href="#"><img src="${item.thumb }" alt=""></a> </div>
                <div class="pb_r ws_2">
                  <p>
					<a href="#"> <span><strong>${item.MAKERNM }</strong></span>
						<span><strong>${item.PRODUCTNM } </strong></span> <%-- <span>${item.grade }등급 / ${item.com_nm }</span> --%>
					</a>
                  </p>
                </div>
              </div>
            </td>
            <td>${item.qty }개</td>
            <td>
            	${suf:getThousand(item.user_price) } 원 
              	<c:if test="${item.qty>1 }"> x ${item.qty}</c:if>
            </td>
<!--             <td> -->
<!--               <p class="first"> -->
<%--             	<c:if test="${item.discount_rate > 0}"> --%>
<%-- 	              ${suf:getThousand(item.user_price - item.sale_price) } --%>
<%--             	</c:if> --%>
<%-- 				<c:if test="${item.discount_rate == 0 || empty item.discount_rate}"> --%>
<!-- 	              0 -->
<%--             	</c:if> 원  --%>
<%--               	<c:if test="${item.qty>1 }"> x ${item.qty}</c:if> --%>
<!--               </p> -->
<!--             </td> -->
            <td>
            	<c:if test="${fee_price_l > 0  }">${fee_price_l} 원<br></c:if> 
            	 <c:choose>
	              	<c:when test="${item.fee_yn eq 'C' }">
	              	착불
	              	</c:when>
	              	<c:when test="${item.fee_yn eq 'Y' }">
	              	<select name="cod_yn" onchange="changeCod('${item.cart_no }', this.value)">
	              		<option value="Y" <c:if test="${item.cod_yn eq 'Y'}">selected="selected"</c:if>>선결제</option>
	              		<option value="N" <c:if test="${item.cod_yn eq 'N'}">selected="selected"</c:if>>착불</option>
	              	</select><br />
	              	(${suf:getThousand(item.fee_amt) } 원)
	              	</c:when>
	              	<c:otherwise>무료</c:otherwise>
				</c:choose>
            </td>
            <td class="b_none">${suf:getThousand(user_price_l)} 원</td>
          </tr>
          </c:forEach>
          </tbody>
          </table>
          
          <ul class="sub_list_1">
            <li><strong>페어링사운드</strong>는 통신판매중개자이며 통신판매의 당사자가 아닙니다. 따라서 <strong>페어링사운드</strong>는 상품ㆍ거래정보 및 거래에 대하여 책임을 지지 않습니다.</li>
          </ul>
          
          <h5 class="no_mem_type">2. 주문회원 정보<span>( <i>필수입력사항입니다.)</i></span></h5>
          
          <div class="sub_table_1">
            <table>
            <colgroup>
            <col width="20%">
            <col width="">
            </colgroup>
            <tbody>
            <tr>
              <th scope="row"><span>주문자명</span></th>
              <td><input type="text" id="m_member_nm" name="m_member_nm" class="input_2 ws_3" required="required"> <span></span></td>
<!--               <td><input type="text" id="m_member_nm" name="m_member_nm" class="input_2 ws_3" readonly="readonly" required="required"> <span><a href="javascript:checkReadName();" class="address_btn">본인인증</a></span></td> -->
            </tr>
            <tr>
              <th scope="row"><span>이메일</span></th>
              <td><input type="text" id="m_email" name="m_email" class="input_2 ws_3"></td>
            </tr>
            <tr>
              <th scope="row"><span>주문자 휴대폰</span></th>
              <td>
                <input type="text" id="m_cell1" name="m_cell1" class="input_2 ws_1" maxlength="3">
                -
                <input type="text" id="m_cell2" name="m_cell2" class="input_2 ws_1" maxlength="4">
                -
                <input type="text" id="m_cell3" name="m_cell3" class="input_2 ws_1" maxlength="4">
              </td>
            </tr>
            <tr>
              <th scope="row"><span>주문비밀번호</span></th>
              <td><input type="password" id="passwd" name="passwd" class="input_2 ws_3" maxlength="30"> <span>&nbsp;&nbsp;띄어쓰기 없는 6~15자의 영문자, 숫자 조합으로만 사용하실 수 있습니다.</span></td>
            </tr>
            <tr>
              <th scope="row"><span>주문비밀번호 확인</span></th>
              <td><input type="password" id="passwd_confirm" name="passwd_confirm" class="input_2 ws_3"> <span class="c1"><strong>&nbsp;&nbsp;※ 주의! : 주문·배송조회 등에 필요하므로 반드시 주문 비밀번호를 기억해 두시기 바랍니다.</strong></span></td>
            </tr>
            </tbody>
            </table>
          </div>
          
          <h5 class="no_mem_type">3. 배송지정보<span>( <i>필수입력사항입니다.)</i></span></h5>
          
          <div class="sub_table_1">
            <table>
            <colgroup>
            <col width="20%">
            <col width="">
            </colgroup>
            <tbody>
            <tr>
            <th scope="row"><span>배송지 주소</span></th>
              <td>
                <div class="input_box_1">
                  <input type="text" id="zip1" name="zip1" class="input_2 ws_1" maxlength="3"> - <input type="text" id="zip2" name="zip2" class="input_2 ws_1" maxlength="3">
                  <a href="javascript:openAddr();" class="address_btn">주소찾기</a>
                </div>
                <div class="input_box_1">
                  <input type="text" id="addr1" name="addr1" class="input_2 ws_2">
                </div>
                <div class="last">
                  <input type="text" id="addr2" name="addr2" class="input_2 ws_2">
                </div>
              </td>
            </tr>
            <tr>
              <th scope="row"><span>수취인 이름</span></th>
              <td><input type="text" id="receiver" name="receiver" class="input_2 ws_3"></td>
            </tr>
            <tr>
              <th scope="row"><span>수취인 휴대폰</span></th>
              <td>
                <input type="text" id="cell1" name="cell1" class="input_2 ws_1" maxlength="3">
                -
                <input type="text" id="cell2" name="cell2" class="input_2 ws_1" maxlength="4">
                -
                <input type="text" id="cell3" name="cell3" class="input_2 ws_1" maxlength="4">
              </td>
            </tr>
            <tr>
              <th scope="row"><span>수취인 연락처</span></th>
              <td>
                <input type="text" id="tel1" name="tel1" class="input_2 ws_1" maxlength="3">
                -
                <input type="text" id="tel2" name="tel2" class="input_2 ws_1" maxlength="4">
                -
                <input type="text" id="tel3" name="tel3" class="input_2 ws_1" maxlength="4">
              </td>
            </tr>
            <tr>
              <th scope="row">
              <p><span>배송시요청사항</span></p>
              </th>
              <td>
                <div class="color_2"><strong>주의!</strong> : 판매자와 사전에 협의되지 않은 선택정보 변경 기재는 반영되지 않을 수 있습니다.</div>
                <c:forEach var="item" items="${data.list }" varStatus="status">
                <div class="middle">
                  <p class="color_1">상품명 : ${item.MAKERNM } / ${item.PRODUCTNM } </p>
                  <p><input type="text" name="message" class="input_2 ws_4" maxlength="100"> ( 0/100 bytes )</p>
                  <input type="hidden" name="cart_no" value="${item.cart_no }"/>
                </div>
                </c:forEach>
              </td>
            </tr>
            </tbody>
            </table>
          </div>
          
          <h5 class="no_mem_type">4. 결제금액 및 구매혜택</h5>
          
          <div class="pricecheck">
            <div class="p_check1">
              <div class="top">
                <span class="pt_l"><strong>정상가격</strong></span>
                <span class="pt_r">선택상품 : <b>${fn:length(data.list) }</b>개</span>
              </div>
              <div class="bottom">
                <p><b>${suf:getThousand(prod_price) }</b>원</p>
                <%-- <p class="pb_type"><span class="pb_l">선결제배송비</span><span class="pb_r"><b>${suf:getThousand(fee_price) }</b>원</span></p> --%>
              </div>
            </div>
            <div class="p_check2">
              <div class="top">
                <span class="pt_l"><strong>할인금액</strong></span>
                <span class="pt_r"><a href="#"><img src="/images/sub_2/guide_btn1.gif" alt="?"></a></span>
              </div>
              <div class="bottom">
                <p class="minus"><b>${suf:getThousand(discount_price) }</b>원</p>
              </div>
            </div>

            <div class="p_check3">
              <div class="top">
                <span class="pt_l"><strong>총 구매금액</strong></span>
              </div>
              <div class="bottom">
              	<c:set var="actual_price" value="${prod_price - discount_price + fee_price}" scope="request"/>
                <p class="equal"><b>${suf:getThousand(actual_price) }</b>원</p>
              </div>
            </div>
          </div>
          
		<div class="sub_table_1">
			<table>
				<colgroup>
					<col width="20%">
					<col width="">
				</colgroup>
				<tbody>
					<tr>
						<th scope="row" rowspan="2"><span>결제 방식 선택</span></th>
						<td>
							<span>
								<label>
									<input type="radio" name="cardPayGubun" class="radio" value="1" checked="checked"><strong>카드인증결제</strong> 
								</label>
								<label>
									<input type="radio" name="cardPayGubun" class="radio" value="2"><strong>카드수기결제(카드정보 수기입력)</strong>
								</label>									
							</span>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
				
          <div class="pay_btn"> 
<!--           <a href="/html/join/no_member_3.html"><img src="/images/sub_2/btn_pay_2.gif" alt="결제하기"></a>  -->
	          <a href="/giftcard/mypage/shopping/cart/index.do" class="clear_btn">장바구니</a>
	          <a href="#" onclick="javascript:goStep3()" style="color: #fff;">결제하기</a> 
          </div>

    </div>
<%
	/* 해쉬 암호화 적용( StoreId + OrdNo + Amt)
	 * StoreId          : 상점아이디		form.StoreId.value
	 * OrdNo          : 주문번호			form.OrdNo.value
	 * Amt      		 : 금액					form.Amt.value
	 * MD5 해쉬데이터 암호화 검증을 위해
	 */
	 
	String StoreId = Util.getProperty("store_id");
	String OrdNo = String.valueOf(request.getAttribute("orderno"));
	String Amt = String.valueOf(request.getAttribute("actual_price"));
	
	StringBuffer sb = new StringBuffer();
	sb.append(StoreId);
	sb.append(OrdNo);
	sb.append(Amt);
	
	byte[] bNoti = sb.toString().getBytes();
	MessageDigest md = MessageDigest.getInstance("MD5");
	byte[] digest = md.digest(bNoti);
	
	StringBuffer strBuf = new StringBuffer();
	for (int i=0 ; i < digest.length ; i++) {
	 	int c = digest[i] & 0xff;
		if (c <= 15){
			strBuf.append("0");
	 	}
	 	strBuf.append(Integer.toHexString(c));
	}
	
	String AGS_HASHDATA = strBuf.toString();
 
%>
<input type="hidden" name="m_zip_cd" id="m_zip_cd" value=""/>
<input type="hidden" name="m_addr1" id="m_addr1" value=""/>
<input type="hidden" name="m_addr2" id="m_addr2" value=""/>
<input type="hidden" name="m_cell" id="m_cell" value=""/>
<input type="hidden" name="m_tel" id="m_tel" value=""/>

<input type="hidden" name="Job" value=""/>
<input type="hidden" name="StoreId" value="<%=StoreId %>"/>
<input type="hidden" name="OrdNo" value="<%=OrdNo %>"/>
<input type="hidden" name="Amt" value="<%=Amt %>"/>
<input type="hidden" name="StoreNm" value="(주)파츠모아"/>
<input type="hidden" name="ProdNm" value="${data.list[0].part3_nm }"/>
<input type="hidden" name="MallUrl" value="http://www.partsmoa.co.kr"/>
<input type="hidden" name="MallPage" value="/nositemesh/mypage/shopping/cart/index.do?mode=virAcctResult">
<input type="hidden" name="UserEmail" value=""/>
<input type="hidden" name="SubjectData" value="(주)파츠모아;${data.list[0].part3_nm };<%=Amt %>;"/>
<input type="hidden" name="card_hp" value=""/>
<input type="hidden" name="UserId" value=""/>
<input type="hidden" name="RcpNm" value=""/>
<input type="hidden" name="RcpPhone" value=""/>
<input type="hidden" name="DlvAddr" value=""/>
<input type="hidden" name="Remark" value=""/>

<!-- 카드 & 가상계좌 결제 사용 변수 -->
<input type="hidden" id="OrdNm" name="OrdNm" value=""/>
<input type="hidden" id="OrdPhone" name="OrdPhone" value=""/>
<input type="hidden" id="OrdAddr" name="OrdAddr" value=""/>

<!-- 스크립트 및 플러그인에서 값을 설정하는 Hidden 필드  !!수정을 하시거나 삭제하지 마십시오-->

<!-- 각 결제 공통 사용 변수 -->
<input type=hidden name=Flag value="">				<!-- 스크립트결제사용구분플래그 -->
<input type=hidden name=AuthTy value="" id="AuthTy">			<!-- 결제형태 -->
<input type=hidden name=SubTy value="">				<!-- 서브결제형태 -->
<input type="hidden" name="AGS_HASHDATA" value="<%=AGS_HASHDATA%>">		<!-- 전역 해쉬 변수 -->

<!-- 신용카드 결제 사용 변수 -->
<input type=hidden name=DeviId value="">			<!-- (신용카드공통)		단말기아이디 -->
<input type=hidden name=QuotaInf value="0">			<!-- (신용카드공통)		일반할부개월설정변수 -->
<input type=hidden name=NointInf value="NONE">		<!-- (신용카드공통)		무이자할부개월설정변수 -->
<input type=hidden name=AuthYn value="">			<!-- (신용카드공통)		인증여부 -->
<input type=hidden name=Instmt value="">			<!-- (신용카드공통)		할부개월수 -->
<input type=hidden name=partial_mm value="">		<!-- (ISP사용)			일반할부기간 -->
<input type=hidden name=noIntMonth value="">		<!-- (ISP사용)			무이자할부기간 -->
<input type=hidden name=KVP_RESERVED1 value="">		<!-- (ISP사용)			RESERVED1 -->
<input type=hidden name=KVP_RESERVED2 value="">		<!-- (ISP사용)			RESERVED2 -->
<input type=hidden name=KVP_RESERVED3 value="">		<!-- (ISP사용)			RESERVED3 -->
<input type=hidden name=KVP_CURRENCY value="">		<!-- (ISP사용)			통화코드 -->
<input type=hidden name=KVP_CARDCODE value="">		<!-- (ISP사용)			카드사코드 -->
<input type=hidden name=KVP_SESSIONKEY value="">	<!-- (ISP사용)			암호화코드 -->
<input type=hidden name=KVP_ENCDATA value="">		<!-- (ISP사용)			암호화코드 -->
<input type=hidden name=KVP_CONAME value="">		<!-- (ISP사용)			카드명 -->
<input type=hidden name=KVP_NOINT value="">			<!-- (ISP사용)			무이자/일반여부(무이자=1, 일반=0) -->
<input type=hidden name=KVP_QUOTA value="">			<!-- (ISP사용)			할부개월 -->
<input type=hidden name=CardNo value="">			<!-- (안심클릭,일반사용)	카드번호 -->
<input type=hidden name=MPI_CAVV value="">			<!-- (안심클릭,일반사용)	암호화코드 -->
<input type=hidden name=MPI_ECI value="">			<!-- (안심클릭,일반사용)	암호화코드 -->
<input type=hidden name=MPI_MD64 value="">			<!-- (안심클릭,일반사용)	암호화코드 -->
<input type=hidden name=ExpMon value="">			<!-- (일반사용)			유효기간(월) -->
<input type=hidden name=ExpYear value="">			<!-- (일반사용)			유효기간(년) -->
<input type=hidden name=Passwd value="">			<!-- (일반사용)			비밀번호 -->
<input type=hidden name=SocId value="">				<!-- (일반사용)			주민등록번호/사업자등록번호 -->

<!-- 계좌이체 결제 사용 변수 -->
<input type=hidden name=ICHE_OUTBANKNAME value="">	<!-- 이체계좌은행명 -->
<input type=hidden name=ICHE_OUTACCTNO value="">	<!-- 이체계좌예금주주민번호 -->
<input type=hidden name=ICHE_OUTBANKMASTER value=""><!-- 이체계좌예금주 -->
<input type=hidden name=ICHE_AMOUNT value="">		<!-- 이체금액 -->

<!-- 핸드폰 결제 사용 변수 -->
<input type=hidden name=HP_SERVERINFO value="">		<!-- 서버정보 -->
<input type=hidden name=HP_HANDPHONE value="">		<!-- 핸드폰번호 -->
<input type=hidden name=HP_COMPANY value="">		<!-- 통신사명(SKT,KTF,LGT) -->
<input type=hidden name=HP_IDEN value="">			<!-- 인증시사용 -->
<input type=hidden name=HP_IPADDR value="">			<!-- 아이피정보 -->

<!-- 가상계좌 결제 사용 변수 -->
<input type=hidden name=ZuminCode value="">			<!-- 가상계좌입금자주민번호 -->
<input type=hidden name=VIRTUAL_CENTERCD value="">	<!-- 가상계좌은행코드 -->
<input type=hidden name=VIRTUAL_NO value="">		<!-- 가상계좌번호 -->
<input type=hidden name=VIRTUAL_DEPODT value="">

<!-- ARS 결제 사용 변수 -->
<input type=hidden name=ARS_PHONE value="">			<!-- ARS번호 -->
<input type=hidden name=ARS_NAME value="">			<!-- 전화가입자명 -->

<input type=hidden name=mTId value="">				

<!-- 에스크로 결제 사용 변수 -->
<input type=hidden name=ES_SENDNO value="">			<!-- 에스크로전문번호 -->

<!-- 계좌이체(소켓) 결제 사용 변수 -->
<input type=hidden name=ICHE_SOCKETYN value="">		<!-- 계좌이체(소켓) 사용 여부 -->
<input type=hidden name=ICHE_POSMTID value="">		<!-- 계좌이체(소켓) 이용기관주문번호 -->
<input type=hidden name=ICHE_FNBCMTID value="">		<!-- 계좌이체(소켓) FNBC거래번호 -->
<input type=hidden name=ICHE_APTRTS value="">		<!-- 계좌이체(소켓) 이체 시각 -->
<input type=hidden name=ICHE_REMARK1 value="">		<!-- 계좌이체(소켓) 기타사항1 -->
<input type=hidden name=ICHE_REMARK2 value="">		<!-- 계좌이체(소켓) 기타사항2 -->
<input type=hidden name=ICHE_ECWYN value="">		<!-- 계좌이체(소켓) 에스크로여부 -->
<input type=hidden name=ICHE_ECWID value="">		<!-- 계좌이체(소켓) 에스크로ID -->
<input type=hidden name=ICHE_ECWAMT1 value="">		<!-- 계좌이체(소켓) 에스크로결제금액1 -->
<input type=hidden name=ICHE_ECWAMT2 value="">		<!-- 계좌이체(소켓) 에스크로결제금액2 -->
<input type=hidden name=ICHE_CASHYN value="">		<!-- 계좌이체(소켓) 현금영수증발행여부 -->
<input type=hidden name=ICHE_CASHGUBUN_CD value="">	<!-- 계좌이체(소켓) 현금영수증구분 -->
<input type=hidden name=ICHE_CASHID_NO value="">	<!-- 계좌이체(소켓) 현금영수증신분확인번호 -->

<!-- 계좌이체-텔래뱅킹(소켓) 결제 사용 변수 -->
<input type=hidden name=ICHEARS_SOCKETYN value="">	<!-- 텔레뱅킹계좌이체(소켓) 사용 여부 -->
<input type=hidden name=ICHEARS_ADMNO value="">		<!-- 텔레뱅킹계좌이체 승인번호 -->
<input type=hidden name=ICHEARS_POSMTID value="">	<!-- 텔레뱅킹계좌이체 이용기관주문번호 -->
<input type=hidden name=ICHEARS_CENTERCD value="">	<!-- 텔레뱅킹계좌이체 은행코드 -->
<input type=hidden name=ICHEARS_HPNO value="">		<!-- 텔레뱅킹계좌이체 휴대폰번호 -->

<!-- 스크립트 및 플러그인에서 값을 설정하는 Hidden 필드  !!수정을 하시거나 삭제하지 마십시오-->
<!-- 다날결제결과같이 보낼데이터 form Start -->
<input type="hidden" name="rapprno"  id="rapprno" 	value="" />
<input type="hidden" name="rdealno"  id="rdealno" 	value="" />
<input type="hidden" name="rapprtm"  id="rapprtm" 	value="" />
<input type="hidden" name="rinstmt"  id="rinstmt" 	value="" />
</form>
<form name="danalFrm"  id="danalFrm" method="post" action="/danal/Ready.do" >
	<!-- 다날결제관련 form Start -->
	 <input type="hidden" name="orderid"  id="orderid" 	value="<%=OrdNo%>" />
	 <input type="hidden" name="amount"  id="amount" 	value="${user_price_l }" />
	 <input type="hidden" name="itemname"  id="itemname" 	value="${productNm}" />		 
	 <input type="hidden" name="useragent"  id="useragent" 	value="" />
	 <input type="hidden" name="dt"  id="dt" 	value="" />
	 <input type="hidden" name="username"  id="username" 	value="${memberInfo.member_nm }" />
	 <input type="hidden" name="userid"  id="userid" 	value="${memberInfo.member_id }" />
	 <input type="hidden" name="useremail"  id="useremail" 	value="${memberInfo.email }" />
	 <input type="hidden" name="SERVICETYPE"  id="SERVICETYPE" 	value="DANALCARD" />
	 <input type="hidden" name="totalQty"  id="totalQty" 	value="${qty }" />
	 <!-- 다날결제관련 form End --> 
</form>

<%
	String home_url = Util.getProperty("home.url");
	/* 실명인증 시작 */
    
	NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();
    
    String sSiteCode = Util.getProperty("name.namesitecd");				// NICE로부터 부여받은 사이트 코드
    String sSitePassword = Util.getProperty("name.namesitepwd");		// NICE로부터 부여받은 사이트 패스워드
    
    String sRequestNumber = "REQ0000000001";        	// 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로 
                                                    	// 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
    sRequestNumber = niceCheck.getRequestNO(sSiteCode);
  	session.setAttribute("REQ_SEQ" , sRequestNumber);	// 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.
  	
   	String sAuthType = "";      	// 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서
   	
   	String popgubun 	= "N";		//Y : 취소버튼 있음 / N : 취소버튼 없음
	String customize 	= "";		//없으면 기본 웹페이지 / Mobile : 모바일페이지
		
    // CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
    String sReturnUrl = home_url+"/CheckPlusSafe/checkplus_success.jsp";      // 성공시 이동될 URL
    String sErrorUrl = home_url+"/CheckPlusSafe/checkplus_fail.jsp";          // 실패시 이동될 URL

    // 입력될 plain 데이타를 만든다.
    String sPlainData = "7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber +
                        "8:SITECODE" + sSiteCode.getBytes().length + ":" + sSiteCode +
                        "9:AUTH_TYPE" + sAuthType.getBytes().length + ":" + sAuthType +
                        "7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl +
                        "7:ERR_URL" + sErrorUrl.getBytes().length + ":" + sErrorUrl +
                        "11:POPUP_GUBUN" + popgubun.getBytes().length + ":" + popgubun +
                        "9:CUSTOMIZE" + customize.getBytes().length + ":" + customize;
    
    String sMessage = "";
    String sEncData = "";
    
    int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);
    sEncData = niceCheck.getCipherData();
    
    /* 실명인증 끝*/
    
    /* IPIN 시작 */
    
    String sIpinSiteCode				= "L228";
	String sIpinSitePw					= "ptMA2580";
	String sIpinReturnURL				= home_url+ "/IPIN/ipin_result.jsp";
	String sIpinCPRequest				= "";
	String sIpinEncData					= "";
	
	IPINClient pClient = new IPINClient();
	
	sIpinCPRequest = pClient.getRequestNO(sIpinSiteCode);
	
	session.setAttribute("CPREQUEST" , sIpinCPRequest);
	
	int iRtn = pClient.fnRequest(sIpinSiteCode, sIpinSitePw, sIpinCPRequest, sIpinReturnURL);
	
	sIpinEncData = pClient.getCipherData();
	
	
	/* IPIN 끝 */
%>
<form name="form_chk" method="post">
<input type="hidden" name="m" value="checkplusSerivce">
<input type="hidden" name="EncodeData" value="<%= sEncData %>">
</form>
</body>