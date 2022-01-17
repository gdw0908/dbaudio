<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld" %>
<%@ taglib prefix="suf" uri="/WEB-INF/tlds/StringUtil_fn.tld" %>
<form id="cancelFrm" name="cancelFrm" method="post">
<c:if test="${item.paytyp != 'card' }">
	<input type="hidden" name="cart_no" value="${item.cart_no }">
	<input type="hidden" name="form_submit" value="Y">
</c:if>
            <div class="address_bg">
              <div class="address_pop">

<!-- 				<div class="pop_strapline"> -->
<!--                   <h3><img src="/images/sub_2/h3_img_4_pop_2_2.gif" alt="취소신청"></h3> -->
<!--                   <div class="state_2"> <span class="first"><strong>2015-06-06</strong></span> <span><strong>장바구니 No.</strong> 123456789</span> </div> -->
<!--                   <p class="pop_close"><a class="close" href="#"><img src="/images/header/popup_close.png" alt="닫기"></a></p> -->
<!--                 </div> -->

				<ul>
                  <li>※ 환불상품의 정보 및 환불예정금액을 확인해 주세요.</li>
                </ul>

				<h4 class="hs_1">환불요청 주문정보</h4>

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
                     <!--    <p class="last_2">
                        <span><strong>취소사유</strong></span> 
                        <span class="input_box">
                        <input type="text" id="sayu" name="sayu" class=""> 
                        <a class="btn_x" href="javascript:$('#sayu').val('')"><img src="/images/sub_2/c_input_cancel.gif" alt=""></a>
                        </span>
                        </p> -->
                      </div>
                    </div>
                  </td>
                  <td class="b_none">
                    <p><b class="c1">${suf:getThousand(item.amt) } 원</b></p>
                  </td>
                </tr>

                </tbody>

                </table>
                <c:if test="${item.paytyp != 'card' }">
                <table class="cart_style_1 refunds_style_1" style="margin-top:30px;">
                <colgroup>
                <col width="50%">
                <col width="50%">
                </colgroup>

                <thead>
                <tr>
                  <th scope="col">은행</th>
                  <th scope="col">계좌번호</th>
                </tr>
                </thead>

                <tbody>

                <tr>
                  <td>
                  <select id="bank" name="bank" class="select_1">
                  	<option value="">= = = 은행 선택 = = =</option>
                  <c:forEach var="bank" items="${bank }" varStatus="status">
                  	<option value="${bank.code_seq }">${bank.code_nm }</option>
                  </c:forEach>
                  </select>
                  </td>
                  <td class="b_none"><input type="text" id="accountNo" name="accountNo" class="input_1 ws_1"></td>
                </tr>

                </tbody>

                </table>
                
                <p class="type_2">※ 환불받을 계좌는 본인명의로 되어있어야만 정상적인 환불절차가 이루어집니다.</p>

				<h4 class="hs_1">환불예정금액 <span class="c1">※ 구매자 사유로 반품시 결제된 원배송비는 환불금액에서 제외됩니다.</span></h4>
				</c:if>
				<table class="cart_style_1 t_top_style_1">
                <colgroup>
                <col width="31%">
                <col width="">
                <col width="31%">
                </colgroup>

                <thead>
                <tr>
                  <th scope="col">취소상품 결제금액</th>
                  <th scope="col">수 량</th>
                  <th scope="col">환불예정액 합계</th>
                </tr>
                </thead>

                <tbody>

                <tr>
                  <td><fmt:formatNumber value="${item.amt div item.qty }"  /> 원</td>
                  <td>${item.qty }</td>
                  <td class="b_none">${suf:getThousand(item.amt) } 원</td>
                </tr>

                </tbody>

                </table>

				<p class="type_2">※ 카드로 구매하신 경우 카드사에 따라 환불받는 방법이 다릅니다.</p>

				<div class="btn_bottom">
				<c:if test="${item.paytyp == 'card' }">
				<a href="#" onclick="refunds_send('${item.cart_no}')"><img src="/images/sub_2/btn_cancel_d1.gif" alt="환불신청"></a>
				</c:if>
				<c:if test="${item.paytyp != 'card' }">
				<a href="#" onclick="cancelBank(document.cancelFrm);"><img src="/images/sub_2/btn_cancel_d1.gif" alt="환불신청"></a>
				</c:if>
				</div>

              </div>
            </div>
</form>