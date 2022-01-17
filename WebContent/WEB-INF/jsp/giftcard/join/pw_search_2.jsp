<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>비밀번호찾기</title>
<link rel="stylesheet" href="/lib/css/join.css" type="text/css">
</head>

<body>
<div class="title_rocation">
  <div class="tr_wrap">
    <h3><img src="/images/join/pw_search_title.gif" alt="비밀번호찾기"></h3>
  </div>
</div>
<div class="j_wrap">
  <div class="j_visual"><img src="/images/join/jm_img1.gif" alt="국내최대 자동차 중고부품 쇼핑몰 PARTS MOA에 오신 것을 환영합니다"></div>
  <div class="pw_last">
    <div class="licensee_3">
      <c:choose>
        <c:when test = "${not empty article }">
          <dl class="type_2">
            <dt>성공적으로 비밀번호가 발급되었습니다.</dt>
            <dd><span class="standard">아이디</span><span>: ${article.member_id }</span></dd>
            <dd> <span class="standard">비밀번호확인</span>
              <c:choose>
                <c:when test = "${not empty article.busi_nm }"> <span>: 회원정보 이메일 (${article.email })</span> </c:when>
                <c:otherwise> <span>: 회원정보 휴대폰 (${article.cell })</span> </c:otherwise>
              </c:choose>
            </dd>
            <dd class="btn_2"> <a href="/giftcard/index.do"><img class="btn" src="/images/join/join4_btn_1.gif" alt=""></a> <a href="/giftcard/login/login.do"><img class="btn" src="/images/join/join4_btn_2.gif" alt=""></a> </dd>
          </dl>
        </c:when>
        <c:otherwise>
          <dl class="type_2">
            <dt>일치하는 회원 정보가 없습니다.</dt>
            <dd class="btn_2"> <a href="/giftcard/index.do"><img class="btn" src="/images/join/join4_btn_1.gif" alt=""></a> <a href="/giftcard/login/login.do"><img class="btn" src="/images/join/join4_btn_2.gif" alt=""></a> </dd>
          </dl>
        </c:otherwise>
      </c:choose>
    </div>
    <div class="b_gui_2">
      <p><strong>위의 방법으로도 찾지 못했다면, 페어링사운드 쇼핑몰 고객센터로 문의주십시오</strong><br>
        고객센터 전화문의 : 1566-6444 (상담가능시간: 평일 오전 9시~오후 6시)</p>
    </div>
  </div>
</div>
</body>
</html>
