<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page language="java" import="Kisinfo.Check.IPINClient" %>
<%@ page import="com.mc.common.util.*" %>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<meta http-equiv="X-UA-Compatible" content="ie=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta content="minimum-scale=1.0, width=device-width, maximum-scale=1, user-scalable=yes" name="viewport" />
<meta name="author" content="31system" />
<meta name="description" content="안녕하세요  페어링 오디오 입니다." />
<meta name="Keywords" content="페어링 오디오, 상품권, 백화점 상품권, 롯데 백화점, 롯데 상품권, 갤러리아 백화점, 갤러리아 상품권, 신세계 백화점, 신세계 상품권" />
<title>비밀번호찾기</title>

<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/xeicon@2.3.3/xeicon.min.css">
<link rel="stylesheet" href="/lib/css/join.css" type="text/css">
<script type="text/javascript" src="/lib/js/common.js"></script>
<script type = "text/javascript">

function search_pw_form_fnc() {
	
	if(document.getElementById("member_pw").value == "") {
		alert("새 비밀번호를 입력하세요.");
		document.getElementById("member_pw").focus();
		document.getElementById("member_pw").value = "";
		return false;
		
	} else if(document.getElementById("member_pw_chk").value == "") {
		alert("새 비밀번호 확인을 입력하세요");
		document.getElementById("member_pw_chk").focus();
		document.getElementById("member_pw_chk").value = "";
		return false;
		
	} else if(jQuery("#member_pw").val() != jQuery("#member_pw_chk").val()) {
		alert("새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.");
		jQuery("#member_pw").val("");
		jQuery("#member_pw_chk").val("");
		jQuery("#member_pw").focus();
		return false;
	} 
}

</script>
</head>

<body>
<div class="j_wrap">
   <h3 class="tit">비밀번호 재설정</h3>
	  <div id="tabNav_j1" class="join_tab pw_search_tab">
	    <div id="tab-1" id="tabNav0101" class="tab-content current">
	      <div class="authentication auth_2">
	        <div class="ipin ipin_3">
	          <form name = "search_pw_form" id = "search_pw_form" method = "post" 
                 action = "/giftcard/join/pw_search_2.do" onsubmit = "return search_pw_form_fnc();">
                <input type = "hidden" name = "mode" id = "mode" value = "pw_update"/>
                <input type = "hidden" name = "member_seq" id = "member_seq" value = "${member_seq}"/>
	            <dl class="type_2 type_2_2">
	              <dt>비밀번호 재설정</dt>
	              <dd><span class="standard">새 비밀번호</span><span>
	                <input type="password" id="member_pw" name="member_pw" class="input_1 ws_2" placeholder="새 비밀번호를 입력해 주세요">
	                <span class="c1">※ 6~15글자 이내, 영문 대/소문자, 숫자 및 특수문자 사용가능</span>
	                </span></dd>
	              <dd><span class="standard">새 비밀번호 확인</span><span>
	                <input type="password" id="member_pw_chk" name="member_pw_chk" class="input_1 ws_2" placeholder="새 비밀번호를 다시한번 입력해 주세요">
	                </span></dd>
	              <dd class="btn">
	              	<input type = "submit" value="확인" class="search_btn">
	              </dd>
	            </dl>
	          </form>
	        </div>
	      </div>
	      <div class="b_gui_2">
	        <p><strong>위의 방법으로도 비밀번호를 찾지 못했다면, 페어링 오디오 쇼핑몰 고객센터로 문의주십시오.</strong><br>
	          			고객센터 전화문의 : 1566-6444 (상담가능시간: 평일 오전 9시~오후 6시)</p>
	      </div>
	    </div>
        <div class="pw_s_btn">
          <input type = "submit" value="이메일로 비밀번호 발급받기">
        </div>
        <div class="b_gui_2">
          <p><strong>위의 방법으로도 찾지 못했다면, 페어링 오디오쇼핑몰 고객센터로 문의주십시오</strong><br>
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
</script>

</body>
</html>
