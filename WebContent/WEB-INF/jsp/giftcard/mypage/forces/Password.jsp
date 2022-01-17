<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:choose>
  <c:when test="${params.mode == 'del' }">
    <c:set var='article_mode' value='삭제'/>
  </c:when>
  <c:otherwise>
    <c:set var='article_mode' value='수정'/>
    <c:set var='article_mode_image' value='<input class="check_btn" type="button" value="확인">'/>
  </c:otherwise>
</c:choose>
<div id="sub">
  <div class="contents">
    <form name="wFrm" id="wFrm" action="${servletPath }" method="post" onSubmit="return articleSubmit(this);">
      <input type="hidden" name="mode" value="${params.mode }"/>
      <input type="hidden" name="article_seq" value="${params.article_seq }"/>
      <table class="write_style_1">
        <colgroup>
        <col width="20%">
        <col width="">
        </colgroup>
        <tbody>
          <tr>
            <th scope="row">비밀번호</th>
            <td><input type="password" id="pass" name="pass" class="input_3" value=""/></td>
          </tr>
        </tbody>
      </table>
      <div class="vr_btn"> ${article_mode_image } 
      	<a class="clear_btn" href="${servletPath }?mode=list&amp;cpage=${params.cpage }&amp;rows=${params.rows }&amp;condition=${params.condition }&amp;keyword=${params.keyword }">입력취소</a>
      </div>
    </form>
  </div>
</div>
