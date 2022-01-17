<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld" %>
<script type="text/javascript">
function mymenu_changeMenu(){
	if($("#mymenu_menu_id").val() == ""){
		$("#mymenu_part2_1").empty();
		$("#mymenu_part2_1").append("<option value=''>전체</option>");
	}else{
		$.getJSON("/json/list/old_code.codeList.do", {upcodeno : "05090100"+$("#mymenu_menu").val().replace("menu","")}, function(data){
			$("#mymenu_part2_1").empty();
			$("#mymenu_part2_1").append("<option value=''>전체</option>");
			$.each(data, function(i, o){
				$("#mymenu_part2_1").append("<option value='"+o.code+"'>"+o.code_nm+"</option>");
			});
		});
	}
}

function mymenu_reset(){
	$("#mymenu_carmodelseq").empty();
	$("#mymenu_carmodelseq").append("<option value=''>전체</option>");
	$("#mymenu_cargradeseq").empty();
	$("#mymenu_cargradeseq").append("<option value=''>전체</option>");
	$("#mymenu_part2_1").empty();
	$("#mymenu_part2_1").append("<option value=''>전체</option>");
	document.mymenu_frm.carmakerseq.options[0].selected = "selected";
	document.mymenu_frm.menu.options[0].selected = "selected";
	document.mymenu_frm.caryyyy.options[0].selected = "selected";
	document.mymenu_frm.grade.options[0].selected = "selected";
	document.mymenu_frm.search_all_text.value = "";	
}

function mymenu_changeCarmaker(){
	if($("#mymenu_carmakerseq").val() == ""){
		$("#mymenu_carmodelseq").empty();
		$("#mymenu_carmodelseq").append("<option value=''>전체</option>");
		$("#mymenu_cargradeseq").empty();
		$("#mymenu_cargradeseq").append("<option value=''>전체</option>");
	}else{
		$("#mymenu_cargradeseq").empty();
		$("#mymenu_cargradeseq").append("<option value=''>전체</option>");
		$.getJSON("/json/list/old_code.carmodel.do", {carmakerseq : $("#mymenu_carmakerseq").val()}, function(data){
			$("#mymenu_carmodelseq").empty();
			$("#mymenu_carmodelseq").append("<option value=''>전체</option>");
			$.each(data, function(i, o){
				$("#mymenu_carmodelseq").append("<option value='"+o.carmodelseq+"'>"+o.carmodelnm+"</option>");
			});
		});
	}
}

function mymenu_changeCarmodel(){
	$.getJSON("/json/list/old_code.cargrade.do", {carmakerseq: $("#mymenu_carmakerseq").val(),  carmodelseq: $("#mymenu_carmodelseq").val()}, function(data){
		$("#mymenu_cargradeseq").empty();
		$("#mymenu_cargradeseq").append("<option value=''>전체</option>");
		$.each(data, function(i, o){
			$("#mymenu_cargradeseq").append("<option value='"+o.cargradeseq+"'>"+o.cargradenm+"</option>");
		});
	});
}

</script>
<!-- all_menu -->
<div class="hide_menu" style="display:none;">
  <div class="scrollOver overthrow">
    <div class="sl_top">
      <p class="my_menu">MY MENU</p>
      <c:if test="${sessionScope.member == null }">
      <span class="btn_join"><a href="/mobile/join/join_1.do"><img src="/images/mobile/common/btn_join.png" alt="회원가입"></a></span>
      </c:if>
      <c:if test="${sessionScope.member != null }">
      <p class="text"><strong>${sessionScope.member.member_nm }</strong> 님 께서 로그인 하셨습니다.</p>
      <span class="btn_log"><a href="/mobile/login/login.do?mode=mlogout"><img src="/images/mobile/common/btn_logout.png" alt="로그아웃"></a></span>
      </c:if>
      <c:if test="${sessionScope.member == null }">
      <span class="btn_log"><a href="/mobile/login/login.do?mode=mlogin"><img src="/images/mobile/common/btn_login.png" alt="로그인"></a></span>
      </c:if>
      <!-- <span><a href="/html/mobile/login/login.html"><img src="/images/mobile/common/btn_login.png" alt="로그인"></a></span> -->
    </div>

    <div class="sl_wrap">

      <div class="sc_box">
        <h3><img src="/images/mobile/common/sl_title1.gif" alt="바로가기 서비스"></h3>
        <ul>
          <li><a href="/mobile/mypage/carallbaro/index.do?menu=menu2"><img src="/images/mobile/common/sc_link1.gif" alt="중고부품정비견적"><span class="coPush"></span>
          </a>
          </li>
          <li><a href="/mobile/mypage/application/index.do?menu=menu1"><img src="/images/mobile/common/sc_link2.gif" alt="이런 부품이 있나요?"></a></li>
          <li><a href="/mobile/mypage/shopping/cart/list.do?menu=menu3&amp;mode=m_add_cart_list"><img src="/images/mobile/common/sc_link3.gif" alt="장바구니"></a></li>
          <li><a href="/mobile/mypage/shopping/state/list.do?menu=menu3&amp;mode=m_list1"><img src="/images/mobile/common/sc_link4.gif" alt="주문조회"></a></li>
        </ul>
      </div>
	<form action="/mobile/goods/list.do" method="post" id="mymenu_frm" name="mymenu_frm">
      <div class="details_search">
        <h3><img src="/images/mobile/common/sl_title2.gif" alt="부품 상세검색"></h3>
        <div class="sl_search_box">
          <p>
          <span class="input_t"><input type="text" name="search_all_text" class="input_m1" value="${param.search_all_text }"></span>
          <span class="btn">
          <a href="javascript:mymenu_reset();"><img src="/images/mobile/common/btn_reset.gif" alt="초기화"></a>
          <a href="javascript:document.mymenu_frm.submit();"><img src="/images/mobile/common/btn_search.gif" alt="검색"></a>
          </span>
          </p>


            
            <!-- END -->
        </div>

      </div>
	</form>
      <div class="visual"><a href="tel:1544-6444"><img src="/images/mobile/common/sl_visual.png" alt="PARTS MOA 고객센터 평일 08:30 ~ 19:00 토,일,공휴일 휴무 1544-6444, FAX / 031.961.4699 help@Insun.com"></a></div>
    </div>

  </div>
  <span class="sl_close"><a href="#"><img src="/images/mobile/common/sl_close.png" alt="닫기"></a></span>
  
</div>
<script type="text/javascript" src="/lib/js/mobile_menu.js"></script>
<!-- //all_menu -->