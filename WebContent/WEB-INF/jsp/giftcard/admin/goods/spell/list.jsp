<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="main" data-ng-init="board.go(param.cpage)">
  <div class="titlebar">
    <h2>주문관리</h2>
    <div> <span>통합관리</span> &gt; <span>상품관리</span> &gt; <span>주문관리</span> &gt;<span class="bar_tx">전체관리</span> </div>
  </div>
  <div class="container"> 
     <div class="tab_menu">
      <ul class="tab">
        <li data-ng-class="{on : param.tab=='all'}"><a data-ng-click="main.tabmove('all')">전체</a></li>
        <li data-ng-class="{on : param.tab=='1'}"><a data-ng-click="main.tabmove('1')">주문접수</a></li>
        <li data-ng-class="{on : param.tab=='2'}"><a data-ng-click="main.tabmove('2')">결제완료</a></li>
        <!-- <li data-ng-class="{on : param.tab=='3'}"><a data-ng-click="main.tabmove('3')">결제취소중</a></li>
        <li data-ng-class="{on : param.tab=='4'}"><a data-ng-click="main.tabmove('4')">결제취소완료</a></li>
        <li data-ng-class="{on : param.tab=='5'}"><a data-ng-click="main.tabmove('5')">배송준비중</a></li>
        <li data-ng-class="{on : param.tab=='6'}"><a data-ng-click="main.tabmove('6')">반품신청중</a></li>
        <li data-ng-class="{on : param.tab=='7'}"><a data-ng-click="main.tabmove('7')">교환신청중</a></li>
        <li data-ng-class="{on : param.tab=='8'}"><a data-ng-click="main.tabmove('8')">환불신청중</a></li>
        <li data-ng-class="{on : param.tab=='9'}"><a data-ng-click="main.tabmove('9')">거래완료</a></li> -->
      </ul>
    </div>
    <div class="contents">
      <form name="searchFrm" method="post" data-ng-submit="board.go(1)">
      <table class="style_1">
        <colgroup>
        <col width="10%" />
        <col width="*" />
        </colgroup>
        <tr>
          <th>상세검색</th>
          <td style="padding-right:15px;">
            <table class="style_4">
                <colgroup>
                <col width="10%" />
                <col width="*" />
                <col width="10%" />
                <col width="*" />
                </colgroup> 
              <tr>
                <th>주문자</th>
                <td class="left" colspan="3"><input type="text" class="input_1" data-ng-model="param.receiver"/></td>
                <%-- <th>매장명</th>
                <td class="left">
                  <c:if test="${sessionScope.member.group_seq eq '1'}">
                  <span>
	              <label><span style="font-weight:bold; padding-right:5px;">매장명</span>
	                <input type="text" class="input_1" data-ng-model="param.com_nm" readonly="readonly"/>
	              </label>
	              <span><a data-ng-click="openCooperationSearch()"><img src="/images/admin/contents/s_btn_search.gif" alt="검색" /></a></span> </span>
	              <!-- 추가 매장명 초기화 -->
	              <span><a data-ng-click="param.com_seq='';param.com_nm=''"><img src="/images/admin/contents/invent_03.gif" alt="초기화" /></a></span>
	              </c:if>
	              <c:if test="${sessionScope.member.group_seq eq '3'}">
	              {{param.com_nm}}
	              </c:if>
                </td> --%>
              </tr>
              <tr>
                <th>상품명</th>
                <td class="left"><input type="text" class="input_1" data-ng-model="param.productnm"/></td>
                <th>결제유형</th>
                <td class="left">
	                <select title="걸제유형 선택" data-ng-model="param.paytyp" data-ng-options="item.code as item.code_nm for item in main.paytyp">
                    	<option value="">전체</option>
		            </select>
                </td>
              </tr>
              <tr data-ng-if="param.tab=='all'">
                <th>주문상태</th>
                <td class="left">
	                <select title="주문상태 선택" data-ng-model="param.status" data-ng-options="item.code as item.code_nm for item in main.status">
                    	<option value="">전체</option>
		            </select>
                </td>
                <th>주문번호</th>
                <td class="left"><input type="text" class="input_1" data-ng-model="param.orderno"/></td>
              </tr>
              <tr data-ng-if="param.tab!='all'">
                <th>주문번호</th>
                <td class="left" colspan="3"><input type="text" class="input_1" data-ng-model="param.orderno"/></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <th>기간조회</th>
          <td>
          	<div>
                <span class="bt_alls"><span><input type="button" value="어제" class="btalls" data-ng-click="search_yesterday()"/></span></span>
                <span class="bt_alls"><span><input type="button" value="오늘" class="btalls" data-ng-click="search_today()"/></span></span>
                <span class="bt_alls"><span><input type="button" value="일주" class="btalls" data-ng-click="search_week()"/></span></span>
                <span class="bt_alls"><span><input type="button" value="한달" class="btalls" data-ng-click="search_month()"/></span></span>
                <span class="bt_alls"><span><input type="button" value="당월" class="btalls" data-ng-click="search_cmonth()"/></span></span>
                <span class="bt_alls"><span><input type="button" value="전월" class="btalls" data-ng-click="search_bmonth()"/></span></span>
                <span class="bt_alls"><span><input type="button" value="전체" class="btalls" data-ng-click="search_all()"/></span></span>
            </div>
            <div style="margin-top:10px;">
              <input type="text" class="input_1 date" data-ng-model="param.sdate" datepicker/>
              ~
              <input type="text" class="input_1 date" data-ng-model="param.edate" datepicker/>
            </div></td>
        </tr>
      </table>
      
      <div class="btn_bottom">
        <div class="r_btn">
          <span class="bt_all">
          	<span><input type="button" value="검색" class="btall" data-ng-click="board.go(1)"/></span>
          </span> 
          <span class="bt_all">
          	<span><input type="button" value="초기화" class="btall" data-ng-click="searchInit()"/></span>
          </span> 
        </div>
      </div>
      </form>
      <div class="top_line" style="margin-top:20px;">
        <div class="select_box"> <span><b>{{board.totalCount|number}}</b> 건[{{board.currentPage}}/{{board.totalPage}}페이지]</span> </div>
      </div>
      <table class="style_3">
        <colgroup>
<%--             <col width="5%" /> --%>
            <col width="8%" />
            <col width="13%" />
            <col width="5%" />
            <col width="10%" />
            <col width="10%" />
            <col width="*%" />
            <%-- <col width="10%" /> --%>
            <col width="10%" />
            <col width="12%" />
            <%-- <col width="12%" />
            <col width="7%" /> --%>
            <col width="7%" />
            <col width="7%" />
        </colgroup>
        <tr>
<!--           <th> -->
<!--             <input type="checkbox" data-ng-model="board.chk_all" data-ng-change="chk_all_btn()"/> -->
<!--           </th> -->
          <th>주문일</th>
          <th>수정일</th>
          <th>주문상태</th>
          <th>주문자</th>
          <th>상품사진</th>
          <th>상품명</th>
          <!-- <th>상품위치</th> -->
          <th>결제금액</th>
          <th>주문번호</th>
          <!-- <th>상품ERP코드</th> -->
          <!-- <th>공급업체</th> -->
          <th>결제유형</th>
          <th>결제방법</th>
        </tr>
        <tr data-ng-if="board.list.length==0"><td colspan="10">결과가 없습니다.</td></tr>
        <tr data-ng-repeat="item in board.list" data-my-href="/modify/{{item.cart_no}}">
<!--           <td onclick="event.cancelBubble = true"> -->
<!--             <input type="checkbox" data-ng-model="item.check" data-ng-true-value="Y" data-ng-false-value="N" data-ng-init="item.check='N'"/> -->
<!--           </td> -->
          <td>{{item.orderdate|myDate:'yyyy-MM-dd'}}</td>
          <td>{{item.mod_dt|myDate:'yyyy-MM-dd HH:mm:ss'}}</td>
          <td>{{item.status_nm}}</td>
          <td>{{item.receiver}}</td>
          <td onclick="event.cancelBubble = true">
          	<a data-ng-click="imgPopupList(item)">
          	<img data-ng-src="{{item.thumb}}" data-err-src="/images/common/no_image.gif" alt="상품사진" style="width: 100px; height: 70px;"/>
          	</a>
          </td>
          <td>{{item.productnm}}</td>
          <!-- <td>{{item.part_location}}</td> -->
          <td>{{item.actual_price|number}} 원</td>
          <td>{{item.orderno}}</td>
          <!-- <td>{{item.erp_code}}</td>
          <td>{{item.com_nm}}</td> -->
          <td>{{item.paytyp_nm}}</td>
          <td data-ng-if="!item.trans_id">PC</td>
          <td data-ng-if="!!item.trans_id">MOBILE</td>
        </tr>
        <tr>
        <tr>
        	<th colspan="2">금액 합계</th>
        	<td></td>
        	<td></td>
        	<td></td>
        	<td></td>
        	<td>{{board.amount|number}} 원</td>
        	<td></td>
        	<td></td>
        	<td></td>
        	<td></td>
        	<td></td>
        	<td></td>
        </tr>
      </table>
      <pagination total-page="board.totalPage" current-page="board.currentPage" on-select-page="board.go(page)"></pagination>
      <div class="btn_bottom">
        <div class="r_btn">
          <!-- <span class="bt_all">
          	<span><input type="button" value="전체선택" class="btall" data-ng-click="board.chk_all=true;chk_all_btn()"/></span>
          </span> 
          <span class="bt_all">
          	<span><input type="button" value="선택취소" class="btall" data-ng-click="board.chk_all=false;chk_all_btn()"/></span>
          </span> --> 
          <span class="bt_all">
          	<span><input type="button" value="엑셀다운로드" class="btall" data-ng-click="excelDown()"/></span>
          </span> 
        </div>
      </div>
    </div>
  </div>
</div>