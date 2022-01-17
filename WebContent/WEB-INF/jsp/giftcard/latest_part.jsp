<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld" %>
<%@ taglib prefix="suf" uri="/WEB-INF/tlds/StringUtil_fn.tld" %>
<c:set var="totalpage" value="${data.pagination.totalpage>20?20:data.pagination.totalpage }"/>
<c:set var="cpage" value="${empty param.cpage?'1':param.cpage }"/>
<c:set var="prev" value="${cpage-1<=0?totalpage:cpage-1 }"/>
<c:set var="next" value="${cpage+1>totalpage?1:cpage+1 }"/>
<head>
	<script type="text/javascript" src="/lib/js/common_sc.js"></script>
</head>
<!-- brand list -->
<div class="brand_roll">
	<div class="sample_bn">
		<div>
			<ul>
            	<!-- brand roll_1 -->
            	<li>
                	<div class="md_list brand_list">
                    	 <ul>
                        	<c:forEach var="item" items="${data.list }">
                            <li>
                            	<div class="md_top">
                              		<p class="mt_img"><a href="/giftcard/goods/view.do?seq=${item.item_seq }"><img src="${item.thumb }" alt=""></a></p>
                              		<p class="mt_btn">
                                		<a href="/giftcard/goods/view.do?seq=${item.item_seq }" target="_blank">새창</a>
                                		<a href="#" onclick="return addCart('${item.item_seq }')">장바구니</a>
                                		<c:choose><c:when test="${item.inquiry_yn eq 'Y' }"><a href="javascript:inquery_y();">바로구매</a>
                                			</c:when><c:otherwise><a href="#" onclick="return directOrder('${item.item_seq }')">바로구매</a>
                                		</c:otherwise></c:choose>
                               		</p>
                            	</div>
                            	<div class="md_bottom">
                              		<p class="mb_1"><strong>제품명</strong> : ${item.PRODUCTNM }</p>
                              		<p class="mb_1"><strong>브랜드</strong> : ${item.MAKERNM } </p>
                              		<p class="mb_2">
                              		<p class="mb_3"><strong>판매가격  : </strong> 
									<span class="c1">${suf:getThousand(item.USER_PRICE) } 원</span>
	                				</p>
                            	</div>
                            </li>
                           </c:forEach>
                	</ul>
            	</div>
			</li>
            <!-- //brand roll_1 -->
			</ul>
		</div>
		 <p class="roll_prev"><a href="javascript:lastest_part('${prev }', '${param.tab }')"><img src="/images/common/left_arrow.png" alt="이전"></a></p>
         <p class="roll_next"><a href="javascript:lastest_part('${next }', '${param.tab }')"><img src="/images/common/right_arrow.png" alt="다음"></a></p>
	</div>
</div>
<!-- brand list -->
<script>
// $("#lastest_cnt").text("${suf:getThousand(data.pagination.totalcount)}");
$("#lastest_page").html("<b>${param.cpage}</b>/${totalpage}");
$("#lastest_page").data("totalpage", "${totalpage}");
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
					location.reload();					//새로고침
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
</script>