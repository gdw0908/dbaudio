<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<script type="text/javascript">
	function withdrawFormChk() {
		if (jQuery("#member_pw").val() == "") {
			alert("비밀번호를 입력하세요.");
			jQuery("#member_pw").val("");
			jQuery("#member_pw").focus();
			return false;
		}
	}
</script>
</head>

<body>
	<div class="wrap" style="width: 100%;">

		<div id="sub">
			<h3 class="sub_tit">회원탈퇴</h3>
			<div class="contents" style="margin-top: 0;">
				<div class="secession_con">

					<div class="t_box2">
						<p class="last">
							티켓모아 <strong>회원탈퇴 시 비밀번호 인증</strong>이 필요합니다. 가입하신 비밀번호를 다시한번
							입력하시기 바랍니다.
						</p>
					</div>
					<form name="withdrawForm" id="withdrawForm" method="post"
						action="${servletPath }" onsubmit="return withdrawFormChk();">
						<input type="hidden" name="mode" value="withdraw1">
						<div class="secession_input">
							<input type="password" id="member_pw" name="member_pw"
								placeholder="비밀번호를 입력해주세요.">
						</div>

						<div class="secession_btn">
							<input type="submit" value="확인">
						</div>
					</form>
				</div>

			</div>
		</div>
	</div>