<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.mc.common.util.*"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="suf" uri="/WEB-INF/tlds/StringUtil_fn.tld"%>
<html lang="ko">
<head>
<link rel="stylesheet" href="/lib/css/common.css" type="text/css">
<link rel="stylesheet" href="/lib/css/article.css" type="text/css">
<link rel="stylesheet" href="/lib/css/join.css" type="text/css">
<script type="text/javascript" src="https://code.jquery.com/jquery-1.11.3.min.js"></script>

<script type="text/javascript">

$(document).ready(function(){
	window.resizeTo(850, 950);
	$('#card_no1').focus();
	
	if('${params.useremail}' == ""){
		$("#email").removeAttr("disabled");
	}
})

function validChk(form){
	if(form.card_no1.value == "" || form.card_no2.value == "" 
		|| form.card_no3.value == "" || form.card_no4.value == ""){
		alert("카드번호를 정확히 입력해주세요");
		return false;
	}
	if(form.card_valid_mm.value == "" || form.card_valid_yy.value == ""){
		alert("유효기간을 정확히 입력해주세요");
		return false;
	}
	if(form.card_birth_dt.value == ""){
		alert("생년월일을 정확히 입력해주세요");
		form.card_birth_dt.focus();
		return false;
	}
	if(form.card_pw.value == ""){
		alert("비밀번호 앞2자리를 정확히 입력해주세요");
		form.card_pw.focus();
		return false;
	}
	if(!isFinite(form.card_no4.value)){
		alert("카드번호는 숫자만 입력할 수 있습니다.");
		form.card_no4.value = "";
		form.card_no4.focus();
		return false;
	}
	if(!isFinite(form.card_valid_mm.value)){
		alert("유효기간은 숫자만 입력할 수 있습니다.");
		form.card_valid_mm.value = "";
		form.card_valid_mm.focus();
		return false;
	}
	if(!isFinite(form.card_valid_yy.value)){
		alert("유효기간은 숫자만 입력할 수 있습니다.");
		form.card_valid_yy.value = "";
		form.card_valid_yy.focus();
		return false;
	}
	return true;
}

function goPayExcute(){	
	if(validChk(frm)){
		$("#ord_nm").removeAttr("disabled");
		$("#goods_nm").removeAttr("disabled");
		$("#amt").removeAttr("disabled");
		$("#mobile").removeAttr("disabled");
		$("#email").removeAttr("disabled");
		
		$("#card_no").val($("#card_no1").val()+$("#card_no2").val()+$("#card_no3").val()+$("#card_no4").val());
		
		$('#frm').submit();
	}
}

function inputMoveNumber(num) {
	if(isFinite(num.value) == false) {
		alert("카드번호는 숫자만 입력할 수 있습니다.");
		num.value = "";
		return false;
	}
	max = num.getAttribute("maxlength");

	if(num.value.length >= max) {
		num.nextElementSibling.focus();
	}
}
</script>
</head>
<body>

<form id = "frm" name="frm" action="/danal/PayExcute.do" method="post">
	<input type="hidden" name="userid"  id="userid" 	value="${params.userid }" />
	<input type="hidden" name="card_no"  id="card_no" 	value="" />
	<input type="hidden" name="cert_num"  id="cert_num" 	value="${params.orderid }" />
	<div class="j_wrap">
		<%-- <input TYPE="hidden" id="STARTPARAMS" name="STARTPARAMS"  value="${STARTPARAMS}" /> --%>
		<h5 class="no_mem_type"><p class="pay_type">카드수기 결제<span>( <i><Strong>필수입력사항입니다.</Strong>)</i></span></p></h5>      
		<div class="sub_table_1">
			<table>
				<colgroup>
					<col width="20%">
					<col width="">
				</colgroup>
				<tbody>
					<tr>
						<th scope="row"><span>주문자명</span></th>
						<td><input type="text" value="${params.username}" id="ord_nm" name="ord_nm" class="input_2 ws_3" required="required" readonly="readonly" disabled="disabled"> <span></span></td>
						<!--               <td><input type="text" id="m_member_nm" name="m_member_nm" class="input_2 ws_3" readonly="readonly" required="required"> <span><a href="javascript:checkReadName();" class="address_btn">본인인증</a></span></td> -->
					</tr>
					<tr>
						<th scope="row"><span>상품명</span></th>
						<td><input type="text" value="${params.itemname}" id="goods_nm" name="goods_nm" class="input_2 ws_3"  required="required" readonly="readonly" disabled="disabled"></td>
					</tr>
					<tr>
						<th scope="row"><span>결제요청금액</span></th> 
						<td><input type="text" value="${ suf:getThousand(params.amount) }" id="amt" name="amt" class="input_2 ws_3"  required="required" readonly="readonly" disabled="disabled"></td>
					</tr>
					<tr>
						<th scope="row"><span>주문자 휴대폰</span></th>
						<td>
							  <input type="text" value="${param.mobile }" id="mobile" name="mobile" class="input_2 ws_3" maxlength="3" readonly="readonly" disabled="disabled">					
						</td>
					</tr>
					<tr>
						<th scope="row"><span>이메일</span></th>
						<td><input type="text"  value="${params.useremail}"  id="email" name="email" class="input_2 ws_3"  required="required" autocomplete='off' readonly="readonly" disabled="disabled"></td>
					</tr>
					<tr>
						<th scope="row"><span>신용카드번호</span></th>
						<td>
							  <input type="text" id="card_no1" name="card_no1" onKeyup="inputMoveNumber(this);" class="input_2 ws_1" maxlength="4" autocomplete='off'>
							  -
							  <input type="text" id="card_no2" name="card_no2"  onKeyup="inputMoveNumber(this);" class="input_2 ws_1" maxlength="4" autocomplete='off'>
							  -
							  <input type="password" id="card_no3" name="card_no3" onKeyup="inputMoveNumber(this);" class="input_2 ws_1" maxlength="4" autocomplete='new-password'>
							  -
							  <input type="text" id="card_no4" name="card_no4"  class="input_2 ws_1" maxlength="4" autocomplete='off'>
						</td>
					</tr>
					<tr>
						<th scope="row"><span>유효기간</span></th>
						<td>
							  <input type="text" id="card_valid_mm" name="card_valid_mm" class="input_2 ws_1" onKeyup="inputMoveNumber(this);"  maxlength="2">
							  월 /
							  <input type="text" id="card_valid_yy" name="card_valid_yy" class="input_2 ws_1" maxlength="2">
							  년
						</td>
					</tr>
					<tr>
						<th scope="row"><span>생년월일</span></th>
						<td><input type="text" id="card_birth_dt" name="card_birth_dt" class="input_2 ws_3" maxlength="6" placeholder="주민번호 앞 6자리"></td>
					</tr>
					<tr>
						<th scope="row"><span>비밀번호</span></th>
						<td><input type="password" id="card_pw" name="card_pw" class="input_2 ws_1" maxlength="2"> <span class="c1"><strong>&nbsp;&nbsp;※ 주의! : 비밀번호 앞 2자리를 입력해주세요.</strong></span></td>
					</tr>
					<tr>
						<th scope="row"><span>할부기간</span></th>
						<td>
							<select id="card_inst" name="card_inst"  class="select_j2"  style="width:80px;" >
			              		<option value="1" >일시불</option>
			              		<option value="2" >2개월</option>
			              		<option value="3" >3개월</option>
			              		<option value="4" >4개월</option>
			              		<option value="5" >5개월</option>
			              		<option value="6" >6개월</option>
			              		<option value="7" >7개월</option>
			              		<option value="8" >8개월</option>
			              		<option value="9" >9개월</option>
			              		<option value="10" >10개월</option>
			              		<option value="11" >11개월</option>
			              		<option value="12" >12개월</option>		              		
		              		</select> 
						</td>
					</tr>
				  </tbody>
			  </table>
		</div> 
		<div class="pay_btn">
			<a href="javascript:goPayExcute()" style="color: #fff;">결제하기</a>
			<!-- <a href="#" onclick="return Pay(frmAGS_pay)">결제하기</a> --> 
		</div>
	</div>
</form>

</body>
</html>