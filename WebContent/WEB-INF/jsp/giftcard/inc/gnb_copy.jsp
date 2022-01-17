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
    <jsp:include page="/inc/all_menu_box.do" />
	<!-- All Menu E -->
	
  <!-- //all_menu -->
  <div id="gnbmenu">
    <div class="inner">
      <ul>
  		<li class="all_menu" id="allmenu_open"><i class="xi-bars"></i>전체메뉴</li>
        <li class="g1 <c:if test="${param.menu eq 'menu1' }">active</c:if>" <c:if test="${param.menu eq 'menu1' }">id="on_gmenu"</c:if>> <a href="/goods/list.do?menu=menu1">바디</a> 
          <!-- 2depth -->
          <div class="depthLayer <c:if test="${param.menu eq 'menu1' }">active</c:if>" <c:if test="${param.menu eq 'menu1' }">id="on_menu"</c:if> style="<c:if test="${param.menu eq 'menu1' }">display:block;</c:if>">
            <div class="depthLayerWrap">
              <div class="cate c1">
                <div class="list_box">
                  <ul>
                    <c:forEach var="item" items="${main.menu1 }">
                      <li><a href="javascript:go_Navigation('${item.codeno }', '${item.c_name }');">${item.c_name } <!-- (${item.cnt }) --></a></li>
                    </c:forEach>
                  </ul>
                </div>
              </div>
              <div class="cate c2">
                <div class="gnb_s_top">
                  <h3><strong>바디</strong>최근입고부품</h3>
                  <p> <span class="m_ver"><b>1</b>/2</span> </p>
                </div>
                
                <!-- gnb_s_roll -->
                <div class="gnb_s_roll">
                  <div class="sample_gnb_s1">
                    <c:set var="top_list" value="${main.menu_top1 }"/>
                    <div>
                      <ul>
                        <li>
                          <ul class="gnb_s_list">
                            <c:forEach var="i" begin="0" end="3">
                              <c:if test="${!empty top_list[i] }">
                                <li> <a href="/goods/view.do?menu=menu1&seq=${top_list[i].item_seq }"> <span class="img"><img src="${top_list[i].thumb }" alt="${top_list[i].attach_nm }"></span> <span class="r_con"> <span class="text"><strong>차량명</strong> : ${top_list[i].carmodelnm } <c:if test="${!empty top_list[i].caryyyy }">(${top_list[i].caryyyy })</c:if></span> <span class="text"><strong>부품명</strong> : ${top_list[i].part3_nm } </span> <span class="text_2"><span class="t2_l">가격 : <b class="c1">${suf:getThousand(top_list[i].user_price) }</b></span><!-- <span class="t2_r">협력사 : <b class="c1">${suf:getThousand(top_list[i].supplier_price) }</b></span>--></span> </span> </a> </li>
                              </c:if>
                            </c:forEach>
                          </ul>
                        </li>
                        <c:if test="${fn:length(top_list) > 4 }">
                          <li>
                            <ul class="gnb_s_list">
                              <c:forEach var="i" begin="4" end="7">
                                <c:if test="${!empty top_list[i] }">
                                  <li> <a href="/goods/view.do?menu=menu1&seq=${top_list[i].item_seq }"> <span class="img"><img src="${top_list[i].thumb }" alt="${top_list[i].attach_nm }" alt=""></span> <span class="r_con"> <span class="text"><strong>차량명</strong> : ${top_list[i].carmodelnm } <c:if test="${!empty top_list[i].caryyyy }">(${top_list[i].caryyyy })</c:if></span> <span class="text"><strong>부품명</strong> : ${top_list[i].part3_nm } </span> <span class="text_2"><span class="t2_l">가격 : <b class="c1">${suf:getThousand(top_list[i].user_price) }</b></span><!-- <span class="t2_r">협력사 : <b class="c1">${suf:getThousand(top_list[i].supplier_price) }</b></span> --></span> </span> </a> </li>
                                </c:if>
                              </c:forEach>
                            </ul>
                          </li>
                        </c:if>
                      </ul>
                    </div>
                    <span class="roll_btn"><a class="roll_prev" href="#"><img src="/images/container/gnb_s_left.gif" alt="left"></a><a class="roll_next" href="#"><img src="/images/container/gnb_s_right.gif" alt="right"></a></span> </div>
                </div>
                <!-- //gnb_s_roll --> 
                
              </div>
            </div>
          </div>
          <!-- //2depth --> 
        </li>
        <li class="g2 <c:if test="${param.menu eq 'menu2' }">active</c:if>" <c:if test="${param.menu eq 'menu2' }">id="on_gmenu"</c:if>> <a href="/goods/list.do?menu=menu2">의장</a> 
          <!-- 2depth -->
          <div class="depthLayer <c:if test="${param.menu eq 'menu2' }">active</c:if>" <c:if test="${param.menu eq 'menu2' }">id="on_menu"</c:if> style="<c:if test="${param.menu eq 'menu2' }">display:block;</c:if>">
            <div class="depthLayerWrap">
              <div class="cate c1">
                <div class="list_box">
                  <ul>
                    <c:forEach var="item" items="${main.menu2 }">
                      <li><a href="/goods/list.do?menu=menu2&part2=${item.codeno }">${item.c_name } <!-- (${item.cnt }) --></a></li>
                    </c:forEach>
                  </ul>
                </div>
              </div>
              <div class="cate c2">
                <div class="gnb_s_top">
                  <h3><strong>의장</strong>최근입고부품</h3>
                  <p> <span class="m_ver"><b>1</b>/2</span> </p>
                </div>
                
                <!-- gnb_s_roll -->
                <div class="gnb_s_roll">
                  <div class="sample_gnb_s2">
                    <c:set var="top_list" value="${main.menu_top2 }"/>
                    <div>
                      <ul>
                        <li>
                          <ul class="gnb_s_list">
                            <c:forEach var="i" begin="0" end="3">
                              <c:if test="${!empty top_list[i] }">
                                <li> <a href="/goods/view.do?menu=menu2&seq=${top_list[i].item_seq }"> <span class="img"><img src="${top_list[i].thumb }" alt="${top_list[i].attach_nm }" alt=""></span> <span class="r_con"> <span class="text"><strong>차량명</strong> : ${top_list[i].carmodelnm } <c:if test="${!empty top_list[i].caryyyy }">(${top_list[i].caryyyy })</c:if></span> <span class="text"><strong>부품명</strong> : ${top_list[i].part3_nm } </span> <span class="text_2"><span class="t2_l">가격 : <b class="c1">${suf:getThousand(top_list[i].user_price) }</b></span><!-- <span class="t2_r">협력사 : <b class="c1">${suf:getThousand(top_list[i].supplier_price) }</b></span> --></span> </span> </a> </li>
                              </c:if>
                            </c:forEach>
                          </ul>
                        </li>
                        <c:if test="${fn:length(top_list) > 4 }">
                          <li>
                            <ul class="gnb_s_list">
                              <c:forEach var="i" begin="4" end="7">
                                <c:if test="${!empty top_list[i] }">
                                  <li> <a href="/goods/view.do?menu=menu2&seq=${top_list[i].item_seq }"> <span class="img"><img src="${top_list[i].thumb }" alt="${top_list[i].attach_nm }" alt=""></span> <span class="r_con"> <span class="text"><strong>차량명</strong> : ${top_list[i].carmodelnm } <c:if test="${!empty top_list[i].caryyyy }">(${top_list[i].caryyyy })</c:if></span> <span class="text"><strong>부품명</strong> : ${top_list[i].part3_nm } </span> <span class="text_2"><span class="t2_l">가격 : <b class="c1">${suf:getThousand(top_list[i].user_price) }</b></span><!-- <span class="t2_r">협력사 : <b class="c1">${suf:getThousand(top_list[i].supplier_price) }</b></span> --></span> </span> </a> </li>
                                </c:if>
                              </c:forEach>
                            </ul>
                          </li>
                        </c:if>
                      </ul>
                    </div>
                    <span class="roll_btn"><a class="roll_prev" href="#"><img src="/images/container/gnb_s_left.gif" alt="left"></a><a class="roll_next" href="#"><img src="/images/container/gnb_s_right.gif" alt="right"></a></span> </div>
                </div>
                <!-- //gnb_s_roll --> 
                
              </div>
            </div>
          </div>
          <!-- //2depth --> 
        </li>
        <li class="g3 <c:if test="${param.menu eq 'menu3' }">active</c:if>" <c:if test="${param.menu eq 'menu3' }">id="on_gmenu"</c:if>> <a href="/goods/list.do?menu=menu3">엔진</a> 
          <!-- 2depth -->
          <div class="depthLayer <c:if test="${param.menu eq 'menu3' }">active</c:if>" <c:if test="${param.menu eq 'menu3' }">id="on_menu"</c:if> style="<c:if test="${param.menu eq 'menu3' }">display:block;</c:if>">
            <div class="depthLayerWrap">
              <div class="cate c1">
                <div class="list_box">
                  <ul>
                    <c:forEach var="item" items="${main.menu3 }">
                      <li><a href="/goods/list.do?menu=menu3&part2=${item.codeno }">${item.c_name } <!-- (${item.cnt }) --></a></li>
                    </c:forEach>
                  </ul>
                </div>
              </div>
              <div class="cate c2">
                <div class="gnb_s_top">
                  <h3><strong>엔진</strong>최근입고부품</h3>
                  <p> <span class="m_ver"><b>1</b>/2</span> </p>
                </div>
                
                <!-- gnb_s_roll -->
                <div class="gnb_s_roll">
                  <div class="sample_gnb_s3">
                    <c:set var="top_list" value="${main.menu_top3 }"/>
                    <div>
                      <ul>
                        <li>
                          <ul class="gnb_s_list">
                            <c:forEach var="i" begin="0" end="3">
                              <c:if test="${!empty top_list[i] }">
                                <li> <a href="/goods/view.do?menu=menu3&seq=${top_list[i].item_seq }"> <span class="img"><img src="${top_list[i].thumb }" alt="${top_list[i].attach_nm }" alt=""></span> <span class="r_con"> <span class="text"><strong>차량명</strong> : ${top_list[i].carmodelnm } <c:if test="${!empty top_list[i].caryyyy }">(${top_list[i].caryyyy })</c:if></span> <span class="text"><strong>부품명</strong> : ${top_list[i].part3_nm } </span> <span class="text_2"><span class="t2_l">가격 : <b class="c1">${suf:getThousand(top_list[i].user_price) }</b></span><!-- <span class="t2_r">협력사 : <b class="c1">${suf:getThousand(top_list[i].supplier_price) }</b></span> --></span> </span> </a> </li>
                              </c:if>
                            </c:forEach>
                          </ul>
                        </li>
                        <c:if test="${fn:length(top_list) > 4 }">
                          <li>
                            <ul class="gnb_s_list">
                              <c:forEach var="i" begin="4" end="7">
                                <c:if test="${!empty top_list[i] }">
                                  <li> <a href="/goods/view.do?menu=menu3&seq=${top_list[i].item_seq }"> <span class="img"><img src="${top_list[i].thumb }" alt="${top_list[i].attach_nm }" alt=""></span> <span class="r_con"> <span class="text"><strong>차량명</strong> : ${top_list[i].carmodelnm } <c:if test="${!empty top_list[i].caryyyy }">(${top_list[i].caryyyy })</c:if></span> <span class="text"><strong>부품명</strong> : ${top_list[i].part3_nm } </span> <span class="text_2"><span class="t2_l">가격 : <b class="c1">${suf:getThousand(top_list[i].user_price) }</b></span><!-- <span class="t2_r">협력사 : <b class="c1">${suf:getThousand(top_list[i].supplier_price) }</b></span> --></span> </span> </a> </li>
                                </c:if>
                              </c:forEach>
                            </ul>
                          </li>
                        </c:if>
                      </ul>
                    </div>
                    <span class="roll_btn"><a class="roll_prev" href="#"><img src="/images/container/gnb_s_left.gif" alt="left"></a><a class="roll_next" href="#"><img src="/images/container/gnb_s_right.gif" alt="right"></a></span> </div>
                </div>
                <!-- //gnb_s_roll --> 
                
              </div>
            </div>
          </div>
          <!-- //2depth --> 
        </li>
        <li class="g4 <c:if test="${param.menu eq 'menu4' }">active</c:if>" <c:if test="${param.menu eq 'menu4' }">id="on_gmenu"</c:if>> <a href="/goods/list.do?menu=menu4">샤시</a> 
          <!-- 2depth -->
          <div class="depthLayer <c:if test="${param.menu eq 'menu4' }">active</c:if>" <c:if test="${param.menu eq 'menu4' }">id="on_menu"</c:if> style="<c:if test="${param.menu eq 'menu4' }">display:block;</c:if>">
            <div class="depthLayerWrap">
              <div class="cate c1">
                <div class="list_box">
                  <ul>
                    <c:forEach var="item" items="${main.menu4 }">
                      <li><a href="/goods/list.do?menu=menu4&part2=${item.codeno }">${item.c_name } <!-- (${item.cnt }) --></a></li>
                    </c:forEach>
                  </ul>
                </div>
              </div>
              <div class="cate c2">
                <div class="gnb_s_top">
                  <h3><strong>샤시</strong>최근입고부품</h3>
                  <p> <span class="m_ver"><b>1</b>/2</span> </p>
                </div>
                
                <!-- gnb_s_roll -->
                <div class="gnb_s_roll">
                  <div class="sample_gnb_s4">
                    <c:set var="top_list" value="${main.menu_top4 }"/>
                    <div>
                      <ul>
                        <li>
                          <ul class="gnb_s_list">
                            <c:forEach var="i" begin="0" end="3">
                              <c:if test="${!empty top_list[i] }">
                                <li> <a href="/goods/view.do?menu=menu4&seq=${top_list[i].item_seq }"> <span class="img"><img src="${top_list[i].thumb }" alt="${top_list[i].attach_nm }" alt=""></span> <span class="r_con"> <span class="text"><strong>차량명</strong> : ${top_list[i].carmodelnm } <c:if test="${!empty top_list[i].caryyyy }">(${top_list[i].caryyyy })</c:if></span> <span class="text"><strong>부품명</strong> : ${top_list[i].part3_nm } </span> <span class="text_2"><span class="t2_l">가격 : <b class="c1">${suf:getThousand(top_list[i].user_price) }</b></span><!--<span class="t2_r"> 협력사 : <b class="c1">${suf:getThousand(top_list[i].supplier_price) }</b></span> --></span> </span> </a> </li>
                              </c:if>
                            </c:forEach>
                          </ul>
                        </li>
                        <c:if test="${fn:length(top_list) > 4 }">
                          <li>
                            <ul class="gnb_s_list">
                              <c:forEach var="i" begin="4" end="7">
                                <c:if test="${!empty top_list[i] }">
                                  <li> <a href="/goods/view.do?menu=menu4&seq=${top_list[i].item_seq }"> <span class="img"><img src="${top_list[i].thumb }" alt="${top_list[i].attach_nm }" alt=""></span> <span class="r_con"> <span class="text"><strong>차량명</strong> : ${top_list[i].carmodelnm } <c:if test="${!empty top_list[i].caryyyy }">(${top_list[i].caryyyy })</c:if></span> <span class="text"><strong>부품명</strong> : ${top_list[i].part3_nm } </span> <span class="text_2"><span class="t2_l">가격 : <b class="c1">${suf:getThousand(top_list[i].user_price) }</b></span><!-- <span class="t2_r">협력사 : <b class="c1">${suf:getThousand(top_list[i].supplier_price) }</b></span> --></span> </span> </a> </li>
                                </c:if>
                              </c:forEach>
                            </ul>
                          </li>
                        </c:if>
                      </ul>
                    </div>
                    <span class="roll_btn"><a class="roll_prev" href="#"><img src="/images/container/gnb_s_left.gif" alt="left"></a><a class="roll_next" href="#"><img src="/images/container/gnb_s_right.gif" alt="right"></a></span> </div>
                </div>
                <!-- //gnb_s_roll --> 
                
              </div>
            </div>
          </div>
          <!-- //2depth --> 
        </li>
        <li class="g5 <c:if test="${param.menu eq 'menu5' }">active</c:if>" <c:if test="${param.menu eq 'menu5' }">id="on_gmenu"</c:if>> <a href="/goods/list.do?menu=menu5">국산차</a> 
          <!-- 2depth -->
          <div class="depthLayer <c:if test="${param.menu eq 'menu5' }">active</c:if>" <c:if test="${param.menu eq 'menu5' }">id="on_menu"</c:if> style="<c:if test="${param.menu eq 'menu5' }">display:block;</c:if>">
            <div class="depthLayerWrap">
              <div class="cate c1 kor_bg">
                <div class="list_box_b">
                  <ul>
                    <c:forEach var="item" items="${main.menu5 }">
                      <li><a href="/goods/list.do?menu=menu5&carmakerseq=${item.carmakerseq }">${item.makernm } <!-- ${item.cnt }) --></a></li>
                    </c:forEach>
                  </ul>
                </div>
              </div>
              <div class="cate c2">
                <div class="gnb_s_top">
                  <h3><strong>국산차</strong>최근입고부품</h3>
                  <p> <span class="m_ver"><b>1</b>/2</span> </p>
                </div>
                
                <!-- gnb_s_roll -->
                <div class="gnb_s_roll">
                  <div class="sample_gnb_s5">
                    <c:set var="top_list" value="${main.menu_top5 }"/>
                    <div>
                      <ul>
                        <li>
                          <ul class="gnb_s_list">
                            <c:forEach var="i" begin="0" end="3">
                              <c:if test="${!empty top_list[i] }">
                                <li> <a href="/goods/view.do?menu=menu5&seq=${top_list[i].item_seq }"> <span class="img"><img src="${top_list[i].thumb }" alt="${top_list[i].attach_nm }" alt=""></span> <span class="r_con"> <span class="text"><strong>차량명</strong> : ${top_list[i].carmodelnm } <c:if test="${!empty top_list[i].caryyyy }">(${top_list[i].caryyyy })</c:if></span> <span class="text"><strong>부품명</strong> : ${top_list[i].part3_nm } </span> <span class="text_2"><span class="t2_l">가격 : <b class="c1">${suf:getThousand(top_list[i].user_price) }</b></span><!-- <span class="t2_r">협력사 : <b class="c1">${suf:getThousand(top_list[i].supplier_price) }</b></span> --></span> </span> </a> </li>
                              </c:if>
                            </c:forEach>
                          </ul>
                        </li>
                        <c:if test="${fn:length(top_list) > 4 }">
                          <li>
                            <ul class="gnb_s_list">
                              <c:forEach var="i" begin="4" end="7">
                                <c:if test="${!empty top_list[i] }">
                                  <li> <a href="/goods/view.do?menu=menu5&seq=${top_list[i].item_seq }"> <span class="img"><img src="${top_list[i].thumb }" alt="${top_list[i].attach_nm }" alt=""></span> <span class="r_con"> <span class="text"><strong>차량명</strong> : ${top_list[i].carmodelnm } <c:if test="${!empty top_list[i].caryyyy }">(${top_list[i].caryyyy })</c:if></span> <span class="text"><strong>부품명</strong> : ${top_list[i].part3_nm } </span> <span class="text_2"><span class="t2_l">가격 : <b class="c1">${suf:getThousand(top_list[i].user_price) }</b></span><!-- <span class="t2_r">협력사 : <b class="c1">${suf:getThousand(top_list[i].supplier_price) }</b></span> --></span> </span> </a> </li>
                                </c:if>
                              </c:forEach>
                            </ul>
                          </li>
                        </c:if>
                      </ul>
                    </div>
                    <span class="roll_btn"><a class="roll_prev" href="#"><img src="/images/container/gnb_s_left.gif" alt="left"></a><a class="roll_next" href="#"><img src="/images/container/gnb_s_right.gif" alt="right"></a></span> </div>
                </div>
                <!-- //gnb_s_roll --> 
                
              </div>
            </div>
          </div>
          <!-- //2depth --> 
        </li>
        <li class="g6 <c:if test="${param.menu eq 'menu6' }">active</c:if>" <c:if test="${param.menu eq 'menu6' }">id="on_gmenu"</c:if>> <a href="/goods/list.do?menu=menu6">수입차</a> 
          <!-- 2depth -->
          <div class="depthLayer <c:if test="${param.menu eq 'menu6' }">active</c:if>" <c:if test="${param.menu eq 'menu6' }">id="on_menu"</c:if> style="<c:if test="${param.menu eq 'menu6' }">display:block;</c:if>">
            <div class="depthLayerWrap">
              <div class="cate c1">
                <div class="list_box">
                  <ul class="import_type">
                    <c:forEach var="item" items="${main.menu6 }">
                      <li><a href="/goods/list.do?menu=menu6&carmakerseq=${item.carmakerseq }">${item.makernm } <!-- (${item.cnt }) --></a></li>
                    </c:forEach>
                  </ul>
                </div>
              </div>
              <div class="cate c2">
                <div class="gnb_s_top">
                  <h3><strong>수입차</strong>최근입고부품</h3>
                  <p> <span class="m_ver"><b>1</b>/2</span> </p>
                </div>
                
                <!-- gnb_s_roll -->
                <div class="gnb_s_roll">
                  <div class="sample_gnb_s6">
                    <c:set var="top_list" value="${main.menu_top6 }"/>
                    <div>
                      <ul>
                        <li>
                          <ul class="gnb_s_list">
                            <c:forEach var="i" begin="0" end="3">
                              <c:if test="${!empty top_list[i] }">
                                <li> <a href="/goods/view.do?menu=menu6&seq=${top_list[i].item_seq }"> <span class="img"><img src="${top_list[i].thumb }" alt="${top_list[i].attach_nm }" alt=""></span> <span class="r_con"> <span class="text"><strong>차량명</strong> : ${top_list[i].carmodelnm } <c:if test="${!empty top_list[i].caryyyy }">(${top_list[i].caryyyy })</c:if></span> <span class="text"><strong>부품명</strong> : ${top_list[i].part3_nm } </span> <span class="text_2"><span class="t2_l">가격 : <b class="c1">${suf:getThousand(top_list[i].user_price) }</b></span><!-- <span class="t2_r">협력사 : <b class="c1">${suf:getThousand(top_list[i].supplier_price) }</b></span> --></span> </span> </a> </li>
                              </c:if>
                            </c:forEach>
                          </ul>
                        </li>
                        <c:if test="${fn:length(top_list) > 4 }">
                          <li>
                            <ul class="gnb_s_list">
                              <c:forEach var="i" begin="4" end="7">
                                <c:if test="${!empty top_list[i] }">
                                  <li> <a href="/goods/view.do?menu=menu6&seq=${top_list[i].item_seq }"> <span class="img"><img src="${top_list[i].thumb }" alt="${top_list[i].attach_nm }" alt=""></span> <span class="r_con"> <span class="text"><strong>차량명</strong> : ${top_list[i].carmodelnm } <c:if test="${!empty top_list[i].caryyyy }">(${top_list[i].caryyyy })</c:if></span> <span class="text"><strong>부품명</strong> : ${top_list[i].part3_nm } </span> <span class="text_2"><span class="t2_l">가격 : <b class="c1">${suf:getThousand(top_list[i].user_price) }</b></span><!-- <span class="t2_r">협력사 : <b class="c1">${suf:getThousand(top_list[i].supplier_price) }</b></span> --></span> </span> </a> </li>
                                </c:if>
                              </c:forEach>
                            </ul>
                          </li>
                        </c:if>
                      </ul>
                    </div>
                    <span class="roll_btn"><a class="roll_prev" href="#"><img src="/images/container/gnb_s_left.gif" alt="left"></a><a class="roll_next" href="#"><img src="/images/container/gnb_s_right.gif" alt="right"></a></span> </div>
                </div>
                <!-- //gnb_s_roll --> 
                
              </div>
            </div>
          </div>
          <!-- //2depth --> 
        </li>
        <!-- <li class="g7 <c:if test="${param.menu eq 'menu7' }">active</c:if>"> <a href="#"><span class="hide_txt">재제조</span></a> 
          <div class="depthLayer <c:if test="${param.menu eq 'menu7' }">active</c:if>" style="<c:if test="${param.menu eq 'menu7' }">display:block;</c:if>">
            <div class="depthLayerWrap">
              <div class="cate c1 g7c1">
                <div class="list_box_c">
                  <ul>
                    <li><a href="/cooperation/list.do?menu=menu7">전체보기</a></li>
                    <c:forEach var="item" items="${main.menu7 }">
                      <li><a href="/cooperation/list.do?menu=menu7&amp;sido=${item.sido }">${item.dong_nm }</a></li>
                    </c:forEach>
                  </ul>
                </div>
              </div>
              <div class="cate c2 g7c2"> <a href="/cooperation/repair_list.do?menu=menu7" > <img src="/images/sub/g7mainimg_02.gif" alt="지역별 장착점 바로가기" /> </a> </div>
            </div>
          </div>
        </li>-->
        <%--<li class="g7 <c:if test="${param.menu eq 'menu9' }">active</c:if>"> 
        <a href="/goods/list.do?menu=menu9&amp;part2=050901006003"><span class="hide_txt">재제조</span></a> 
          <!-- 2depth -->
          <div class="depthLayer <c:if test="${param.menu eq 'menu9' }">active</c:if>" style="<c:if test="${param.menu eq 'menu9' }">display:block;</c:if>">
            <div class="depthLayerWrap">
              <div class="cate c3">
             	<div class="list_box_01">
	              	<ul>
	              		<li><a href="/goods/list.do?menu=menu9&amp;part2=050901006003"><img src="/images/gnb/gnb_c_01_${c01 }.gif" alt="알터네이터" ${c01_c }></a></li>
	              		<li><a href="/goods/list.do?menu=menu9&amp;part2=050901006001"><img src="/images/gnb/gnb_c_02_${c02 }.gif" alt="A/C콤프레셔" ${c02_c }></a></li>
	              		<li class="list_last"><a href="/goods/list.do?menu=menu9&amp;part2=050901006002"><img src="/images/gnb/gnb_c_03_${c03 }.gif" alt="스타트모터" ${c03_c }></a></li>
	              	</ul>
             	</div>
              </div>
            </div>
          </div>--%>
       <li class="g7 <c:if test="${param.menu eq 'menu9' }">active</c:if>" <c:if test="${param.menu eq 'menu9' }">id="on_gmenu"</c:if>> 
        <a href="/goods/contents_list.do?menu=menu9&amp;part2=050901006003">재제조</a> 
          <!-- 2depth -->
          <div class="depthLayer <c:if test="${param.menu eq 'menu9' }">active</c:if>" <c:if test="${param.menu eq 'menu9' }">id="on_menu"</c:if> style="<c:if test="${param.menu eq 'menu9' }">display:block;</c:if>">
            <div class="depthLayerWrap">
              <div class="cate c3">
             	<div class="list_box_01">
	              	<ul>
	              		<li><a href="/goods/contents_list.do?menu=menu9&amp;part2=050901006003"><img src="/images/gnb/gnb_c_01_${c01 }.gif" alt="알터네이터" ${c01_c }></a></li>
	              		<li><a href="/goods/contents_list.do?menu=menu9&amp;part2=050901006001"><img src="/images/gnb/gnb_c_02_${c02 }.gif" alt="A/C콤프레셔" ${c02_c }></a></li>
	              		<li class="list_last"><a href="/goods/contents_list.do?menu=menu9&amp;part2=050901006002"><img src="/images/gnb/gnb_c_03_${c03 }.gif" alt="스타트모터" ${c03_c }></a></li>
	              	</ul>
             	</div>
              </div>
            </div>
          </div>
          <!-- //2depth --> 
        </li>
          <!-- //2depth --> 
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
