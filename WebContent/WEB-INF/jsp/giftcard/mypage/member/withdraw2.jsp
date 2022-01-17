<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE HTML>
<html lang="ko">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="Keywords" content="티켓모아, 상품권, 백화점 상품권, 롯데 백화점, 롯데 상품권, 갤러리아 백화점, 갤러리아 상품권, 신세계 백화점, 신세계 상품권" />
<title>회원탈퇴</title>
<link rel="stylesheet" href="/lib/css/sub_2.css" type="text/css">
<script type = "text/javascript">

function withdrawFormAgreeChk()
{
	if( $(":checkbox[name='agree']:checked").length == 0 )
	{
		alert("체크박스에 체크를 해주세요.");
		return false;
	}
}
</script>
</head>

<body>
<div class="wrap" style="width: 100%;">
      <div id="sub">
        <div class="strapline">
          <h3>회원탈퇴</h3>
          <div class="state"> <span>홈</span> &gt; <span>고객센터</span> &gt; <span><strong>회원정보</strong></span> </div>
        </div>
        <div class="contents">
          <div class="secession_con">

            <p class="sece_t_text">※ 티켓모아쇼핑몰의 회원탈퇴를 하시는 여러분께 알려드리는 사항입니다.</p>

            <div class="t_box2">
              <p><strong>기존 아이디로 재가입이 불가능 합니다. </strong></p>
              <p><strong>회원 탈퇴 신청 아이디는 즉시 탈퇴 처리되며, 이후 영구적으로 사용이 중지되므로 새로운 아이디로만 재가입이 가능 합니다.</strong></p>
              <p class="last"><strong>사이트 이용중 게시하신 게시물은 삭제되지 않습니다. 또한, 탈퇴 아이디로 등록하신 게시물은 삭제 및 수정이 불가능 합니다.</strong></p>
            </div>
			<form name = "withdrawFormAgree" id = "withdrawFormAgree" method = "post" action = "${servletPath }" onsubmit = "return withdrawFormAgreeChk();">
			<input type = "hidden" name = "mode" value = "withdraw2">
            <div class="secession_checkbox"><label><input type="checkbox" id="agree" name="agree" class="check">위 사항에 모두 확인하였으며 티켓모아쇼핑몰의 회원탈퇴를 신청합니다.</label></div>
            <div class="secession_btn_2">
              <a href="/"><img src="/images/sub_2/btn_secession_2.gif" alt=""></a>
              <input type = "image" src = "/images/sub_2/btn_secession_3.gif">
            </div>
			</form>
          </div>

        </div>
      </div>
    </div>