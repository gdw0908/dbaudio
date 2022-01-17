<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE HTML>
<html lang="ko">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="Keywords" content="티켓모아, 상품권, 백화점 상품권, 롯데 백화점, 롯데 상품권, 갤러리아 백화점, 갤러리아 상품권, 신세계 백화점, 신세계 상품권" />
<title>회원정보 변경</title>
<link rel="stylesheet" href="/lib/css/sub_2.css" type="text/css">
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
function open_zipcode()
{
	var param = "";
	
	if(arguments[0]){
		param = "?fun="+arguments[0];
	}
	
	window.open("/addr/road.jsp"+param,"addr","width=570,height=420");
}

function setAddr(roadAddrPart1, addrDetail, zipNo, jibunAddr)
{
	var zip = zipNo.split("-");
	jQuery("#zip_cd").val(zip[0]);
	jQuery("#zip_cd1").val(zip[1]);
	jQuery("#addr1").val(jibunAddr);
	jQuery("#addr2").val(addrDetail);
	
	
}

function busiModfiyFormCheck()
{
	if(jQuery("#member_pw_df").val() == "")
	{
		alert("기존 비밀번호를 입력하세요.");
		jQuery("#member_pw_df").val("");
		jQuery("#member_pw_df").focus();
		return false;
		
	}
	else if(jQuery("#member_pw").val() != jQuery("#member_pw_check").val())
	{
		alert("새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.");
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
	else if(jQuery("#busi_no").val() == "" || isNaN(jQuery("#busi_no").val()))
	{
		alert("사업자번호를 입력하지 않았거나 숫자만 입력가능합니다.");
		jQuery("#busi_no").val("");
		jQuery("#busi_no").focus();
		return false;
		
	}
	else if(jQuery("#busi_no1").val() == "" || isNaN(jQuery("#busi_no1").val()))
	{
		alert("사업자번호를 입력하지 않았거나 숫자만 입력가능합니다.");
		jQuery("#busi_no1").val("");
		jQuery("#busi_no1").focus();
		return false;
		
	}
	else if(jQuery("#busi_no2").val() == "" || isNaN(jQuery("#busi_no2").val()))
	{
		alert("사업자번호를 입력하지 않았거나 숫자만 입력가능합니다.");
		jQuery("#busi_no2").val("");
		jQuery("#busi_no2").focus();
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
		alert("종목를 입력하세요.");
		jQuery("#comptyp2").val("");
		jQuery("#comptyp2").focus();
		return false;
		
	}
	else if(jQuery("#staff_nm").val() == "")
	{
		alert("담당자명를 입력하세요.");
		jQuery("#staff_nm").val("");
		jQuery("#staff_nm").focus();
		return false;
		
	}
	else if(jQuery("#tel").val() == "" || isNaN(jQuery("#tel").val()))
	{
		alert("전화번호를 입력하지 않았거나 숫자만 입력가능합니다.");
		jQuery("#tel").val("");
		jQuery("#tel").focus();
		return false;
		
	}
	else if(jQuery("#tel1").val() == "" || isNaN(jQuery("#tel1").val()))
	{
		alert("전화번호를 입력하지 않았거나 숫자만 입력가능합니다.");
		jQuery("#tel1").val("");
		jQuery("#tel1").focus();
		return false;
		
	}
	else if(jQuery("#tel2").val() == "" || isNaN(jQuery("#tel2").val()))
	{
		alert("전화번호를 입력하지 않았거나 숫자만 입력가능합니다.");
		jQuery("#tel2").val("");
		jQuery("#tel2").focus();
		return false;
		
	}
	else if(jQuery("#staff_tel").val() == "" || isNaN(jQuery("#staff_tel").val()))
	{
		alert("담당자 전화번호를 입력하지 않았거나 숫자만 입력가능합니다.");
		jQuery("#staff_tel").val("");
		jQuery("#staff_tel").focus();
		return false;
		
	}
	else if(jQuery("#staff_tel1").val() == "" || isNaN(jQuery("#staff_tel1").val()))
	{
		alert("담당자 전화번호를 입력하지 않았거나 숫자만 입력가능합니다.");
		jQuery("#staff_tel1").val("");
		jQuery("#staff_tel1").focus();
		return false;
		
	}
	else if(jQuery("#staff_tel2").val() == "" || isNaN(jQuery("#staff_tel2").val()))
	{
		alert("담당자 전화번호를 입력하지 않았거나 숫자만 입력가능합니다.");
		jQuery("#staff_tel2").val("");
		jQuery("#staff_tel2").focus();
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
		alert("이메일을 선택하거나 직접등록을 통하여 직접 입력을 해야합니다.");
		jQuery("#email1").val("");
		return false;
		
	}
	else if(jQuery("#zip_cd1").val() == "")
	{
		alert("주소를 입력해야 합니다 주소검색 버튼을 통하여 입력해주세요.");
		jQuery("#zip_cd").val("");
		jQuery("#zip_cd1").val("");
		jQuery("#addr1").val("");
		jQuery("#addr1").val("");
		return false;
		
	}
	else
	{
		if(confirm("수정 하시겠습니까?"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
</script>
</head>

<body>
<div class="wrap">
  <? include "../../include/header.html"; ?>
  <div id="container">
    <div class="c_wrap">
      <? include "../../include/left.html"; ?>
      <div id="sub">
        <div class="strapline">
          <h3><img src="/images/sub_2/h3_img_5_1.gif" alt="회원정보변경"></h3>
          <div class="state"> <span>홈</span> &gt; <span>고객센터</span> &gt; <span><strong>회원정보</strong></span> </div>
        </div>
        <div class="contents">

          <h4 class="hs_1">회원정보 <span>[ <b class="star">*</b>표시된 항목은 필수사항입니다. ]</span></h4>
		 <form name = "busiModifyForm" id = "busiModifyForm" method = "post" action = "${servletPath}" onsubmit = "return busiModfiyFormCheck();">
		 <input type = "hidden" name = "mode" value = "busiModify">
		 <input type = "hidden" name = "member_seq" value = "${userData.member_seq }">
          <div class="user_info_box">
            <table class="user_table_1">
            <colgroup>
            <col width="20%">
            <col width="">
            </colgroup>
            <tbody>

            <tr>
              <th scope="row"><b>*</b> 아이디</th>
              <td class="fs_style_1">${userData.member_id}</td>
            </tr>

            <tr>
              <th scope="row"><b>*</b> 기존 비밀번호</th>
              <td><input type="password" id="member_pw_df" name="member_pw_df" class="input_3 ws_1" placeholder="현재 비밀번호를 입력해 주세요"></td>
            </tr>

            <tr>
              <th scope="row">새 비밀번호</th>
              <td><input type="password" id="member_pw" name="member_pw" class="input_3 ws_1" placeholder="새 비밀번호를 입력해 주세요"> <span class="c1">※ 6~15글자 이내, 영문 대/소문자,  숫자 및 특수문자 사용가능</span></td>
            </tr>

            <tr>
              <th scope="row">새 비밀번호확인</th>
              <td><input type="password" id="member_pw_check" name="member_pw_check" class="input_3 ws_1" placeholder="새 비밀번호를 다시한번 입력해 주세요"></td>
            </tr>
            <tr>
              <th scope="row">업체명</th>
              <td class="fs_style_1"><input type="text" id="busi_nm" name="busi_nm" class="input_3 ws_2" style="width: 150px;" value = "${userData.busi_nm}"></td>
            </tr>
            <tr>
              <th scope="row">사업자번호</th>
              <td class="fs_style_1">
                <input type="text" id="busi_no" name="busi_no" class="input_3 ws_2" maxlength = "3" value = "${busi_no}"> 
                - <input type="text" id="busi_no1" name="busi_no1" class="input_3 ws_2" style="width: 100px;" maxlength = "2" value = "${busi_no1}"> - <input type="text" id="busi_no2" name="busi_no2" class="input_3 ws_2" maxlength = "5" value = "${busi_no2}">
              </td>
            </tr>
            <tr>
              <th scope="row">업태</th>
              <td class="fs_style_1"><input type="text" id="comptyp1" name="comptyp1" class="input_3 ws_2" style="width: 150px;" value = "${userData.comptyp1}"></td>
            </tr>
            <tr>
              <th scope="row">종목</th>
              <td class="fs_style_1"><input type="text" id="comptyp2" name="comptyp2" class="input_3 ws_2" style="width: 150px;" value = "${userData.comptyp2}"></td>
            </tr>
            <tr>
              <th scope="row">대표자명 (실명)</th>
              <td class="fs_style_1">${userData.ceo_nm}</td>
            </tr>
            <tr>
              <th scope="row">담당자명</th>
              <td class="fs_style_1"><input type="text" id="staff_nm" name="staff_nm" class="input_3 ws_2" style="width: 150px;" value = "${userData.staff_nm}"></td>
            </tr>
            <tr>
              <th scope="row">전화번호</th>
              <td>
               <input type="text" id="tel" name="tel" class="input_3 ws_2" value = "${tel}" maxlength = "3">
                - <input type="text" id="tel1" name="tel1" class="input_3 ws_2" value = "${tel1}" maxlength = "4"> - <input type="text" id="tel2" name="tel2" class="input_3 ws_2" value = "${tel2}" maxlength = "4">
              </td>
            </tr>

            <tr>
              <th scope="row"><b>*</b>담당자전화번호</th>
              <td>
			  <input type="text" id="staff_tel" name="staff_tel" class="input_3 ws_2" value = "${staff_tel}" maxlength = "3">
                - <input type="text" id="staff_tel1" name="staff_tel1" class="input_3 ws_2" value = "${staff_tel1}" maxlength = "4"> - <input type="text" id="staff_tel2" name="staff_tel2" class="input_3 ws_2" value = "${staff_tel2}" maxlength = "4">
			  </td>
            </tr>

            <tr>
              <th scope="row" class="fs_1">알림 설정</th>
              <td><label><input type="checkbox" id="sms_yn" name="sms_yn" class="check" value = "Y" <c:if test = "${userData.sms_yn eq 'Y'}">checked</c:if>> 휴대폰 알림문자를 받겠습니다.</label></td>
            </tr>

            <tr>
              <th scope="row"><b>*</b> 이메일</th>
              <td>
                 <input type="text" id="email1" name="email1" class="input_3" value = "${email }"> @ <input type="text" id="email2" name="email2" class="input_3" value = "${email1 }" readonly>
                <select id="choice_email" name="choice_email" class="select_1" onchange = "inputEmail2(this.value);">
		          <option value="hanmail.net" <c:if test = "${email1 eq 'hanmail.net' }">selected</c:if>>hanmail.net</option>
		          <option value="naver.com" <c:if test = "${email1 eq 'naver.com'  }">selected</c:if>>naver.com</option>
		          <option value="daum.net" <c:if test = "${email1 eq 'daum.net'  }">selected</c:if>>daum.net</option>
		          <option value="nate.com" <c:if test = "${email1 eq 'nate.com'  }">selected</c:if>>nate.com</option>
		          <option value="gmail.com" <c:if test = "${email1 eq 'gmail.com'  }">selected</c:if>>gmail.com</option>
		          <option value="korea.com" <c:if test = "${email1 eq 'korea.com'  }">selected</c:if>>korea.com</option>
		          <option value="dreamwiz.com" <c:if test = "${email1 eq 'dreamwiz.com'  }">selected</c:if>>dreamwiz.com</option>
		          <option value="hotmail.com" <c:if test = "${email1 eq 'hotmail.com'  }">selected</c:if>>hotmail.com</option>
		          <option value="yahoo.co.kr" <c:if test = "${email1 eq 'yahoo.co.kr'  }">selected</c:if>>yahoo.co.kr</option>
		          <option value="sportal.or.kr" <c:if test = "${email1 eq 'sportal.or.kr'  }">selected</c:if>>sportal.or.kr</option>
		          <option value = "" >직접입력</option>
		        </select>
              </td>
            </tr>

            <tr>
              <th scope="row" class="fs_1">광고성 메일 수신</th>
              <td><label><input type="checkbox" id="email_yn" name="email_yn" class="check" value = "Y" <c:if test = "${userData.email_yn eq 'Y'}">checked</c:if>> 수신함</label> <label style="margin-left:15px;"><input type="checkbox" id="email_yn" name="email_yn" class="check" value = "N" <c:if test = "${userData.email_yn eq 'N'}">checked</c:if>> 수신안함</label> <span class="c2">※ 주요 공지사항 및 알림 등은 설정에 관계 없이 발송됩니다.</span></td>
            </tr>

            <tr>
              <th scope="row"><b>*</b> 주소</th>
              <td class="adress">
                <p><input type="text" id="zip_cd1" name="zip_cd1" class="input_3 ws_2" value = "${zip_code1}" readonly> - <input type="text" id="zip_cd2" name="zip_cd2" class="input_3 ws_2" value = "${zip_code2}" readonly>
                 <a href="javascript:open_zipcode();"><img src="/images/sub_2/address_btn1.gif" alt="주소검색"></a> </p>
                <p><input type="text" id="addr1" name="addr1" class="input_3 ws_3" readonly value = "${userData.addr1}"></p>
                <p class="last"><input type="text" id="addr2" name="addr2" class="input_3 ws_3" readonly value = "${userData.addr2}"></p>
              </td>
            </tr>

            </tbody>
            </table>
          </div>

		  <div class="btn_bottom_1">
		  <input type = "image" src = "/images/sub_2/btn_mod.gif">
		  </div>
		</form>
        </div>
      </div>
    </div>
  </div>
  <? include "../../include/footer.html"; ?>
</div>
</body>
</html>
