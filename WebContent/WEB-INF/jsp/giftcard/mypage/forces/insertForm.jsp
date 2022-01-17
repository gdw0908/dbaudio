<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<meta http-equiv="X-UA-Compatible" content="ie=edge" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta
	content="minimum-scale=1.0, width=device-width, maximum-scale=1, user-scalable=yes"
	name="viewport" />
<meta name="author" content="31system" />
<meta name="description" content="안녕하세요  티켓모아 입니다." />
<meta name="Keywords" content="티켓모아, 상품권, 백화점 상품권, 롯데 백화점, 롯데 상품권, 갤러리아 백화점, 갤러리아 상품권, 신세계 백화점, 신세계 상품권" />
<title>문의하기</title>

<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/xeicon@2.3.3/xeicon.min.css">

<script>
	$(function() {
		$("#allmenu_open").click(function() {
			$('i.xi-bars').toggleClass('xi-close');
		});

		// 햄버거 메뉴
		$(document).ready(function() {
			$('.ham_wrap').click(function() {
				$(this).toggleClass('open');
				$('.mo_menu_wrap').toggleClass('active');
				$('.mo_bg').toggleClass('active');
			});
		});
	});
</script>
</head>
<form name="wFrm" id="wFrm" action="${servletPath }" method="post"
	enctype="multipart/form-data" onsubmit="return articleSubmit(this);">
	<input type="hidden" name="mode" value="insert" /> <input
		type="hidden" name="status" value="0" />
	<table class="write_style_1">
		<colgroup>
			<col width="20%">
			<col width="">
		</colgroup>
		<tbody>

			<tr>
				<th scope="row">문의제목</th>
				<td><input type="text" id="title" name="title" class="input_1"></td>
			</tr>
			<c:if test="${sessionScope.member == null }">
				<tr>
					<th scope="row">이름</th>
					<td><input type="text" id="member_nm" name="member_nm" class="input_1" style="width: 100px;"></td>
				</tr>
				<tr>
					<th scope="row">비밀번호</th>
					<td><input type="password" id="pass" name="pass" class="input_1" style="width: 100px;"></td>
				</tr>
			</c:if>
			<tr>
				<th scope="row">공개여부</th>
				<td><input type="radio" name="public_yn" value="Y"	checked="checked" /> 공개 <input type="radio" name="public_yn" value="N" /> 비공개</td>
			</tr>
			<tr>
				<th scope="row">전화번호</th>
				<td>
					<p>
						<select id="charge_tel1" name="charge_tel1" class="select_u1">
							<c:forEach items="${code }" var="code" varStatus="status">
								<option value="${code.code }">${code.code }</option>
							</c:forEach>
						</select> - <input type="text" id="charge_tel2" name="charge_tel2"
							class="input_3 ws_2"> - <input type="text"
							id="charge_tel3" name="charge_tel3" class="input_3 ws_2">
					</p>
					<p class="ph_text">
						※ 입력하신 개인정보는 별도 요청이 없을 경우 6개월 보관 후 삭제되며, 문의 사항 확인이나 답변의 전달을 위해
						고객님께 연락을 취하는 용도 이외에는 사용되지 않습니다. 또한 항목에 체크하실 경우 이에 동의하는 것으로 간주하며,
						동의하지 않을 경우, 게시가 이루어지지 않습니다. 
						<label><input type="checkbox" id="agree" name="agree" class="check"> 동의합니다</label>
					</p>
				</td>
			</tr>

			<tr>
				<th scope="row" class="w_main">문의내용<br /> <span>(2000자 입력가능)</span></th>
				<td><textarea cols="" rows="" name="conts" style="width: 100%;" id="smarteditor"></textarea></td>
			</tr>

			<tr class="last">
				<th scope="row">첨부파일</th>
				<td id="fileList">
					<div id="file1">
						<input type="file" id="file" name="attach" style="margin-bottom: 5px;" /> <a href="#fileAdd" onclick="fileListAdd()"><img src="/images/article/file_btn_1.png" alt="추가"></a>
					</div>
				</td>
			</tr>

		</tbody>
	</table>

	<div class="vr_btn">
		<input type="submit" value="문의등록" class="check_btn" /> <a
			href="${servletPath }?mode=list&amp;cpage=${params.cpage }&amp;rows=${params.rows }&amp;condition=${params.condition }&amp;keyword=${params.keyword }"
			class="clear_btn">입력취소</a>
	</div>
</form>

</html>