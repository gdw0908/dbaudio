<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>파츠모아 통합관리 시스템</title>
<link href="/lib/css/cmsbase.css" rel="stylesheet" type="text/css" />
<link href="/lib/css/cmsadmin.css" rel="stylesheet" type="text/css" />
<link href="/lib/css/redmond/jquery-ui-1.9.1.custom.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/lib/js/jquery-1.10.2.min.js"></script> 
<script type="text/javascript" src="/lib/js/jquery-ui-1.10.3.custom.min.js"></script> 
<script type = "text/javascript">
function go_insert()
{
	if(jQuery("input[name='makernm']").val() == "")
	{
		alert("유통사를 입력하세요.");
		jQuery("input[name=makernm]").focus();
		return;
	}
	else
	{
		if(confirm("유통사를 등록하시겠습니까?")) jQuery("form[name='cateProduce_insert']").submit();
	}
}

function go_update()
{
	if(confirm("유통사를 수정하시겠습니까?")) jQuery("form[name='cateProduce_modify']").submit();
}

function goPage(cpage){
	var f = document.searchProduce;
	f.method.value = "list";
	f.cpage.value = cpage;
	f.submit();
	return false;
}

function  go_delete()
{
	var carmakerseq = '${article.carmakerseq}';
	if(confirm("유통사를 삭제하시겠습니까?")) location.replace("${servletPath}?method=delete&cpage=${params.cpage}&rows=${params.rows}&carmakerseq=" + carmakerseq);
	
}


</script>
</head>
<body>
<div id="main">
  <div class="titlebar">
    <h2>카테고리관리</h2>
    <div> <span>통합관리</span> &gt; <span>상품관리</span> &gt; <span>상품목록</span> &gt;<span class="bar_tx">카테고리관리</span> </div>
  </div>
  <div class="container"> 
    <div class="tab_menu">
      <ul class="tab">
        <!-- <li ><a href="javascript:;" onclick = "goBody('/admin/goods/inventory/inventory_cate.do?codeno=0509');">공통코드</a></li> -->
        <li class="on"><a href="javascript:;" onclick = "goBody('/giftcard/admin/goods/inventory/inventory_cate2.do');">카테고리</a></li>
        <li><a href="javascript:;" onclick = "goBody('/giftcard/admin/goods/inventory/inventory_cate3.do');">모델명</a></li>
        <!-- <li><a href="javascript:;" onclick = "goBody('/admin/goods/inventory/inventory_cate4.do');">차량모델명</a></li>
        <li><a href="javascript:;" onclick = "goBody('/admin/goods/inventory/inventory_cate5.do');">부품정보</a></li> -->
      </ul>
    </div>
    <div class="contents">
       <div class="btn_bottom" style="margin-top: 0px;">
        <div class="r_btn">
	        <form name = "searchProduce" method = "post" action = "${servletPath }" onsubmit="return goPage(1);">
	        <input type="hidden" name="cpage" value="${params.cpage }" />
			<input type="hidden" name="method" value="list" />
			<input type = "hidden" name = "rows" value = "${params.rows }"/>
	        	<span style="font-weight: bold;">유통사</span>
	          	<input  name = "keyword" type="text" class="input_1" value="${params.keyword }"/>
	          	<span><input type = "image" src = "/images/admin/contents/s_btn_search.gif"/></span>
	        </form>
        </div>
      </div>
       <div class="category car">
         <div class="category_left carleft">
             <table class="style_4" style="margin-top:0px;">
               <colgroup>
                 <col width="25%" />
                 <col width="*" />
               </colgroup>
               <tr>
                 <th>번호</th>
                 <th>유통사명</th>
               </tr>
                <c:forEach items="${list }" var="produceList">
               <tr>
                 <td>${produceList.carmakerseq }</td>
                 <td style = "cursor:pointer;" onclick = "goBody('${servletPath }?seq=${produceList.carmakerseq }&keyword=${params.keyword }');">${produceList.makernm }</td>
               </tr>
               </c:forEach>
               
               
             </table>
             <jsp:include page="/WEB-INF/jsp/giftcard/paging.jsp" />
         </div>
         
         <div class="category_right carright">
           <table class="style_1">
             <colgroup>
               <col width="15%" />
               <col width="*" />
               <col width="15%" />
               <col width="*" />
             </colgroup>
             <tr>
               <th>유통사</th>
               <td>${article.makernm }</td>
               <th>사용여부</th>
               <td>${article.useyn }</td>
             </tr>
             <tr>
               <th>국/외산</th>
               <td colspan="3">
               <c:if test = "${not empty article.nation }">
               <c:choose>
               	<c:when test = "${article.nation eq 'Y' }">
               	국내
               	</c:when>
               	<c:otherwise>
               	해외
               	</c:otherwise>
               </c:choose>
               	</c:if>
               </td>
             </tr>
           </table>
           <div class="btn_bottom">
		        <div class="r_btn">
		        	<a href="#" id="dialog-link"><span class="bt_all"><span><input type="button" value="신규등록" onclick="" class="btall"/></span></span></a>
		        	<c:if test = "${not empty article.makernm }">
		          	<a href="#" id="dialog-link2"><span class="bt_all"><span><input type="button" value="수정" onclick="" class="btall"/></span></span></a>
			        <span class="bt_all"><span><input type="button" value="삭제" onclick="go_delete();" class="btall"/></span></span>
			       	</c:if>
		        </div>
		    </div>
         </div>
         <!-- 유통사 등록 팝업 (신규등록)-->
	     <div id="dialog" title="유통사등록" >
	     <form id = "cateProduce_insert" name = "cateProduce_insert" method = "post" action = "${servletPath }">
	     	<input type = "hidden" name = "ordernum" value = "${lastest.ordernum + 1 }"/>
	     	<input type = "hidden" name = "method" value = "insert"/>
	     	<input type = "hidden" name = "cpage" value = "${params.cpage }"/>
	     	<input type = "hidden" name = "rows" value = "${params.rows }"/>
	     	<input type = "hidden" name = "lastest_seq" value = "${lastest_seq.carmakerseq + 1}"/>
	     	<input type = "hidden" name = "maker_yn" value = "P"/>
	     	
		       <table class="style_1" style="table-layout: fixed;">
	             <colgroup>
	               <col width="15%;" />
	               <col width="*" />
	               <col width="15%;" />
	               <col width="*" />
	             </colgroup>
	            <tr>
	              <th>유통사</th>
	              <td>
	                <input type="text" name="makernm" id="makernm" class="input_1"/>
	              </td>
	              <th>사용여부</th>
	              <td>
	                <select title="선택 사용여부 확인" name = "useyn" id = "useyn">
	                  <option value = "Y" selected>사용</option>
	                  <option value = "N">사용안함</option>
	                </select>
	              </td>
	            </tr>
	            <tr>
	              <th>국/외산</th>
	              <td colspan="3">
	                <select title="국/외산 선택"  name = "nation" id = "nation">
	                  <option value = "Y" selected>국산</option>
	                  <option value = "N">해외</option>
	                </select>
	              </td>
	            </tr>
	           </table>
	           <div class="btn_bottom">
			        <div class="r_btn">
			         <span class="bt_all"><span><input type="button" value="등록" onclick="go_insert();" class="btall"/></span></span>
			        </div>
			    </div>
			  </form>
	     	</div>
	     
	     <!-- 유통사 등록 팝업 (수정)-->
	     <div id="dialog2" title="유통사수정" >
	     	<form id = "cateProduce_modify" name = "cateProduce_modify" method = "post" action = "${servletPath }">
	     		<input type = "hidden" name = "carmakerseq" value = "${article.carmakerseq}"/>
	     		<input type = "hidden" name = "method" value = "update"/>
	     		<input type = "hidden" name = "cpage" value = "${params.cpage }"/>
	     		<input type = "hidden" name = "rows" value = "${params.rows }"/>
	     		<input type = "hidden" name = "maker_yn" value = "P"/>
	     		<input type = "hidden" name = ordernum value = "${article.ordernum}"/>
	     		<input type = "hidden" name = "qry_method" value = "<c:choose><c:when test = "${article.maker_yn eq 'I' }">qry_insert</c:when><c:otherwise>qry_update</c:otherwise></c:choose>"/>
			       <table class="style_1" style="table-layout: fixed;">
		             <colgroup>
		               <col width="15%;" />
		               <col width="*" />
		               <col width="15%;" />
		               <col width="*" />
		             </colgroup>
		            <tr>
		              <th>유통사</th>
		              <td>
		                <input type="text" name="makernm" id="makernm" class="input_1" value = "${article.makernm }"/>
		              </td>
		              <th>사용여부</th>
		              <td>
		                <select title="선택 사용여부 확인" name = "useyn" id = "useyn">
		                  <option value = "Y" <c:if test = "${article.makernm  eq 'Y'}">selected</c:if>>사용</option>
		                  <option value = "N" <c:if test = "${article.makernm  eq 'N'}">selected</c:if>>사용안함</option>
		                </select>
		              </td>
		            </tr>
		            <tr>
		              <th>국/외산</th>
		              <td colspan="3">
		                <select title="국/외산 선택" name = "nation" id = "nation">
		                  <option value = "Y" <c:if test = "${article.nation  eq 'Y'}">selected</c:if>>국내</option>
		                  <option value = "N" <c:if test = "${article.nation  eq 'N'}">selected</c:if>>해외</option>
		                </select>
		              </td>
		            </tr>
		           </table>
	           <div class="btn_bottom">
			        <div class="r_btn">
			         <span class="bt_all"><span><input type="button" value="수정" onclick="go_update();" class="btall"/></span></span>
			        </div>
			    </div>
			 </form>
	     	</div>
           
       </div>
    </div>
  </div>
</div>

<script>
$("input.date").datepicker({
		showOn: "button",      
		buttonImage: "/images/admin/contents/calendar.png",
});

$( "#tabs" ).tabs();
$( "#dialog" ).dialog({
	autoOpen: false,
	width: 650,
});

// Link to open the dialog
$( "#dialog-link" ).click(function( event ) {
	$( "#dialog" ).dialog( "open" );
	event.preventDefault();
});

$( "#dialog2" ).dialog({
	autoOpen: false,
	width: 650,
});

// Link to open the dialog
$( "#dialog-link2" ).click(function( event ) {
	$( "#dialog2" ).dialog( "open" );
	event.preventDefault();
});

function goBody(target_url){
	parent.bodyFrame.location.href = target_url;
}
</script>

</body>
</html>
