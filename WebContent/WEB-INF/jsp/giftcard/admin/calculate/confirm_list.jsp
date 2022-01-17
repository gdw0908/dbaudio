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
	
	$("#cpage").val(1);
	$('#frm').attr('action', '/giftcard/admin/calculate/confirm_list.do');
	$('#frm').submit();
}

</script>
</head>
<body>
<div id="main">
  <div class="titlebar">
    <h2>마감정산등록</h2>
    <div> <span>통합관리</span> &gt; <span>정산관리</span> &gt; <span class="bar_tx">마감정산등록</span> </div>
  </div>
  <div class="container"> 
    <div class="contents">
      <form action="/giftcard/admin/calculate/confirm_list.do" method = "post" name = "frm" id = "frm">
      <input type = "hidden" name = "cpage" id = "cpage" />
      <table class="style_1">
         <colgroup>
          <col width="10%" />
          <col width="*" />
          </colgroup>
        <tr>
          <th>조회조건</th>
          <td>
            <select title="업체개별선택"  name = "com_nm" >
            	<option value = "">업체전체</option>
            	<c:forEach items = "${com_list }" var = "vo">
            	<option value = "${vo.com_nm }" <c:if test = "${vo.com_nm == params.com_nm}">selected</c:if>>${vo.com_nm }</option>
            	</c:forEach>
            </select>
          </td>
        </tr> 
        <tr>
          <th>날짜선택</th>
          <td>
          	<input type="text" class="input_1 date" id="start_date" name = "start_date" value = "${params.start_date }" />
              ~
            <input type="text" class="input_1 date" id="end_date" name = "end_date" value = "${params.end_date }" />
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
          <col width="15%" />
          <col width="12%" />
          <col width="13%" />
          <col width="5%" />
          <col width="*" />
        </colgroup>
        <tr>
          <th>번호</th>
          <th>정산처리일시</th>
          <th>정산코드</th>
          <th>정산기간</th>
          <th>정산일수</th>
          <th>정산포함업체명</th>
        </tr>
        
        <c:forEach items = "${list }" var = "vo">
        <tr>
          <td>${page_info.totalcount - vo.rn + 1}</td>
          <td>${vo.confirm_dt}</td>
          <td class="blue"><a href="/giftcard/admin/calculate/confirm_view.do?calculate_confirm_seq=${vo.calculate_confirm_seq }">${vo.calculate_confirm_seq }</a></td>
          <td>${vo.period}</td>
          <td>${vo.days}</td>
          <td class="left" >
            ${vo.cooperations}
          </td>
        </tr>
        </c:forEach>
        
        
        
      </table>
      <!-- <div class="paging"> <span id="pagingWrap"> <a class="p_first2" href="#">&lt;&lt;</a> <a class="p_first" href="#">&lt;</a> <a href="#" class="on">1</a> <a href="#">2</a> <a href="#">3</a> <a href="#">4</a> <a href="#">5</a> <a class="p_last" href="#">&gt;</a> <a class="p_last2" href="#">&gt;&gt;</a> </span> </div>
       -->
      <jsp:include page="/giftcard/inc/paging2.do">
				<jsp:param  name="cpage" value="${param.cpage }"/>
				<jsp:param  name="rows" value="${param.rows }"/>
				<jsp:param  name="totalpage" value="${page_info.totalpage }"/>
			</jsp:include>
      
      
      
        
        
    </div>
  </div>
</div>
</body>
</html>
