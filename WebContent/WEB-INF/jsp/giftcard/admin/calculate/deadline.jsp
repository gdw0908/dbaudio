<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="c_date" value="${fn:replace(params.c_date, \"'\", '') }" />
<c:set var="c_date_array" value="${fn:split(c_date, ',') }" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>파츠모아 통합관리 시스템</title>
<link href="/lib/css/cmsbase.css" rel="stylesheet" type="text/css" />
<link href="/lib/css/cmsadmin.css" rel="stylesheet" type="text/css" />
<link href="/lib/css/redmond/jquery-ui-1.9.1.custom.min.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="/lib/css/redmond/jquery-ui-1.9.1.custom.min.css" type="text/css">
<script type="text/javascript" src="/lib/js/jquery-1.10.2.min.js"></script> 
<script type="text/javascript" src="/lib/js/jquery-ui-1.10.3.custom.min.js"></script> 
<script type="text/javascript" src="/lib/js/jquery.ui.datepicker-ko.js"></script>

<script>
$(document).ready(function(){
	$("input.date").datepicker({
		showOn: "button",      
		buttonImage: "/images/admin/contents/calendar.png"
	});
	
	$('input[name=term_radio]').click(function(){
		if($(this).val() == "1"){
			$('.check_input').prop("checked", true);
			$('#date_wrap').hide();
			$('#s_date').val('');
			$('#e_date').val('');
		}else{
			$('.check_input').prop("checked", false);
			$('#date_wrap').show();
		}
	});
	<c:forEach items = "${c_date_array }" var = "vo">
	$('.check_input[value=${vo}]').prop("checked", true);
	</c:forEach>
});

function select_term(){
	var s_date = $('#s_date').val();
	var e_date = $('#e_date').val();
	$('.check_input').each(function(){
		var c_date = $(this).val();
		if(c_date >= s_date && c_date <= e_date)
			$(this).prop("checked", true);
		else
			$(this).prop("checked", false);
	});
}

function go_search(){
	
	var retVal = new Array();
	$('.check_input').each(function() {
		if($(this).is(":checked"))
			retVal.push($(this).val()); 
    });
	
	if(retVal.join(',') == ''){
		alert("선택된 항목이 없습니다.");
		return;
	}
	
	$('#c_date').val(retVal.join("','"));
	$('#frm').attr('action', '/giftcard/admin/calculate/deadline.do');
	$('#frm').submit();
}

function go_proc(){
	
	<c:if test="${empty list }">
	alert('조회된 데이터가 없습니다.');
	return;
	</c:if>
	
	if(!confirm('마감처리 하시겠습니까?'))
		return;
		
	$('#frm').attr('action', '/giftcard/admin/calculate/deadline_proc.do');
	$('#frm').submit();
}

function go_excel(){
	
	<c:if test="${empty list }">
	alert('조회된 데이터가 없습니다.');
	return;
	</c:if>
	
	$('#frm').attr('action', '/giftcard/admin/calculate/deadline_excel.do');
	$('#frm').submit();
}
</script>
</head>
<body>
<form action="/giftcard/admin/calculate/deadline.do" method = "post" id = "frm" name = "frm">
<input type = "hidden" name = "c_date" id = "c_date" value = "${params.c_date }"/> 
</form>
<div id="main">
  <div class="titlebar">
    <h2>마감정산등록</h2>
    <div> <span>통합관리</span> &gt; <span>정산관리</span> &gt; <span class="bar_tx">마감정산등록</span> </div>
  </div>
  <div class="container"> 
    <!-- <div class="tab_menu">
      <ul class="tab">
        <li><a href="#">이달의 우수상점</a></li>
        <li class="on"><a href="#">상점소개</a></li>
      </ul>
    </div>  -->
    <div class="contents">
    <c:choose>
    <c:when test="${empty deadline_list }">
    대기중 정산데이터가 없습니다.
    </c:when>
    <c:otherwise>
    <table class="style_1">
         <colgroup>
          <col width="10%" />
          <col width="*" />
          </colgroup>
        <tr>
          <th>기간선택</th>
          <td>
            <label>
              <input type="radio" name="term_radio" value = "1" <c:if test="${empty params.c_date }">checked</c:if>/>
              <span>정산대기 전체선택</span>
            </label>
            
            <label>
              <input type="radio" name="term_radio" value = "2" <c:if test="${not empty params.c_date }">checked</c:if>/>
              <span>기간선택</span><br />
            </label>
            
            <p id = "date_wrap" style = "display:${empty params.c_date ? 'none' : 'block'};">
            <input type="text" class="input_1 date" id="s_date" />
              ~
            <input type="text" class="input_1 date" id="e_date" />
            <span class="bt_all"><span><input type="button" value="선택 " onclick="select_term();" class="btall"/></span></span>
            </p>
            
          </td>
        </tr>
        <tr>
          <th>대기중<br />정산데이터</th>
          <td>
          <c:forEach items = "${deadline_list }" var = "vo">
          <c:choose>
          <c:when test="${empty params.c_date }"><c:set var = "checked" value = "checked" /></c:when>
          <c:otherwise>
          <c:set var = "checked" value = "" />
          <c:forEach items = "${c_date_array }" var = "search">
          <c:if test="${search eq vo.c_date }"><c:set var = "checked" value = "checked" /></c:if>
          </c:forEach>
          </c:otherwise>
          </c:choose>
          	<label>
              <input type="checkbox" class = "check_input"  value = "${vo.c_date }" ${checked }/>
              <span>${vo.c_date }</span>
            </label>
          </c:forEach>
          </td>
        </tr>
      </table>
      <div class="btn_bottom">
        <div class="r_btn">
          <span class="bt_all"><span><input type="button" value="조회 " onclick="go_search();" class="btall"/></span></span> 
          </div>
      </div>
    </c:otherwise>
    </c:choose>
      
      
      <c:if test="${!(params.c_date == null || params.c_date eq '')}">
      <div class="dead_box">
        <div class="dead_list">
            <span>조회된 일일정산데이터</span>
            <span class="dead_listright">정산대기 일자 수 : ${fn:length(c_date_array) }건 </span>
        </div>
        <div class="dead_day">
        ${c_date }
        </div>
      </div>
      
      <table class="style_6">
        <colgroup>
          <col width="10%" />
          <col width="7%" />
          <col width="6%" />
          <col width="6%" />
          <col width="6%" />
          <col width="6%" />
          <col width="6%" />
          <col width="6%" />
          <col width="6%" />
          <col width="6%" />
          <col width="12%" />
          <col width="*" />
        </colgroup>
        <tr>
          <th rowspan="2">업체명</th>
          <th colspan="2">판매금액</th>
          <th colspan="2">취소/환불 총액</th>
          <th rowspan="2">판매총액</th>
          <th rowspan="2">주문총액</th>
          <th colspan="3">공제내역</th>
          <th rowspan="2">총정산액</th>
          <th rowspan="2">송금액</th>
        </tr>
        <tr>
          <th>카드</th>
          <th>현금</th>
          <th>카드</th>
          <th>현금</th>
          <th>카드</th>
          <th>현금</th>
          <th>본사수수료</th>
        </tr>
        
        <c:forEach items = "${list }" var = "vo">
        <tr>
          <td>${vo.com_nm == null ? '합계' : vo.com_nm }</td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.card}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.iche + vo.virtual}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.cancel_card_sum + vo.refund_card_sum}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.cancel_cash_sum + refund_cash_sum}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.total}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.order_sum}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.card_pg_commission}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.iche_pg_commission + vo.virtual_pg_commission}" /> </td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.u_m_commission + c_m_commission}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.total}" /> - <fmt:formatNumber type="number" pattern="###,###" value="${vo.cancel_card_sum + vo.refund_card_sum + vo.cancel_cash_sum + refund_cash_sum}" /> - <fmt:formatNumber type="number" pattern="###,###" value="${vo.card_pg_commission + vo.iche_pg_commission + vo.virtual_pg_commission + vo.u_m_commission + c_m_commission}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.total - (vo.cancel_card_sum + vo.refund_card_sum + vo.cancel_cash_sum + refund_cash_sum) - (vo.u_m_commission + c_m_commission)}" /></td>
        </tr>
        </c:forEach>
        
      </table>
      <div class="btn_bottom">
        <div class="r_btn">
          <span class="bt_all"><span><input type="button" value="마감처리 정산등록 " onclick="go_proc();" class="btall"/></span></span> 
          <span class="bt_all"><span><input type="button" value="엑셀다운로드" onclick="go_excel();" class="btall"/></span></span> 
          </div>
      </div>
      </c:if>
    </div>
  </div>
</div>
</body>
</html>
