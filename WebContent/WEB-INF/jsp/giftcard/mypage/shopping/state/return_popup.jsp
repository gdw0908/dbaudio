<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
                  <li>※ 반품상품의 정보를 확인해 주세요.</li>
                </ul>

				<h4 class="hs_1">반품요청 주문정보</h4>

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
                        <input type="text" id="sayu" name="sayu" class=""> 
                        <a class="btn_x" href="javascript:$('#sayu').val('')"><img src="/images/sub_2/c_input_cancel.gif" alt=""></a>
                        </span>
                        </p>
                      </div>
                    </div>
                  </td>
                  <td class="b_none">
                    <p><b class="c1">${suf:getThousand(item.amt) } 원</b></p>
                  </td>
                </tr>

                </tbody>

                </table>

				<div class="btn_bottom">
				<a href="#" onclick="return_send('${item.cart_no}')"><img src="/images/sub_2/btn_cancel_b1.gif" alt="반품신청"></a>
				</div>

              </div>
            </div>
</form>