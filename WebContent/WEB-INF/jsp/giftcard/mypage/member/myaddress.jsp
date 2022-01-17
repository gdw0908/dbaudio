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
<link rel="stylesheet" href="/lib/css/sub_2.css" type="text/css">
<!-- <script type="text/javascript" src="/lib/js/common.js"></script> -->
<script type="text/javascript">
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

function go_delete(seq)
{
	if(confirm("삭제하시겠습니까?"))
	{
		location.replace("${servletPath}?mode=mydelete&seq=" + seq);
	}
}

function default_add()
{
	var url = "";
	
	if( $(":checkbox[name='default_yn']:checked").length == 0 )
	{
	    alert("최소 한개 이상은 선택해야합니다.");
	    return;
	}
	else
	{
		var i = 0;
		var checkC = false;
		$("input[name=default_yn]:checked").each(function() {

			if(i > 0)
			{
				alert("기본배송지는 한개만 선택 가능합니다.");
				
				$("input[name=default_yn]:checkbox").each(function() {
					$(this).prop("checked", false);
				});
				
				checkC = false;
			}
			else
			{
				url += $(this).val() + ",";
				checkC = true;
			}
			
			i++;
		});
		
		if(checkC)
		{
			if(confirm("기본 배송지로 등록 하시겠습니까?"))
			{
				
				//alert("${servletPath}?method=mydefault&seq=" + url.substring(0, url.length -1));
				location.replace("${servletPath}?mode=mydefault&seq=" + url.substring(0, url.length -1));
			}
		}
	}
}

function go_myAddressForm()
{
	jQuery("#myaddressForm").submit();
}

function go_update(seq)
{
	if(confirm("수정 하시겠습니까?"))
	{
		location.replace("${servletPath}?mode=myaddressForm&seq=" + seq);
	}
	
}

</script>
</head>

<body>
	<div class="wrap" style="width: 100%;">

		<div id="sub">
			<h3 class="sub_tit">나의 배송지 관리</h3>
			<div class="contents">

				<div class="t_box1">
					<p>
						<strong>새로운 배송주소를 등록하시려면 주소록 추가하기를 눌러주세요.</strong> <a
							href="javascript:;" class="add_btn1"
							onclick="go_myAddressForm();">주소록 추가하기 ></a>
					</p>
				</div>

				<h4 class="hs_1">배송지 주소록</h4>

				<article class="table_container">
					<table class="s_style_1">
						<colgroup>
							<col width="18%">
							<col width="9%">
							<col width="">
							<col width="14%">
							<col width="16%">
						</colgroup>

						<thead>
							<tr>
								<th scope="col" class="left"><label> 배송지명</label></th>
								<th scope="col">수취인명</th>
								<th scope="col">&nbsp;</th>
								<th scope="col">휴대폰/연락처</th>
								<th scope="col" class="b_none">선택</th>
							</tr>
						</thead>

						<tbody>
							<c:forEach items="${list }" var="article" varStatus="status">
								<tr>
									<td class="left">
										<label>
											<input type="checkbox" id="default_yn" name="default_yn" class="check" value="${article.seq}"
												<c:if test = "${article.default_yn eq 'Y'}">checked</c:if>>
												<c:choose>
												<c:when test="${article.default_yn eq 'Y'}">기본 배송지</c:when>
												<c:otherwise>${article.receiver_title}</c:otherwise>
												</c:choose> 
										</label>
									</td>
									<td>${article.receiver_nm}</td>
									<td class="left">
										<p>[${article.zip_cd}]${article.addr1}</p>
										<p>${article.addr2}</p>
									</td>
									<td>
										<p>${article.cell}</p>
										<p>${article.tel}</p>
									</td>
									<td class="b_none"><a href="#"
									onclick="go_update(${article.seq});">수정</a> <a href="#"
									onclick="go_delete(${article.seq});">삭제</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</article>
				<div class="d_btn_bottom">
					<a href="javascript:;" onclick="default_add();">선택한 주소를 기본배송지로</a>
				</div>
			</div>
		</div>
	</div>
	<form name="myaddressForm" id="myaddressForm" method="post"
		action="${servletPath}">
		<input type="hidden" name="mode" value="myaddressForm" />
	</form>