<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld" %>
<%@ taglib prefix="suf" uri="/WEB-INF/tlds/StringUtil_fn.tld" %>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<link rel="stylesheet" href="/lib/css/redmond/jquery-ui-1.9.1.custom.min.css" type="text/css">
<title>주문/배송조회</title>
<script type="text/javascript" src="/lib/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript" src="/lib/js/jquery.ui.datepicker-ko.js"></script>
<script type="text/javascript" src="/lib/js/jquery.cookie.js"></script>
<script type="text/javascript" src="/lib/js/jquery.lck.util.js"></script>
<script type="text/javascript">
$(function(){
	
	$("#sdate,#edate").datepicker();
	
	$.ajaxSetup ({
		cache: false
	});
	
	$("td[orderno]").each(function(){
		var _o = $("td[orderno='"+$(this).attr("orderno")+"']");
		var size = _o.size();
		if(size > 1){
			$(this).attr("rowspan", size);
			_o.last().remove();
		}
	});
	$( "#modal_dialog" ).dialog({
		autoOpen: false,
		modal : true,
		width:"1100",
		height:"750"
	});
});

function cancel_popup(cart_no){
	$("#modal_dialog").load("/popup/mypage/shopping/state/index.do?mode=cancel_popup", {cart_no : cart_no});
	$("#modal_dialog").dialog({title:'취소신청'}).dialog("open");
}

function pay_cancel(cart_no){
	if(!confirm("정말 취소 하시겠습니까?")){
		return;
	}
	$("#modal_dialog").load("/popup/mypage/shopping/state/index.do?mode=pay_cancel", {cart_no:cart_no, sayu : $('#sayu').val()});
	$("#modal_dialog").dialog("open");
}

function return_popup(cart_no){
	$("#modal_dialog").load("/popup/mypage/shopping/state/index.do?mode=return_popup", {cart_no : cart_no});
	$("#modal_dialog").dialog({title:'반품신청'}).dialog("open");
}

function return_send(cart_no){
	$("#modal_dialog").load("/popup/mypage/shopping/state/index.do?mode=return_send", {cart_no:cart_no, sayu : $('#sayu').val()});
	$("#modal_dialog").dialog("open");
}

function exchange_popup(cart_no){
	$("#modal_dialog").load("/popup/mypage/shopping/state/index.do?mode=exchange_popup", {cart_no : cart_no});
	$("#modal_dialog").dialog({title:'교환신청'}).dialog("open");
}

function exchange_send(cart_no){
	$("#modal_dialog").load("/popup/mypage/shopping/state/index.do?mode=exchange_send", {cart_no:cart_no, sayu : $('#sayu').val()});
	$("#modal_dialog").dialog("open");
}

function refunds_popup(cart_no){
	$("#modal_dialog").load("/popup/mypage/shopping/state/index.do?mode=refunds_popup", {cart_no : cart_no});
	$("#modal_dialog").dialog({title:'환불신청'}).dialog("open");
}

function refunds_send(cart_no){
	$("#modal_dialog").load("/popup/mypage/shopping/state/index.do?mode=refunds_send", {cart_no:cart_no});
	$("#modal_dialog").dialog("open");
}

function week(){
	var sdt = new Date();
	sdt.setDate(sdt.getDate() - 7);
	var edt = new Date();
	
	$("#sdate").val($.datepicker.formatDate('yy-mm-dd', sdt));
	$("#edate").val($.datepicker.formatDate('yy-mm-dd', edt));
}
function day15(){
	var sdt = new Date();
	sdt.setDate(sdt.getDate() - 15);
	var edt = new Date();
	
	$("#sdate").val($.datepicker.formatDate('yy-mm-dd', sdt));
	$("#edate").val($.datepicker.formatDate('yy-mm-dd', edt));
}
function month1(){
	var today = new Date();
	var sdt = new Date(today.getFullYear(), today.getMonth()-1, today.getDate());
	var edt = new Date();
	
	$("#sdate").val($.datepicker.formatDate('yy-mm-dd', sdt));
	$("#edate").val($.datepicker.formatDate('yy-mm-dd', edt));
}
function month3(){
	var today = new Date();
	var sdt = new Date(today.getFullYear(), today.getMonth()-3, today.getDate());
	var edt = new Date();
	
	$("#sdate").val($.datepicker.formatDate('yy-mm-dd', sdt));
	$("#edate").val($.datepicker.formatDate('yy-mm-dd', edt));
}
function bmonth(){
	var today = new Date();
	var sdt = new Date(today.getFullYear(), today.getMonth()-1, 1);
	var edt = new Date(today.getFullYear(), today.getMonth(), 0);
	
	$("#sdate").val($.datepicker.formatDate('yy-mm-dd', sdt));
	$("#edate").val($.datepicker.formatDate('yy-mm-dd', edt));
}
function cmonth(){
	var today = new Date();
	var sdt = new Date(today.getFullYear(), today.getMonth(), 1);
	var edt = new Date(today.getFullYear(), today.getMonth()+1, 0);
	
	$("#sdate").val($.datepicker.formatDate('yy-mm-dd', sdt));
	$("#edate").val($.datepicker.formatDate('yy-mm-dd', edt));
}

function goSubmit(){
	$("#searchFrm").submit();
}
</script>
</head>
<body>
<div id="sub">
        <div class="contents">

          <div class="period_line">
          <form id="searchFrm" name="searchFrm" action="index.do?mode=list4" method="post">

            <p class="title">기간별조회</p>

            <ul>
              <li><a class="first" href="#" onclick="week();">1주일</a></li>
              <li><a href="#"onclick="day15();">15일</a></li>
              <li><a href="#"onclick="month1();">1개월</a></li>
              <li><a href="#"onclick="month3();">3개월</a></li>
              <li class="cut"><a class="first" href="#"onclick="bmonth();">전월</a></li>
              <li><a href="#"onclick="cmonth();">당월</a></li>
            </ul>

            <div class="inquiry_top">
              <input type="text" id="sdate" name="sdate" value ="${param.sdate }">
              ~
              <input type="text" id="edate" name="edate" value ="${param.edate }">
              <a href="javascript:goSubmit();"><img src="/images/sub_2/btn_inquiry.gif" alt="조회하기"></a>
            </div>
		  </form>
          </div>

          <table class="cart_style_1 t_top_style_1">
          <colgroup>
          <col width="12%">
          <col width="">
          <col width="13%">
          <col width="15%">
          </colgroup>

          <thead>
          <tr>
            <th scope="col">주문번호/날짜</th>
            <th scope="col">&nbsp;</th>
            <th scope="col">상태</th>
            <th scope="col">확인</th>
          </tr>
          </thead>

          <tbody>
          <c:forEach var="item" items="${data.list }" varStatus="status">
          <tr>
            <td orderno="${item.orderno }">
              <p class="date">${item.orderno }</p>
              <p>${dtf:simpleDateFormat(item.orderdate, 'yyyy-MM-dd HH:mm:ss' , 'yyyy-MM-dd') }</p>
<!--               <p class="btn_m1"><a href="#"><img src="/images/sub_2/pay_c_btn3.gif" alt="주문상세보기"></a></p> -->
            </td>
            <td class="cart_main">
              <div class="product_box">
                <div class="pb_l"> <a href="#"><img src="${item.thumb }" alt=""></a> </div>
                <div class="pb_r_2">
                  <p>
                  <a href="#">
                  <span>
                  <strong>${item.part3_nm } / ${item.carmodelnm } ${item.cargradenm } (${item.caryyyy }) / ${item.grade }급</strong>
                  <c:if test="${item.discount_rate > 0}">
                  <span class="f_style_1">${item.discount_rate }%↓</span>
                  </c:if>
                  </span>
                  <span>${item.com_nm } / ${item.sigungu_nm }</span>
                  </a>
                  </p>
                  <p class="last">
	                 <b class="c1">${suf:getThousand(item.amt) } 원(수량:${item.qty })</b>
	                 <c:if test="${item.cod_yn eq 'Y' }">
		                 <b class="c1">배송비 ${suf:getThousand(item.fee_amt) } 원</b>
	                 </c:if>
<!--                   <a href="#"><img src="/images/sub_2/cart_btn2.gif" alt="상세내역"></a> -->
                  </p>
                </div>
              </div>
            </td>
            <td>
              <p class="status_back status_${item.status }">${item.status_nm }</p>
            </td>
            <td class="b_none">
            	<a href="#"><img src="/images/sub_2/btn_type_a2.gif" alt="증빙서류발급"></a>
            </td>
          </tr>
		  </c:forEach>

          </tbody>

          </table>

        </div>
      </div>
      
<div id="modal_dialog" title="취소신청">
</div>
</body>