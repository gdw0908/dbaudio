<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="main" data-ng-init="group.go(1)" data-ng-cloak>
  <div class="titlebar">
    <h2>코드관리</h2>
    <div> <span>시스템 관리</span> &gt; <span class="bar_tx">코드관리</span> </div>
  </div>
  <div class="container"> 
    <div class="contents">
      <div class="top_line">
      	<form name="searchFrm" method="post" data-ng-submit="group.go(1)">
        <div class="search_box">
          <select class="select_1" data-ng-model="param.condition" data-ng-init="param.condition='group_nm'">
            <option value="group_nm">그룹명</option>
          </select>
          <input type="text" class="input_1" data-ng-model="param.keyword"/>
          <a data-ng-click="group.go(1)"><img src="/images/admin/contents/btn_search.gif" alt="검색" /></a> </div>
        <div class="select_box"> <span><b>{{group.totalCount}}</b> 건[{{group.currentPage}}/{{group.totalPage}}페이지]</span>
        	<select class="select_1" data-ng-model="param.rows" data-ng-init="param.rows='10'" data-ng-change="group.go(1)">
	            <option value="10">10건씩보기</option>
	            <option value="20">20건씩보기</option>
	            <option value="30">30건씩보기</option>
	            <option value="50">50건씩보기</option>
	            <option value="100">100건씩보기</option>
          	</select>
        </div>
        </form>
      </div>
      <div class="contents_view">
        <table class="style_1">
          <colgroup>
          <col width="10%" />
          <col width="" />
          <col width="10%" />
          <col width="10%" />
          <col width="10%" />
          </colgroup>
          <tr>
            <th>그룹번호</th>
            <th>그룹명</th>
            <th>공통코드건수</th>
            <th>수정</th>
            <th>삭제</th>
          </tr>
          <tr data-ng-if="group.list.length==0"><td colspan="5">결과가 없습니다.</td></tr>
          <tr data-ng-repeat="item in group.list">
            <td>{{item.code_group_seq}}</td>
            <td><a data-ng-click="code.openDialog(item)">{{item.group_nm}}</a></td>
            <td>{{item.code_cnt}}</td>
            <td><a data-ng-click="group.modify(item)"><img src="/images/admin/contents/s_btn_3.gif" alt="수정" /></a></td>
            <td><a data-ng-click="group.remove(item)"><img src="/images/admin/contents/s_btn_1.gif" alt="삭제" /></a></td>
          </tr>
        </table>
        
        <pagination total-page="group.totalPage" current-page="group.currentPage" on-select-page="group.go(page)"></pagination>
        
        <div class="btn_bottom">
	        <div class="r_btn">
	          <span class="bt_all">
	          	<span><input type="button" value="등록" class="btall" data-ng-click="group.add()"/></span>
	          </span> 
	        </div>
		</div>
        
      </div>
    </div>
  </div>
</div>