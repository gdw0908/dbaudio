<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page language="java" import="Kisinfo.Check.IPINClient" %>
<%@ page import="com.mc.common.util.*" %>
<%
	String home_url = Util.getProperty("home.url");
	String namesitecd = Util.getProperty("name.namesitecd");
	String namesitepwd = Util.getProperty("name.namesitepwd");
	String ipinsitecd = Util.getProperty("ipin.ipinsitecd");
	String ipinsitepwd = Util.getProperty("ipin.ipinsitepwd");
	

	/* 실명인증 시작 */
    
	NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();
    
    String sSiteCode = namesitecd;				// NICE로부터 부여받은 사이트 코드
    String sSitePassword = namesitepwd;		// NICE로부터 부여받은 사이트 패스워드
    
    String sRequestNumber = "REQ0000000001";        	// 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로 
                                                    	// 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
    sRequestNumber = niceCheck.getRequestNO(sSiteCode);
  	session.setAttribute("REQ_SEQ" , sRequestNumber);	// 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.
  	
   	String sAuthType = "";      	// 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서
   	
   	String popgubun 	= "N";		//Y : 취소버튼 있음 / N : 취소버튼 없음
	String customize 	= "";		//없으면 기본 웹페이지 / Mobile : 모바일페이지
		
    // CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
    String sReturnUrl = home_url+"/CheckPlusSafe/checkplus_success.jsp";      // 성공시 이동될 URL
    String sErrorUrl = home_url+"/CheckPlusSafe/checkplus_fail.jsp";          // 실패시 이동될 URL

    // 입력될 plain 데이타를 만든다.
    String sPlainData = "7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber +
                        "8:SITECODE" + sSiteCode.getBytes().length + ":" + sSiteCode +
                        "9:AUTH_TYPE" + sAuthType.getBytes().length + ":" + sAuthType +
                        "7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl +
                        "7:ERR_URL" + sErrorUrl.getBytes().length + ":" + sErrorUrl +
                        "11:POPUP_GUBUN" + popgubun.getBytes().length + ":" + popgubun +
                        "9:CUSTOMIZE" + customize.getBytes().length + ":" + customize;
    
    String sMessage = "";
    String sEncData = "";
    
    int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);
    sEncData = niceCheck.getCipherData();
    
    /* 실명인증 끝*/
    
    /* IPIN 시작 */
    
    String sIpinSiteCode				= ipinsitecd;
	String sIpinSitePw					= ipinsitepwd;
	String sIpinReturnURL				= home_url+ "/IPIN/ipin_result.jsp";
	String sIpinCPRequest				= "";
	String sIpinEncData					= "";
	
	IPINClient pClient = new IPINClient();
	
	sIpinCPRequest = pClient.getRequestNO(sIpinSiteCode);
	
	session.setAttribute("CPREQUEST" , sIpinCPRequest);
	
	int iRtn = pClient.fnRequest(sIpinSiteCode, sIpinSitePw, sIpinCPRequest, sIpinReturnURL);
	
	sIpinEncData = pClient.getCipherData();
	
	
	/* IPIN 끝 */
%>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<title>회원가입</title>
<link rel="stylesheet" href="/lib/css/join.css" type="text/css">
<script type = "text/javascript">
function checkReadName()
{
	window.open('', 'popupChk', 'width=500, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
	document.form_chk.action = "https://nice.checkplus.co.kr/CheckPlusSafeModel/checkplus.cb";
	document.form_chk.target = "popupChk";
	document.form_chk.submit();

}

function failCerti()
{
	alert("인증에 실패하였습니다. 다시 시도 하여주십시오.\n계속 문제가 발생할 경우 관리자에게 문의하시기 바랍니다.");
	return;
}

function go_next(str, ipin)
{
	$.ajax({
		url : "/json/list/member.joined.do", 
		type: "POST", 
		data : {"member_nm" : str, "ipin" : ipin}, 
		dataType : "json", 
		async: false, 
		cache : false, 
		success : function(data)
		{
			if(data.length > 0)
			{
				alert("가입되어 있는 정보가 포함 되어있습니다.");
				return;
			}
			else
			{
				alert("인증이 완료 되었습니다.");
				document.next_step.member_nm.value = str;
				document.next_step.ipin.value = ipin;
				document.next_step.submit();
			}
		},
		error : function(data)
		{
			alert("통신에 실패 하였습니다.\n관리자에게 문의바랍니다.");
		}
	});
}

function iPinPop(){
	window.open('', 'popupIPIN2', 'width=450, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
	document.form_ipin.target = "popupIPIN2";
	document.form_ipin.action = "https://cert.vno.co.kr/ipin.cb";
	document.form_ipin.submit();
}

</script>
</head>

<body>

    <div class="title_rocation">
      <div class="tr_wrap">
        <h3><img src="/images/join/join_title.gif" alt="회원가입"></h3>
        <ul>
          <li><img src="/images/join/join_r1_on.gif" alt="01. 실명인증"></li>
          <li><img src="/images/join/join_r2.gif" alt="02. 약관동의"></li>
          <li><img src="/images/join/join_r3.gif" alt="03. 회원정보입력"></li>
          <li><img src="/images/join/join_r4.gif" alt="04. 가입완료"></li>
        </ul>
      </div>
    </div>

    <div class="j_wrap">

      <div class="j_visual"><img src="/images/join/jm_img1.gif" alt="국내최대 자동차 중고부품 쇼핑몰 PARTS MOA에 오신 것을 환영합니다"></div>

        
        <div class="join_type_1">

          <div class="t_box1">
            <p>
            페어링 오디오에서는 깨끗하고 <strong>안전한 인터넷 서비스 이용을 위해 본인확인</strong>을 받고 있습니다. <br>
            입력하신 개인정보는 회원님의 동의 없이 제3자에게 제공되지 않으며, <strong>개인정보취급방침에 따라 보호</strong>되고 있습니다.
            </p>
          </div>

          <div class="authentication">

            <div class="phone">

              <dl>
              <dt>휴대폰인증 서비스</dt>
              <dd>
              이용자의 개인정보를 보호하기 위해 웹사이트에<br>
              주민등록번호를 제공하지 않고 본인확인 하는<br>
              인터넷상의 개인식별번호 서비스입니다.
              </dd>
              <!-- <dd><a href="/join/join_2.do"><img class="btn" src="/images/join/phone_auth_btn_off.gif" alt="휴대폰 본인인증"></a></dd> -->
               <dd><img class="btn" src="/images/join/phone_auth_btn_off.gif" alt="휴대폰 본인인증" onclick = "checkReadName();" style = "cursor:pointer;"></dd>  
             <!-- <dd><a href = "/join/join_2.do"><img class="btn" src="/images/join/phone_auth_btn_off.gif" alt="휴대폰 본인인증" style = "cursor:pointer;"></a></dd> -->
              </dl>

            </div>

            <div class="ipin">

              <dl>
              <dt>아이핀인증 서비스</dt>
              <dd>
              이용자의 개인정보를 보호하기 위해 웹사이트에<br>
              주민등록번호를 제공하지 않고 본인확인 하는<br>
              인터넷상의 개인식별번호 서비스입니다.
              </dd>
               <dd><img class="btn" src="/images/join/ipin_auth_btn_off.gif" alt="아이핀 본인인증" onclick = "iPinPop();" style = "cursor:pointer;"></dd>   
              <!-- <dd><a href = "/join/join_2.do"><img class="btn" src="/images/join/ipin_auth_btn_off.gif" alt="아이핀 본인인증" style = "cursor:pointer;"></a></dd> -->
              </dl>

            </div>

          </div>

          <p class="warning">※ 입력하신 주민등록번호는 신용평가기관에 <strong>본인확인용으로 제공되며, 회원정보에 저장하지 않습니다.</strong><br>
          <strong>타인의 정보 및 주민등록번호를 부정하게 사용하는 경우 3년 이하의 징역 또는 1천만원 이하의 벌금에 처해지게 됩니다.</strong> ※ 관련법률 : 주민등록법 제 37조(벌칙)</p>

        </div>

      
    </div>
    
    <form name="form_chk" method="post">
		<input type="hidden" name="m" value="checkplusSerivce">
		<input type="hidden" name="EncodeData" value="<%= sEncData %>">
	</form>

	<form name="form_ipin" method="post">
		<input type="hidden" name="m" value="pubmain">
    	<input type="hidden" name="enc_data" value="<%= sIpinEncData %>">
	</form>

	<form name="vnoform" method="post">
		<input type="hidden" name="enc_data">
	</form>
	
	<form name = "next_step" id = "next_step" method = "post" action = "/giftcard/join/join_2.do">
		<input type = "hidden" name = "member_nm" id = "member_nm" value = ""/>
		<input type = "hidden" name = "ipin" id = "ipin" value = ""/>
	</form>
</body>

</html>
