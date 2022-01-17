<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="main" data-ng-init="board.go(1)" data-ng-cloak>
  <div class="titlebar">
    <h2>문의사항</h2>
    <div> <span>통합관리</span> &gt; <span>시스템관리</span> &gt;<span class="bar_tx">문의사항</span> </div>
  </div>
  <div class="container"> 
    <div class="contents">
    <form name="searchFrm" method="post" data-ng-submit="board.go(1)">
      <div class="btn_bottom">
        <div class="r_btn">
          <select title="공지사항 검색조건 선택" data-ng-model="param.condition" data-ng-init="param.condition='title'">
            <option value="title">제목</option>
            <option value="reg_nm">작성자</option>
          </select> 
          <input type="text" class="input_1" data-ng-model="param.keyword"/>
          <span><a data-ng-click="board.go(1)"><img src="/images/admin/contents/s_btn_search.gif" alt="검색" /></a></span>
        </div>
      </div>
      <div class="btn_bottom">
        <div class="l_btn">
          <select title="건별보기 선택" data-ng-model="param.rows" data-ng-change="board.go(1)" data-ng-init="param.rows = '10'">
            <option value="10">10건씩보기</option>
            <option value="15">15건씩보기</option>
            <option value="20">20건씩보기</option>
          </select> 
        </div>
        <div class="r_btn"  style="margin-top: 5px;">
          <span class="board_listing">총 <span>{{board.totalCount|number}}</span> 건</span>
        </div>
      </div>
    </form>
      <table class="style_3">
        <colgroup>
          <col width="5%" />
          <col width="5%" />
          <col width="*" />
          <col width="10%" />
          <col width="8%" />
          <col width="8%" />
          <col width="6%" />
        </colgroup>
        <tr>
          <th>번호</th>
          <th>공개</th>
          <th>제목</th>
          <th>작성일</th>
          <th>작성자</th>
          <th>상태</th>
          <th>조회수</th>
        </tr>
        <tr data-ng-if="board.list.length==0"><td colspan="6">결과가 없습니다.</td></tr>
        <tr data-ng-repeat="item in board.list">
          <td>{{(board.totalCount|num) + 1 - (item.rn|num)}}</td>
          <td>{{item.public_yn == 'Y' ? '공개' : '비공개'}}</td>
          <td class="left"><a data-my-href="/modify/{{item.article_seq}}">{{item.title}}<span class="commentnum">{{item.comm_cnt != 0 ? ' ('+item.comm_cnt+')' : ''}}</span></a></td>
          <td>{{item.reg_dt|myDate:'yyyy/MM/dd'}}</td>
          <td>{{item.reg_nm}}</td>
          <td>{{item.status == '0' ? '접수완료' : item.status == '1' ? '답변대기' : '답변완료'}}</td>
          <td>{{item.view_cnt}}</td>
        </tr>
      </table>
      <pagination total-page="board.totalPage" current-page="board.currentPage" on-select-page="board.go(page)"></pagination>
       <div class="btn_bottom">
        <div class="r_btn">
          <span class="bt_all"><span>
          <input type="button" value="등록" class="btall" data-my-href="/write/{{param.group_seq}}"/>
          </span></span> 
        </div>
      </div>
    </div>
  </div>
</div>