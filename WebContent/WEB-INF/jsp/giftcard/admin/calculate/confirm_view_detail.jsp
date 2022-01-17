<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="tot_price" value="0"/>
<c:set var="tot_commission" value="0"/>
	<div class="tab_menu tab_menu_a">
      <ul class="tab">
      	<li <c:if test="${params.type == 'N' }">class="on"</c:if>><a href="javascript:dialog_pop('${params.com_seq }','${params.odate }','N')">판매</a></li>
      	<li <c:if test="${params.type == 'Y' }">class="on"</c:if>><a href="javascript:dialog_pop('${params.com_seq }','${params.odate }','Y')">취소/환불</a></li>
      </ul>
    </div>
      <div id="dialog" title="부품검색">
          <table class="style_3" id="item_list">
          	<colgroup>
	        <col width="6%" />
	        <col width="10%" />
	        <col width="*" />
	        <col width="9%" />
	        <col width="7%" />
	        <col width="11%" />
	        <col width="12%" />
	        <col width="6%" />
	        <col width="6%" />
	        <col width="6%" />
	        <col width="7%" />
	        </colgroup>
            <tr>
              <th>주문자</th>
              <th>상품사진</th>
              <th>상품명</th>
              <th><c:if test="${params.type == 'N' }">주문일<br/>(결제완료일)</c:if><c:if test="${params.type == 'Y' }">취소완료일<br/>(환불완료일)</c:if></th>
              <th>결제금액</th>
              <th>주문번호</th>
              <th>상품ERP코드</th>
              <th>결제유형</th>
              <th>PG수수료</th>
              <th>수수료율</th>
              <th>본사 수수료</th>
            </tr>
            <c:if test="${empty list }">
            	<tr><td colspan="10">결과가 없습니다.</td></tr>
            </c:if>
            <c:forEach items = "${list }" var = "item">
            <tr>
	          <td>${item.receiver}</td>
	          <td><img src="${item.thumb }" style="width: 100px; height: 70px;"></td>
	          <td>${item.productnm}</td>
	          <td><c:if test="${params.type == 'N' }">${item.pay_dt}</c:if><c:if test="${params.type == 'Y' }">${item.pay_e_dt}</c:if></td>
	          <td><fmt:formatNumber type="number" pattern="###,###" value="${item.money}" /></td>
	          <td>${item.orderno}</td>
	          <td>${item.erp_code}</td>
	          <!-- <td>${item.com_nm}</td> -->
	          <td>${item.paytyp == 'iche' ? '계좌이체' : item.paytyp == 'virtual' ? '가상계좌' : '신용카드'}</td>
	          <td><fmt:formatNumber type="number" pattern="###,###" value="${item.pg_commission }" /></td>
	          <td>
	          	<%-- <fmt:formatNumber type="number" pattern="###.##" value="${(item.m_commission/item.money)*100 }" /> % --%>
	          	<fmt:formatNumber type="number" pattern="###.##" value="${(item.m_commission/item.totmoney)*100 }" /> %
	          </td>
	          <td><fmt:formatNumber type="number" pattern="###,###" value="${item.m_commission}" /></td>
	        </tr>
	        <c:set var="tot_price" value="${tot_price + item.money }"/>
	        <c:set var="tot_commission" value="${tot_commission + item.m_commission }"/>	        
	        </c:forEach>
	        <tr>
	        	<th colspan="3"><c:if test="${params.type == 'N' }">결제 금액 합계</c:if><c:if test="${params.type == 'Y' }">취소 금액 합계</c:if></th>
	        	<td></td>
	        	<td><fmt:formatNumber type="number" pattern="###,###" value="${tot_price }" /></td>
	        	<th colspan="3">본사 수수료 합계</th>
	        	<td></td>
	        	<td></td>
	        	<td><fmt:formatNumber type="number" pattern="###,###" value="${tot_commission }" /></td>
	        </tr>
          </table>
         	<div class="btn_bottom">
        		<div class="r_btn">
          			<span class="bt_all"><span><input type="button" value="엑셀다운로드 " onclick="exceldown('${params.com_seq }','${params.type}','${params.odate }');" class="btall"></span></span>
          		</div>
          	</div>
        </div>
