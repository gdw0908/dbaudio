<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld" %>
<%@ taglib prefix="suf" uri="/WEB-INF/tlds/StringUtil_fn.tld" %>
<c:set var="c01" value="off"/>
<c:set var="c01_c" value="class='btn'"/>
<c:set var="c02" value="off"/>
<c:set var="c02_c" value="class='btn'"/>
<c:set var="c03" value="off"/>
<c:set var="c03_c" value="class='btn'"/>
<c:choose>
	<c:when test="${param.menu=='menu9' && param.part2=='050901006002' }">
		<c:set var="c03" value="on"/>
		<c:set var="c03_c" value=""/>
	</c:when>
	<c:when test="${param.menu=='menu9' && param.part2=='050901006001' }">
		<c:set var="c02" value="on"/>
		<c:set var="c02_c" value=""/>
	</c:when>
	<c:when test="${param.menu=='menu9' && param.part2=='050901006003'}">
		<c:set var="c01" value="on"/>
		<c:set var="c01_c" value=""/>
	</c:when>
</c:choose>
<script type="text/javascript">
$( document ).ready(function() {
	var swiper = new Swiper(".mainSwiper", {
		loop: true,
        pagination: {
          el: ".swiper-pagination",
        },
      });

	$(".h_slide").slideDown(0);

	$("#pop_close").click(function() {
		$(this).css("display", "none");
		$(".util_nav").css("top", "0px");
	});
	
	
	//전체 카테고리
	$("#allmenu_open").click(function() {
		$("#all_menu").slideToggle(250);
	});
	$("#allmenu_close").click(function() {
		$("#all_menu").slideToggle(250);
	});
	
	$("#MainRoll").jCarouselLite({
		visible : 1,
		speed : 500, //슬라이드 속도
		auto : 7000, //자동탐색 시간ms
		circular : true, //탐색이 끝나면 처음부터 돌린다.
		vertical : false, //수직,수평
		btnGo : "#roll1 > li a", //바로가기 버튼 리스트
		btnPrev : "#roll1_prev",
		btnNext : "#roll1_next",
		btnStop : "#roll1_pause",
		//myHover: ".spot",	//마우스오버 옵션
		easing : "swing"
	});
	
	//gnb 오른쪽 숫자버튼 눌렀을때 숫자 변경
	//자동 페이지 변경시에는 적용안됨 따로 놀수 있으니 그땐 다시 작업필요
	$("#gnbmenu .roll_prev").on("click", function(){
		var gnb_s_roll = $(this).closest(".gnb_s_roll");
		var no = gnb_s_roll.siblings(".gnb_s_top").find("span.m_ver>b").text();
		no--;
		if(no <= 0){
			no = gnb_s_roll.find("div>ul>li").size();
		}
		gnb_s_roll.siblings(".gnb_s_top").find("span.m_ver>b").text(no);
	});	
	$("#gnbmenu .roll_next").on("click", function(){
		var gnb_s_roll = $(this).closest(".gnb_s_roll");
		var no = gnb_s_roll.siblings(".gnb_s_top").find("span.m_ver>b").text();
		no++;
		if(no > gnb_s_roll.find("div>ul>li").size()){
			no = 1;
		}
		gnb_s_roll.siblings(".gnb_s_top").find("span.m_ver>b").text(no);
	});
});

function go_Navigation(codeno, cname)
{
	location.replace("/goods/list.do?menu=menu1&part2=" + codeno + "&cname=" + cname);
}
</script>

<script type="text/javascript" src="/lib/js/gnb.js"></script>

<div class="main_visual_wrap"> 
  <!-- all_menu -->
	
	<!-- All Menu S -->
    <jsp:include page="/giftcard/inc/all_menu_box.do" />
	<!-- All Menu E -->
	
  <!-- //all_menu -->
  <div id="gnbmenu">
    <div class="inner">
      <ul>
  		<li class="all_menu" id="allmenu_open"><i class="xi-bars"></i>전체 카테고리</li>
  		<li>제휴사</li>
  		<li>혜택</li>
      </ul>
    </div>
  </div>
  
  <!-- 메인롤링 -->
<!--   <div class="main_visual"> -->
<!--     <ul class="btn_area" id="roll1"> -->
<%--       <c:choose> --%>
<%--         <c:when test="${fn:length(main.main_popup) > 0}"> --%>
<%--           <c:forEach var="item" items="${main.main_popup }" varStatus="status"> --%>
<%--             <li><a href="#">${status.count }</a></li> --%>
<%--           </c:forEach> --%>
<%--         </c:when> --%>
<%--         <c:otherwise> --%>
<!--           <li><a href="#">1</a></li> -->
<%--         </c:otherwise> --%>
<%--       </c:choose> --%>
<!--     </ul> -->
<!--     <div id="MainRoll"> -->
<!--       <ul class="roll_img"> -->
<%--         <c:choose> --%>
<%--           <c:when test="${fn:length(main.main_popup) > 0}"> --%>
<%--             <c:forEach var="item" items="${main.main_popup }" varStatus="status"> --%>
<%--               <li><a <c:choose><c:when test="${item.link_yn == 'Y' }">href="${item.link_url }" target="${item.link_target }"</c:when><c:otherwise>href="#"</c:otherwise></c:choose>><img src="${item.file_path }" alt="${item.alt }"></a></li> --%>
<%--             </c:forEach> --%>
<%--           </c:when> --%>
<%--           <c:otherwise> --%>
<!--             <li><a href="#"><img src="/images/container/main_roll1.png" alt="PARTSMOA"></a></li> -->
<%--           </c:otherwise> --%>
<%--         </c:choose> --%>
<!--       </ul> -->
<!--     </div> -->
<!--   </div> -->


	<div class="main_visual_container">
		<div class="swiper mainSwiper">
      		<div class="swiper-wrapper">
       			 <div class="swiper-slide">Slide 1</div>
        		 <div class="swiper-slide">Slide 2</div>
        		 <div class="swiper-slide">Slide 3</div>
      		</div>
      		<div class="swiper-pagination"></div>
    	</div>
  <!-- //메인롤링 --> 
  
  <!-- right_icon -->
<!--   <div class="right_icon"> -->
<!--     <ul> -->
<!--       <li class="ri_1"><a href="/mypage/shopping/cart/index.do"><img class="btn" src="/images/container/right_icon1_off.gif" alt="나의쇼핑"></a></li> -->
<%--       <c:if test="${empty sessionScope.member }"> --%>
<!--       <li class="ri_2"><a href="/login/login.do?mode=guest"><img class="btn" src="/images/container/right_icon2_off.gif" alt="비회원 주문조회"></a></li> -->
<%--       </c:if> --%>
<%--       <c:if test="${!empty sessionScope.member }"> --%>
<!--       <li class="ri_2"><a href="/mypage/shopping/cart/index.do"><img class="btn" src="/images/container/right_icon2_off.gif" alt="비회원 주문조회"></a></li> -->
<%--       </c:if> --%>
<!--       <li class="ri_3"><a href="/mypage/carallbaro/index.do"><img class="btn" src="/images/container/right_icon3_off.gif" alt="중고부품정비견적"></a></li> -->
<!--       <li class="ri_4"><a href="/mypage/application/index.do"><img class="btn" src="/images/container/right_icon4_off.gif" alt="이런 부품이 있나요?"></a></li> -->
<!--     </ul> -->
<!--   </div> -->
<!--   <!-- //right_icon --> 
<!--  </div> -->
  
</div>
