<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld" %>
<%@ taglib prefix="suf" uri="/WEB-INF/tlds/StringUtil_fn.tld" %>
<c:if test="${((pageContext.request.requestURI) eq '/' or (pageContext.request.requestURI) eq '/giftcard/index.do')}" >
<script type="text/javascript">
function setCookie( name, value, expiredays ) { 
	var todayDate = new Date(); 
	todayDate.setDate( todayDate.getDate() + expiredays ); 
	document.cookie = name + "=" + escape( value ) + "; path=/; expires=" + todayDate.toGMTString() + ";" 
} 
function getCookie(cname) {
	var name = cname + "=";
	var ca = document.cookie.split(';');
	for(var i=0; i<ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1);
		if (c.indexOf(name) != -1) {
			return c.substring(name.length, c.length);
		}
	}
	return "";
}
function closeWin(seq) {
	if(document.getElementById("chkbox"+seq).checked){ 
  		setCookie( "maindiv"+seq, seq , 1 ); 
	}
 	$('#divpop'+seq).css("visibility","hidden");
}

$( document ).ready(function() {
<c:forEach var="item" items="${list.layer_popup }" varStatus="status">
	if(getCookie("maindiv${item.popupzone_seq}") != '${item.popupzone_seq}'){$("#divpop${item.popupzone_seq}").css("visibility","visible");}
</c:forEach>

});
</script>
<script type="text/javascript" src="/lib/js/gnb.js"></script>
<c:if test="${fn:length(list.layer_popup) > 0 }">
<!-- main_pop -->
<div class="pop_wrap">
	<c:forEach var="item" items="${list.layer_popup }" varStatus="status">
    <div class="divpop" id="divpop${item.popupzone_seq}" style="visibility:hidden;top:${item.y_coord}px;left:${item.x_coord}px;">
      <div class="divpopa">
        <a <c:choose><c:when test="${item.link_yn == 'Y' }">href="${item.link_url }" target="${item.link_target }"</c:when><c:otherwise>href="#"</c:otherwise></c:choose>><img src="${item.file_path }" alt="${item.alt }"></a>
      </div>
      <form name="notice_form">
        <div class="divpopb">
          <label><input type="checkbox" name="chkbox" id="chkbox${item.popupzone_seq }" value="${item.popupzone_seq }" onchange="closeWin('${item.popupzone_seq }')">
          <span><img src="/images/header/img201503041_03.png" alt="오늘 하루동안 닫기" /></span></label>
        </div>
        <div class="divpopc">
          <a href="javascript:closeWin('${item.popupzone_seq }');"><img src="images/container/pop_close.png" alt="닫기"></a>
        </div>
    </form>
    </div>
    
    </c:forEach>
  </div>
  <!-- //main_pop -->
</c:if>
  <!-- 탑 슬라이드 -->
  <c:if test="${fn:length(list.top_popup) > 0 }">
  <div class="h_slide">
    <div class="hs_wrap">
    <c:forEach var="item" items="${list.top_popup }" varStatus="status">
      <a class="link_${status.count }" <c:choose><c:when test="${item.link_yn == 'Y' }">href="${item.link_url }" target="${item.link_target }"</c:when><c:otherwise>href="#"</c:otherwise></c:choose>><img src="${item.file_path }" alt="${item.alt }" style="height:69px;"></a>
    </c:forEach>
    </div>
    <span class="h_pop_close"><a href="#" id="pop_close"><img src="images/header/popup_close.png" onClick="popup_close();" alt="배너닫기"></a></span>
  </div>
  </c:if>
  <!-- //탑 슬라이드 -->
</c:if>
	
<div class="mo_bg"></div>

<div id="header">

	<div class="mo_logo"><a href="/giftcard/index.do"><img src="/images/logo/logo.svg" alt="페어링오디오"></a></div>
	
	<!--  hamburger menu  -->
	<div class="ham_wrap">
		<a href="#"></a>
		<a href="#"></a>
		<a href="#"></a>
	</div>
	
	<!--  mobail menu -->
	<div class="mo_menu_wrap">
		<nav>
			<ul class="mo_menu">
				<!-- <li><a href="/giftcard/goods/list.do?menu=menu1">갤러리아</a></li>
				<li><a href="/giftcard/goods/list.do?menu=menu2">신세계</a></li>
				<li><a href="/giftcard/goods/list.do?menu=menu3">롯데</a></li> -->
				<c:forEach var="item" items="${list.category }" varStatus="status">
					<li><a href="/giftcard/goods/list.do?menu=menu${status.count}">${item.makernm }</a></li>
				</c:forEach>		
				<li><a href="/giftcard/mypage/notice/index.do">고객센터</a></li>
			</ul>
			
			<ul class="mo_member_wrap">
        		<c:if test="${empty sessionScope.member }">
        			<li class="last"><a href="/giftcard/join/join_2.do">가입하기</a></li>
        		</c:if>
        		<c:if test="${!empty sessionScope.member }">
        		<c:if test = "${sessionScope.member.group_seq eq '2' or sessionScope.member.group_seq eq '9' }">
					<li class="last"><a href="/giftcard/mypage/member/index.do?mode=join">회원정보</a></li>
				</c:if>
				</c:if>
		
        		<li>
        			<c:choose>
          				<c:when test="${empty sessionScope.member }">
          					<a href="/giftcard/login/login.do">로그인</a>
          				</c:when>
          			<c:otherwise>
          				<a href="/giftcard/login/login.do?mode=logout">로그아웃</a>
          			</c:otherwise>
          		</c:choose>
       		  </li>
      		</ul>
		</nav>
	</div>
	
    <div class="util_nav">
     <ul>
  	   <li class="all_menu" id="allmenu_open"><i class="xi-bars"></i>전체 카테고리</li>
  	   <li class="cart_item">
  	   	<a href="/giftcard/mypage/shopping/cart/index.do" class="cart">
  	   		<span class="cart_count">${fn:length(cartCnt.list) }</span>
  	   		<i ><img src="/images/common/cart_icon.png"></i>
  	   		장바구니</a>
  	   	</li>
     </ul>
     <jsp:include page="/giftcard/inc/all_menu_box.do" />
     
     <h1 class="top_logo"><a href="/giftcard/index.do"><img src="/images/logo/logo.svg" alt="페어링오디오"></a></h1>
     
     <ul>
       <li>
         <div class="search_box">
          <form action="/giftcard/goods/list.do" id="search_all_form" name="search_all_form" method="post" onsubmit = "return search_all();">
            <fieldset>
      	      <legend>페어링오디오 통합검색</legend>
      		  <div class="search_top ui-widget">
       		    <span class="search">
       		      <input type="text" id="search_all_text" autocomplete="off" name="search_all_text" value = "${param.search_all_text }" class="main_search" title="페어링 통합검색">
       		      <i class="xi-search" ></i>
       		    </span>
      		  </div>
            </fieldset>
      	  </form>
        </div>
       </li>
        <c:if test="${empty sessionScope.member }">
        	<li class="last"><a href="/giftcard/join/join_2.do">가입하기</a></li>
        </c:if>
        <c:if test="${!empty sessionScope.member }">
        	<c:if test = "${sessionScope.member.group_seq eq '2' or sessionScope.member.group_seq eq '9' }">
				<!-- <li class="last"><a href="/mypage/member/index.do?mode=<c:choose><c:when test = "${sessionCom_nm ne '0'}">busi</c:when><c:otherwise>join</c:otherwise></c:choose>">회원정보</a></li> -->
				<li class="last"><a href="/giftcard/mypage/member/index.do?mode=join">회원정보</a></li>
			</c:if>
		</c:if>
		
        <li>
        	<c:choose>
          		<c:when test="${empty sessionScope.member }">
          			<a href="/giftcard/login/login.do">로그인</a>
          		</c:when>
          		<c:otherwise>
          			<a href="/giftcard/login/login.do?mode=logout">로그아웃</a>
          		</c:otherwise>
          	</c:choose>
        </li>

        <li class="first c1"><a href="/giftcard/mypage/notice/index.do" onclick="window.external.AddFavorite('<spring:eval expression="@config['home.url']" />', 'DBSOUND')">고객센터</a></li>
      </ul>
    </div>

</div>

<script type="text/javascript">
$(document).ready(function(){

	//question
	
	$("#question_open").click(function(){
		$("#question").slideToggle(0);
	});
	$("#question_close").click(function(){
		$("#question").slideToggle(0);
	});

	//자동완성
	var cache = {};
	$( "#search_all_text" ).autocomplete({
		minLength: 1,
		source: function( request, response ) {
			var str = $("#search_all_text").val();
			var regex = new RegExp(str, "gim");
		    var term = request.term;
		    if (term in cache) {
			    response(cache[term]);
			    $("#ui-id-1 li>a").each(function(i, o){
			    	$(o).html($(o).text().replace(regex, "<span style='color:#eb0c00;'>$&</span>"));
			    });
			    return;
		    }
		    
		    $.ajax({
				type : "POST",
				url : "/autocomplete.do",
				data : {prefix : str},
				dataType : "json",
				success : function(data){
					cache[term] = data.data;
				    response(data.data);
				    $("#ui-id-1 li>a").each(function(i, o){
				    	$(o).html($(o).text().replace(regex, "<span style='color:#eb0c00;'>$&</span>"));
				    });
				}
			});
		}
	});

});

function search_all()
{
	if($("#search_all_text").val() == "")
	{
		alert("통합검색은 한글자 이상 입력을 하셔야 검색이 진행이 됩니다.");
		$("#search_all_text").val("");
		$("#search_all_text").focus();
		return false;
		
	}
}

function headerSnsLink(gubun){
	var title = encodeURIComponent(":: 국내최대 음향기기 사이트 페어링 오디오! ::");
	if(gubun == "twt"){
		window.open("http://twitter.com/home?status=" + title + ":http://www.partsmoa.co.kr");
	}else if(gubun == "face"){
		window.open("http://www.facebook.com/sharer/sharer.php?u=http://www.partsmoa.co.kr");
	}
}
</script>