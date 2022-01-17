<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="order_date" value="${vo.period }"/>
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
<script>
function go_proc(){
	if(!confirm('마감취소 하시겠습니까?'))
		return;
		
	$('#frm').attr('action', '/giftcard/admin/calculate/confirm_proc.do');
	$('#frm').submit();
}

function go_excel(){
	$('#frm').attr('action', '/giftcard/admin/calculate/confirm_excel.do');
	$('#frm').submit();
}
function dialog_pop(order_comseq,order_date,t){
	//return false;
	$("#modal_dialog2" ).dialog({
		autoOpen: false,
		modal : true,
		width:"1200",
		height:"700"
	});
	$("#modal_dialog2").load("/giftcard/admin/calculate/confirm_view_detail.do", {com_seq : order_comseq, odate : order_date, type:t});
	$("#modal_dialog2").dialog({title:'자세히보기'}).dialog("open");
}

function exceldown(order_comseq, selecttype, order_date){
	document.excelFrm.com_seq.value = order_comseq;
	document.excelFrm.odate.value = order_date;
	document.excelFrm.type.value = selecttype;
	document.excelFrm.submit();
}
</script>
</head>
<body>
<form action="/giftcard/admin/calculate/confirm_view.do" method = "post" id = "frm" name = "frm">
<input type = "hidden" name = "calculate_confirm_seq" value = "${params.calculate_confirm_seq }"/> 
</form>
<div id="main">
  <div class="titlebar">
    <h2>마감정산조회</h2>
    <div> <span>통합관리</span> &gt; <span>정산관리</span> &gt; <span>마감정산조회</span> &gt;<span class="bar_tx">상세보기</span> </div>
  </div>
  <div class="container"> 
    <div class="contents">
      <table class="style_1" style="table-layout: fixed;">
         <colgroup>
          <col width="10%" />
          <col width="*" />
          <col width="10%" />
          <col width="*" />
          </colgroup>
          
         <tr>
           <th>정산일시</th>
           <td>${vo.confirm_dt }</td>
           <th>등록자</th>
           <td>${vo.reg_nm }</td>
         </tr>
         
         <tr>
         	<th>정산코드</th>
           <td>${vo.calculate_confirm_seq }</td>
           <th>정산일수</th>
           <td>${vo.days }일</td>
         </tr>
      </table>
      <div class="dead_box">
        <div class="dead_list">
            <span>총 정산일자</span>
        </div>
        <div class="dead_day">
         ${vo.period }
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
          <th rowspan="2">총정산액<br/>( 판매금액 - 취소/환불액 - 본사수수료 )</th>
          <th rowspan="2">송금액<br/>( 판매금액 - 취소/환불액 - 본사수수료 )</th>
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
          <td><c:if test="${vo.com_nm != null }"><a href="javascript:dialog_pop('${vo.com_seq }','${order_date }','N')"></c:if>${vo.com_nm == null ? '합계' : vo.com_nm }<c:if test="${vo.com_nm != null }"></a></c:if></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.card}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.iche + vo.virtual}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.cancel_card_sum + vo.refund_card_sum}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.cancel_cash_sum + refund_cash_sum}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.total}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.order_sum}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.card_pg_commission}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.iche_pg_commission + vo.virtual_pg_commission}" /> </td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.u_m_commission + vo.c_m_commission}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.total}" /> - <fmt:formatNumber type="number" pattern="###,###" value="${vo.cancel_card_sum + vo.refund_card_sum + vo.cancel_cash_sum + refund_cash_sum}" /> - <fmt:formatNumber type="number" pattern="###,###" value="${vo.u_m_commission + vo.c_m_commission}" /></td>
          <td><fmt:formatNumber type="number" pattern="###,###" value="${vo.total - (vo.u_m_commission + vo.c_m_commission)}" /></td>
        </tr>
        </c:forEach>
      </table>
      
      <div class="btn_bottom">
        <div class="r_btn">
          <span class="bt_all"><span><input type="button" value="마감정산취소 " onclick="go_proc();" class="btall"/></span></span> 
          <span class="bt_all"><span><input type="button" value="엑셀다운로드" onclick="go_excel();" class="btall"/></span></span> 
          </div>
      </div>
        
    </div>
  </div>
</div>
    <div class="contents" id="modal_dialog2">
    </div>
    
<form id="excelFrm" name="excelFrm" action="/giftcard/admin/calculate/confirm_view_detail_excel.do" method="post">
<input type="hidden" name="com_seq">
<input type="hidden" name="odate">
<input type="hidden" name="type">
</form>
</body>
</html>

