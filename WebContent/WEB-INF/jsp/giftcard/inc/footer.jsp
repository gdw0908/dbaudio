<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld" %>
<script language="javascript">
	$(function() {
		// back to top button
		  $('.top_btn').on('click', function(e) {
		    e.preventDefault();
		    $('html, body').animate({scrollTop:0}, '300');
		  });
	})
		function action(){
			popwindow = window.open('','pop_window','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,width=405,height=310');
			document.form.target = "pop_window";
			document.form.submit();
			popwindow.focus();
		}
	</script>
<footer id="footer">
  <div class="f_wrap">
    <div class="f_logo"><img src="/images/logo/logo.svg" alt="티켓모아"></div>
    <div class="copy">
      <div class="c_top">
	  <ul>
	  	<li><a href="/giftcard/mypage/annc/annc1.do">이용약관</a></li>
	  	<li><a href="/giftcard/mypage/annc/annc2.do">전자금융거래약관</a></li>
	  	<li><a href="/giftcard/mypage/annc/annc3.do">개인정보 취급방침</a></li>
	  	<li><a href="/giftcard/mypage/annc/annc4.do">E-mail 무단수집거부</a></li>
	  <!-- <li><a href="/mypage/mantoman/index.do">1:1문의</a></li> -->
	  </ul>
	  </div>
      <div class="c_bottom">
	  	<p><strong>대표자</strong> : 이상민 ㅣ <strong>상호</strong> : (주)주식회사 페어링 ㅣ  <br>
	  		<strong>사업자등록번호</strong> : 240-87-01710 <strong>사업자주소</strong> : 경기도 성남시 분당구 서현동251-3 엘지분당에클라트 1109호 ㅣ <br>
	  	<p><strong>TEL</strong> : 1522-7622 ㅣ <strong>E-mail</strong> : pairingpayments@gmail.com<br><span>Copyrightⓒpairingpayments.All Rights Reserved.</span></p>
	  </div>
    </div>
	<p class="top"><a href="#" class="top_btn"><img src="/images/footer/f_top.png" alt="top"></a></p>
	
	<form name="form" method="post" action="http://www.allthegate.com/hyosung/paysafe/escrow_check.jsp">	
		<table border=0 width=100% height=100% cellpadding=0 cellspacing=0 style="display:none;">
			<tr>
				<td align=center>
					<table width=500 border=0 cellpadding=0 cellspacing=0>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><hr></td>
						</tr>
						<tr>
							<td class=clsleft><b>◈ 에스크로 조회페이지 ◈</b></td>
						</tr>
						<tr>
							<td class=clsleft>
							1) 본 에스크로 조회페이지를 상점에 맞게 적절하게 수정하여 사용하십시오.<br>
							2) http://www.allthegate.com/hyosung/paysafe/escrow_check.jsp 경로에<br>&nbsp;&nbsp;&nbsp;상점아이디(service_id) 값과 사업자번호(biz_no) 값을 넣어서 띄워 주시면 됩니다.<br>
							3) 팝업으로 띄우고자 하시는 경우 본 페이지의 스크립트를 그대로 사용하시면 되며<br>&nbsp;&nbsp;&nbsp;팝업사이즈는 405 X 310으로 설정 하시면 됩니다.<br>
							4) 에스크로 이미지는 아래 이미지 이외에 총 4가지의 이미지가 제공되며<br>&nbsp;&nbsp;&nbsp;각 이미지는 소스에 주석처리 되어 있으므로 확인하여 사용하시기 바랍니다.
							</td>
						</tr>
						<tr>
							<td><hr></td>
						</tr>
						<tr>
							<td>
							<table width=500 border=0 cellpadding=0 cellspacing=0>
								<tr>
									<td class=clsleft>상점아이디 :</td>
									<td><input type=text name=service_id value="partsmoa"></td>
									<td></td>
								</tr>
								<tr>
									<td class=clsleft>사업자번호 :</td>
									<td><input type=text name=biz_no value="1288719389"></td>
									<td></td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td><hr></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td align=center>
								<!-- a href="javascript:action();"><img src="images/234-60.gif" border="0"></a-->
								<!-- <a href="javascript:action();"><img src="images/120-60.gif" border="0"></a> -->
								<!-- <a href="javascript:action();"><img src="images/177-100.gif" border="0"></a> -->
								<!-- <a href="javascript:action();"><img src="images/468-60.gif" border="0"></a> -->
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		</form>
	
  </div>
</footer>