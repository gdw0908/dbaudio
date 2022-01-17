<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld" %>
<%@ taglib prefix="suf" uri="/WEB-INF/tlds/StringUtil_fn.tld" %>
<form id="cancelFrm" name="cancelFrm" method="post">
            <div class="address_bg">
              <div class="address_pop">

<!-- 				<div class="pop_strapline"> -->
<!--                   <h3><img src="/images/sub_2/h3_img_4_pop_2_2.gif" alt="취소신청"></h3> -->
<!--                   <div class="state_2"> <span class="first"><strong>2015-06-06</strong></span> <span><strong>장바구니 No.</strong> 123456789</span> </div> -->
<!--                   <p class="pop_close"><a class="close" href="#"><img src="/images/header/popup_close.png" alt="닫기"></a></p> -->
<!--                 </div> -->

				<ul>
                  <li>※ 취소상품의 정보 및 환불예정금액을 확인해 주세요.</li>
                </ul>

				<h4 class="hs_1">취소요청 주문정보</h4>

				<table class="cart_style_1 t_top_style_1">
                <colgroup>
                <col width="16%">
                <col width="">
                <col width="16%">
                </colgroup>

                <thead>
                <tr>
                  <th scope="col">주문번호</th>
                  <th scope="col">주문상품</th>
                  <th scope="col">구매금액</th>
                </tr>
                </thead>

                <tbody>
                <tr>
                  <td class="mid">${item.orderno }</td>
                  <td class="cart_main">
                    <div class="product_box">
                      <div class="pb_l"> <a href="#"><img src="${item.thumb }" alt=""></a> </div>
                      <div class="pb_r_2 ws_4">
                        <p>
                        <a href="#">
                        <span>
                        <strong>${item.part3_nm } / ${item.carmodelnm } ${item.cargradenm } (${item.caryyyy }) / ${item.grade }급</strong>
                        <c:if test="${item.discount_rate > 0}">
		                <span class="f_style_1">${item.discount_rate }%↓</span>
		                </c:if>
                        </span>
                        <span>${item.com_nm } / ${item.sigungu_nm }</span>
                        </a>
                        </p>
                        <p class="last_2">
                        <span><strong>취소사유</strong></span> 
                        <span class="input_box">
                        ${item.sayu } 
                        </span>
                        </p>
                      </div>
                    </div>
                  </td>
                  <td class="b_none">
                  	<c:if test="${item.cod_yn eq 'Y' }">
                    <p><b class="c1">${suf:getThousand(item.amt + item.fee_amt) } 원</b></p>
                  	</c:if>
                  	<c:if test="${item.cod_yn ne 'Y' }">
                    <p><b class="c1">${suf:getThousand(item.amt) } 원</b></p>
                  	</c:if>
                  </td>
                </tr>

                </tbody>

                </table>
				<!-- 
				<h4 class="hs_1">환불예정금액 <span class="c1">※ 구매자 사유로 반품시 결제된 원배송비는 환불금액에서 제외됩니다.</span></h4>

				<table class="cart_style_1 t_top_style_1">
                <colgroup>
                <col width="20%">
                <col width="">
                <col width="20%">
                <col width="20%">
                </colgroup>

                <thead>
                <tr>
                  <th scope="col">취소상품 결제금액</th>
                  <th scope="col">수 량</th>
                  <th scope="col">배송비</th>
                  <th scope="col">환불예정액 합계</th>
                </tr>
                </thead>

                <tbody>

                <tr>
                  <td><fmt:formatNumber value="${item.amt div item.qty }"  /> 원</td>
                  <td>${item.qty }</td>
                  <td>
                  	<c:if test="${item.cod_yn eq 'Y' }">
                    ${suf:getThousand(item.fee_amt) }
                  	</c:if>
                  	<c:if test="${item.cod_yn ne 'Y' }">
                    0
                  	</c:if>
                  </td>
                  <td class="b_none">
                  	<c:if test="${item.cod_yn eq 'Y' }">
                    ${suf:getThousand(item.amt + item.fee_amt) }원
                  	</c:if>
                  	<c:if test="${item.cod_yn ne 'Y' }">
                    ${suf:getThousand(item.amt) }원
                  	</c:if>
                  </td>
                </tr>

                </tbody>

                </table>

				<p class="type_2">※ 카드로 구매하신 경우 카드사에 따라 환불받는 방법이 다릅니다.</p>

				<div class="btn_bottom">
				<a href="#" onclick="pay_cancel('${item.cart_no}')"><img src="/images/sub_2/btn_cancel_1.gif" alt="취소신청"></a>
				</div>
 -->
              </div>
            </div>
</form>