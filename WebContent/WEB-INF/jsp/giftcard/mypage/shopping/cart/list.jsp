<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld"%>
<%@ taglib prefix="suf" uri="/WEB-INF/tlds/StringUtil_fn.tld"%>
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
<title>장바구니</title>

<link rel="stylesheet"
	href="//cdn.jsdelivr.net/npm/xeicon@2.3.3/xeicon.min.css">
<link rel="stylesheet" href="/lib/css/sub_2.css" type="text/css">
<link rel="stylesheet" href="/lib/css/join.css" type="text/css">
<link rel="stylesheet" href="/lib/css/article.css" type="text/css">

<script type="text/javascript" src="/lib/js/jquery.cookie.js"></script>
<script type="text/javascript" src="/lib/js/jquery.lck.util.js"></script>

<script type="text/javascript">
	$(function() {
		// 햄버거 메뉴
		$(document).ready(function() {
			$('.ham_wrap').click(function() {
				$(this).toggleClass('open');
				$('.mo_menu_wrap').toggleClass('active');
				$('.mo_bg').toggleClass('active');
			});
		});

		//전체 카테고리
		$("#allmenu_open").click(function() {
			$('i.xi-bars').toggleClass('xi-close');
		});

		$(".chk_all").on("click", function() {
			if ($(this).prop("checked")) {
				$("input[name='chk']").prop("checked", true);
			} else {
				$("input[name='chk']").prop("checked", false);
			}
			chkReset();
		});

		$("input[name='chk']").on("click", function() {
			chkReset();
		});

		$("input[name='qty']").on(
				"keyup",
				function(e) {
					$(this).val($.Number($(this).val()));
					// TODO 재고수량 가져와서 체크

					$(this).closest("tr").find("input[name='chk']").attr("qty",
							$(this).val());
					// 		chkReset();
				});
		chkReset();
	});

	function changeQty(idx) {
		var obj = $("input[name='chk']").eq(idx);
		var qty = $("input[name='qty']").eq(idx).val();
		$.getJSON("/giftcard/mypage/shopping/cart/index.do?mode=qtyChange", {
			cart_no : obj.val(),
			item_seq : obj.attr("item_seq"),
			qty : qty
		}, function(data) {
			if (data.rst == "1") {
				obj.attr("qty", qty);
			} else {
				alert("최대 구매하실수 있는 수량은 " + data.qty + " 입니다.");
				$("input[name='qty']").eq(idx).val(data.qty);
				obj.attr("qty", data.qty);
			}
			placeCalc(idx);
			chkReset();
		});
	}

	//배송비 선결제 설정
	function changeCod(idx, val) {
		var obj = $("input[name='chk']").eq(idx);
		var cart_no = obj.val();
		obj.attr("cod_yn", val);
		$.getJSON("/giftcard/mypage/shopping/cart/index.do?mode=changeCod", {
			cart_no : cart_no,
			cod_yn : val
		}, function(data) {
			if (data.rst == "1") {

			} else {
				alert("배송비 선결제/착불 변경에 실패하였습니다.");
			}
			chkReset();
		});
	}

	function placeCalc(idx) {
		var obj = $("input[name='chk']").eq(idx);
		var html = "";

		html += "<p><b class='c1'>"
				+ $.addComma(obj.attr("prod_price") * obj.attr("qty"))
				+ "원</b><p>";
		$(".price_calc").eq(idx).html(html);
	}

	function chkReset() {
		var user_price_sum = 0;
		var sale_price_sum = 0;
		var fee_price_sum = 0;
		$("input[name='chk']:checked").each(function() {
			var qty = $(this).attr("qty");

			user_price_sum += Number($(this).attr("prod_price")) * qty;
			sale_price_sum += Number($(this).attr("prod_price")) * qty;
		});
		$("#select_cnt").text($("input[name='chk']:checked").size());
		$("#user_price").text($.addComma(user_price_sum));
		$("#discount_price").text($.addComma(user_price_sum - sale_price_sum));
		$("#fee_price").text($.addComma(fee_price_sum));//배송비
		$("#actual_price").text($.addComma(sale_price_sum + fee_price_sum));
	}

	function removeCart() {
		var rst = [];
		$("input[name='chk']:checked").each(function() {
			rst.push({
				cart_no : $(this).val()
			});
		});
		$.getJSON("/giftcard/mypage/shopping/cart/index.do?mode=remove_cart", {
			jData : JSON.stringify({
				del : rst
			})
		}, function(data) {
			location.href = "index.do";
		});
	}

	function goStep2() {
		$("#frm").submit();
	}
</script>
</head>
<body>
	<div id="sub">
		<div class="contents">
			<div class="title_rocation">
				<h4>장바구니</h4>
			</div>
			<ul class="cart_list_1">
				<li>장바구니에 담긴 상품은 7일동안 보관되며 자동으로 삭제됩니다.</li>
				<li class="last"><strong>주문하실 상품을 선택 해 주세요.</strong></li>
			</ul>
			<form id="frm" name="frm" action="index.do" method="get">
				<input type="hidden" name="mode" value="step2" />
				<article class="table_container" style="margin-bottom: 60px;">
					<table class="cart_style_1 cart_style_list">
						<caption>장바구니 리스트</caption>
						<colgroup>
							<col width="8%">
							<col width="42%">
							<col width="15%">
							<col width="17%">
							<col width="17%">
						</colgroup>
						<thead>
							<tr>
								<th scope="col"><input type="checkbox" class="chk_all"></th>
								<th scope="col">제품정보</th>
								<th scope="col">수량</th>
								<th scope="col">판매가격</th>
								<th scope="col">배송비</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${fn:length(data.list) == 0}">
									<td class="b_none" colspan="6"
										style="width: 100vw; height: 240px;">
										<p class="none_img">
											<img src="/images/sub_2/none_cart.gif" alt="상품없음이미지">
										</p>
										<p class="none_text">상품이 존재하지 않습니다.</p>
									</td>
								</c:when>
								<c:otherwise>
									<c:forEach var="item" items="${data.list }" varStatus="status">
										<tr>
											<td class="b_none"><input type="checkbox" name="chk"
												value="${item.cart_no }" item_seq="${item.item_seq }"
												prod_price="${item.user_price }" qty="${item.qty }"
												fee_amt="${item.fee_amt}">
											</td>
											<td class="cart_main">
												<div class="product_box">
													<div class="pb_l">
														<a
															href="/giftcard/goods/view.do?menu=menu2&seq=${item.item_seq }">
															<img src="${item.thumb }" alt="">
														</a>
													</div>
													<div class="pb_r ws_1">
														<p>
															<a
																href="/giftcard/goods/view.do?menu=menu2&seq=${item.item_seq }">
																<span><strong>${item.MAKERNM }</strong></span> <span
																class="pro_name"> <strong>${item.PRODUCTNM }</strong></span>
															</a>
														</p>
													</div>
												</div>
											</td>
											<td>
												<p class="first">
													<input type="text" name="qty" class="cart_type_1"
														value="${item.qty }" min="0" autocomplete="off">
												</p>
												<p>
													<a href="javascript:changeQty('${status.index }')"><img
														src="/images/sub_2/cart_btn1.gif" alt="변경"></a>
												</p>
											</td>
											<td class="price_calc">
												<p>
													<b class="c1">${suf:getThousand(item.USER_PRICE * item.QTY) }원</b>
												<p>
											</td>
											<td class="b_none"><c:choose>
													<c:when test="${item.fee_yn eq 'C' }">착불</c:when>
													<c:when test="${item.fee_yn eq 'Y' }">
														<select class="delivery_sel" name="cod_yn"
															onchange="changeCod('${status.index }', this.value)">
															<option value="Y"
																<c:if test="${item.cod_yn eq 'Y'}">selected="selected"</c:if>>선결제</option>
															<option value="N"
																<c:if test="${item.cod_yn eq 'N'}">selected="selected"</c:if>>착불</option>
														</select>
														<br />
	              										(${suf:getThousand(item.FEE_AMOUNT) } 원)
	              									</c:when>
													<c:otherwise>무료</c:otherwise>
												</c:choose></td>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</article>
			</form>

			<div class="product_btn">
				<label><input type="checkbox" class="chk_all"> <span>전체선택</span></label>
				<a href="javascript:removeCart()"><img
					src="/images/sub_2/cart_s_btn1.gif" alt="선택삭제"></a>
			</div>

			<div class="pricecheck">
				<div class="p_check1">
					<div class="top">
						<span class="pt_l"><strong>정상가격</strong></span> <span class="pt_r">선택상품
							: <b id="select_cnt">0</b>개
						</span>
					</div>
					<div class="bottom">
						<p>
							<b id="user_price">0</b>원
						</p>
						<!-- <p class="pb_type">
							<span class="pb_l">선결제배송비</span>
							<span class="pb_r"><b id="fee_price">0</b>원</span>
						</p> -->
					</div>
				</div>
				<div class="p_check2">
					<div class="top">
						<span class="pt_l"> <strong>할인금액</strong>
						</span> <span class="pt_r"> <a href=""><img
								src="/images/sub_2/guide_btn1.gif" alt="?"></a>
						</span>
					</div>
					<div class="bottom">
						<p class="minus">
							<b id="discount_price">0</b>원
						</p>
					</div>
				</div>

				<div class="p_check3">
					<div class="top">
						<span class="pt_l"><strong>총 구매금액</strong></span>
					</div>
					<div class="bottom">
						<p class="equal">
							<b id="actual_price">0</b>원
						</p>
					</div>
				</div>
			</div>

			<div class="cart_btn">
				<a href="javascript:goStep2()" style="color: #fff;">주문결제</a>
			</div>

		</div>
	</div>
</body>