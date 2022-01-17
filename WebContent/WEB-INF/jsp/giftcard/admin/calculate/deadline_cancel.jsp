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
});

function go_re(){
	
	if(!$('#date').val()){
		alert('재정산 일자를 선택해주세요');
		return;
	}
	
	if(!confirm('재정산 하시겠습니까?'))
		return;
	
	$('#frm').attr('action', '/giftcard/admin/calculate/deadline_re_proc.do');
	$('#frm').submit();
}

function go_cancel(){
	
	var retVal = new Array();
	$('.check_input').each(function() {
		if($(this).is(":checked"))
			retVal.push($(this).val()); 
    });
	
	if(retVal.join(',') == ''){
		alert("선택된 항목이 없습니다.");
		return;
	}
	
	if(!confirm('취소처리 하시겠습니까?'))
		return;
	
	$('#c_date').val(retVal.join("','"));
	$('#frm').attr('action', '/giftcard/admin/calculate/deadline_cancel_proc.do');
	$('#frm').submit();
}

</script>
</head>
<body>

<div id="main">
  <div class="titlebar">
    <h2>대기중 데이터 관리</h2>
    <div> <span>통합관리</span> &gt; <span>정산관리</span> &gt; <span class="bar_tx">대기중 데이터 관리</span> </div>
  </div>
  <div class="container"> 
    <!-- <div class="tab_menu">
      <ul class="tab">
        <li><a href="#">이달의 우수상점</a></li>
        <li class="on"><a href="#">상점소개</a></li>
      </ul>
    </div>  -->
    <div class="contents">
    <form action="/giftcard/admin/calculate/deadline_cancel.do" method = "post" id = "frm" name = "frm">
    <input type = "hidden" name = "c_date" id = "c_date" />
    <table class="style_1">
         <colgroup>
          <col width="10%" />
          <col width="*" />
          </colgroup>
        <tr>
          <th>특정일자 정산</th>
          <td>
            
            <input type="text" class="input_1 date" id="date" name="date" value = "${param.date }"/>
            <span class="bt_all"><span><input type="button" value="정산 " onclick="go_re();" class="btall"/></span></span>
          </td>
        </tr>
        
        <tr>
          <th>대기중<br />정산데이터</th>
          <td>
          <c:forEach items = "${deadline_list }" var = "vo">
          	<label>
              <input type="checkbox" class = "check_input"  value = "${vo.c_date }" />
              <span>${vo.c_date }</span>
            </label>
          </c:forEach>
          </td>
        </tr>
      </table>
      </form>
      <div class="btn_bottom">
        <div class="r_btn">
          <span class="bt_all"><span><input type="button" value="취소" onclick="go_cancel();" class="btall"/></span></span> 
          </div>
      </div>
      
    </div>
  </div>
</div>
</body>
</html>
