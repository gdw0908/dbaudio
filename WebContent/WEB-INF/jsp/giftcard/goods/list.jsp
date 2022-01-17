<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld" %>
<%@ taglib prefix="suf" uri="/WEB-INF/tlds/StringUtil_fn.tld" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<!DOCTYPE HTML>
<html lang="ko">
<head>
   <meta http-equiv="X-UA-Compatible" content="ie=edge" />
   <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
   <meta name="format-detection" content="telephone=no" />
   <meta content="minimum-scale=1.0, width=device-width, maximum-scale=1, user-scalable=yes" name="viewport" />
</head>
<!-- 파라미터 -->
<c:set var="paramset" value=""/>
<c:forEach var="item" items="${pageContext.request.parameterNames}">
   <c:if test="${!(item eq 'totalpage' || item eq 'cpage' || item eq 'rows' || item eq 'sort' || item eq 'focus' || item eq 'show')}">
   <c:choose>
      <c:when test="${paramset eq '' }">
         <c:set var="paramset" value="${paramset }?${item }=${param[item] }"/>
      </c:when>
      <c:otherwise>
         <c:set var="paramset" value="${paramset }&${item }=${param[item] }"/>
      </c:otherwise>
   </c:choose>
   </c:if>
</c:forEach>
<c:choose>
   <c:when test="${paramset eq '' }">
      <c:set var="paramset" value="?"/>
   </c:when>
   <c:otherwise>
      <c:set var="paramset" value="${paramset }&"/>
   </c:otherwise>
</c:choose>
<!-- //파라미터 -->
<!DOCTYPE HTML>
<html lang="ko">
<head>
<title>상품검색</title>
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/xeicon@2.3.3/xeicon.min.css">
<link rel="stylesheet" href="/lib/css/sub.css" type="text/css">
<script type="text/javascript" src="/lib/js/common.js"></script>
<script type="text/javascript" src="/lib/js/common_sc.js"></script>
<script type="text/javascript">
$(document).ready(function(){
   $(window).scrollTop($("#content_view").offset().top);
   
   
//#submenu
$("#submenu_open").toggle(function(){
   $("#sub_menu").slideToggle(250);
   this.src = "/images/sub/sub_menu_open.gif";
   }, function() {
   $("#sub_menu").slideToggle(250);
   this.src = "/images/sub/sub_menu_close.gif";
});

<c:if test="${param.menu eq 'menu5' || param.menu eq 'menu6'}">
$("#submenu_open").attr("src","/images/sub/sub_menu_close.gif");
</c:if>
});
 
 
function changeMenu(){
   var upcodenoNumber = '';
   if($("#menu_id").val() == 'menu9'){
      upcodenoNumber = '6';
   }else{
      upcodenoNumber = $("#menu_id").val().replace("menu","");
   }
   $.getJSON("/json/list/old_code.codeList.do", {upcodeno : "05090100"+upcodenoNumber}, function(data){
      $("#part2_1").empty();
      $("#part2_1").append("<option value=''>선 택</option>");
      $.each(data, function(i, o){
         $("#part2_1").append("<option value='"+o.code+"'>"+o.code_nm+"</option>");
      });
   });
}

function replaceApplication(){
   if(confirm("조회 건수가 없습니다.\n부품문의로 이동하시겠습니까?")){
      location.href="/mypage/application/index.do";
   }
}

function changeCarmaker(){
   
   $.getJSON("/json/list/old_code.carmodel.do", {carmakerseq : $("#carmakerseq").val()}, function(data){
      $("#carmodelseq").empty();
      $("#carmodelseq").append("<option value=''>선 택</option>");
      $.each(data, function(i, o){
         $("#carmodelseq").append("<option value='"+o.carmodelseq+"'>"+o.carmodelnm+"</option>");
      });
   });
}

function changeCarmodel(){
   $.getJSON("/json/list/old_code.cargrade.do", {carmakerseq: $("#carmakerseq").val(),  carmodelseq: $("#carmodelseq").val()}, function(data){
      $("#cargradeseq").empty();
      $("#cargradeseq").append("<option value=''>선 택</option>");
      $.each(data, function(i, o){
         $("#cargradeseq").append("<option value='"+o.cargradeseq+"'>"+o.cargradenm+"</option>");
      });
   });
}

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

function list(value)
{
   location.replace("${paramset}rows=" + value+"&show=${param.show}&sort=${param.sort}&focus=${param.focus}");
}

function go_url(url)
{   
   location.replace(url);

}

function frmreset(){
   <c:if test="${param.menu eq '' || param.menu eq null}">
   document.frm.menu.value = "";
   </c:if>
   document.frm.carmakerseq.value = "";
   document.frm.carmodelseq.value = "";
   document.frm.cargradeseq.value = "";
   document.frm.caryyyy.value = "";
   <c:if test="${param.menu eq 'menu1' || param.menu eq 'menu2' || param.menu eq 'menu3' || param.menu eq 'menu4' || param.menu eq '' || param.menu eq null}">
   document.frm.part2_1.value = "";
   </c:if>
   document.frm.grade.value = "";
   document.frm.sido.value = "";
   document.frm.sigungu.value = "";
   document.frm.dong.value = "";
   document.frm.keyword.value = "";
   document.frm.search_all_text.value = "";
}

function inquery_y(){
   alert("협의가 필요한 물품입니다.\n고객센터로 문의 바랍니다.");
}

/* //장바구니 추가
function addCart(item_seq){

   if(confirm("선택한 제품을 장바구니에 추가하시겠습니까?")){
      var seq =  $("input[name='item_seq']").val(item_seq);
      
      
      $.getJSON("/giftcard/mypage/shopping/cart/index.do?mode=add_cartAjax", {
         seq : item_seq,
         qty : '1'
         
      }, function(data) {
            if (data.rst == "1") {
            if(confirm("장바구니로 이동하시겠습니까?")){
               location.href = "/giftcard/mypage/shopping/cart/index.do?mode=add_cart&seq=${param.seq }&qty=1";
               alert("장바구니에 추가되었습니다.");
            } else {
               location.reload();               //새로고침
               return alert("장바구니에 추가되었습니다.");
               
            }
         } else {
            alert("장바구니 추가 오류입니다.");         
         }
      });     
   } else {
      return;
   }
   
} */

function directOrder(item_seq){
   $("#cartFrm>[name='mode']").val("direct_order");
   $("#cartFrm>[name='seq']").val(item_seq);
   $("#cartFrm").submit();
   return false;
}
<c:if test="${empty data.list}">
   replaceApplication();
</c:if>
</script>
</head>

<body>
   <article class="menu_info_wrap">
        <h3 class="menu_info">홈 > 카테고리 > 상품 페이지</h3>
   </article>
   <div class="container">
    <div class="gnb_wrap">
        <!-- gnb메뉴 -->
<%--       <page:applyDecorator name="gnb" /> --%>
        <!-- //gnb메뉴 -->
    </div>

    <div class="sub_line_bg">
      <div class="sub_line">
        <div class="sl_wrap">
          <div class="sl_l" id="content_view">
             <c:choose>
                <c:when test="${param.menu eq 'menu1' }"><h3>마이크</h3></c:when>
                <c:when test="${param.menu eq 'menu2' }"><h3>엠프/스피커</h3></c:when>
                <c:when test="${param.menu eq 'menu3' }"><h3>오디오믹서</h3></c:when>
                <c:when test="${param.menu eq 'menu4' }"><h3>악세서리</h3></c:when>                
             </c:choose>
            <c:if test = "${not empty param.menu}">
            <span>${param.cname }</span>
            </c:if>
             <!-- &gt; <span><strong>총 부품 : <b>${suf:getThousand(data.pagination.totalcount) }</b> 개</strong></span>  -->
<!--             </p> -->
          </div>
        </div>
      </div>
    
    <div class="sub_wrap product_container">
       <aside class="side_menu">
           <h6>카테고리</h6>
          <ul>
          <c:forEach var="item" items="${data.category }" varStatus="status">
             <li><a href="./list.do?menu=menu${status.count}">${item.makernm }</a></li>
          </c:forEach>
             <!-- <li><a href="./list.do?menu=menu1">롯데</a></li>
             <li><a href="./list.do?menu=menu2">신세계</a></li>
             <li><a href="./list.do?menu=menu3">갤러리아</a></li> -->
          </ul>
       </aside>
       
        <div class="sub_contents">
         <div class="sc_top">
                <ul class="sc_tab">
                  <li><a <c:if test = "${empty param.focus}">class="on"</c:if> href="javascript:go_url('${paramset }show=${param.show }&rows=${param.rows }');">최근등록순</a></li>
                  <li><a <c:if test = "${param.focus eq '1'}">class="on"</c:if> href="javascript:go_url('${paramset }sort=row&focus=1&show=${param.show }&rows=${param.rows }');">낮은가격</a></li>
                  <li class="last"><a <c:if test = "${param.focus eq '2'}">class="on"</c:if> href="javascript:go_url('${paramset }sort=high&focus=2&show=${param.show }&rows=${param.rows }');">높은가격</a></li>
                </ul>
      
                <div class="sc_type">
                  <span>
                  <c:choose>
                     <c:when test="${param.show == '2' }">
                     <select id="rows" name="rows" class="select_1" onchange = "list(this.value);">
                     <option value = "12" <c:if test = "${param.rows eq '12'}">selected</c:if>>12개</option>
                     <option value = "24" <c:if test = "${param.rows eq '24'}">selected</c:if>>24개</option>
                     <option value = "48" <c:if test = "${param.rows eq '48'}">selected</c:if>>48개</option>
                     </select>
                     </c:when>
                     <c:otherwise>
                     <select id="rows" name="rows" class="select_1" onchange = "list(this.value);">
                     <option value = "10" <c:if test = "${param.rows eq '10'}">selected</c:if>>10개</option>
                     <option value = "30" <c:if test = "${param.rows eq '30'}">selected</c:if>>30개</option>
                     <option value = "50" <c:if test = "${param.rows eq '50'}">selected</c:if>>50개</option>
                     </select>
                     </c:otherwise>
                  </c:choose>
                  </span>
                </div>
              </div>
        
            <!-- 방명록 스타일 시작 -->
              <div class="sub_list">
              
                   <div class="list_item_wrap">
                      <ul class="item_wrap">
                       <c:if test="${empty data.list }"><li>조회 건수가 없습니다.</li></c:if>
                            <c:forEach var="item" items="${data.list }">
                         <li class="item_list">
                            <div class="item">
                               <a href="view.do?menu=${param.menu }&seq=${item.item_seq }" class="img_wrap">
                                   <img src="${item.thumb }" alt="${item.PRODUCTNM }">
<%--                               <img src="/images/products/gal_1.jpg" alt="${item.part3_nm }"> --%>
                               </a>
                               <div class="m_btn">
                                  <a href="view.do?menu=${param.menu }&seq=${item.item_seq }" target="_blank">새창</a>
                                  <%-- <a href="#" onclick="return addCart('${item.item_seq }')">장바구니</a> --%>
                                  <a onclick="return addCart('${item.item_seq }')">장바구니</a>
                                  <a href="#" onclick="return directOrder('${item.item_seq }')">바로구매</a>
                               </div>
                            </div>
                            <div class="list_info">
<%--                                <span class="tit">${item.part3_nm }</span> --%>
                               <span class="tit">${item.PRODUCTNM }</span>
                               <ul>
<%--                                   <li>${item.part1_nm } / ${item.part2_nm }</li> --%>
                                  <li>${suf:getThousand(item.USER_PRICE) }</li>
                               </ul>
                            </div>
                         </li>
                         </c:forEach>
                      </ul>
                   </div>
                
               <jsp:include page="/giftcard/inc/paging2.do">
                  <jsp:param  name="cpage" value="${param.cpage }"/>
                  <jsp:param  name="rows" value="${param.rows }"/>
                  <jsp:param  name="totalpage" value="${data.pagination.totalpage }"/>
               </jsp:include>
      
              </div>

      </div>
    </div>
    <form id="cartFrm" name="cartFrm" method="post" action="/giftcard/mypage/shopping/cart/index.do">
       <input type="hidden" name="mode" value=""/>
       <input type="hidden" name="seq" value=""/>
       <input type="hidden" name="qty" value="1"/>
    </form>
    </div>
</body>
</html>