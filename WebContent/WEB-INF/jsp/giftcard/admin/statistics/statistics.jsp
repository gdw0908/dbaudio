<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
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
			$('#date_wrap').hide();
			$('#start_date').val('');
			$('#end_date').val('');
		}else{
			$('#date_wrap').show();
		}
	});
});

function go_search(){
	if($('#start_date').val() != '' && $('#end_date').val() == ''){
		alert('검색기간을 선택해주세요.');
		$('#end_date').focus();
		return;
	}
	
	if($('#end_date').val() != '' && $('#start_date').val() == ''){
		alert('검색기간을 선택해주세요.');
		$('#start_date').focus();
		return;
	}
	
	$('#frm').attr('action', '/giftcard/admin/statistics/statistics.do');
	$('#frm').submit();
}

function go_excel(){
	
	<c:if test="${empty list }">
	alert('조회된 데이터가 없습니다.');
	return;
	</c:if>
	
	$('#frm').attr('action', '/giftcard/admin/statistics/statistics_excel.do');
	$('#frm').submit();
}

</script>
</head>
<body>
<div id="main">
  <div class="titlebar">
    <h2>통계관리</h2>
    <div> <span>통합관리</span> &gt; <span>통계관리</span> &gt; <span class="bar_tx">통계관리</span> </div>
  </div>
  <div class="container">
    <div class="contents">
    <form action="/giftcard/admin/statistics/statistics.do" method = "post" name = "frm" id = "frm">
      <table class="style_1">
         <colgroup>
          <col width="10%" />
          <col width="*" />
          </colgroup>
        <tr>
          <th>기간선택</th>
          <td>
            <td>
            <label>
              <input type="radio" name="term_radio" value = "1" <c:if test="${empty params.start_date }">checked</c:if>/>
              <span>기간전체</span>
            </label>
            
            <label>
              <input type="radio" name="term_radio" value = "2" <c:if test="${not empty params.start_date }">checked</c:if>/>
              <span>기간선택</span><br />
            </label>
            
            <p id = "date_wrap" style = "display:${empty params.start_date ? 'none' : 'block'};">
          	<input type="text" class="input_1 date" id="start_date" name = "start_date" value = "${params.start_date }" />
              ~
            <input type="text" class="input_1 date" id="end_date" name = "end_date" value = "${params.end_date }" />
          	</p>
          </td>
        </tr>
      </table>
      <div class="btn_bottom">
        <div class="r_btn">
          <span class="bt_all"><span><input type="button" value="조회 " onclick="go_search();" class="btall"/></span></span> 
          </div>
      </div>
     </form>
     
      <table class="style_6">
        <colgroup>
          <col width="5%" />
          <col width="7%" />
          <col width="6%" />
          <col width="6%" />
          <col width="6%" />
          <col width="6%" />
          <col width="6%" />
          <%-- <col width="6%" />
          <col width="6%" />
          <col width="6%" /> --%>
          <col width="*" />
        </colgroup>
        <tr>
          <th rowspan="2">업체명</th>
          <th colspan="4">주문</th>
          <!-- <th colspan="3">반품</th> -->
          <th rowspan="2" colspan="2">합계</th>
        </tr>
        <tr>
          <th>주문건수</th>
          <th>주문수량</th>
          <th colspan="2">금액</th>
          <!-- <th>반품건수</th>
          <th>반품수량</th>
          <th>금액</th> -->
        </tr>
        
        <c:forEach items = "${list }" var = "vo">
        <tr>
          <td>${vo.com_nm == null ? '합계' : vo.com_nm }</td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.u_cnt + vo.c_cnt}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.u_qty + vo.c_qty}" /></td>
          <td colspan="2"><fmt:formatNumber type="number" pattern="###,###" value="${vo.u_sum + vo.c_sum}" /></td>
          <%-- <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.refund_cash_cnt + vo.refund_card_cnt}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.refund_cash_qty + vo.refund_card_qty}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.refund_cash_sum + vo.refund_card_sum}" /></td> --%>
          <td colspan="2"><fmt:formatNumber type="number" pattern="###,###" value="${(vo.u_sum + vo.c_sum)}" /></td>
        </tr>
        </c:forEach>
      </table>
      <div class="btn_bottom">
        <div class="r_btn"> 
          <span class="bt_all"><span><input type="button" value="엑셀다운로드" onclick="go_excel();" class="btall"/></span></span> 
          </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
