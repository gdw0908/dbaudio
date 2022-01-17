<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div id="sub">
  <div class="strapline">
    <h3><img src="/images/sub_2/h3_img_8.gif" alt="이벤트"></h3>
    <div class="state"> <span>홈</span> &gt; <span>고객센터</span> &gt; <span><strong>이벤트</strong></span> </div>
  </div>
  <div class="contents">
    <div class="list_top">
      <p class="hit"><b>5</b>개의 글이 등록되어 있습니다.</p>
      <p class="select_box">
        <select class="select_1" onchange="pageRows(this.value);">
          <option value="10" selected="selected">10건씩 보기</option>
          <option value="20">20건씩 보기</option>
          <option value="50">50건씩 보기</option>
        </select>
      </p>
    </div>
    <table class="list_style_1">
      <colgroup>
      <col width="8%">
      <col width="">
      <col width="12%">
      <col width="12%">
      </colgroup>
      <thead>
        <tr>
          <th scope="col">번호</th>
          <th scope="col">제목</th>
          <th scope="col">작성일</th>
          <th scope="col">상태</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>3</td>
          <td class="title"><a href="/giftcard/mypage/event/event_view.do"> 이벤트 두번째 글 </a></td>
          <td>2015-03-24</td>
          <td><img src="/images/article/ev_state_1.gif" alt="진행중"></td>
        </tr>
        <tr>
          <td>2</td>
          <td class="title"><a href="/giftcard/mypage/event/event_view.do"> 이벤트 첫번째 글 </a></td>
          <td>2015-03-24</td>
          <td><img src="/images/article/ev_state_2.gif" alt="종료"></td>
        </tr>
        <tr>
          <td>1</td>
          <td class="title"><a href="/giftcard/mypage/event/event_view.do"> 이벤트 첫번째 글 </a></td>
          <td>2015-03-24</td>
          <td><img src="/images/article/ev_state_3.gif" alt="대기"></td>
        </tr>
      </tbody>
    </table>
    <div class="paging"> <span id="pagingWrap"> <b><a href="/giftcard/mypage/notice/index.do?cpage=1&amp;rows=10&amp;condition=&amp;keyword=" onclick="return goPage(1);">1</a></b> </span> </div>
    <div class="bottom_search">
      <form action="/mypage/notice/index.do" name="articleSearchForm" id="articleSearchForm" method="post" onsubmit="return goPage(1);">
        <input type="hidden" name="rows" value="10">
        <input type="hidden" name="cpage" value="1">
        <input type="hidden" name="mode" value="list">
        <input type="hidden" name="article_seq">
        <select name="condition" class="select_1">
          <option value="TITLE">제목</option>
          <option value="REG_NM">작성자</option>
          <option value="CONTS">내용</option>
        </select>
        <span class="bottom_search_add">
        <input type="text" class="input_2" name="keyword" value="" title="검색바">
        <span class="bottom_search_bt">
        <input type="image" class="search_vd" src="/images/article/btn_bottom_search.gif" name="image" alt="검색버튼">
        </span> </span>
      </form>
    </div>
  </div>
</div>
