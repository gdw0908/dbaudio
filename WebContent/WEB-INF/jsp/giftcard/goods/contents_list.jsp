<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<title>상품검색</title>
<link rel="stylesheet" href="/lib/css/sub.css" type="text/css">
<script type="text/javascript">
$(document).ready(function(){
	$(window).scrollTop($("#content_view").offset().top);
//#submenu

$("#submenu_open").toggle(function(){
$("#sub_menu").slideToggle(250);
this.src = "/images/sub/sub_menu_close.gif";
}, function() {
$("#sub_menu").slideToggle(250);
this.src = "/images/sub/sub_menu_open.gif";
});

});
function changeSido(){
	$.getJSON("/json/list/code.sigungu.do", {sido: $("#sido").val()}, function(data){
		$("#sigungu").empty();
		$("#sigungu").append("<option value=''>시/군/구</option>");
		$.each(data, function(i, o){
			$("#sigungu").append("<option value='"+o.sigungu+"'>"+o.dong_nm+"</option>");
		});
	});
}

function changeSigungu(){
	$.getJSON("/json/list/code.dong.do", {sido: $("#sido").val(), sigungu: $("#sigungu").val()}, function(data){
		$("#dong").empty();
		$("#dong").append("<option value=''>읍/면/동</option>");
		$.each(data, function(i, o){
			$("#dong").append("<option value='"+o.dong+"'>"+o.dong_nm+"</option>");
		});
	});
}

function goSubmit(){
	$("#frm").submit();
}


function search_cooperation(f){
	f.submit();
}
function search_cooperation_reset(f){
	f.sido.value = "";
	f.sigungu.value = "";
	f.dong.value = "";
}
</script>
</head>

<body>
    <div class="gnb_wrap">
        <!-- gnb메뉴 -->
		<page:applyDecorator name="gnb" />
        <!-- //gnb메뉴 -->
    </div>

    <div class="sub_line_bg bg7">
      <div class="sub_line sub_lineg7">
        <div class="sl_wrap">
          <div class="sl_l" id="content_view">
          	<c:if test="${param.part2 == '050901006003'}">
            <h3><img src="/images/sub/title_7_1.gif" alt="알터네이터"></h3>
            </c:if>
            <c:if test="${param.part2 == '050901006001' }">
            <h3><img src="/images/sub/title_7_2.gif" alt="A/C콤프레서"></h3>
            </c:if>
            <c:if test="${param.part2 == '050901006002' }">
            <h3><img src="/images/sub/title_7_3.gif" alt="스타트모터"></h3>
            </c:if>
          </div>
          <div class="sl_r">
          </div>
        </div>
      </div>

      <!-- all_menu on -->
      <div class="sm_wrap sm7">
        <div class="sub_menu">
        </div>
      </div>
    </div>
    <!-- all_menu on End -->

    <div class="sub_wrap">
      <div class="sub_contents">
        <div class="sub_contents_img">
        	<c:if test="${param.part2 == '050901006003'}">
      		<p><img src="/images/sub/contents_img_01.gif" alt="알터네이터 문의"></p>
      		</c:if>
      		<c:if test="${param.part2 == '050901006001'}">
      		<p><img src="/images/sub/contents_img_02.gif" alt="A/C콤프레서 문의"></p>
      		</c:if>
      		<c:if test="${param.part2 == '050901006002'}">
      		<p><img src="/images/sub/contents_img_03.gif" alt="스타트모터 문의"></p>
      		</c:if>
      	</div>
      </div>
    </div>
    
</body>
</html>
