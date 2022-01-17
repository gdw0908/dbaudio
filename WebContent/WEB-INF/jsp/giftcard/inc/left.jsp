<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/xeicon@2.3.3/xeicon.min.css">
<c:set var="requestURI" value="${pageContext.request.requestURI }"/>

<article class="top_info_state">
	<ul>
		<li>홈<span>></span></li>
		<li>고객센터<span>></span></li>
		<li class="change_state"></li>
	</ul>
</article>
<div class="left_box">
<h2 class="menu_tit">고객센터</h2>
<!-- All Menu S -->
<jsp:include page="/giftcard/inc/all_menu_box.do"/>
<!-- All Menu E -->

  <div id="left_menu">
    <ul class="">
      <li class="left_depth2"><a class="left_menu_a" href="/giftcard/mypage/notice/index.do" menu_level="2"><span>공지사항</span></a></li>
      <li class="left_depth2"><a class="left_menu_a" href="/giftcard/mypage/forces/index.do" menu_level="2"><span>문의하기</span></a></li>
      <li class="left_depth2">
      <a href="#" class="left_menu_a" menu_level="2"><span class="plus">나의쇼핑</span><i class="xi-angle-down"></i></a>
      <ul class="left_menu_ul">
        <li class="left_depth3"><a href="/giftcard/mypage/shopping/cart/index.do" class="left_menu_b" menu_level="3">장바구니</a></li>
        <li class="left_depth3"><a href="/giftcard/mypage/shopping/state/index.do?mode=list1" class="left_menu_b" menu_level="3">주문/배송조회</a></li>
        <li class="left_depth3"><a href="/giftcard/mypage/shopping/state/index.do?mode=list2" class="left_menu_b" menu_level="3">취소/반품/교환 조회</a></li>
        <li class="left_depth3"><a href="/giftcard/mypage/shopping/state/index.do?mode=list3" class="left_menu_b" menu_level="3">환불/입금내역</a></li>
      </ul>
      </li>
      <li class="left_depth2">
      <a href="#" class="left_menu_a" menu_level="2"><span class="plus">회원정보</span><i class="xi-angle-down"></i></a>
      <ul class="left_menu_ul">
      <c:if test = "${sessionScope.member.group_seq eq '2' or sessionScope.member.group_seq eq '9' }">
	    <li class="left_depth3"><a href="/giftcard/mypage/member/index.do?mode=<c:choose><c:when test = "${not empty sessionData.busi_no }">busi</c:when><c:otherwise>join</c:otherwise></c:choose>" class="left_menu_b" menu_level="3">회원정보 변경</a></li>
	  </c:if>
        
        <li class="left_depth3"><a href="/giftcard/mypage/member/index.do?mode=myaddress" class="left_menu_b" menu_level="3">나의 배송지관리</a></li>
        <c:if test = "${sessionScope.member.group_seq eq '2' }">
        	<li class="left_depth3"><a href="/giftcard/mypage/member/index.do?mode=withdraw" class="left_menu_b" menu_level="3">회원탈퇴</a></li>
        </c:if>
      </ul>
      </li>
      <!-- li class="left_depth2"><a class="left_menu_a" href="/mypage/late/index.do" menu_level="2"><span>구매후기</span></a></li -->
      <!-- <li class="left_depth2"><a class="left_menu_a" href="/mypage/event/event_list.do" menu_level="2"><span>이벤트</span></a></li> -->
<!--       <li class="left_depth2"><a class="left_menu_a" href="/mypage/special_contract/special_contract.do" menu_level="2"><span>부품사용특약</span><i class="xi-angle-right"></i></a></li> -->
      <li class="left_depth2">
      <a href="#" class="left_menu_a" menu_level="2"><span class="plus">이용약관</span><i class="xi-angle-down"></i></a>
      <ul class="left_menu_ul">
        <li class="left_depth3"><a href="/giftcard/mypage/annc/annc1.do" class="left_menu_b" menu_level="3">서비스이용약관</a></li>
        <li class="left_depth3"><a href="/giftcard/mypage/annc/annc2.do" class="left_menu_b" menu_level="3">전자금융거래약관</a></li>
		<li class="left_depth3"><a href="/giftcard/mypage/annc/annc3.do" class="left_menu_b" menu_level="3">개인정보수집</a></li>
		<li class="left_depth3"><a href="/giftcard/mypage/annc/annc4.do" class="left_menu_b" menu_level="3">이메일수집</a></li>
      </ul>
      </li>
      <!-- <li class="left_depth2"><a class="left_menu_a" href="#" menu_level="2"><span>사이트맵</span></a></li> -->
    </ul>
  </div>
<!--   <p class="left_banner"><a href="#"><img src="/images/sub_2/left_banner.gif" alt="고객센터 1544-6444 FAX / 031.961.4699  평일 08:30 ~ 19:00 토,일,공휴일 휴무"></a></p> -->
</div>

<script type="text/javascript">
$( document ).ready(function() {
	
	if(location.pathname.indexOf("/mypage/carallbaro/") > -1){
		var O = $("a[href^='/mypage/carallbaro/index.do']");
		var fullO = $("a[href^='/mypage/carallbaro/index.do']");
	}else{
		if(location.pathname == "/mypage/mantoman_late/index.do"){
			var O = $("a[href^='/mypage/mantoman/index.do']");
			var fullO = $("a[href^='/mypage/mantoman/index.do"+location.search+"']");
		}else{
			var O = $("a[href^='"+location.pathname+"']");
			var fullO = $("a[href^='"+location.pathname+location.search+"']");
		}
	}
	var tit = $(document).find("title").text();
	if(fullO.size() > 0){
		fullO.closest("li").addClass("select");
		fullO.closest("li.left_depth2").addClass("select");
// 		fullO.closest("ul.left_menu_ul").show();
		$('.menu_tit').text(tit);
		$('.change_state').text(tit);
	}else{
		O.closest("li").addClass("select");
// 		O.closest("li.left_depth2").addClass("select");
		O.closest("ul.left_menu_ul").show();
		$('.menu_tit').text(tit);
		$('.change_state').text(tit);
	}

	
	// 2depth menu
	$('.left_menu_a').click(function(e){
		e.stopPropagation();
		$('.left_menu_ul').slideUp();
		$(this).next().slideToggle(300);
		$(this).parent().siblings().removeClass('select');
		$(this).parent().addClass('select');
	});
	
// 	var url = window.location.pathname,
//     	   urlRegExp = new RegExp(url.replace(/\/$/, '') + "$");
// 	$('.left_menu_a').each(function () {
//   		if (urlRegExp.test(this.href.replace(/\/$/, ''))) {
//     		$(this).parents().addClass('select');
// 			$('.menu_tit').text($(this).text());
// 			$('.change_status').text($(this).text());
//   		} 
// 	});
	
// 	$('.left_depth3 > a').each(function () {
//   		if (urlRegExp.test(this.href.replace(/\/$/, ''))) {
// 			$('.menu_tit').text($(this).text());
// 			$('.change_status').text($(this).text());
// 			return false;
//   		} 
// 	});
	
	
	//전체 카테고리
	$("#allmenu_open").click(function() {
		$("#all_menu").slideToggle(250);
	});
	$("#allmenu_close").click(function() {
		$("#all_menu").slideToggle(250);
	});
	
});
</script>