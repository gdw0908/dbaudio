<%@ page import="java.util.*,java.text.*,java.net.*" contentType="text/html; charset=utf-8" %>

<%
/**********************************************************************************************
*
* 파일명 : AGS_cancel_result.jsp
* 최종수정일자 : 2007/04/25
*
* 소켓취소결과를 처리합니다.
*
* Copyright AEGIS ENTERPRISE.Co.,Ltd. All rights reserved.
*
**********************************************************************************************/ 

String rStoreId = request.getParameter("rStoreId");			//업체ID
String rApprNo = request.getParameter("rApprNo");			//승인번호
String rApprTm = request.getParameter("rApprTm");			//승인시각
String rBusiCd = request.getParameter("rBusiCd");			//전문코드
String rOrdNo = request.getParameter("rOrdNo");				//주문번호
String rInstmt = request.getParameter("rInstmt");			//할부개월
String rAmt = request.getParameter("rAmt");					//결제금액
String rCardCd = request.getParameter("rCardCd");			//카드사코드
String rMembNo = request.getParameter("rMembNo");			//가맹점번호
String rAquiCd = request.getParameter("rAquiCd");			//매입사코드
String rDealNo = request.getParameter("rDealNo");			//전표번호
String rCardNm = request.getParameter("rCardNm");		//카드사명
String rAquiNm = request.getParameter("rAquiNm");		//매입사명
String rCancelSuccYn = request.getParameter("rCancelSuccYn");			//성공여부
String rCancelResMsg = request.getParameter("rCancelResMsg");		//실패사유
String PartCancelAmt = request.getParameter("PartCancelAmt");	//부분취소금액
%>
<html>
<head>
<title>올더게이트</title>
<style type="text/css">
<!--
body { font-family:"돋움"; font-size:9pt; color:#000000; font-weight:normal; letter-spacing:0pt; line-height:180%; }
td { font-family:"돋움"; font-size:9pt; color:#000000; font-weight:normal; letter-spacing:0pt; line-height:180%; }
.clsright { padding-right:10px; text-align:right; }
.clsleft { padding-left:10px; text-align:left; }
-->
</style>
<script language=javascript>
<!--
var openwin = window.open("AGS_progress.html","popup","width=300,height=160");
openwin.close();
-->
</script>
</head>
<body topmargin=0 leftmargin=0 rightmargin=0 bottommargin=0>
<table border=0 width=100% height=100% cellpadding=0 cellspacing=0>
	<tr>
		<td align=center>
		<table width=400 border=0 cellpadding=0 cellspacing=0>
			<tr>
				<td><hr></td>
			</tr>
			<tr>
				<td class=clsleft>취소 결과</td>
			</tr>
			<tr>
				<td><hr></td>
			</tr>
			<tr>
				<td>
				<table width=400 border=0 cellpadding=0 cellspacing=0>
					<tr>
						<td class=clsright width=100>상점아이디 : </td>
						<td class=clsleft width=300><%=rStoreId%></td>
					</tr>
					<tr>
						<td class=clsright>승인번호 : </td>
						<td class=clsleft><%=rApprNo%></td>
					</tr>
					<tr>
						<td class=clsright>승인시각 : </td>
						<td class=clsleft><%=rApprTm%></td>
					</tr>
					<tr>
						<td class=clsright>전문코드 : </td>
						<td class=clsleft><%=rBusiCd%></td>
					</tr>
					<tr>
						<td class=clsright>성공여부 : </td>
						<td class=clsleft><%=rCancelSuccYn%></td>
					</tr>
					<tr>
						<td class=clsright>처리메세지 : </td>
						<td class=clsleft><%=rCancelResMsg%></td>
					</tr>
					<tr>
						<td class=clsright>주문번호 : </td>
						<td class=clsleft><%=rOrdNo%></td>
					</tr>
					<tr>
						<td class=clsright>할부개월 : </td>
						<td class=clsleft><%=rInstmt%></td>
					</tr>
					<tr>
						<td class=clsright>결제금액 : </td>
						<td class=clsleft><%=rAmt%></td>
					</tr>
					<tr>
						<td class=clsright>카드사코드 : </td>
						<td class=clsleft><%=rCardCd%></td>
					</tr>
					<tr>
						<td class=clsright>카드사명 : </td>
						<td class=clsleft><%=rCardNm%></td>
					</tr>
					<tr>
						<td class=clsright>매입사코드 : </td>
						<td class=clsleft><%=rAquiCd%></td>
					</tr>
					<tr>
						<td class=clsright>매입사명 : </td>
						<td class=clsleft><%=rAquiNm%></td>
					</tr>
					<tr>
						<td class=clsright>가맹점번호 : </td>
						<td class=clsleft><%=rMembNo%></td>
					</tr>
					<tr>
						<td class=clsright>전표번호 : </td>
						<td class=clsleft><%=rDealNo%></td>
					</tr>
					<tr>
						<td class=clsright>부분취소금액 : </td>
						<td class=clsleft><%=PartCancelAmt%></td>
					</tr>
					<tr>
						<td colspan=2>&nbsp;</td>
					</tr>
					<tr>
						<td align=center colspan=2>카드 이용명세서에 구입처가 <font color=red>이지스엔터프라이즈(주)</font>로 표기됩니다.</td>
					</tr>
					
				</table>
				</td>
			</tr>
			<tr>
				<td><hr></td>
			</tr>
			<tr>
				<td class=clsleft>Copyright AEGIS ENTERPRISE.Co.,Ltd. All rights reserved.</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html>
