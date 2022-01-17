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
<script type = "text/javascript">
function goBody(target_url){
	parent.bodyFrame.location.href = target_url;
}

function goPage(cpage){
	var f = document.carNameSearchForm;
	f.method.value = "list";
	f.cpage.value = cpage;
	f.submit();
	return false;
}

function showCarNameList(seq)
{
	location.replace("${servletPath}?method=list&cpage=${params.cpage}&rows=${params.rows}&condition="+seq);
}

function go_insert()
{
	if(jQuery("input[name='carmodelnm']").val() == "")
	{
		alert("상품권금액을 입력하세요.");
		jQuery("input[name=carmodelnm]").focus();
		return;
	}
	else
	{
		if(confirm("상품권금액을 등록하시겠습니까?")) jQuery("form[name='cateCarName_insert']").submit();
	}
}

function go_delete(seq)
{
	if(seq == "" || seq == null)
	{
		alert("삭제할 대상이 없습니다.");
	}
	else
	{
		if(confirm("삭제하시겠습니까?")) location.replace("${servletPath}?method=delete&cpage=${params.cpage}&rows=${params.rows}&condition="+seq);
	}
}

function go_update()
{
	if(confirm("수정하시겠습니까?")) jQuery("form[name='carNameModify']").submit();
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
        <!-- <li><a href="javascript:;" onclick = "goBody('/admin/goods/inventory/inventory_cate.do?codeno=0509');">공통코드</a></li> -->
        <li><a href="javascript:;" onclick = "goBody('/giftcard/admin/goods/inventory/inventory_cate2.do');">유통사</a></li>
        <li class="on"><a href="javascript:;" onclick = "goBody('/giftcard/admin/goods/inventory/inventory_cate3.do');">상품권</a></li>
        <!-- <li><a href="javascript:;" onclick = "goBody('/admin/goods/inventory/inventory_cate4.do');">차량모델명</a></li>
        <li><a href="javascript:;" onclick = "goBody('/admin/goods/inventory/inventory_cate5.do');">부품정보</a></li> -->
      </ul>
    </div>
    <div class="contents">
       <div class="btn_bottom" style="margin-top: 0px;">
       <form name="carNameSearchForm" action="${servletPath }" onsubmit="return goPage(1);" method = "post">
		<input type="hidden" name="cpage" value="${params.cpage }" />
		<input type="hidden" name="method" value="list" />
		<input type = "hidden" name = "rows" value = "${params.rows }"/>
		<input type = "hidden" name = "searchChk" value = "1"/>
	        <div class="r_btn">
	          <select title="유통사선택" name="condition" id="condition" class="cho2">
	            <option value = "">유통사선택</option>
	            <c:forEach items="${comInfo }" var="comInfoList">
	            <option value = "${comInfoList.carmakerseq }" <c:if test = "${params.condition eq comInfoList.carmakerseq }">selected</c:if>>${comInfoList.makernm }</option>	
	            </c:forEach>
	          </select>
	          <label>
	            <span style="font-weight: bold; padding: 0px 5px 0px 15px;">상품권금액</span><input type="text" class="input_1" id="keyword" name="keyword" value = "${params.keyword }"/>
	          </label>
	          <span><input type = "image" src = "/images/admin/contents/s_btn_search.gif"/></span>
	        </div>
        </form>
      </div>
       <div class="category car">
         <div class="category_left carleft">
             <table class="style_5" style="margin-top:0px;">
               <colgroup>
                 <col width="25%" />
                 <col width="*" />
                 <col width="*" />
               </colgroup>
                <tr>
                 <th>번호</th>
                 <th>유통사명</th>
                 <th>상품권금액</th>
               </tr>
                <c:forEach items="${list }" var="carNameList">
                
                <tr>
                 <td>${carNameList.carmodelseq }</<td>
              	 <td><a href = "javascript:;" onclick = "goBody('${servletPath}?seq=${carNameList.carmodelseq }&keyword=${params.keyword }&cpage=${params.cpage }&rows=${params.rows }');">${carNameList.makernm }</a></<td>
                 <td><a href = "javascript:;" onclick = "goBody('${servletPath}?seq=${carNameList.carmodelseq }&keyword=${params.keyword }&cpage=${params.cpage }&rows=${params.rows }');">${carNameList.carmodelnm }</a></td>
               </tr>
                </c:forEach>
             
             </table>
             <jsp:include page="/WEB-INF/jsp/giftcard/paging.jsp" />
         </div>
         <div class="category_right carright">
           <table class="style_1" style="table-layout: fixed;">
             <colgroup>
               <col width="15%" />
               <col width="*" />
               <col width="15%" />
               <col width="*" />
             </colgroup>
             <tr>
               <th>유통사</th>
               <td>${view.makernm }</td>
               <th>상품권금액</th>
               <td>${view.carmodelnm }</td>
             </tr>
             <tr>
               <th>사용여부</th>
               <td colspan="3">${view.useyn }</td>
             </tr>
           </table>
           <div class="btn_bottom">
		        <div class="r_btn">
		       	<c:choose>
		        	<c:when test = "${view.carmodelseq eq null || view.carmodelseq eq ''}">
		        		 <a href="#" id="dialog-link"><span class="bt_all"><span><input type="button" value="신규등록" onclick="" class="btall"/></span></span></a>
		        	</c:when>
		        	<c:otherwise>
		        		<a href="#" id="dialog-link"><span class="bt_all"><span><input type="button" value="신규등록" onclick="" class="btall"/></span></span></a>
		          		<a href="#" id="dialog-link2"><span class="bt_all"><span><input type="button" value="수정" onclick="" class="btall"/></span></span></a>
		         		<span class="bt_all"><span><input type="button" value="삭제" onclick="go_delete('${view.carmodelseq }');" class="btall"/></span></span>
		        	</c:otherwise>
		        </c:choose>
		        </div>
		    </div>
         </div>
         <!-- 유통사 등록 팝업 (등록)-->
         
		     <div id="dialog" title="유통사등록" >
		      <form id = "cateCarName_insert" name = "cateCarName_insert" method = "post" action = "${servletPath }">
		     	<input type = "hidden" name = "method" value = "insert"/>
		     	<input type = "hidden" name = "cpage" value = "${params.cpage }"/>
		     	<input type = "hidden" name = "rows" value = "${params.rows }"/>
		     	<input type = "hidden" name = "lastest_seq" value = "${lastest_seq.carmodelseq + 1}"/>
		     	<input type = "hidden" name = "model_yn" value = "P"/>
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
	               <select title="유통사선택" name="condition_insert" id="condition_insert" class="cho2">
		            <c:forEach items="${comInfo }" var="comInfoList">
		            <option value = "${comInfoList.carmakerseq }">${comInfoList.makernm }</option>	
		            </c:forEach>
		          </select>
	              </td>
	              <th>상품권금액</th>
	              <td>
	                <input type="text" name="carmodelnm_insert" id="carmodelnm_insert" class="input_1"/>
	              </td>
	            </tr>
	            <tr>
	              <th>사용여부</th>
	              <td colspan="3">
	                 <select title="선택 사용여부 확인" name = "useyn_insert" id = "useyn_insert">
	                  <option value = "Y" selected>사용</option>
	                  <option value = "N" >사용안함</option>
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
	    
	 	     <div id="dialog2" title="유통사등록" >
	 	     	 <form name = "carNameModify" method = "post" action = "${servletPath }">
			     	<input type = "hidden" name = "carmodelseq" value = "${view.carmodelseq}"/>
		     		<input type = "hidden" name = "method" value = "update"/>
		     		<input type = "hidden" name = "cpage" value = "${params.cpage }"/>
		     		<input type = "hidden" name = "rows" value = "${params.rows }"/>
		     		<input type = "hidden" name = "model_yn" value = "P"/>
		     		<input type = "hidden" name = "carmakerseq" value = "${view.carmakerseq }"/>
		     		<input type = "hidden" name = "qry_method" value = "<c:choose><c:when test = "${view.model_yn eq 'I' }">qry_insert</c:when><c:otherwise>qry_update</c:otherwise></c:choose>"/>
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
			                 <select title="유통사선택" name="condition" id="condition" class="cho2">
					            <option value = "">유통사선택</option>
					            <c:forEach items="${comInfo }" var="comInfoList">
					            <option value = "${comInfoList.carmakerseq }" <c:if test = "${comInfoList.carmakerseq eq view.carmakerseq }">selected</c:if>>${comInfoList.makernm }</option>	
					            </c:forEach>
					          </select>
			              </td>
			              <th>상품권금액</th>
			              <td>
			                <input type="text" name="makernm" id="makernm" class="input_1" value = "${view.carmodelnm }"/>
			              </td>
			            </tr>
			            <tr>
			              <th>사용여부</th>
			              <td colspan="3">
			                <select title="선택 사용여부 확인" name = "useyn" id = "useyn">
			                  <option value = "Y">사용</option>
			                  <option value = "N" >사용안함</option>
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
<script type="text/javascript" src="/lib/js/jquery-1.10.2.min.js"></script> 
<script type="text/javascript" src="/lib/js/jquery-ui-1.10.3.custom.min.js"></script> 
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


</script>
</body>
</html>
