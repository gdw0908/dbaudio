<%@ page import="aegis.pgclient.*,java.text.*,java.net.*,java.lang.*" contentType="text/html; charset=euc-kr" %>
<%@ page import="com.mc.common.util.*" %>

<%
	/**********************************************************************************************
	*
	* 파일명 : AGS_cancel_ing.jsp
	* 최종수정일자 : 2007/04/25
	* 
	* 올더게이트 플러그인에서 리턴된 데이타를 받아서 소켓취소요청을 합니다.
	*
	* Copyright  AEGIS ENTERPRISE.Co.,Ltd. All rights reserved.
	*
	**********************************************************************************************/ 

	/****************************************************************************
	*
	* [1] 올더게이트 결제취소시 사용할 로컬 통신서버 IP/Port 번호
	*
	* ENCTYPE	: 0:안심클릭,일반결제 2:ISP
	* LOCALADDR	: PG서버와 통신을 담당하는 암호화Process가 위치해 있는 IP 
	* LOCALPORT	: 포트
	*
	****************************************************************************/

	String LOCALADDR = "220.85.12.3";
	int LOCALPORT = 29760;
	String ENCTYPE = "0";
	 
	/****************************************************************************
	*
	* 승인/취소에 사용될 클래스 객체 생성
	*
	****************************************************************************/

	PgClientBean40 agspay = new PgClientBean40( LOCALADDR, LOCALPORT );

	/****************************************************************************
	*
	* [2] AGS_cancel.html 로 부터 넘겨받을 데이타
	*
	****************************************************************************/

	/*공통사용*/
	//agspay.setValue("AgsPayHome","C:/htdocs/agspay");			//올더게이트 결제설치 디렉토리 (상점에 맞게 수정)
	agspay.setValue("AgsPayHome",Util.getProperty("agspay_path"));		//올더게이트 결제설치 디렉토리 (상점에 맞게 수정)
	agspay.setValue("log","true");							//true : 로그기록, false : 로그기록안함.
	agspay.setValue("logLevel","INFO");						//로그레벨 : DEBUG, INFO, WARN, ERROR, FATAL (해당 레벨이상의 로그만 기록됨)
	agspay.setValue("Type", "Cancel");						//고정값(수정불가)

	agspay.setValue("StoreId",request.getParameter("StoreId"));		//상점아이디
	agspay.setValue("AuthTy",request.getParameter("AuthTy"));			//결제형태
	agspay.setValue("SubTy",request.getParameter("SubTy"));			//서브결제형태
	agspay.setValue("rApprNo",request.getParameter("rApprNo"));			//승인번호
	agspay.setValue("rApprTm",request.getParameter("rApprTm"));			//승인일자
	agspay.setValue("rDealNo",request.getParameter("rDealNo"));			//거래번호
	agspay.setValue("PartCancelAmt",request.getParameter("PartCancelAmt"));	//부분취소금액

	/****************************************************************************
	*
	* [4] 올더게이트 결제서버로 결제를 요청합니다.
	*
	****************************************************************************/
	agspay.startPay();

	/****************************************************************************
	*
	* [5] 취소요청결과에 따른 상점DB 저장 및 기타 필요한 처리작업을 수행하는 부분입니다.
	*
	* 신용카드결제 취소결과가 정상적으로 수신되었으므로 DB 작업을 할 경우 
	* 결과페이지로 데이터를 전송하기 전 이부분에서 하면된다.
	*
	* 여기서 DB 작업을 해 주세요.
	* 취소성공여부 : agspay.getResult("rCancelSuccYn") (성공:y 실패:n)
	* 취소결과메시지 : agspay.getResult("rCancelResMsg")
	*
	****************************************************************************/		
		
	if(agspay.getResult("rCancelSuccYn").equals("y") )
	{ 
		// 결제취소에 따른 처리부분
		out.println ("신용카드 승인취소가 성공처리되었습니다. [" + agspay.getResult("rCancelSuccYn")+"]"+ agspay.getResult("rCancelResMsg") );
	}
	else
	{
		// 결제취소실패에 따른 상점처리부분
		out.println ("신용카드 승인취소가 실패처리되었습니다. [" + agspay.getResult("rCancelSuccYn")+"]"+ agspay.getResult("rCancelResMsg") );
	}
%>
<html>
<head>
</head>
<body onload="javascript:frmAGS_cancel_ing.submit();">
<form name=frmAGS_cancel_ing method=post action=AGS_cancel_result.jsp>
<input type=hidden name=rStoreId value="<%=agspay.getResult("rStoreId")%>">
<input type=hidden name=rApprNo value="<%=agspay.getResult("rApprNo")%>">
<input type=hidden name=rApprTm value="<%=agspay.getResult("rApprTm")%>">
<input type=hidden name=rBusiCd value="<%=agspay.getResult("rBusiCd")%>">
<input type=hidden name=rCancelSuccYn value="<%=agspay.getResult("rCancelSuccYn")%>">
<input type=hidden name=rCancelResMsg value="<%=agspay.getResult("rCancelResMsg")%>">
<input type=hidden name=rOrdNo value="<%=agspay.getResult("rOrdNo")%>">
<input type=hidden name=rInstmt value="<%=agspay.getResult("rInstmt")%>">
<input type=hidden name=rAmt value="<%=agspay.getResult("rAmt")%>">
<input type=hidden name=rCardNm value="<%=agspay.getResult("rCardNm")%>">
<input type=hidden name=rCardCd value="<%=agspay.getResult("rCardCd")%>">
<input type=hidden name=rMembNo value="<%=agspay.getResult("rMembNo")%>">
<input type=hidden name=rAquiCd value="<%=agspay.getResult("rAquiCd")%>">
<input type=hidden name=rAquiNm value="<%=agspay.getResult("rAquiNm")%>">
<input type=hidden name=rDealNo value="<%=agspay.getResult("rDealNo")%>">
<input type=hidden name=PartCancelAmt value="<%=agspay.getResult("PartCancelAmt")%>">
</form>
</body>
</html>
