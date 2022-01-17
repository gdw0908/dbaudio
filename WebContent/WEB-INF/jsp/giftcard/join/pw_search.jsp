<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<meta http-equiv="X-UA-Compatible" content="ie=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta content="minimum-scale=1.0, width=device-width, maximum-scale=1, user-scalable=yes" name="viewport" />
<meta name="author" content="31system" />
<meta name="description" content="안녕하세요 페어링사운드 입니다." />
<meta name="Keywords" content="페어링사운드 , 상품권, 백화점 상품권, 롯데 백화점, 롯데 상품권, 갤러리아 백화점, 갤러리아 상품권, 신세계 백화점, 신세계 상품권" />
<title>비밀번호찾기</title>

<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/xeicon@2.3.3/xeicon.min.css">
<link rel="stylesheet" href="/lib/css/join.css" type="text/css">
<script type="text/javascript" src="/lib/js/common.js"></script>
<script type = "text/javascript">


// function openPop()
// {
// 	if(document.getElementById("auth_member_nm_input").value == "")
// 	{
// 		alert("이름을 입력하세요.");
// 		document.getElementById("auth_member_nm_input").value = "";
// 		document.getElementById("auth_member_nm_input").focus();
// 		return;
// 	}
// 	else if(document.getElementById("member_id").value == "")
// 	{
// 		alert("아이디를 입력하세요.");
// 		document.getElementById("member_id").value = "";
// 		document.getElementById("member_id").focus();
// 		return;
// 	}
// 	else
// 	{
// 		if(document.pw_search_method.CheckUser[0].checked == true)
// 		{
// 			checkReadName();
// 		}
// 		else
// 		{
// 			iPinPop();
// 		}
		
// 	}
	
// }

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
	alert("인증이 완료 되었습니다.");

	document.next_step.auth_member_nm.value = str;
	document.next_step.member_id.value = document.getElementById("member_id").value;
	document.next_step.auth_member_nm_1.value = document.getElementById("auth_member_nm_input").value;
	document.next_step.ipin.value = ipin;
	document.next_step.submit();
}

function iPinPop(){
	window.open('', 'popupIPIN2', 'width=450, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
	document.form_ipin.target = "popupIPIN2";
	document.form_ipin.action = "https://cert.vno.co.kr/ipin.cb";
	document.form_ipin.submit();
}

function search_email_form_fnc()
{
	if(document.getElementById("email_member_nm").value == "")
	{
		alert("이름을 입력하세요.");
		document.getElementById("email_member_nm").focus();
		document.getElementById("email_member_nm").value = "";
		return false;
	}
	else if(document.getElementById("email_member_id").value == "")
	{
		alert("아이디를 입력하세요.");
		document.getElementById("email_member_id").focus();
		document.getElementById("email_member_id").value = "";
		return false;
	}
	else if(document.getElementById("email").value == "")
	{
		alert("이메일을 입력하세요.");
		document.getElementById("email").focus();
		document.getElementById("email").value = "";
		return false;
	}
}

</script>
</head>

<body>
<div class="j_wrap">
   <h3 class="tit">비밀번호 찾기</h3>
   <h4 class="login_subtit">회원님이 가입하신 이메일 주소를 통해 비밀번호를 찾을 수 있습니다.</h4>
  <div id="tabNav_j1" class="join_tab pw_search_tab">
    <div id="tab-1" id="tabNav0101" class="tab-content current">
      <div class="t_box1">
        <p><span>개인회원</span>이신경우 : ① 이메일 인증정보 기재&nbsp;&nbsp;&nbsp;② 본인인증 완료 후 비밀번호 재설정</p>
      </div>
      <div class="authentication auth_2">
        <div class="ipin ipin_3">
        <form name = "search_email_form" id = "search_email_form" method = "post" action = "/giftcard/join/pw_search_2.do" onsubmit = "return search_email_form_fnc();">
            <input type = "hidden" name = "mode" id = "mode" value = "pw_search"/>
          <dl class="type_2 type_2_2">
          <dt>이메일 본인인증</dt>
            <dd><span class="standard">이름</span><span>
              <input type="text" id="email_member_nm" name="member_nm" class="input_1 ws_2" placeholder="띄어쓰기 없이 입력해주세요.">
              </span></dd>
            <dd><span class="standard">아이디</span><span>
              <input type="text" id="email_member_id" name="member_id" class="input_1 ws_2">
              </span></dd>
            <dd> <span class="standard">이메일</span> <span>
              <input type="text" id="email" name="email" class="input_1 ws_2">
              </span> </dd>
            <dd class="btn">
              <input type = "submit" value="확인" class="search_btn">
            </dd>
            </dl>
          </form>
        </div>
      </div>
      <div class="b_gui_2">
        <p><strong>위의 방법으로도 비밀번호를 찾지 못했다면, 페어링사운드 쇼핑몰 고객센터로 문의주십시오.</strong><br>
          고객센터 전화문의 : 1566-6444 (상담가능시간: 평일 오전 9시~오후 6시)</p>
      </div>
    </div>
        <div class="pw_s_btn">
          <input type = "submit" value="이메일로 비밀번호 발급받기">
        </div>
      </form>
      <div class="b_gui_2">
        <p><strong>위의 방법으로도 찾지 못했다면, 페어링사운드 쇼핑몰 고객센터로 문의주십시오</strong><br>
          고객센터 전화문의 : 11661-8431 (상담가능시간: 평일 오전 9시~오후 6시)</p>
      </div>
    </div>
  </div>
  <script type="text/javascript">
	//tabs
	$(function() {
	  $('ul.tabs li').click(function() {
	    var tab_id = $(this).attr('data-tab');
	    
	    $('ul.tabs li').removeClass('current');
	    $('.tab-content').removeClass('current');
	    
	    $(this).addClass('current');
	    $('#' + tab_id).addClass('current');
	  });
	});
//       function shwoTabNav(eName, totalNum, showNum) {
//       	for(i=1; i<=totalNum; i++){
//       		var zero = (i >= 10) ? "" : "0";
//       		var e = document.getElementById("tabNav" + eName + zero + i);
//       		var eTitle = document.getElementById("tabNavTitle" + eName + zero + i);
//       		e.style.display = "none";
//       		eTitle.className = "";
//       	}

//       	var zero = (showNum >= 10) ? "" : "0";
//       	var e = document.getElementById("tabNav" + eName + zero + showNum);
//       	var eTitle = document.getElementById("tabNavTitle" + eName + zero + showNum);
//       	e.style.display = "block";
//       	eTitle.className = "on";
//       }
      </script> 
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
<form name = "next_step" id = "next_step" method = "post" action = "/giftcard/join/pw_search_2.do">
  <input type = "hidden" name = "auth_member_nm" id = "auth_member_nm" value = ""/>
  <input type = "hidden" name = "mode" id = "mode" value = "pw_search"/>
  <input type = "hidden" name = "member_id" id = "member_id" value = ""/>
  <input type = "hidden" name = "ipin" id = "ipin" value = ""/>
  <input type = "hidden" name = "auth_member_nm_1" id = "auth_member_nm_1" value = ""/>
</form>
</body>
</html>
