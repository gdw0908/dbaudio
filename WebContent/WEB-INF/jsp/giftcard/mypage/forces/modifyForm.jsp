<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="charge_tel" value="${fn:split(view.charge_tel,'-') }" />
<c:set var="charge_tel1" value="${charge_tel[0] }" />
<c:set var="charge_tel2" value="${charge_tel[1] }" />
<c:set var="charge_tel3" value="${charge_tel[2] }" />

<form name="wFrm" id="wFrm" action="${servletPath }" method="post"
	enctype="multipart/form-data" onsubmit="return articleSubmit(this);">
	<input type="hidden" name="mode" value="modify" /> <input type="hidden"
		name="article_seq" value="${view.article_seq }" />
	<table class="write_style_1">
		<colgroup>
			<col width="20%">
			<col width="">
		</colgroup>
		<tbody>
			<tr>
				<th scope="row">문의제목</th>
				<td><input type="text" id="title" name="title" class="input_1" value="${view.title }"></td>
			</tr>
			<c:if test="${view.reg_seq == 0 }">
				<tr>
					<th scope="row">이름</th>
					<td><input type="text" id="member_nm" name="member_nm"	class="input_1" style="width: 100px;" value="${view.reg_nm }"></td>
				</tr>
				<tr>
					<th scope="row">기존 비밀번호</th>
					<td><input type="password" id="pass" name="pass" class="input_1" style="width: 100px;"></td>
				</tr>
			</c:if>
			<tr>
				<th scope="row">공개여부</th>
				<td><input type="radio" name="public_yn" value="Y"
					<c:if test="${view.public_yn == 'Y' }">checked="checked"</c:if> />공개 <input type="radio" name="public_yn" value="N"
					<c:if test="${view.public_yn == 'N' }">checked="checked"</c:if> />비공개
				</td>
			</tr>
			<tr>
				<th scope="row">전화번호</th>
				<td>
					<select id="charge_tel1" name="charge_tel1" class="select_u1">
						<c:forEach items="${code }" var="code" varStatus="status">
							<option value="${code.code }"
								<c:if test="${code.code == charge_tel1 }"> selected="selected"</c:if>>${code.code }</option>
						</c:forEach>
					</select> - 
					<input type="text" id="charge_tel2" name="charge_tel2" class="input_3 ws_2" value="${charge_tel2 }"> - <input type="text" id="charge_tel3" name="charge_tel3" class="input_3 ws_2" value="${charge_tel3 }"></td>
			</tr>
			<tr>
				<th scope="row" class="w_main">문의내용<br />
				<span>(2000자 입력가능)</span></th>
				<td><textarea cols="" rows="" name="conts" style="width: 100%;"	id="smarteditor">${view.conts }</textarea></td>
			</tr>
			<c:if test="${fn:length(files) > 0 }">
				<tr>
					<th scope="row">기존 첨부파일</th>
					<td id="oldfileList"><c:forEach items="${files }" var="files" varStatus="status">${files.attach_nm} 
						<input type="checkbox" name="delattach" value="${files.uuid }" />삭제<br />
						</c:forEach>
					</td>
				</tr>
			</c:if>
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
		<input type="button" value="문의등록" class="check_btn" />
		<a href="${servletPath }?mode=list&amp;cpage=${params.cpage }&amp;rows=${params.rows }&amp;condition=${params.condition }&amp;keyword=${params.keyword }" class="clear_btn">입력취소</a>
	</div>
</form>