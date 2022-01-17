<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.util.HashMap"%>
<%@ page import="kr.co.allthegate.mobile.*"%>
<%@ page import="com.mc.common.util.*" %>
<%

	///////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// 올더게이트 모바일 카드 결제취소 페이지
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////
	
	String logPath = Util.getProperty("mobile_path");
	String store_id = Util.getProperty("store_id");

	String tracking_id = request.getParameter("tracking_id");
	String transaction = request.getParameter("transaction");
	String SendNo = request.getParameter("SendNo");
	String AdmNo = request.getParameter("AdmNo");
	String AdmDt = request.getParameter("AdmDt");
	String Store_OrdNo = request.getParameter("Store_OrdNo");
	String log_path = logPath;
	String textValue = "";

	if( Cancel_Check(Store_OrdNo) == true ){
	
		AGSMobile mobile = new AGSMobile(store_id, tracking_id, transaction, log_path);
		HashMap<String, Object> ret = new HashMap<String, Object>();
		mobile.setLogging(true);	//true : 로그기록, false : 로그기록안함.
		
		ret = mobile.cancel(AdmNo, AdmDt, SendNo, "");
		JSONObject data = ((JSONObject)ret.get("data"));

		if(ret.get("status").equals("ok")){	// 승인성공

		}else{	// 승인실패

		textValue = (String)ret.get("message");
		
		}
	}else{
		textValue = "취소 원거래건을 찾지 못했습니다.";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type = "text/javascript">
alert("결제를 취소 하셨습니다\n\n사유 : <%=textValue%>");
location.replace("/mobile/mypage/shopping/cart/list.do?mode=m_add_cart_list");
</script>
</head>
<body>
</body>	
</html>

<%!
	public boolean Cancel_Check(String Store_OrdNo)
	{
		boolean flag = false;

		/***********************************************************************************
		*여기서 상점측 원거래 정보를 가져옵니다.
		*취소요청 건의 원거래가 상점측 원거래 정보와 동일하고
		*취소가 가능한 상태이면 True, 아니면 False 
		*원거래 체크로직은 상점에 알맞게 추가/변경하세요     
		************************************************************************************/

	/*	Dim Order			//ex. 상점 원거래정보
		
		if( Store_OrdNo == Order ) {
		   flag = true;
		}else{
		   flag = false;
		}
	*/

		return flag;
	}
%>