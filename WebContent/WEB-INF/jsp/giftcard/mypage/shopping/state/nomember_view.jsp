<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld" %>
<%@ taglib prefix="suf" uri="/WEB-INF/tlds/StringUtil_fn.tld" %>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<link rel="stylesheet" href="/lib/css/redmond/jquery-ui-1.9.1.custom.min.css" type="text/css">
<link rel="stylesheet" href="/lib/css/join.css" type="text/css">
<title>비회원 주문/배송조회</title>
<script type="text/javascript" src="/lib/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript" src="/lib/js/jquery.cookie.js"></script>
<script type="text/javascript" src="/lib/js/jquery.lck.util.js"></script>
<script type="text/javascript">
$(function(){
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
	var param = $("#recFrm").serializeObject();
	param.cart_no = cart_no;
	$("#modal_dialog").load("/popup/mypage/shopping/state/index.do?mode=nomember_pay_cancel", param);
	$("#modal_dialog").dialog("open");
}

function return_popup(cart_no){
	$("#modal_dialog").load("/popup/mypage/shopping/state/index.do?mode=return_popup", {cart_no : cart_no});
	$("#modal_dialog").dialog({title:'반품신청'}).dialog("open");
}

function return_send(cart_no){
	var param = $("#recFrm").serializeObject();
	param.cart_no = cart_no;
	$("#modal_dialog").load("/popup/mypage/shopping/state/index.do?mode=nomember_return_send", param);
	$("#modal_dialog").dialog("open");
}

function exchange_popup(cart_no){
	$("#modal_dialog").load("/popup/mypage/shopping/state/index.do?mode=exchange_popup", {cart_no : cart_no});
	$("#modal_dialog").dialog({title:'교환신청'}).dialog("open");
}

function exchange_send(cart_no){
	var param = $("#recFrm").serializeObject();
	param.cart_no = cart_no;
	$("#modal_dialog").load("/popup/mypage/shopping/state/index.do?mode=nomember_exchange_send", param);
	$("#modal_dialog").dialog("open");
}

function refunds_popup(cart_no){
	$("#modal_dialog").load("/popup/mypage/shopping/state/index.do?mode=refunds_popup", {cart_no : cart_no});
	$("#modal_dialog").dialog({title:'환불신청'}).dialog("open");
}

function refunds_send(cart_no){
	var param = $("#recFrm").serializeObject();
	param.cart_no = cart_no;
	$("#modal_dialog").load("/popup/mypage/shopping/state/index.do?mode=nomember_refunds_send", param);
	$("#modal_dialog").dialog("open");
}

function order_cancel(cart_no){
	if(confirm("주문접수를 취소하시겠습니까?")){
		$.ajax({
			url : "/popup/mypage/shopping/state/index.do?mode=order_cancel", 
			type: "POST", 
			data : {cart_no : cart_no}, 
			dataType : "json", 
			async: false, 
			cache : false, 
			success : function(data){
				if(data.rst == "1"){
					location.reload();
				}else{
					alert("주문접수 취소에 실패하였습니다.");
				}
			},
			error : function(data){
				alert("주문접수 취소에 실패하였습니다.");
			}
		});
	}
}

function track(cart_no){
	window.open("/popup/mypage/shopping/state/index.do?mode=track&cart_no="+cart_no,"delivery","width=1000,height=750, scrollbars=yes, resizable=yes"); 
}
</script>
</head>
<body>
	<div class="title_rocation">
      <div class="tr_wrap">
        <h3><img src="/images/join/no_mem_title.gif" alt="비회원 주문/배송조회"></h3>
      </div>
    </div>

    <div class="j_wrap">

      <div class="no_mem_visual">
        <strong class="c1">비회원으로 주문하신 내용은 다음과 같습니다.</strong>  주문/배송조회 이외의 서비스는 회원가입 후 이용이 가능합니다.<br>
        <strong class="c1">반품/환불 및 교환은 판매자와 협의 후 진행 하시기 바랍니다.</strong> <strong>(기타 문의사항은 파츠모아쇼핑몰 고객센터로 전화주시기 바랍니다)</strong>
      </div>
      
      <h5 class="no_mem_type" style="margin:45px 0 8px 0;">상품정보</h5>
      
      <table class="cart_style_1 t_top_style_1">
          <colgroup>
          <col width="12%">
          <col width="">
          <col width="12%">
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
          <c:set var="user_price" value="${user_price + (item.user_price * item.qty) }"/>
          <c:set var="discount_price" value="${discount_price + ((item.user_price * item.qty) - (item.sale_price * item.qty)) }"/>
          <tr>
            <td orderno="${item.orderno }">
				<p class="date">${data.resultInfo.orderno }</p>
                <p>${dtf:simpleDateFormat(data.resultInfo.orderdate, 'yyyy-MM-dd HH:mm:ss' , 'yyyy-MM-dd') }</p>
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
                  </p>
                </div>
              </div>
            </td>
            <td>
              <p class="status_back status_${item.status }">${item.status_nm }</p>
              <c:if test="${item.status eq '7' || item.status eq '8' }">
              <p class="btn_m2"><a href="#" onclick="track('${item.cart_no}')"><img src="/images/sub_2/btn_trace.gif" alt="배송추적"></a></p>
              </c:if>
            </td>
            <td class="b_none">
            	<c:if test="${item.status eq '99' }">
             		<p class="btn_m1"><a href="#" onclick="order_cancel('${item.cart_no}');"><img src="/images/sub_2/btn_type_b99.gif" alt="주문취소"></a></p>
            	</c:if>
            	<c:if test="${item.status eq '1' }">
             		<p class="btn_m1"><a href="#" onclick="cancel_popup('${item.cart_no}');"><img src="/images/sub_2/btn_type_b1.gif" alt="취소신청"></a></p>
            	</c:if>
            	<c:if test="${item.status eq '8' || item.status eq '18'}">
              		<p class="btn_m1"><a href="#"><img src="/images/sub_2/btn_type_a1.gif" alt="수취확인"></a></p>
            	</c:if>
            	<c:if test="${item.status eq '8' || item.status eq '18' }">
             		<p class="btn_m1"><a href="#" onclick="return_popup('${item.cart_no}');"><img src="/images/sub_2/btn_type_b3.gif" alt="반품신청"></a></p>
            	</c:if>
            	<c:if test="${item.status eq '8' }">
             		<p class="btn_m1"><a href="#" onclick="exchange_popup('${item.cart_no}');"><img src="/images/sub_2/btn_type_b4.gif" alt="교환신청"></a></p>
            	</c:if>
            	<c:if test="${item.status eq '12' }">
             		<p class="btn_m1"><a href="#" onclick="refunds_popup('${item.cart_no}');"><img src="/images/sub_2/btn_type_b5.gif" alt="환불신청"></a></p>
            	</c:if>
            	
            	<c:if test="${item.status eq '22' }">
            		<fmt:formatNumber value="${item.amt }"  /> 원
            		<a href="#"><img src="/images/sub_2/btn_type_a2.gif" alt="증빙서류발급"></a>
            	</c:if>
            </td>
          </tr>
		  </c:forEach>
          </tbody>

          </table>
          
          <h5 class="no_mem_type">결제내역</h5>
          
          <div class="cart_style_2">

            <table>
            <colgroup>
            <col width="20%">
            <col width="">
            </colgroup>
            <tbody>
            <tr>
              <th scope="row" rowspan="1">
              	<c:choose>
              		<c:when test="${data.resultInfo.paytyp eq 'card' }">
              			신용카드결제
              		</c:when>
              		<c:when test="${data.resultInfo.paytyp eq 'iche' }">
              			계좌이체
              		</c:when>
              		<c:when test="${data.resultInfo.paytyp eq 'hp' }">
              			핸드폰결제
              		</c:when>
              		<c:when test="${data.resultInfo.paytyp eq 'ars' }">
              			ARS결제
              		</c:when>
              		<c:when test="${data.resultInfo.paytyp eq 'virtual' }">
              			가상계좌결제
              		</c:when>
              	</c:choose>
              </th>
              <td>결제금액 : <b class="b_num">${suf:getThousand(data.resultInfo.payamt) }</b>원</td>
            </tr>
            </tbody>
            </table>

          </div>
          
          <h5 class="no_mem_type">결제금액</h5>
          
          <div class="pricecheck" style="margin-top:0;">

            <div class="p_check1">
              <div class="top">
                <span class="pt_l"><strong>정상가격</strong></span>
                <span class="pt_r">선택상품 : <b>${fn:length(data.list) }</b>개</span>
              </div>
              <div class="bottom">
                <p><b>${suf:getThousand(user_price) }</b>원</p>
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
              	<c:set var="actual_price" value="${user_price-discount_price }" scope="request"/>
                <p class="equal"><b>${suf:getThousand(user_price-discount_price) }</b>원</p>
              </div>
            </div>

          </div>
          
          <div class="info_b1">

            <div class="info_l">

              <h5 class="no_mem_type">주문회원 정보</h5>

              <div class="sub_table_1">
                <table>
                <colgroup>
                <col width="30%">
                <col width="">
                </colgroup>
                <tbody>
                <tr>
                  <th scope="row">주문자</th>
                  <td>${data.resultInfo.order_nm }</td>
                </tr>
                <tr>
                  <th scope="row">이메일</th>
                  <td>${data.resultInfo.email }</td>
                </tr>
                <tr>
                  <th scope="row">주문자 휴대폰</th>
                  <td>${data.resultInfo.rehp }</td>
                </tr>
                </tbody>
                </table>
              </div>

            </div>

            <div class="info_r">

              <h5 class="no_mem_type">배송정보 내역</h5>

              <div class="sub_table_1">
                <table>
                <colgroup>
                <col width="30%">
                <col width="">
                </colgroup>
                <tbody>
                <tr>
                  <th scope="row">수취인</th>
                  <td>${data.resultInfo.receiver }</td>
                </tr>
                <tr>
                  <th scope="row">배송지 주소</th>
                  <td>
                    <p>(${data.resultInfo.re_zipcd }) ${data.resultInfo.re_addr1 }</p>
                    <p>${data.resultInfo.re_addr2 }</p>
                  </td>
                </tr>
                <tr>
                  <th scope="row">수취인 휴대폰</th>
                  <td>${data.resultInfo.re_cell }</td>
                </tr>
                <tr>
                  <th scope="row">수취인 연락처</th>
                  <td>${data.resultInfo.re_tel }</td>
                </tr>
                </tbody>
                </table>
              </div>

            </div>

          </div>
          
          <h5 class="no_mem_type">배송시 요청사항</h5>
          
          <div class="sub_table_1">
            <table>
            <colgroup>
            <col width="20%">
            <col width="">
            </colgroup>
            <tbody>
            <tr>
              <th scope="row">배송시요청사항</th>
              <td class="request_type">
				<c:forEach var="item" items="${data.list }" varStatus="status">
                <div class="request_top">
                  <p class="request_c1"><strong>상품명</strong> : ${item.part3_nm } / ${item.carmodelnm } ${item.cargradenm } (${item.caryyyy })</p>
                  <p><strong>요청사항</strong> :  ${item.message }</p>
                </div>
				</c:forEach>
              </td>
            </tr>
            </tbody>
            </table>
          </div>
          <script type="text/javascript">
          $(document).ready(function(){
        	  $('td .request_top:last').css('background', 'none');
          });
          </script>
          
          <ul class="pay_c_list">
            <li class="c1"><strong>PARTSMOA</strong>는 통신판매중개자이며 통신판매의 당사자가 아닙니다. 따라서 <strong>PARTSMOA</strong>는 상품ㆍ거래정보 및 거래에 대하여 책임을 지지 않습니다.</li>
            <li>구매주문내역, 배송상태 확인, 구매영수증 출력, 구매취소/반품/교환은 사이트상단의 주문/배송조회에서 확인할 수 있습니다.</li>
            <li class="c1">
            고객님의 주문이 체결된 후 상품품절 및 단종 등에 의해 배송이 불가능할 경우, 전자상거래등에서의 소비자 보호에 관한 법률 제15조 2항에 의거하여
            3영업일(공휴일제외) 이내에 자동으로 취소될 수 있으며, 이 경우 취소 안내 메일이 고객님께 발송되오니 양지 바랍니다. 
            </li>
            <li>극히 일부 상품의 경우, 상품페이지의 팔자주문수량이 해당 상품의 실제 재고수량과 다를 수 있는 점 양해바랍니다. </li>
            <li>일부상품에 대해 수량부족, 카드결제승인오류 등의 사례가 간혹 있을 수 있으니 나의쇼핑정보에서 다시 한번 확인해주세요. </li>
            <li>무통장 입금으로 구매하셨을 경우, 입금 하신 후 판매자에게 입금확인이 되었는지를 다시 한번 확인해주세요. </li>
          </ul>

    </div>
<div id="modal_dialog" title="취소신청">
</div>
<form id="recFrm">
<input type="hidden" name="receiver" id="receiver" value="${param.receiver }"/>
<input type="hidden" name="orderno" id="orderno" value="${param.orderno }"/>
<input type="hidden" name="passwd" id="passwd" value="${param.passwd }"/>
</form>
</body>