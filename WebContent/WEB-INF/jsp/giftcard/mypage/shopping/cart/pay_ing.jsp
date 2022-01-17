<%@ page import="aegis.pgclient.*,java.text.*,java.net.*,java.lang.*" contentType="text/html; charset=utf-8" %>
<%@ page import="com.mc.common.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
//PgClientBean40 agspay = (PgClientBean40)request.getAttribute("agspay");
%>
<html>
<head>
<script type="text/javascript">
$(function(){
	frmAGS_pay_ing.submit();
});
</script>
</head>
<!-- <body > -->
<body>
<form name="frmAGS_pay_ing" method=post action="/giftcard/mypage/shopping/cart/${pathIndex }.do?mode=pay_result">

<!-- 각 결제 공통 사용 변수 -->
<%-- <input type=hidden name=AuthTy value="<%=agspay.getResult("AuthTy")%>">		<!-- 결제형태 -->
<input type=hidden name=SubTy value="<%=agspay.getResult("SubTy")%>">		<!-- 서브결제형태 -->
<input type=hidden name=rStoreId value="<%=agspay.getResult("rStoreId")%>">	<!-- 상점아이디 --> --%>
<input type=hidden name=rOrdNo value="${params.orderno }">		<!-- 주문번호 -->
<input type=hidden name=virAcct value="${params.virAcct }">		<!-- 가상결제계좌번호 -->
<%-- <input type=hidden name=rProdNm value="<%=agspay.getResult("ProdNm")%>">	<!-- 상품명 -->
<input type=hidden name=rAmt value="<%=agspay.getResult("rAmt")%>">			<!-- 결제금액 -->
<input type=hidden name=rOrdNm value="<%=agspay.getResult("OrdNm")%>">		<!-- 주문자명 -->

<input type=hidden name=AGS_HASHDATA value="<%=agspay.getResult("AGS_HASHDATA")%>">		<!-- 전역 해쉬 변수 -->

<input type=hidden name=rSuccYn value="<%=agspay.getResult("rSuccYn")%>">	<!-- 성공여부 -->
<input type=hidden name=rResMsg value="<%=agspay.getResult("rResMsg")%>">	<!-- 결과메시지 -->
<input type=hidden name=rApprTm value="<%=agspay.getResult("rApprTm")%>">	<!-- 결제시간 -->

<!-- 신용카드 결제 사용 변수 -->
<input type=hidden name=rBusiCd value="<%=agspay.getResult("rBusiCd")%>">	<!-- (신용카드공통)전문코드 -->
<input type=hidden name=rApprNo value="<%=agspay.getResult("rApprNo")%>">	<!-- (신용카드공통)승인번호 -->
<input type=hidden name=rCardCd value="<%=agspay.getResult("rCardCd")%>">	<!-- (신용카드공통)카드사코드 -->
<input type=hidden name=rDealNo value="<%=agspay.getResult("rDealNo")%>">	<!-- (신용카드공통)거래번호 -->
<input type=hidden name=Instmt value="<%=agspay.getResult("Instmt")%>">		<!-- 할부개월수 -->

<input type=hidden name=rCardNm value="<%=agspay.getResult("rCardNm")%>">	<!-- (안심클릭,일반사용)카드사명 -->
<input type=hidden name=rMembNo value="<%=agspay.getResult("rMembNo")%>">	<!-- (안심클릭,일반사용)가맹점번호 -->
<input type=hidden name=rAquiCd value="<%=agspay.getResult("rAquiCd")%>">	<!-- (안심클릭,일반사용)매입사코드 -->
<input type=hidden name=rAquiNm value="<%=agspay.getResult("rAquiNm")%>">	<!-- (안심클릭,일반사용)매입사명 -->


<!-- 계좌이체 결제 사용 변수 -->
<input type=hidden name=ICHE_OUTBANKNAME value="<%=agspay.getResult("ICHE_OUTBANKNAME")%>">		<!-- 이체은행명 -->
<input type=hidden name=ICHE_OUTBANKMASTER value="<%=agspay.getResult("ICHE_OUTBANKMASTER")%>">	<!-- 이체계좌예금주 -->
<input type=hidden name=ICHE_AMOUNT value="<%=agspay.getResult("ICHE_AMOUNT")%>">				<!-- 이체금액 -->

<!-- 핸드폰 결제 사용 변수 -->
<input type=hidden name=rHP_HANDPHONE value="<%=agspay.getResult("HP_HANDPHONE")%>">			<!-- 핸드폰번호 -->
<input type=hidden name=rHP_COMPANY value="<%=agspay.getResult("HP_COMPANY")%>">				<!-- 통신사명(SKT,KTF,LGT) -->
<input type=hidden name=rHP_TID value="<%=agspay.getResult("rHP_TID")%>">						<!-- 결제TID -->
<input type=hidden name=rHP_DATE value="<%=agspay.getResult("rHP_DATE")%>">						<!-- 결제일자 -->

<!-- ARS 결제 사용 변수 -->
<input type=hidden name=rARS_PHONE value="<%=agspay.getResult("ARS_PHONE")%>">							<!-- ARS번호 -->

<!-- 가상계좌 결제 사용 변수 -->
<input type=hidden name=rVirNo value="<%=agspay.getResult("rVirNo")%>">							<!-- 가상계좌번호 -->
<input type=hidden name=VIRTUAL_CENTERCD value="<%=agspay.getResult("VIRTUAL_CENTERCD")%>">		<!-- 입금가상계좌은행코드(우리은행:20,신한은행:88) -->

<input type=hidden name=mTId value="<%=agspay.getResult("mTId")%>">								

<!-- 이지스에스크로 결제 사용 변수 -->
<input type=hidden name=ES_SENDNO value="<%=agspay.getResult("ES_SENDNO")%>">					<!-- 이지스에스크로(전문번호) --> --%>

</form>
</body>
</html>
