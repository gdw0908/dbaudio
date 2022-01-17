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
<title>공지사항</title>

<script>
$(function() {
	// 햄버거 메뉴
	$(document).ready(function(){
		$('.ham_wrap').click(function(){
			$(this).toggleClass('open');
			$('.mo_menu_wrap').toggleClass('active');
			$('.mo_bg').toggleClass('active');
		});
	});
	
	//전체 카테고리
	$("#allmenu_open").click(function() {
		$('i.xi-bars').toggleClass('xi-close');
	});

});
</script>

</head>
	<div class="contents">
		<table class="view_style_1 view_s1">
			<colgroup>
				<col width="15%">
				<col width="">
				<col width="8%">
				<col width="13%">
				<col width="8%">
				<col width="9%">
			</colgroup>
		<thead>
			<tr>
  				<th scope="row" class="c1">제목</th>
  				<td colspan="5" class="title">${view.title}</td>
			</tr>
		</thead>
		<tbody>
			<tr>
  				<th scope="row">작성일</th>
  				<td colspan="5">${view.reg_dt}</td>
			</tr>
			<c:if test="${fn:length(files) > 0 }">
			<tr>
  				<th scope="row">파일</th>
  				<td colspan="5">
  				<c:forEach items="${files }" var="files" varStatus = "status">
  					<a href="/download.do?uuid=${files.uuid }" target="_blank">${files.attach_nm}</a>&nbsp;
  				</c:forEach>
  				</td>
			</tr>
			</c:if>
			<tr>
  				<td colspan="6" class="view_text">${view.conts}</td>
			</tr>
		</tbody>
	</table>

	<div class="view_top">
  		<p class="btn_box">
  			<a href="${servletPath }?mode=list&amp;cpage=${params.cpage }&amp;rows=${params.rows }&amp;condition=${params.condition }&amp;keyword=${params.keyword }"><img src="/images/article/board_btn3.gif" alt="목록으로"></a>
  		</p>
	</div>

	<table class="view_prevnext">
		<colgroup>
			<col width="8%">
			<col width="">
		</colgroup>
		<tbody>
			<tr>
  				<th scope="row">다음글</th>
  				<td>
  					<c:choose>
  						<c:when test="${view.next_article_seq eq null}">다음 글이 없습니다.</c:when>
  						<c:otherwise><a href="${servletPath }?mode=view&amp;article_seq=${view.next_article_seq }&amp;cpage=${params.cpage }&amp;rows=${params.rows }&amp;condition=${params.condition }&amp;keyword=${params.keyword }">${view.next_article_title }</a></c:otherwise>
  					</c:choose>
  				</td>
			</tr>
			<tr>
  				<th scope="row">이전글</th>
  					<td>
  						<c:choose>
  							<c:when test="${view.pre_article_seq eq null}">이전 글이 없습니다.</c:when>
  							<c:otherwise><a href="${servletPath }?mode=view&amp;article_seq=${view.pre_article_seq }&amp;cpage=${params.cpage }&amp;rows=${params.rows }&amp;condition=${params.condition }&amp;keyword=${params.keyword }">${view.pre_article_title }</a></c:otherwise>
  						</c:choose>
  					</td>
				</tr>
			</tbody>
		</table>
	</div>
</html>