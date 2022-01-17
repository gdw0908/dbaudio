<%@ page language="java" contentType="text/html;charset=utf-8" %>
<%@ page import="com.mc.common.util.*" %>
<%
    NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();

    String sEncodeData = requestReplace(request.getParameter("EncodeData"), "encodeData");
    String sReserved1  = requestReplace(request.getParameter("param_r1"), "");
    String sReserved2  = requestReplace(request.getParameter("param_r2"), "");
    String sReserved3  = requestReplace(request.getParameter("param_r3"), "");
    
    String namesitecd = Util.getProperty("name.namesitecd");
	String namesitepwd = Util.getProperty("name.namesitepwd");

    String sSiteCode = namesitecd;				   // NICE로부터 부여받은 사이트 코드
    String sSitePassword = namesitepwd;			 // NICE로부터 부여받은 사이트 패스워드

    String sCipherTime = "";				 // 복호화한 시간
    String sRequestNumber = "";			 // 요청 번호
    String sResponseNumber = "";		 // 인증 고유번호
    String sAuthType = "";				   // 인증 수단
    String sName = "";							 // 성명
    String sDupInfo = "";						 // 중복가입 확인값 (DI_64 byte)
    String sConnInfo = "";					 // 연계정보 확인값 (CI_88 byte)
    String sBirthDate = "";					 // 생일
    String sGender = "";						 // 성별
    String sNationalInfo = "";       // 내/외국인정보 (개발가이드 참조)
    String sMessage = "";
    String errorCode = "";
    String sPlainData = "";
    
    int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);

    if( iReturn == 0 )
    {
        sPlainData = niceCheck.getPlainData();
        sCipherTime = niceCheck.getCipherDateTime();
        
        // 데이타를 추출합니다.
        java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);
        
        sRequestNumber  = (String)mapresult.get("REQ_SEQ");
        sResponseNumber = (String)mapresult.get("RES_SEQ");
        sAuthType 			= (String)mapresult.get("AUTH_TYPE");
        sName 					= (String)mapresult.get("NAME");
        sBirthDate 			= (String)mapresult.get("BIRTHDATE");
        sGender 				= (String)mapresult.get("GENDER");
        sNationalInfo  	= (String)mapresult.get("NATIONALINFO");
        sDupInfo 				= (String)mapresult.get("DI");
        sConnInfo 			= (String)mapresult.get("CI");
        
        String session_sRequestNumber = (String)session.getAttribute("REQ_SEQ");
        if(!sRequestNumber.equals(session_sRequestNumber))
        {
            sMessage = "세션값이 다릅니다. 올바른 경로로 접근하시기 바랍니다.";
            sResponseNumber = "";
            sAuthType = "";
        }
    }
%>
<%!

	public String requestReplace (String paramValue, String gubun) {

        String result = "";
        
        if (paramValue != null) {
        	
        	paramValue = paramValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

        	paramValue = paramValue.replaceAll("\\*", "");
        	paramValue = paramValue.replaceAll("\\?", "");
        	paramValue = paramValue.replaceAll("\\[", "");
        	paramValue = paramValue.replaceAll("\\{", "");
        	paramValue = paramValue.replaceAll("\\(", "");
        	paramValue = paramValue.replaceAll("\\)", "");
        	paramValue = paramValue.replaceAll("\\^", "");
        	paramValue = paramValue.replaceAll("\\$", "");
        	paramValue = paramValue.replaceAll("'", "");
        	paramValue = paramValue.replaceAll("@", "");
        	paramValue = paramValue.replaceAll("%", "");
        	paramValue = paramValue.replaceAll(";", "");
        	paramValue = paramValue.replaceAll(":", "");
        	paramValue = paramValue.replaceAll("-", "");
        	paramValue = paramValue.replaceAll("#", "");
        	paramValue = paramValue.replaceAll("--", "");
        	paramValue = paramValue.replaceAll("-", "");
        	paramValue = paramValue.replaceAll(",", "");
        	
        	if(gubun != "encodeData"){
        		paramValue = paramValue.replaceAll("\\+", "");
        		paramValue = paramValue.replaceAll("/", "");
            paramValue = paramValue.replaceAll("=", "");
        	}
        	
        	result = paramValue;
            
        }
        return result;
  }
        
 
%>
<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8" />
    <title>NICE평가정보 - CheckPlus 안심본인인증</title>
<script type="text/javascript" src="/lib/js/jquery-1.7.1.min.js"></script>
</head>
<body>
	
	<form name = "next_step" id = "next_step" method = "post" action = "/mobile/join/join_2.do">
		<input type = "hidden" name = "member_nm" id = "member_nm" value = ""/>
		<input type = "hidden" name = "ipin" id = "ipin" value = ""/>
	</form>
    <script>
    $.ajax({
		url : "/json/list/member.joined.do", 
		type: "POST", 
		data : {"member_nm" : '<%= sName %>', "ipin" : '<%= sDupInfo %>'}, 
		dataType : "json", 
		async: false, 
		cache : false, 
		success : function(data)
		{
			if(data.length > 0)
			{
				alert("가입되어 있는 정보가 포함 되어있습니다.");
				location.href="/mobile/login/login.do?mode=mlogin";
			}
			else
			{
				alert("인증이 완료 되었습니다.");
				$("#member_nm").val('<%= sName %>');
				$("#ipin").val('<%= sDupInfo %>');
				$("#next_step").submit();
			}
		},
		error : function(data)
		{
			alert("통신에 실패 하였습니다.\n관리자에게 문의바랍니다.");
		}
	});
    </script>
</body>
</html>