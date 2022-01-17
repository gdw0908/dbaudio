<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld"%>
<%@ taglib prefix="suf" uri="/WEB-INF/tlds/StringUtil_fn.tld"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
   prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<c:set var="view" value="${data.view }" />
<c:set var="files" value="${data.files }" />
<c:set var="otherList" value="${otherList.list}" />
<!DOCTYPE HTML>
<html lang="ko">
<head>
<meta http-equiv="X-UA-Compatible" content="ie=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta content="minimum-scale=1.0, width=device-width, maximum-scale=1, user-scalable=yes" name="viewport" />
<meta name="author" content="31system" />
<meta name="description" content="안녕하세요  티켓모아 입니다." />
<meta name="Keywords" content="티켓모아, 음향기기, 중고음향기기, 중고악기, 중고 쇼핑몰, 중고 악기 쇼핑몰, 중고 음향기기 쇼핑몰" />
<title>상품상세보기</title>

<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/xeicon@2.3.3/xeicon.min.css">
<link rel="stylesheet" href="https://unpkg.com/swiper@7/swiper-bundle.min.css"/>
<link rel="stylesheet" href="/lib/css/sub.css" type="text/css">

<script type="text/javascript" src="/lib/js/jquery.cookie.js"></script>
<script type="text/javascript" src="/lib/js/jcarousellite_1.0.1.js"></script>
<script type="text/javascript" src="/lib/js/jquery.xml2json.js"></script>
<script type="text/javascript" src="/smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script>
<script type="text/javascript"   src="http://openapi.map.naver.com/openapi/naverMap.naver?ver=2.0&key=<spring:eval expression="@config['navar.map.key']" />"></script>
<script src="https://unpkg.com/swiper@7/swiper-bundle.min.js"></script>
<script type="text/javascript" src="/lib/js/common.js"></script>
<script type="text/javascript">
       
/* $(window).scrollTop($("#content_view").offset().top);
   nmap({ngTitle:"${view.com_nm}" ,ngXcoord:"${view.x_coord}", ngYcoord:"${view.y_coord}", ngWidth: 416, ngHeight:342});
   other_listPage(1);

}); */

function setCookie(cname,cvalue,exdays) {
   var d = new Date();
   d.setTime(d.getTime() + (exdays*24*60*60*1000));
   var expires = "expires=" + d.toGMTString();
   document.cookie = cname+"="+cvalue+"; "+expires+"; path=/";
}
setCookie("part_view_${view.item_seq}",'${view.item_seq}',1);

//다른상품
function other_listPage(page){
   $(".d_con2").load("/popup/goods/other_list.do?seq=${param.seq}&cpage="+page);
}


function cart(seq){
   <c:choose>
      <c:when test="${empty view.stock_num || view.stock_num <= 0}">
         alert("재고수량이 없습니다.");
      </c:when>
      <c:otherwise>
         location.href="/giftcard/mypage/shopping/cart/index.do?mode=add_cart&seq="+seq+"&qty="+$("#qty").val();
      </c:otherwise>
   </c:choose>
}

function directOrder(seq){
   <c:choose>
      <c:when test="${empty view.stock_num || view.stock_num <= 0}">
         alert("재고수량이 없습니다.");
      </c:when>
      <c:otherwise>
         location.href="/giftcard/mypage/shopping/cart/index.do?mode=direct_order&seq="+seq+"&qty="+$("#qty").val();
      </c:otherwise>
   </c:choose>
}


function goSubmit(){
   $("#frm").submit();   
}

function view(opt) {
     if(opt) {
        spec1.style.display = "block";
     }
     else {
        spec1.style.display = "none";
     }
}

</script>
</head>

<body>
   <article class="menu_info_wrap">
      <h3 class="menu_info">홈 > 카테고리 > 상세 페이지</h3>
   </article>

   <div class="sub_wrap">
      <div class="sub_contents" style="position: relative;">

         <div class="details_1">
            <div id="galleryWrap">
               <div class="galleryView">
                  <c:forEach var="item" items="${files }">
                     <div class="item">
                        <div style='position: absolute; width: 100%; height: 100%;'>
                            <img src='/upload/board/${item.yyyy }/${item.mm }/${item.uuid }' style="width: 100%; height: 100%; object-fit: contain;">
                           <!-- <img src='/images/products/gal_1.jpg' style="width: 100%; height: 100%; object-fit: contain;"> -->
                        </div>
                     </div>
                  </c:forEach>
               </div>
            </div>
            <script type="text/javascript" src="../../lib/js/gallery.js"></script>
            <script type="text/javascript">
          jQuery(function($){
             var options = {};
             options['animSpeed'] = 0; //애니시간
             options['pauseTime'] = 5000; //대기시간
             options['dataViewType'] = "none";  //내용보기효과 'none','Randam','top','bottom','topleft','topright','bottomleft','bottomright'   none일경우 사용안함
             options['ImgViewType'] = "fade";  //이미지효과 'Randam','sliceDownRight','sliceDownLeft','sliceUpRight','sliceUpLeft','sliceUpDown','boxblockRight','fade','sliceLeft','sliceLeftUp','sliceRight','sliceRightUp','sliceLeftRight',               'boxRandom','boxblock','boxblockDown','boxblockUp','boxblockLeft'
             options['tailCut'] =4; //썸네일출력수
             $('#galleryWrap').galleryeffect(options);

          });

          </script>
            <div class="details_info">
               <h3 class="details_title">
                  <strong>${view.PRODUCTNM } </strong>
               </h3>
               <article class="product_info">
                  <p class="price">${suf:getThousand(view.USER_PRICE) }<b>원</b></p>
                  <ul class="info_list">
                     <li>
                        <a href="javascript:void();"> <span>브랜드</span> <span>${view.MAKERNM } </span></a>
                     </li>
                     <li>
                        <a href="javascript:void();" class="deliver_hover">
                           <span>배송비<i class="xi-help-o"></i></span> 
                           <div class="delivery_info">
                              <p>도서산간배송비 추가</p>
                              <span>제주: 10,000원</span>
                              <span>도서산간지역: 20,000원</span>
                           </div>
                           <c:choose>
                              <c:when test="${view.FEE_YN == 'Y' }">
                                 <span>${suf:getThousand(view.FEE_AMOUNT) }원</span>
                              </c:when>
                              <c:otherwise>
                                 <span>무료</span>
                              </c:otherwise>
                           </c:choose>
                        </a>
                     </li>

                     <li>
                        <a href="javascript:void();">
                           <span>수령방법</span>
                           <select name="" id="">
                              <option value="0">선택</option>
                              <option value="1">직접 수령</option>
                              <option value="2">배송 수령</option>
                           </select>
                        </a>
                     </li>
                     <li>
                        <a href="javascript:cntCalc();">
                           <span>구매수량</span>
                           <input type="number" class="count_input" name="count" id="count" value="1" onblur="cntCalc()" onkeyup="cntCalc()">
                        </a>
                     </li>
                  </ul>
               </article>

               <div class="total_price">
                  <p>
                     총 상품금액<span class="to_price">${suf:getThousand(view.USER_PRICE) }</span><span class="unit">원</span>
                  </p>
               </div>
               <div class="d_info_btn">
                  <%-- <a class="cart_btn" href="/giftcard/mypage/shopping/cart/index.do?mode=add_cart&seq=${param.seq }&qty=1">장바구니담기</a> --%>
                  <a class="cart_btn" onclick="return addCart('${param.seq }')">장바구니 담기</a>
                  <a class="buy_btn" href="/giftcard/mypage/shopping/cart/index.do?mode=direct_order&seq=${param.seq }&qty=1">구매하기</a>
               </div>
            </div>
         </div>
         
         <!--  다른 고객이 함께 구매한 상품  -->
         <article class="swiper-container other_slide">
            <h6>다른 고객이 함께 구매한 상품</h6>
               <div class="swiper-wrapper other_wrap">
               <c:forEach var="item" items="${otherList}"> 
                  <div class="swiper-slide item">
                     <a href="/giftcard/goods/view.do?seq=${item.item_seq }"><img src="${item.thumb }"></a>
                     <p class="pro_tit">${item.productnm }</p>
                     <p class="pro_pay">${suf:getThousand(item.user_price) }<span>원</span></p>
                  </div>
               </c:forEach>
                 </div>
             </article>
         

         <!-- 상품설명 tab  -->
         <article class="content_container">
            <div class="wrap">
               <ul class="tabs">
                  <li class="tab-link current" data-tab="tab-1">상품설명</li>
                  <li class="tab-link" data-tab="tab-2">반품/교환/환불정보</li>
               </ul>

               <section id="tab-1" class="tab-content current">
                  <h3 class="tit">상품상세 설명</h3>
                  <div class="content">
                     ${view.conts } 
                  </div>
               </section>

               <section id="tab-2" class="tab-content">
                  <div class="guide_content">
                     <h3>1. 배송</h3>
                     <ul>
                        <li>택배 집하시간은 4시까지이며, 익일배송을 원칙으로 처리하여 드립니다.</li>
                        <li>기타 궁금하신 사항은 1661-8431 고객센터로 문의주시면 언제든지 상세히 답변 드리도록
                           하겠습니다.
                        </li>
                     </ul>
                  </div>
                  <div class="guide_content">
                     <h3>2. 배송비</h3>
                     <ul>
                        <li>모든 배송비는 착불을 기본으로 합니다.</li>
                        <li>도서산간배송비 추가</li>
                        <li>- 제주: 10,000원</li>
                        <li>- 도서산간지역: 20,000원</li>
                     </ul>
                  </div>
                  <div class="guide_content">
                     <h3>3. 교환 및 반품안내</h3>
                     <ul>
                        <li>상품을 배송 받은 날부터 7일 이내 교환은 언제든지 가능합니다.</li>
                        <li>단, 고객님의 과실 혹은 단순 변심으로 인해 교환이 불가능할 수 있습니다.</li>
                        <li>상품 특성상 포장을 개봉하시거나 상품을 사용하신 후에는 반품이 불가하오니 이점 유의하시기 바랍니다.</li>
                        <li>단, 제품하자 시에는 1661-8431 고객센터로 문의 주시면 처리절차에 대해 친절하게 안내해
                           드리겠습니다.</li>
                     </ul>
                  </div>
               </section>
            </div>
         </article>
      </div>
   </div>

   <script type="text/javascript">
      $(function() {
         $('ul.tabs li').click(function() {
           let tab_id = $(this).attr('data-tab');
           
           $('ul.tabs li').removeClass('current');
           $('.tab-content').removeClass('current');
           
           $(this).addClass('current');
           $('#' + tab_id).addClass('current');
         });
         
         
         $("#count").spinner();
         
       // slide
           var other_slide = new Swiper('.other_slide', {
             slidesPerView: 4,
             slidesPerGroup: 1,
             observer: true,
             observeParents: true,
             loopFillGroupWithBlank : true,
             spaceBetween: 100,
             touchRatio: 0,
             autoplay: {
               delay: 5000,
               disableOnInteraction: false,
             },
             breakpoints: {
               1200 : {slidesPerView: 4, touchRatio: 1},
               1020 : {slidesPerView: 3, touchRatio: 1},
               840 : {slidesPerView: 2, touchRatio: 1},
               700 : {slidesPerView: 2, touchRatio: 1},
               560 : {slidesPerView: 2, touchRatio: 1},
               480 : {slidesPerView: 1, touchRatio: 1, spaceBetween: 5},
               360 : {slidesPerView: 1, touchRatio: 1, spaceBetween: 10},
               320 : {slidesPerView: 1, touchRatio: 1, spaceBetween: 8},
             }
           });
         
       });
      
         function view_move() {
            location.href = "./question_view.do";
         }
         
         function cntCalc() {
            if($("#count").val() > 0){
               var userPrice = Number('${view.USER_PRICE}');
               var toPrice =Number($("#count").val())*userPrice;
               $(".to_price").html(String(toPrice).replace(/\B(?=(\d{3})+(?!\d))/g, ","));
            }else{
               $("#count").val(''); 
               $(".to_price").html('0');               
            }            
         }
         
          //장바구니 추가
          function addCart(item_seq){

             if(confirm("선택한 제품을 장바구니에 추가하시겠습니까?")){
                var seq =  $("input[name='seq']").eq(item_seq);
                 var qty = $("#count").val();
                 
                $.getJSON("/giftcard/mypage/shopping/cart/index.do?mode=add_cartAjax", {
                   seq : item_seq,
                   qty : qty
                }, function(data) {
                   if (data.rst == "1") {
                      seq.attr("qty", qty);
                      
                      if(confirm("장바구니로 이동하시겠습니까?")){
                         location.href = "/giftcard/mypage/shopping/cart/index.do";
                         alert("장바구니에 추가되었습니다.");
                      } else {
                         location.reload();                  //새로고침
                         return alert("장바구니에 추가되었습니다.");
                         
                      }
                   } else {
                      alert("장바구니 추가 오류입니다.");      
                   }
                });     
             } else {
                return;
             }
          }
   </script>

</body>
</html>