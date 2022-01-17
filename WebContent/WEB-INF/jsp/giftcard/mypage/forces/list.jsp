<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<meta http-equiv="X-UA-Compatible" content="ie=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta content="minimum-scale=1.0, width=device-width, maximum-scale=1, user-scalable=yes" name="viewport" />
<meta name="author" content="31system" />
<meta name="description" content="안녕하세요  티켓모아 입니다." />
<meta name="Keywords" content="티켓모아, 상품권, 백화점 상품권, 롯데 백화점, 롯데 상품권, 갤러리아 백화점, 갤러리아 상품권, 신세계 백화점, 신세계 상품권" />
<title>문의하기</title>

<script>
$(function() {
	$("#allmenu_open").click(function() {
		$('i.xi-bars').toggleClass('xi-close');	
	});
	
	// 햄버거 메뉴
	$(document).ready(function(){
		$('.ham_wrap').click(function(){
			$(this).toggleClass('open');
			$('.mo_menu_wrap').toggleClass('active');
			$('.mo_bg').toggleClass('active');
		});
	});
});
</script>

</head>
	<div id="sub">
		<div class="contents">
			<div class="list_top">
 				<p class="hit"><b>${page_info.totalcount}</b>개의 글이 등록되어 있습니다.</p> 
  				<p class="select_box">
  					<select class="select_1" onchange="pageRows(this.value);">
  						<option value="10" <c:if test="${params.rows eq '10' }">selected="selected"</c:if>>10건씩 보기</option>
  						<option value="20" <c:if test="${params.rows eq '20' }">selected="selected"</c:if>>20건씩 보기</option>
  						<option value="50" <c:if test="${params.rows eq '50' }">selected="selected"</c:if>>50건씩 보기</option>
  					</select>
 				 </p>
			</div>

		<table class="list_style_1">
			<colgroup>
				<col width="8%" />
				<col width="" />
				<col width="15%" />
				<col width="15%" />
				<col width="15%" />
			</colgroup>
			<thead>
				<tr>
  					<th scope="col">번호</th>
  					<th scope="col">제목</th>
  					<th scope="col">작성자</th>
  					<th scope="col">작성일</th>
  					<th scope="col">상태</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${empty list }">
					<tr><td colspan="5">등록 된 게시물이 없습니다.</td></tr>
				</c:if>
				<c:forEach items="${list }" var="article" varStatus = "status">
					<tr>
						<td>${page_info.totalcount - article.rn + 1}</td>
						<td class="title">
							<a href="${servletPath }?mode=view&amp;article_seq=${article.article_seq }&amp;cpage=${params.cpage }&amp;rows=${params.rows }&amp;condition=${params.condition }&amp;keyword=${params.keyword }">
								${article.title }${article.public_yn == 'Y' ? '':'( 비공개 )'} 
								<c:if test="${article.comm_cnt > 0}"> (${article.comm_cnt })</c:if>
							</a>
						</td>
						<td>${(article.member_id == '' || article.member_id == null) ? article.reg_nm : article.member_id}</td>	
						<td>${article.reg_dt }</td>
						<td>${article.status == '0' ? '<img src="/images/article/state_3.gif" alt="접수완료">' : article.status == '1' ? '<img src="/images/article/state_1.gif" alt="답변대기">' : '<img src="/images/article/state_2.gif" alt="답변완료">'}</td>	
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<div class="vr_btn"><a href="${servletPath }?mode=insertForm">문의하기</a></div>

		<jsp:include page="/giftcard/inc/paging.do" />

		<div class="bottom_search">
  			<form action="${servletPath }" name="articleSearchForm" id="articleSearchForm" method="post" onsubmit="return goPage(1);">
  				<input type="hidden" name="rows" value="${params.rows}"/>
  				<input type="hidden" name="cpage" value="${params.cpage }" />
  				<input type="hidden" name="mode" value="list" />
  				<input type="hidden" name="article_seq" />  
  				<select name="condition" class="select_1">
  					<option value="TITLE" <c:if test="${params.condition eq 'TITLE' }">selected="selected"</c:if>>제목</option>
  					<option value="REG_NM" <c:if test="${params.condition eq 'REG_NM' }">selected="selected"</c:if>>작성자</option>
  					<option value="CONTS" <c:if test="${params.condition eq 'CONTS' }">selected="selected"</c:if>>내용</option>
  				</select>
  				<span class="bottom_search_add"><input type="text" class="input_2" name="keyword" value="${params.keyword}" title="검색바" />
  					<span class="bottom_search_bt"><input type="button" class="search_vd" name="image" value="검색"></span>
  				</span>
  			</form>
		</div>
	</div>
</div>
