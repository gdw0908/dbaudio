<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>회원정보입력</title>
<link rel="stylesheet" href="/lib/css/common.css" type="text/css">
<link rel="stylesheet" href="/lib/css/article.css" type="text/css">
<link rel="stylesheet" href="/lib/css/join.css" type="text/css">
<script type="text/javascript" src="/lib/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/lib/js/common_sc.js"></script>
<script type="text/javascript" src="/lib/js/mc.js"></script>
<script type = "text/javascript">

function inputEmail2(value)
{
	if(value == "")
	{
		jQuery("#email2").attr("readonly",false);
		jQuery("#email2").val("");
		jQuery("#email2").focus();
	}
	else
	{
		jQuery("#email2").attr("readonly",true);
		jQuery("#email2").val(value);
	}
}

function CheckPassword(uid, upw) 
{
	if(upw == "")
	{
		return false;
	}
	
    if(!/^[a-zA-Z0-9]{6,15}$/.test(upw))
    { 
        return false;
    }
  
    var chk_num = upw.search(/[0-9]/g); 
    var chk_eng = upw.search(/[a-z]/ig); 

    if(chk_num < 0 || chk_eng < 0)
    { 
        return false;
    }
    
    if(/(\w)\1\1\1/.test(upw))
    {
        return false;
    }
    
    if(upw.search(uid)>-1)
    {
        return false;
    }
    
    return true;
}  

function checkBizID(bizID)  //사업자등록번호 체크 
{ 
	// bizID는 숫자만 10자리로 해서 문자열로 넘긴다. 
	var checkID = new Array(1, 3, 7, 1, 3, 7, 1, 3, 5, 1); 
	var tmpBizID, i, chkSum=0, c2, remander; 
	bizID = bizID.replace(/-/gi,''); 
	for (i=0; i<=7; i++) chkSum += checkID[i] * bizID.charAt(i); 
	c2 = "0" + (checkID[8] * bizID.charAt(8)); 
	c2 = c2.substring(c2.length - 2, c2.length); 
	chkSum += Math.floor(c2.charAt(0)) + Math.floor(c2.charAt(1)); 
	remander = (10 - (chkSum % 10)) % 10 ; 
	if (Math.floor(bizID.charAt(9)) == remander) return true ; // OK! 
	
	return false; 
}

function join_form_chk()
{
	if(jQuery("#member_id").val() == "" || jQuery("#member_id").val().length < 6)
	{
		alert("아이디를 입력하지 않았거나 6자리 이상 등록해야합니다.");	
		jQuery("#member_id").val("");
		jQuery("#member_id").focus();
		return false;
	}
	else if(jQuery("#member_id_chk").val() == "")
	{
		alert("아이디 중복확인을 진행해주세요.");	
		return false;
	}
	else if(!CheckPassword(jQuery("#member_id").val(), jQuery("#member_pw").val() ))
	{
		alert("비밀번호는 아이디와 같을 수 없으며\n6~15글자 이내, 영문 대/소문자, 숫자를 조합해야합니다.");	
		jQuery("#member_pw").val("");
		jQuery("#member_pw").focus();
		return false;
	}
	
	else if(jQuery("#member_pw_chk").val() == "")
	{
		alert("비밀번호 확인을 입력하세요.");	
		jQuery("#member_pw_chk").val("");
		jQuery("#member_pw_chk").focus();
		return false;
	}
	else if(jQuery("#member_pw_chk").val() != jQuery("#member_pw").val())
	{
		alert("비밀번호가 일치 하지 않습니다. 확인해주세요.");	
		jQuery("#member_pw").val("");
		jQuery("#member_pw_chk").val("");
		jQuery("#member_pw").focus();
		return false;
	}
	else if(jQuery("#busi_nm").val() == "")
	{
		alert("업체명을 입력하세요.");	
		jQuery("#busi_nm").val("");
		jQuery("#busi_nm").focus();
		return false;
	}
	else if(jQuery("#busi_no1").val() == "" || isNaN(jQuery("#busi_no1").val()))
	{
		alert("사업자 번호를 입력하지 않았거나 숫자만 입력가능합니다.");	
		jQuery("#busi_no1").val("");
		jQuery("#busi_no1").focus();
		return false;
	}
	else if(jQuery("#busi_no2").val() == "" || isNaN(jQuery("#busi_no2").val()))
	{
		alert("사업자 번호를 입력하지 않았거나 숫자만 입력가능합니다.");	
		jQuery("#busi_no2").val("");
		jQuery("#busi_no2").focus();
		return false;
	}
	else if(jQuery("#busi_no3").val() == "" || isNaN(jQuery("#busi_no3").val()))
	{
		alert("사업자 번호를 입력하지 않았거나 숫자만 입력가능합니다.");	
		jQuery("#busi_no3").val("");
		jQuery("#busi_no3").focus();
		return false;
	}
	else if(!checkBizID(jQuery("#busi_no1").val() + "-" + jQuery("#busi_no2").val() + "-" + jQuery("#busi_no3").val()))
	{
		alert("사업자 번호가 유효하지 않습니다. 확인 부탁드립니다.");	
		jQuery("#busi_no1").val("");
		jQuery("#busi_no2").val("");
		jQuery("#busi_no3").val("");
		jQuery("#busi_no1").focus();
		return false;
	}
	else if(jQuery("#comptyp1").val() == "")
	{
		alert("업태를 입력하세요.");	
		jQuery("#comptyp1").val("");
		jQuery("#comptyp1").focus();
		return false;
	}
	else if(jQuery("#comptyp2").val() == "")
	{
		alert("종목을 입력하세요.");	
		jQuery("#comptyp2").val("");
		jQuery("#comptyp2").focus();
		return false;
	}
	else if(jQuery("#member_nm").val() == "")
	{
		alert("이름(실명) 을 입력하세요.");	
		jQuery("#member_nm").val("");
		jQuery("#member_nm").focus();
		return false;
	}
	else if(jQuery("#staff_nm").val() == "")
	{
		alert("담당자명 입력하세요.");	
		jQuery("#staff_nm").val("");
		jQuery("#staff_nm").focus();
		return false;
	}
	
	else if(jQuery("#tel2").val() == "" || isNaN(jQuery("#tel2").val()))
	{
		alert("전화번호를 입력하지 않았거나 숫자만 입력가능합니다.");	
		jQuery("#tel2").val("");
		jQuery("#tel2").focus();
		return false;
	}
	else if(jQuery("#tel3").val() == "" || isNaN(jQuery("#tel3").val()))
	{
		alert("전화번호를 입력하지 않았거나 숫자만 입력가능합니다.");	
		jQuery("#tel3").val("");
		jQuery("#tel3").focus();
		return false;
	}
	else if(jQuery("#staff_tel1").val() == "" || isNaN(jQuery("#staff_tel1").val()))
	{
		alert("담당자번호를 입력하지 않았거나 숫자만 입력가능합니다.");	
		jQuery("#staff_tel1").val("");
		jQuery("#staff_tel1").focus();
		return false;
	}
	else if(jQuery("#staff_tel2").val() == "" || isNaN(jQuery("#staff_tel2").val()))
	{
		alert("담당자번호를 입력하지 않았거나 숫자만 입력가능합니다.");	
		jQuery("#staff_tel2").val("");
		jQuery("#staff_tel2").focus();
		return false;
	}
	else if(jQuery("#staff_tel3").val() == "" || isNaN(jQuery("#staff_tel3").val()))
	{
		alert("담당자번호를 입력하지 않았거나 숫자만 입력가능합니다.");	
		jQuery("#staff_tel3").val("");
		jQuery("#staff_tel3").focus();
		return false;
	}
	else if(jQuery("#email1").val() == "")
	{
		alert("이메일을 입력하세요.");	
		jQuery("#email1").val("");
		jQuery("#email1").focus();
		return false;
	}
	else if(jQuery("#email2").val() == "")
	{
		alert("이메일을 선택하거나 직접입력을 선택하여 직접 입력하세요.");	
		jQuery("#email2").val("");
		jQuery("#email2").focus();
		return false;
	}
	else if(!chk_email(jQuery("#email2").val()))
	{
		alert("입력하신 이메일 형식이 올바르지 않습니다. 다시 입력하세요.");	
		jQuery("#email2").val("");
		jQuery("#email2").focus();
		return false;
	}
	
	else if(jQuery("#zip_cd1").val() == "" || jQuery("#addr1").val() == "")
	{
		alert("주소검색 버튼을 이용하여 주소를 등록해야합니다.");	
		return false;
	}
	else if(jQuery("#addr2").val() == "")
	{
		alert("상세주소를 입력하세요.");	
		jQuery("#addr2").val("");

		jQuery("#addr2").focus();
		return false;
	}
	
	return true;
}

function chk_email(value)
{
	var pattern = new RegExp(/^([\w-]+\.)+([a-zA-Z]{2,3}$|[\.]?[a-zA-Z]?$)/);
	
	return pattern.test(value);
}

function chk_member_id()
{
	if(jQuery("#member_id").val() == "")
	{
		alert("아이디를 입력하세요.");
		jQuery("#member_id").val("");
		jQuery("#member_id").focus();
		return;
	}
	else
	{
		getJSON("/json/list/member.getMemberIdCheck.do",{"member_id":jQuery("#member_id").val()},function(data){
			$("body").data("chk_member_id",data);
			var chk_member_id = $("body").data("chk_member_id");
			
			$.each(chk_member_id, function(){
				var data = this["member_id"];
			});

			if(data == "" || data == null)
			{
				jQuery("#check_member_id").html("사용 가능한 아이디 입니다.");
				jQuery("#member_id_chk").val("Y");
				return ;
			}
			else
			{
				jQuery("#check_member_id").html("이미 가입 되어있는 아이디 입니다.");
				jQuery("#member_id").val("");
				jQuery("#member_id").focus();
				return ;
			}
			
		});
	}

}

function open_zipcode()
{
	var param = "";
	
	if(arguments[0]){
		param = "?fun="+arguments[0];
	}
	
	window.open("/addr/road.jsp"+param,"addr","width=570,height=420");
	
	com_juso = true;
}

function setAddr(roadAddrPart1, addrDetail, zipNo, jibunAddr)
{
	var zip = zipNo.split("-");
	jQuery("#zip_cd1").val(zip[0]);
	jQuery("#zip_cd2").val(zip[1]);
	jQuery("#addr1").val(roadAddrPart1);
	jQuery("#addr2").val(addrDetail);
}


function move_tab()
{

	jQuery("#move_form").submit();
}
</script>
</head>

<body>
<div class="title_rocation">
  <div class="tr_wrap">
    <h3><img src="/images/join/join_title.gif" alt="회원가입"></h3>
    <ul>
      <li><img src="/images/join/join_r1.gif" alt="01. 실명인증"></li>
      <li><img src="/images/join/join_r2.gif" alt="02. 약관동의"></li>
      <li><img src="/images/join/join_r3_on.gif" alt="03. 회원정보입력"></li>
      <li><img src="/images/join/join_r4.gif" alt="04. 가입완료"></li>
    </ul>
  </div>
</div>
<div class="j_wrap">
  <div class="j_visual_2"><img src="/images/join/jm_img2.gif" alt="국내최대 자동차 중고부품 쇼핑몰 PARTS MOA에 오신 것을 환영합니다"></div>
  <div id="tabNav_j1" class="join_tab">
    <h4 id="tabNavTitle0102" class="on"><a href="javascript:;" >사업자회원</a></h4>
    <div id="tabNav0101" style="display:block;">
    <form method = "post" name = "join_form" id = "join_form" action = "/join/join_4.do" onsubmit = "return join_form_chk();">
        <input type = "hidden" name = "mode" value = "insert"/>
        <input type = "hidden" name = "ipin" value = "<%=request.getParameter("ipin")%>"/>
        <input type = "hidden" name = "group_seq" value = "4"/>
      <div class="j_write">
      <h4><span><img src="/images/join/j_write_title1.gif" alt="회원정보 입력"></span> <span class="s_text">[ <b>*</b> 표시된 항목은 필수사항입니다. ]</span></h4>
        <table class="join_style_1">
          <colgroup>
          <col width="21%">
          <col width="19%">
          <col width="13%">
          <col width="47%">
          </colgroup>
          <tbody>
            <tr>
              <th scope="row"><b>*</b> 아이디</th>
              <td colspan="3"><p>
                  <input type="text" id="member_id" name="member_id" class="input_j1 ws_1" placeholder="아이디를 입력해 주세요.">
                  <a href="javascript:;" onclick = "chk_member_id();"><img class="btn" src="/images/join/jw_btn1_off.gif" alt="아이디 중복확인"></a></p>
                <br>
                <div id = "check_member_id"></div>
                <p class="c1">※ 6~15자의 영문과 숫자만 사용 가능합니다. </p>
                <input type = "hidden" name = "member_id_chk" id = "member_id_chk" value = ""/></td>
            </tr>
            <tr>
              <th scope="row"><b>*</b> 비밀번호</th>
              <td colspan="3"><input type="password" id="member_pw" name="member_pw" class="input_j1 ws_2" placeholder="비밀번호를 입력해 주세요"></td>
            </tr>
            <tr>
              <th scope="row"><b>*</b> 비밀번호확인</th>
              <td colspan="3"><p>
                  <input type="password" id="member_pw_chk" name="member_pw_chk" class="input_j1 ws_2" placeholder="비밀번호를 다시한번 입력해 주세요">
                </p>
                <p class="c1">※ 6~15글자 이내, 영문 대/소문자,  숫자 및 특수문자 사용가능</p></td>
            </tr>
            <tr>
              <th scope="row" class="fv_1">업체명</th>
              <td colspan="3" class="fs_2"><input type="text" id="busi_nm" name="busi_nm" class="input_j1 ws_1"></td>
            </tr>
            <tr>
              <th scope="row" class="fv_1">사업자번호</th>
              <td colspan="3" class="fs_2"><input type="text" id="busi_no1" name="busi_no1" class="input_j1 ws_1" maxlength = "3">
                -
                <input type="text" id="busi_no2" name="busi_no2" class="input_j1 ws_1" maxlength = "2">
                -
                <input type="text" id="busi_no3" name="busi_no3" class="input_j1 ws_1" maxlength = "5"></td>
            </tr>
            <tr>
              <th scope="row" class="fv_1"><b>*</b> 업태</th>
              <td><input type="text" id="comptyp1" name="comptyp1" class="input_j1 ws_4"></td>
              <th scope="row" class="fv_1"><b>*</b> 종목</th>
              <td><input type="text" id="comptyp2" name="comptyp2" class="input_j1 ws_4"></td>
            </tr>
            <tr>
              <th scope="row" class="fv_1"><b>*</b> 대표자명</th>
              <td><input type="text" id="ceo_nm" name="ceo_nm" class="input_j1 ws_4" value = "<%=request.getParameter("member_nm")%>" readonly>
                <input type = "hidden" name = "member_nm" id = "member_nm" value = "<%=request.getParameter("member_nm")%>"></td>
              <!-- 
         <th scope="row" class="fv_1"><b>*</b> 대표자명</th>
        <td>
        	<input type = "text" name = "member_nm" id = "member_nm" value = "">
        </td>
        -->
              <th scope="row" class="fv_1"><b>*</b> 담당자명</th>
              <td><input type="text" id="staff_nm" name="staff_nm" class="input_j1 ws_4"></td>
            </tr>
            <tr>
              <th scope="row"><b>*</b> 전화번호</th>
              <td colspan="3"><select id="tel1" name="tel1" class="select_j1">
                  <option value = "02" selected>02</option>
                  <option value = "051">051</option>
                  <option value = "053">053</option>
                  <option value = "032">032</option>
                  <option value = "062">062</option>
                  <option value = "042">042</option>
                  <option value = "052">052</option>
                  <option value = "044">044</option>
                  <option value = "031">031</option>
                  <option value = "033">033</option>
                  <option value = "043">043</option>
                  <option value = "041">041</option>
                  <option value = "063">063</option>
                  <option value = "061">061</option>
                  <option value = "054">054</option>
                  <option value = "055">055</option>
                  <option value = "064">064</option>
                  <option value = "070">070</option>
                  <option value = "080">080</option>
                </select>
                -
                <input type="text" id="tel2" name="tel2" class="input_j1 ws_4" maxlength = "4">
                -
                <input type="text" id="tel3" name="tel3" class="input_j1 ws_4" maxlength = "4"></td>
            </tr>
            <tr>
              <th scope="row"><b>*</b> 담당자전화번호</th>
              <td colspan="3"><input type="text" id="staff_tel1" name="staff_tel1" class="input_j1 ws_3" maxlength = "3">
                -
                <input type="text" id="staff_tel2" name="staff_tel2" class="input_j1 ws_4" maxlength = "4">
                -
                <input type="text" id="staff_tel3" name="staff_tel3" class="input_j1 ws_4" maxlength = "4"></td>
            </tr>
            <tr>
              <th scope="row" class="fs_1">알림 설정</th>
              <td colspan="3"><label>
                  <input type="checkbox" id="sms_yn" name="sms_yn" value = "Y" class="check">
                  휴대폰 알림문자를 받겠습니다.</label></td>
            </tr>
            <tr>
              <th scope="row"><b>*</b> 이메일</th>
              <td colspan="3"><input type="text" id="email1" name="email1" class="input_j1 ws_5">
                @
                <input type="text" id="email2" name="email2" class="input_j1 ws_5" >
                <select id="" name="" class="select_j2" onchange = "inputEmail2(this.value);">
                  <option value="hanmail.net">hanmail.net</option>
                  <option value="naver.com" selected>naver.com</option>
                  <option value="daum.net">daum.net</option>
                  <option value="nate.com">nate.com</option>
                  <option value="gmail.com">gmail.com</option>
                  <option value="korea.com">korea.com</option>
                  <option value="dreamwiz.com">dreamwiz.com</option>
                  <option value="hotmail.com">hotmail.com</option>
                  <option value="yahoo.co.kr">yahoo.co.kr</option>
                  <option value="sportal.or.kr">sportal.or.kr</option>
                  <option value = "">직접입력</option>
                </select></td>
            </tr>
            <tr>
              <th scope="row" class="fs_1">광고성 메일 수신</th>
              <td colspan="3"><p>
                  <label>
                    <input type="checkbox" id="email_yn" name="email_yn" value = "Y" class="check">
                    수신함</label>
                  <label style="margin-left:15px;">
                    <input type="checkbox" id="email_yn" name="email_yn" value = "N" class="check">
                    수신안함</label>
                </p>
                <p class="c2"> ※ 주요 공지사항 및 알림 등은 설정에 관계 없이 발송되며,<br>
                  설정변경은 고객센터>회원정보 에서 변경 가능합니다. </p></td>
            </tr>
            <tr>
              <th scope="row"><b>*</b> 주소</th>
              <td colspan="3"><p>
                  <input type="text" id="zip_cd1" name="zip_cd1" class="input_j1 ws_4" readonly>
                  -
                  <input type="text" id="zip_cd2" name="zip_cd2" class="input_j1 ws_4" readonly >
                  <a href="javascript:open_zipcode();"> <img class="btn" src="/images/join/jw_btn2_off.gif" alt="주소검색"></a></p>
                <p style="margin-top:8px;">
                  <input type="text" id="addr1" name="addr1" class="input_j1 ws_6" readonly>
                </p></td>
            </tr>
            <tr>
              <th scope="row">상세주소</th>
              <td colspan="3"><input type="text" id="addr2" name="addr2" class="input_j1 ws_6"></td>
            </tr>
          </tbody>
        </table>
        </div>
        <div class="j_btn1">
          <input type = "image" src = "/images/join/jw_bottom_btn.gif">
        </div>
      </form>
    </div>
    <h4 id="tabNavTitle0101"><a href="javascript:;" onclick = "move_tab();">개인회원</a></h4>
  </div>
</div>
<form name = "move_form" id = "move_form" method = "post" action = "/join/join_3.do">
  <input type = "hidden" name = "member_nm" value = "<%=request.getParameter("member_nm")%>"/>
  <input type = "hidden" name = "ipin" value = "<%=request.getParameter("ipin")%>"/>
</form>
</body>
</html>
