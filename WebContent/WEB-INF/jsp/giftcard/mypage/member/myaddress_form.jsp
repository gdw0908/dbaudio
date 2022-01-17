<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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

<title>나의 배송지관리</title>
<script type="text/javascript" src="/lib/js/common.js"></script>
<link rel="stylesheet" href="/lib/css/sub_2.css" type="text/css">
<script type="text/javascript">
	function myAddressFormCheck() {
		if (jQuery("#receiver_nm").val() == "") {
			alert("수취인 이름을 입력하세요.");
			jQuery("#receiver_nm").val("");
			jQuery("#receiver_nm").focus();
			return false;

		} else if (jQuery("#zip_cd1").val() == "") {
			alert("주소 찾기 버튼을 이용하여 주소를 입력하세요\n \"오른쪽 주소록에 기본배송지로 저장\"\n 체크 시 기본 배송지로 등록 됩니다.");
			return false;

		} else if (jQuery("#receiver_title").val() == "") {
			alert("배송지명을 입력하세요\n \"오른쪽 수취인명과 동일하게 적용\"\n 체크 시 하면 수취인명과 동일하게 사용 가능합니다.");
			jQuery("#receiver_title").val("");
			jQuery("#receiver_title").focus();
			return false;

		} else if (jQuery("#tel").val() == "" || isNaN(jQuery("#tel").val())) {
			alert("수취인 휴대폰 번호를 입력하지 않았거나 숫자만 입력가능합니다.");
			jQuery("#tel").val("");
			jQuery("#tel").focus();
			return false;

		} else if (jQuery("#tel1").val() == "" || isNaN(jQuery("#tel1").val())) {
			alert("수취인 휴대폰 번호를 입력하지 않았거나 숫자만 입력가능합니다.");
			jQuery("#tel1").val("");
			jQuery("#tel1").focus();
			return false;

		}

		else if (jQuery("#tel2").val() == "" || isNaN(jQuery("#tel2").val())) {
			alert("수취인 휴대폰 번호를 입력하지 않았거나 숫자만 입력가능합니다.");
			jQuery("#tel2").val("");
			jQuery("#tel2").focus();
			return false;

		}

		else if (jQuery("#cell").val() == "" || isNaN(jQuery("#cell").val())) {
			alert("수취인 연락처를 입력하지 않았거나 숫자만 입력가능합니다.");
			jQuery("#cell").val("");
			jQuery("#cell").focus();
			return false;

		} else if (jQuery("#cell1").val() == ""
				|| isNaN(jQuery("#cell1").val())) {
			alert("수취인 연락처를 입력하지 않았거나 숫자만 입력가능합니다.");
			jQuery("#cell1").val("");
			jQuery("#cell1").focus();
			return false;

		} else if (jQuery("#cell2").val() == ""
				|| isNaN(jQuery("#cell2").val())) {
			alert("수취인 연락처를 입력하지 않았거나 숫자만 입력가능합니다.");
			jQuery("#cell2").val("");
			jQuery("#cell2").focus();
			return false;

		}
	}

	function open_zipcode() {
		var param = "";

		if (arguments[0]) {
			param = "?fun=" + arguments[0];
		}

		window.open("/addr/road.jsp" + param, "addr", "width=570,height=420");
	}

	function setAddr(roadAddrPart1, addrDetail, zipNo, jibunAddr) {
		var zip = zipNo.split("-");
		jQuery("#zip_cd1").val(zip[0]);
		jQuery("#zip_cd2").val(zip[1]);
		jQuery("#addr1").val(roadAddrPart1);
		jQuery("#addr2").val(addrDetail);
	}

	var addSw = 0;
	function addOption() {
		if (addSw == 0) {
			jQuery("#receiver_title").val(jQuery("#receiver_nm").val());
			jQuery("#receiver_title").attr("readonly", true);

			addSw = 1;

		} else {
			jQuery("#receiver_title").val("");
			jQuery("#receiver_title").focus();
			jQuery("#receiver_title").attr("readonly", false);

			addSw = 0;
		}

	}
</script>
</head>

<body>
	<div class="wrap" style="width: 100%;">
		<div id="sub">
			<h3 class="sub_tit">나의 배송지관리</h3>
			<div class="contents">

				<div class="t_box1">
					<p>
						<strong>새로운 배송주소를 등록하시려면 주소록 추가하기를 눌러주세요.</strong> <a href="#"
							class="add_btn1">주소록 추가하기</a>
					</p>
				</div>

				<div class="hs_line">
					<h4 class="hs_1" style="margin: 0;">
						배송지 등록 <span>[ <span class="s_icon">표시된 항목은 필수사항입니다.
								]</span></span>
					</h4>
					<p>
						총 <b>${myaddress.total}</b> 개의 주소가 등록되어 있습니다
					</p>
				</div>
				<form name="myAddressForm" id="myAddressForm" method="post"
					action="${servletPath }" onsubmit="return myAddressFormCheck()">
					<input type="hidden" name="mode" value="${mode }" /> <input
						type="hidden" name="seq" value="${myaddress.seq }" />
					<div class="sub_table_1">
						<table>
							<colgroup>
								<col width="20%">
								<col width="">
							</colgroup>
							<tbody>

								<tr>
									<th scope="row"><span>수취인 이름</span></th>
									<td><input type="text" id="receiver_nm" name="receiver_nm"
										class="input_2 ws_3" value="${myaddress.receiver_nm }"></td>
								</tr>

								<tr>
									<th scope="row"><span>배송지 주소</span></th>
									<td>
										<div class="input_box_1">
											<input type="text" id="zip_cd1" name="zip_cd1"
												class="input_2 ws_1" value="${zip_cd1 }" readonly> -
											<input type="text" id="zip_cd2" name="zip_cd2"
												class="input_2 ws_1" value="${zip_cd2 }" readonly> <a
												href="#" onclick="open_zipcode();" class="address_btn">주소찾기</a>
											<label> <input type="checkbox" id="default_yn"
												name="default_yn" class="check" value="Y"
												<c:if test = "${myaddress.default_yn eq 'Y'}">checked</c:if>>
												주소록에 기본배송지로 저장
											</label>
										</div>
										<div class="input_box_1">
											<input type="text" id="addr1" name="addr1"
												class="input_2 ws_2" value="${myaddress.addr1 }" readonly>
										</div>
										<div class="last">
											<input type="text" id="addr2" name="addr2"
												class="input_2 ws_2" value="${myaddress.addr2 }" readonly>
										</div>
									</td>
								</tr>

								<tr>
									<th scope="row"><span>배송지명</span></th>
									<td><input type="text" id="receiver_title"
										name="receiver_title" class="input_2 ws_3"
										value="${myaddress.receiver_title }"> <label><input
											type="checkbox" id="" name="" class="check"
											onclick="addOption();"> 수취인명과 동일하게 적용</label></td>
								</tr>

								<tr>
									<th scope="row"><span>수취인 휴대폰</span></th>
									<td><input type="text" id="tel" name="tel"
										class="input_2 ws_1" value="${tel }" maxlength="3"> -
										<input type="text" id="tel1" name="tel1" class="input_2 ws_1"
										value="${tel1 }" maxlength="4"> - <input type="text"
										id="tel2" name="tel2" class="input_2 ws_1" value="${tel2 }"
										maxlength="4"></td>
								</tr>
								<tr>
									<th scope="row"><span>수취인 연락처</span></th>
									<td><input type="text" id="cell" name="cell"
										class="input_2 ws_1" value="${cell }" maxlength="3"> -
										<input type="text" id="cell1" name="cell1"
										class="input_2 ws_1" value="${cell1 }" maxlength="4">
										- <input type="text" id="cell2" name="cell2"
										class="input_2 ws_1" value="${cell2 }" maxlength="4">
									</td>
								</tr>

							</tbody>
						</table>
					</div>

					<div class="btn_bottom_1">
						<input type="submit" value="확인"> <a
							href="javascript:history.back(-1);">취소</a>
					</div>
				</form>
			</div>
		</div>
	</div>