<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="main" data-ng-init="board.go(param.cpage)" >
  <div class="titlebar">
    <h2>상품권목록</h2>
    <div> <span>통합관리</span> &gt; <span>상품권관리</span> &gt; <span>상품등록</span> &gt;<span class="bar_tx">상품권목록</span> </div>
  </div>
  <div class="container"> 
    <div class="contents">
      	<form name="searchFrm" method="post" data-ng-submit="board.go(1)">
		<table class="style_1" style="table-layout: fixed;">
          <colgroup>
          <col width="10%" />
          <col width="*" />
          <col width="10%" />
          <col width="*" />
          </colgroup>
        <tr>
          <th>상세검색</th>
          <td colspan="3">
	          <div class="seldiv" style="margin-top:10px;">
	                <select title="유통사선택" data-ng-model="param.carmakerseq" data-ng-options="item.carmakerseq as item.makernm for item in carmaker" data-ng-change="changeCarmaker()">
		          		<option value="">카테고리</option>
		            </select>
		          	<select title="상품권선택" data-ng-model="param.carmodelseq" data-ng-options="item.carmodelseq as item.carmodelnm for item in carmodel" data-ng-change="changeCarmodel()">
		          		<option value="">모델</option>
		            </select>
	            </div>
            </td>
        </tr>
        <tr>
          <th>기간조회</th>
          <td colspan="3">
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
              <input type="text" class="input_1" data-ng-model="param.sdate" datepicker/> ~ <input type="text" class="input_1" data-ng-model="param.edate" datepicker/>
            </div>
          </td>
        </tr>
        <tr>
          <th>승인여부</th>
          <td  colspan="3">
          	<select title="승인여부 선택" data-ng-model="param.approval_yn">
          		<option value="">선택</option>
          		<option value="Y">승인</option>
          		<option value="N">미승인</option>
            </select>
          </td>
        </tr>
        <tr>
          <th>상품일련번호</th>
          <td  colspan="3">
            <input type="text" class="input_1" data-ng-model="param.item_code"/>
          </td>
        </tr>
      </table>
      </form>
      
      <div class="btn_bottom">
        <div class="r_btn">
          <span class="bt_all">
          	<span><input type="button" value="검색" class="btall" data-ng-click="board.go(1)"/></span>
          </span> 
        </div>
      </div>
      
      <div class="top_line" style="margin-top:20px;">
        <div class="select_box"> <span><b>{{board.totalCount|number}}</b> 건[{{board.currentPage}}/{{board.totalPage}}페이지]</span> </div>
      </div>
      
      <table class="style_3">
        <colgroup>
          <col width="4%" />
          <col width="4%" />
          <col width="8%" />
          <col width="8%" />
          <col width="*" />
          <col width="15%" />
          <col width="15%" />
          <col width="8%" />
          <col width="5%" />
        </colgroup>
        <tr>
          <th>
            <input type="checkbox" data-ng-model="board.chk_all" data-ng-change="chk_all_btn()"/>
          </th>
          <th>번호</th>
          <th>상품일련번호</th>
          <th>상품사진</th>
          <th>상품명</th>
          <th>상품가격</th>
          <th>유통사</th>
          <th>카테고리</th>
          <th>승인여부</th>
        </tr>
        <tr data-ng-if="board.list.length==0"><td colspan="14">결과가 없습니다.</td></tr>
        <tr data-ng-repeat="item in board.list" data-my-href="/modify/{{item.item_seq}}">
          <td onclick="event.cancelBubble = true">
            <input type="checkbox" data-ng-model="item.check" data-ng-true-value="Y" data-ng-false-value="N" data-ng-init="item.check='N'"/>
          </td>
          <td>{{(board.totalCount|num) + 1 - (item.rn|num)}}</td>
          <td onclick="event.cancelBubble = true">{{item.item_code}}</td>
          <td onclick="event.cancelBubble = true">
          	<a data-ng-click="imgPopupList(item)">
            <img data-ng-src="{{item.thumb}}" data-err-src="/images/common/no_image.gif" alt="{{item.attach_nm}}" style="width:100px;"/>
            </a>
          </td>
          <td>{{item.productnm}}</td>
          <td>{{item.user_price|number}} 원</td>
          <td>{{item.makernm}}</td>
          <td>{{item.reg_dt|myDate:'yyyy-MM-dd'}}</td>
          <td>{{item.approval_nm}}</td>
        </tr>
      </table>
      <pagination total-page="board.totalPage" current-page="board.currentPage" on-select-page="board.go(page)"></pagination>
      <div class="btn_bottom">
        <div class="r_btn">
          <span class="bt_all">
          	<span><input type="button" value="등록" class="btall" data-my-href="/write"/></span>
          </span> 
          <span class="bt_all">
          	<span><input type="button" value="삭제" class="btall" data-ng-click="del()"/></span>
          </span> 
        </div>
      </div>
    </div>
  </div>
</div>