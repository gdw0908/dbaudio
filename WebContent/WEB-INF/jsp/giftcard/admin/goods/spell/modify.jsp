<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="main">
  <div class="titlebar">
    <h2>주문상세내역</h2>
    <div> <span>통합관리</span> &gt; <span>상품관리</span> &gt; <span>주문관리</span> &gt;<span class="bar_tx">주문상세내역</span> </div>
  </div>
  <div class="container"> 
    <div class="contents">
      <h3>상품정보</h3>
      <table class="style_1" style="table-layout:fixed; margin-top:10px;">
        <colgroup>
                <col width="15%" />
                <col width="*" />
                <col width="15%" />
                <col width="*" />
        </colgroup> 
        <tr>
          <th>상품명</th>
          <td>{{view.productnm}}</td>
          <th>주문상태</th>
          <td>{{view.status_nm}}</td>
        </tr>
        <tr>
<!--           <th>ERP코드</th>
          <td>{{view.erp_code}}</td> -->
          <th>상품코드</th>
          <td colspan="3">{{view.item_code}}</td>
        </tr>
        <tr>
<!--           <th>업체명</th>
          <td>{{view.com_nm}}</td> -->
          <th>유통사</th>
          <td colspan="3">{{view.makernm}}</td>
        </tr>
<!--         <tr>
          <th>차종명</th>
          <td>{{view.carmodelnm}}</td>
          <th>부품명</th>
          <td>{{view.part3_nm}}</td>
        </tr> -->
        <tr>
          <th>판매가격</th>
          <td>{{view.user_price|number}} 원</td>
          <th>수량</th>
          <td>{{view.qty}}개</td>
        </tr>
        <tr>
          <th>배송비</th>
          <td>{{getFeeAmt()}}</td>
          <th>결제금액</th>
          <td>{{view.actual_price|number}} 원</td>
        </tr>
        <tr>
          <th>결제유형</th>
          <td>{{view.paytyp_nm}}</td>
          <th>주문번호</th>
          <td>{{view.orderno}}</td>
        </tr>
      </table>
      <h3 style="margin-top:15px;">주문자정보</h3>
       <table class="style_1" style="table-layout:fixed; margin-top:10px;">
        <colgroup>
                <col width="15%" />
                <col width="*" />
                <col width="15%" />
                <col width="*" />
        </colgroup> 
        <tr>
          <th>주문자</th>
          <td>{{view.order_nm}}</td>
          <th>이메일</th>
          <td>{{view.email}}</td>
        </tr>
        <tr>
          <th>전화번호</th>
          <td>{{view.retel}}</td>
          <th>휴대폰번호</th>
          <td>{{view.rehp}}</td>
        </tr>
        <tr>
          <th>주문날짜</th>
          <td>{{view.orderdate|myDate:'yyyy-MM-dd'}}</td>
          <th>거래완료일</th>
          <td></td>
        </tr>
        <tr>
          <th>주소</th>
          <td colspan="3">({{view.zipcd}}) {{view.addr}} {{view.addrdetail}}</td>
        </tr>
      </table>    
      <h3 style="margin-top:15px;">배송정보</h3>
       <table class="style_1" style="table-layout:fixed; margin-top:10px;">
        <colgroup>
                <col width="15%" />
                <col width="*" />
                <col width="15%" />
                <col width="*" />
        </colgroup> 
        <tr>
          <th>받는분</th>
          <td>{{view.receiver}}</td>
          <th>연락처</th>
          <td>{{view.re_cell}}</td>
        </tr>
        <tr>
          <th>배송일</th>
          <td></td>
          <th>송장번호</th>
          <td></td>
        </tr>
        <tr>
          <th>메시지</th>
          <td colspan="3">{{view.message}}</td>
        </tr>
        <tr>
          <th>주소</th>
          <td colspan="3">({{view.re_zip_cd}}) {{view.re_addr1}} {{view.re_addr2}}</td>
        </tr>
      </table> 
      <!-- <h3 style="margin-top:15px;">교환/환불/주문취소 정보</h3>
      <table class="style_1" style="table-layout:fixed; margin-top:10px;">
        <colgroup>
                <col width="15%" />
                <col width="*" />
                <col width="15%" />
                <col width="*" />
        </colgroup> 
        <tr>
          <th>교환신청일</th>
          <td>{{view.ch_dt|myDate:'yyyy/MM/dd'}}</td>
          <th>교환제품도착일</th>
          <td>{{view.ch_d_dt|myDate:'yyyy/MM/dd'}}</td>
        </tr>
        <tr>
          <th>교환제품발송일</th>
          <td>{{view.ch_b_dt|myDate:'yyyy/MM/dd'}}</td>
          <th>교환송장번호</th>
          <td>{{view.ch_c_no}}</td>
        </tr>
        <tr>
          <th>교환배송비</th>
          <td> 원</td>
          <th>반품신청일</th>
          <td>{{view.ban_dt|myDate:'yyyy/MM/dd'}}</td>
        </tr>
        <tr>
          <th>반품제품도착일</th>
          <td>{{view.ban_d_dt|myDate:'yyyy/MM/dd'}}</td>
          <th>반품배송일</th>
          <td>{{view.ban_b_dt|myDate:'yyyy/MM/dd'}}</td>
        </tr>
        <tr>
          <th>환불신청일</th>
          <td>{{view.han_dt|myDate:'yyyy/MM/dd'}}</td>
          <th>환불제품도착일</th>
          <td>{{view.han_d_dt|myDate:'yyyy/MM/dd'}}</td>
        </tr>
        <tr>
          <th>환불완료일</th>
          <td>{{view.han_c_dt|myDate:'yyyy/MM/dd'}}</td>
          <th>환불배송비</th>
          <td> 원</td>
        </tr>
        <tr>
          <th>결제취소신청일</th>
          <td>{{view.pay_c_dt|myDate:'yyyy/MM/dd'}}</td>
          <th>결제취소완료일</th>
          <td>{{view.pay_e_dt|myDate:'yyyy/MM/dd'}}</td>
        </tr>
        <tr>
          <th>환불은행 및 계좌번호</th>
          <td colspan="3">{{view.bank_nm != null ? view.bank_nm + ' : ' + view.accountno : ''}}</td>
        </tr>
        <tr>
          <th>주문취소일</th>
          <td colspan="3">{{view.order_c_dt|myDate:'yyyy/MM/dd'}}</td>
        </tr>
        <tr>
          <th>사유</th>
          <td colspan="3">{{view.sayu}}</td>
        </tr>
      </table> -->
      <h3 style="margin-top:15px;">주문상태 변경</h3>
		<form id="wFrm" name="frm" method="post" novalidate="novalidate">
      	<table class="style_1" style="table-layout:fixed; margin-top:10px;">
        <colgroup>
                <col width="15%" />
                <col width="*" />
                <col width="15%" />
                <col width="*" />
        </colgroup> 
        <tr>
          <th>주문상태</th>
          <td colspan="3">
			<select title="주문상태 선택" data-ng-model="form.status" data-ng-options="item.code as item.code_nm for item in main.status">
				<option value="">전체</option>
            </select>
          </td>
        </tr>
        <tr>
          <th>일자</th>
          <td>
            <input type="text" class="input_1" data-ng-model="form.change_dt" datepicker readonly="readonly"/>
          </td>
          <th>송장번호</th>
          <td>
          	<div data-ng-if="isSongjang()">
          	<select title="택배사 선택" data-ng-model="form.delivery" data-ng-options="item.code as item.code_nm for item in main.delivery" required="required">
          		<option value="">선택</option>
          	</select>
          	<input type="text" class="input_1" data-ng-model="form.ch_c_no" required="required" data-ng-if="form.delivery!='99' && isSongjang()"/>
            </div>
          	<div data-ng-if="!isSongjang()">
          	<select title="택배사 선택" data-ng-model="form.delivery" data-ng-options="item.code as item.code_nm for item in main.delivery">
          		<option value="">선택</option>
          	</select>
          	<input type="text" class="input_1" data-ng-model="form.ch_c_no" required="required" data-ng-if="form.delivery!='99' && isSongjang()"/>
            </div>
          </td>
        </tr>
        <tr>
          <th>사유</th>
          <td colspan="3">
            <textarea rows="" cols="" style="width: 95%; height: 80px;" data-ng-model="form.sayu"></textarea>
          </td>
        </tr>
      	</table>   
      
		<div class="btn_bottom">
			<div class="r_btn">
			    <span class="bt_all">
			    	<span><input type="button" value="거래명세서" class="btall" data-ng-click = "open_specification(view.cart_no);"/></span>
			    </span> 
			    <span class="bt_all">
			    	<span><input type="button" value="저장" class="btall" data-ng-click="save()"/></span>
			    </span> 
			    <span class="bt_all">
			    	<span><input type="button" value="목록" class="btall" data-ng-click="list()"/></span>
			    </span> 
			</div>
      	</div>
      	</form>
    </div>
  </div>
</div>