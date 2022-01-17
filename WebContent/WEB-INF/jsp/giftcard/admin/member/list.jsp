<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="main" data-ng-init="board.go(param.cpage)">
  <div class="titlebar">
    <h2>회원관리</h2>
    <div> <span>통합관리</span> &gt; <span>회원관리</span> &gt; <span class="bar_tx">회원관리리스트</span> </div>
  </div>
  <div class="container membercontainer"> 
    <div class="member_wrap">
      <h3>회원그룹</h3>
      <div>
        <ul>
          <li data-ng-class="{on:param.group_seq=='1'}"><a data-ng-click="goPage('1');">관리자</a></li>
          <li data-ng-class="{on:param.group_seq=='2'}"><a data-ng-click="goPage('2');">일반회원</a></li>
          <!-- <li data-ng-class="{on:param.group_seq=='4'}"><a data-ng-click="goPage('4');">기업회원</a></li> -->
          <!-- <li data-ng-class="{on:param.group_seq=='8'}"><a data-ng-click="goPage('8');">자원관리회원</a></li>
          <li data-ng-class="{on:param.group_seq=='9'}"><a data-ng-click="goPage('9');">장착점회원</a></li> -->
        </ul>
      </div>
    </div>
    <div class="contents membercontents">
      <div class="btn_bottom">
      <form name="searchFrm" method="post" data-ng-submit="board.go(1)">
        <div class="r_btn">
          <select title="회원 검색조건 선택" class="select_1" data-ng-model="param.condition" data-ng-init="param.condition='member_nm'">
            <option value="member_nm">이름</option>
            <option value="member_id">아이디</option>
          </select> 
          <input type="text" class="input_1" data-ng-model="param.keyword"/>
          <span><a data-ng-click="board.go(1)"><img src="/images/admin/contents/s_btn_search.gif" alt="검색" /></a></span>
        </div>
        <div class="select_box"> <span><b>{{board.totalCount|number}}</b> 건[{{board.currentPage}}/{{board.totalPage}}페이지]</span>
        	<select class="select_1" data-ng-model="param.rows" data-ng-init="param.rows='10'" data-ng-change="board.go(1)">
	            <option value="10">10건씩보기</option>
	            <option value="20">20건씩보기</option>
	            <option value="30">30건씩보기</option>
	            <option value="50">50건씩보기</option>
	            <option value="100">100건씩보기</option>
          	</select>
        </div>
      </form>
      </div>
      <table class="style_3">
        <colgroup>
          <col width="5%" />
          <col width="*" />
          <col width="18%" />
          <col width="12%" />
          <col width="12%" />
          <col width="8%" />
          <col width="10%" />
        </colgroup>
        <tr>
          <th>번호</th>
          <th>주소</th>
          <th>아이디</th>
          <th>이름</th>
          <th>등록날짜</th>
          <th>수정</th>
          <th>비밀번호초기화</th>
          <th>그룹</th>
        </tr>
        <tr data-ng-if="board.list.length==0"><td colspan="7">결과가 없습니다.</td></tr>
        <tr data-ng-repeat="item in board.list">
          <td>{{(board.totalCount|num) + 1 - (item.rn|num)}}</td>
          <td>{{item.addr1}} {{item.addr2}}</td>
          <td>{{item.member_id}}<span class="bt_alls"><span><input type="button" value="로그인" class="btalls" data-ng-click="login(item)"/></span></span> </td>
          <td><span data-ng-if="item.bk_yn"><img src="/images/admin/contents/vip_03.gif" alt="vip회원" /></span>{{item.member_nm}}</td>
          <td>{{item.reg_dt|myDate:'yyyy/MM/dd'}}</td>
          <td>
            <span class="bt_alls"><span><input type="button" value="수정" class="btalls" data-my-href="/modify/{{item.member_seq}}"/></span></span> 
          </td>
          <td>
            <span class="bt_alls"><span><input type="button" value="초기화" class="btalls" data-ng-click="passwordInit(item)"/></span></span> 
          </td>
          <td data-ng-if = "item.group_seq == 1">관리자</td>
          <td data-ng-if = "item.group_seq == 2">일반회원</td>
          <!-- <td data-ng-if = "item.group_seq == 4">기업회원</td> -->
          <!-- <td data-ng-if = "item.group_seq == 8">자원관리회원</td>
          <td data-ng-if = "item.group_seq == 9">장착점회원</td> -->
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