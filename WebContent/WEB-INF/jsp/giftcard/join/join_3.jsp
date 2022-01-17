<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<meta http-equiv="X-UA-Compatible" content="ie=edge" />
<meta name="viewport"
   content="width=device-width, initial-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta
   content="minimum-scale=1.0, width=device-width, maximum-scale=1, user-scalable=yes"
   name="viewport" />
<meta name="author" content="31system" />
<meta name="description" content="안녕하세요  페어링 오디오 입니다." />
<meta name="Keywords"
   content="페어링 오디오, 음향기기, 중고음향기기, 중고악기, 중고 쇼핑몰, 중고 악기 쇼핑몰, 중고 음향기기 쇼핑몰" />
<title>회원정보입력</title>

<link rel="stylesheet"
   href="//cdn.jsdelivr.net/npm/xeicon@2.3.3/xeicon.min.css">
<link rel="stylesheet" href="/lib/css/common.css" type="text/css">
<link rel="stylesheet" href="/lib/css/article.css" type="text/css">
<link rel="stylesheet" href="/lib/css/join.css" type="text/css">

<script type="text/javascript" src="/lib/js/common_sc.js"></script>
<script type="text/javascript" src="/lib/js/mc.js"></script>
<script type="text/javascript" src="/lib/js/common.js"></script>
<script type="text/javascript">
   function inputEmail2(value) {
      if (value == "") {
         jQuery("#email2").attr("readonly", false);
         jQuery("#email2").val("");
         jQuery("#email2").focus();
      } else {
         jQuery("#email2").attr("readonly", true);
         jQuery("#email2").val(value);
      }
   }

   function CheckPassword(uid, upw) {
      if (upw == "") {
         return false;
      }

      if (!/^[a-zA-Z0-9]{6,15}$/.test(upw)) {
         return false;
      }

      var chk_num = upw.search(/[0-9]/g);
      var chk_eng = upw.search(/[a-z]/ig);

      if (chk_num < 0 || chk_eng < 0) {
         return false;
      }

      if (/(\w)\1\1\1/.test(upw)) {
         return false;
      }

      if (upw.search(uid) > -1) {
         return false;
      }

      return true;
   }

   function join_form_chk() {
      if (jQuery("#member_id").val() == ""
            || jQuery("#member_id").val().length < 6) {
         alert("아이디를 입력하지 않았거나 6자리 이상 등록해야합니다.");
         jQuery("#member_id").val("");
         jQuery("#member_id").focus();
         return false;
      } else if (jQuery("#member_id_chk").val() == "") {
         alert("아이디 중복확인을 진행해주세요.");
         return false;
      } else if (!CheckPassword(jQuery("#member_id").val(), jQuery(
            "#member_pw").val())) {
         alert("비밀번호는 아이디와 같을 수 없으며\n6~15글자 이내, 영문 대/소문자, 숫자를 조합해야합니다.");
         jQuery("#member_pw").val("");
         jQuery("#member_pw").focus();
         return false;
      }

      else if (jQuery("#member_pw_chk").val() == "") {
         alert("비밀번호 확인을 입력하세요.");
         jQuery("#member_pw_chk").val("");
         jQuery("#member_pw_chk").focus();
         return false;
      } else if (jQuery("#member_nm").val() == "") {
         alert("이름(실명) 을 입력하세요.");
         jQuery("#member_nm").val("");
         jQuery("#member_nm").focus();
         return false;
      }

      else if (jQuery("#member_pw_chk").val() != jQuery("#member_pw").val()) {
         alert("비밀번호가 일치 하지 않습니다. 확인해주세요.");
         jQuery("#member_pw").val("");
         jQuery("#member_pw_chk").val("");
         jQuery("#member_pw").focus();
         return false;
      }
      /*
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
       */
      else if (jQuery("#cell1").val() == "" || isNaN(jQuery("#cell1").val())) {
         alert("휴대폰번호를 입력하지 않았거나 숫자만 입력가능합니다.");
         jQuery("#cell1").val("");
         jQuery("#cell1").focus();
         return false;
      } else if (jQuery("#cell2").val() == ""
            || isNaN(jQuery("#cell2").val())) {
         alert("휴대폰번호를 입력하지 않았거나 숫자만 입력가능합니다.");
         jQuery("#cell2").val("");
         jQuery("#cell2").focus();
         return false;
      } else if (jQuery("#cell3").val() == ""
            || isNaN(jQuery("#cell3").val())) {
         alert("휴대폰번호를 입력하지 않았거나 숫자만 입력가능합니다.");
         jQuery("#cell3").val("");
         jQuery("#cell3").focus();
         return false;
      } else if (jQuery("#check_member_cell").html() == ""
    		|| jQuery("#check_member_cell").html() == "이미 가입 되어있는 번호입니다.") { //중복체크 추가
          alert("휴대폰번호 중복확인을 진행해주세요.");
          return false;
      } else if (jQuery("#email1").val() == "") {
         alert("이메일을 입력하세요.");
         jQuery("#email1").val("");
         jQuery("#email1").focus();
         return false;
      } else if (jQuery("#email2").val() == "") {
         alert("이메일을 선택하거나 직접입력을 선택하여 직접 입력하세요.");
         jQuery("#email2").val("");
         jQuery("#email2").focus();
         return false;
      } else if (!chk_email(jQuery("#email2").val())) {
         alert("입력하신 이메일 형식이 올바르지 않습니다. 다시 입력하세요.");
         jQuery("#email2").val("");
         jQuery("#email2").focus();
         return false;
      } else if (jQuery("#check_member_email").html() == ""
    		|| jQuery("#check_member_email").html() == "이미 가입 되어있는 이메일입니다.") { //중복체크 추가
          alert("이메일 중복확인을 진행해주세요.");
          return false;
      }

      else if (jQuery("#zip_cd1").val() == "" || jQuery("#addr1").val() == "") {
         alert("주소검색 버튼을 이용하여 주소를 등록해야합니다.");
         return false;
      } else if (jQuery("#addr2").val() == "") {
         alert("상세주소를 입력하세요.");
         jQuery("#addr2").val("");

         jQuery("#addr2").focus();
         return false;
      }

      return true;
   }

   function chk_email(value) {
      var pattern = new RegExp(/^([\w-]+\.)+([a-zA-Z]{2,3}$|[\.]?[a-zA-Z]?$)/);

      return pattern.test(value);
   }

   function chk_member_id() {
      if (jQuery("#member_id").val() == "") {
         alert("아이디를 입력하세요.");
         jQuery("#member_id").val("");
         jQuery("#member_id").focus();
         return;
      } else {
         getJSON("/json/list/member.getMemberIdCheck.do", {
            "member_id" : jQuery("#member_id").val()
         }, function(data) {
            $("body").data("chk_member_id", data);
            var chk_member_id = $("body").data("chk_member_id");

            $.each(chk_member_id, function() {
               var data = this["member_id"];
            });

            if (data == "" || data == null) {
               jQuery("#check_member_id").html("사용 가능한 아이디 입니다.");
               jQuery("#member_id_chk").val("Y");
               return;
            } else {
               jQuery("#check_member_id").html("이미 가입 되어있는 아이디 입니다.");
               jQuery("#member_id").val("");
               jQuery("#member_id").focus();
               return;
            }

         });
      }

   }

    /* 휴대폰 번호 중복체크 추가 */
	function chk_member_cellChk() {
      if (jQuery("#cell1").val() == "") {
         alert("휴대폰번호를 입력하세요.");
         jQuery("#cell1").val("");
         jQuery("#cell1").focus();
         return;
      } else if(jQuery("#cell2").val() == ""){
         alert("휴대폰번호를 입력하세요.");
         jQuery("#cell2").val("");
         jQuery("#cell2").focus();
         return;         
      } else if(jQuery("#cell3").val() == ""){
         alert("휴대폰번호를 입력하세요.");
         jQuery("#cell3").val("");
         jQuery("#cell3").focus();
         return;         
      } else {
         getJSON("/json/list/member.getMemberCellCheck.do", {
            "cell1" : jQuery("#cell1").val(),
            "cell2" : jQuery("#cell2").val(),
            "cell3" : jQuery("#cell3").val()
         }, function(data) {
            $("body").data("chk_member_cell", data);
            var chk_member_cell = $("body").data("chk_member_cell");

            $.each(chk_member_cell, function() {
               var data = this["cell1"] + this["cell2"] + this["cell3"] ;
            });
            
            if (data.length <= 0) {
               jQuery("#check_member_cell").css('color', 'blue').html("사용 가능한 번호입니다.");
               return; 
            } else {
               jQuery("#check_member_cell").css('color', 'red').html("이미 가입 되어있는 번호입니다.");
               jQuery("#cell2").val("");
               jQuery("#cell3").val("");
               return;
            }
         });
      }
   }
    
    
	/* 이메일 중복체크 추가 */
	function chk_member_emailChk() {
      if (jQuery("#email1").val() == "") {
         alert("이메일을 입력하세요.");
         jQuery("#email1").val("");
         jQuery("#email1").focus();
         return;
      } else {
    	  
    	  getJSON("/json/list/member.getMemberEmailCheck.do", {
          		"email1" : jQuery("#email1").val(),
           		"email2" : jQuery("#email2").val()
           		
          }, function(data) {
        	$.each(chk_member_emailChk, function() {
                var data = this["email1"] + this["email2"];
            });
            
            console.log("########");
            console.log("data >" + data);
            console.log("data.length >" + data.length);
            
            if (data.length <= 0) {
               jQuery("#check_member_email").css("color", "blue").html("사용 가능한 이메일입니다.");
               return; 
            } else {
               jQuery("#check_member_email").css("color", "red").html("이미 가입 되어있는 이메일입니다.");
               jQuery("#email1").val("");
               return;
            }
         });
      }
   }

   
   function open_zipcode() {
      var param = "";

      if (arguments[0]) {
         param = "?fun=" + arguments[0];
      }

      window.open("/addr/road.jsp" + param, "addr", "width=570,height=420");

      com_juso = true;
   }

   function setAddr(roadAddrPart1, addrDetail, zipNo, jibunAddr) {
      var zip = zipNo.split("-");
      jQuery("#zip_cd1").val(zip[0]);
      jQuery("#zip_cd2").val(zip[1]);
      jQuery("#addr1").val(roadAddrPart1);
      jQuery("#addr2").val(addrDetail);
   }

   function move_tab() {
      jQuery("#move_form").submit();
   }
</script>
</head>

<body>
   <div class="title_rocation">
      <div class="tr_wrap">
         <h3 style="text-align: center;">회원가입</h3>
         <ul class="join_process">
            <li class="active"><span class="icon"><i
                  class="xi-check"></i></span>약관동의</li>
            <li class="active"><span class="icon"><i>2</i></span>회원정보입력</li>
            <li><span class="icon"><i>3</i></span>가입완료</li>
         </ul>
      </div>
   </div>
   <div class="j_wrap" style="max-width: 720px">
      <div id="tabNav_j1" class="join_tab" style="max-width: 720px">
         <!--     <h4 id="tabNavTitle0101" class="on"><a href="#" >사업자회원</a></h4> -->
         <div id="tabNav0101" style="display: block; padding: 30px 0 0 0;">
            <form method="post" name="join_form" id="join_form"
               action="/giftcard/join/join_4.do" onsubmit="return join_form_chk();">
               <input type="hidden" name="mode" value="insert" /> <input
                  type="hidden" name="ipin"
                  value="<%=request.getParameter("ipin")%>" /> <input type="hidden"
                  name="group_seq" value="2" /> <input type="hidden"
                  name="member_type" value="1" />
               <div class="j_write">
                  <h4>회원정보 입력</h4>
                  <p class="required">
                     <span>*</span>필수입력사항
                  </p>
                  <table class="join_style_1 join_3_table">
                     <tbody>
                        <tr>
                           <th scope="row"><b>*</b> 아이디</th>
                           <td>
                              <p class="id_wrap">
                                 <input type="text" id="member_id" name="member_id"
                                    class="input_j1 ws_1" placeholder="아이디를 입력해 주세요."> <a
                                    href="javascript:;" onclick="chk_member_id();"
                                    class="overlap_btn" style="color: #fff;">중복확인</a>
                              </p>
                              <div id="check_member_id"></div>
                              <p class="c1">※ 6~15자의 영문과 숫자만 사용 가능합니다.</p> <input
                              type="hidden" name="member_id_chk" id="member_id_chk" value="" />
                           </td>
                        </tr>
                        <tr>
                           <th scope="row"><b>*</b> 비밀번호</th>
                           <td><input type="password" id="member_pw" name="member_pw"
                              class="input_j1 ws_2" placeholder="비밀번호를 입력해 주세요"></td>
                        </tr>
                        <tr>
                           <th scope="row"><b>*</b> 비밀번호확인</th>
                           <td><p>
                                 <input type="password" id="member_pw_chk"
                                    name="member_pw_chk" class="input_j1 ws_2"
                                    placeholder="비밀번호를 다시한번 입력해 주세요">
                              </p>
                              <p class="c1">※ 6~15글자 이내, 영문 대/소문자, 숫자 및 특수문자 사용가능</p></td>
                        </tr>
                        <tr>
                           <th scope="row" class="fv_1">이름 (실명)</th>
                           <td class="fs_2"><input type="text" name="member_nm"
                              id="member_nm" class="input_j1" placeholder="이름을 입력해 주세요"></td>
                        </tr>
                        <!-- 
     <tr>
      <th scope="row" class="fv_1"><b>*</b>이름 (실명)</th>
      <td class="fs_2"><input type = "text" name = "member_nm" id = "member_nm" value = ""></td>
    </tr>
    -->
                        <tr>
                           <th scope="row">전화번호</th>
                           <td><select id="tel1" name="tel1" class="select_j1">
                                 <option value="02" selected>02</option>
                                 <option value="051">051</option>
                                 <option value="053">053</option>
                                 <option value="032">032</option>
                                 <option value="062">062</option>
                                 <option value="042">042</option>
                                 <option value="052">052</option>
                                 <option value="044">044</option>
                                 <option value="031">031</option>
                                 <option value="033">033</option>
                                 <option value="043">043</option>
                                 <option value="041">041</option>
                                 <option value="063">063</option>
                                 <option value="061">061</option>
                                 <option value="054">054</option>
                                 <option value="055">055</option>
                                 <option value="064">064</option>
                                 <option value="070">070</option>
                                 <option value="080">080</option>
	                           </select> - 
	                           <input type="text" id="tel2" name="tel2" class="input_j1 ws_4" maxlength="4"> - 
	                           <input type="text" id="tel3" name="tel3" class="input_j1 ws_4" maxlength="4">
                           </td>
                        </tr>
                        <tr>
                           <th scope="row"><b>*</b> 휴대폰번호</th>
                           <td>
                           		<p>
                           			<select type="text" id="cell1" name="cell1" class="select_j1">
		                                 <option value="010">010</option>
		                                 <option value="011">011</option>
                              		</select> - 
	                                <input type="text" id="cell2" name="cell2" class="input_j1 ws_4" maxlength="4"> - 
	                                <input type="text" id="cell3" name="cell3" class="input_j1 ws_4" maxlength="4">
                              		<a href="javascript:;" onclick="chk_member_cellChk();" class="overlap_btn" style="color: #fff;">휴대폰 중복확인</a>
                              	</p>
                              <div id="check_member_cell"></div>
                           </td>
                        </tr>
                        <tr>
                           <th scope="row" class="fs_1">알림 설정</th>
                           <td><label> <input type="checkbox" id="sms_yn"
                                 name="sms_yn" class="check" value="Y"> 휴대폰 알림문자를 받겠습니다.
                           </label></td>
                        </tr>
                        <tr>
                           <th scope="row"><b>*</b> 이메일</th>
                           <td>
	                           <p>
	                           	<input type="text" id="email1" name="email1"
	                              class="input_j1 ws_5"> @ <input type="text"
	                              id="email2" name="email2" class="input_j1 ws_5"> 
	                              <select id="" name="" class="select_j2" onchange="inputEmail2(this.value);">
	                                 <option value="hanmail.net">hanmail.net</option>
	                                 <option value="naver.com">naver.com</option>
	                                 <option value="daum.net">daum.net</option>
	                                 <option value="nate.com">nate.com</option>
	                                 <option value="gmail.com">gmail.com</option>
	                                 <option value="korea.com">korea.com</option>
	                                 <option value="dreamwiz.com">dreamwiz.com</option>
	                                 <option value="hotmail.com">hotmail.com</option>
	                                 <option value="yahoo.co.kr">yahoo.co.kr</option>
	                                 <option value="sportal.or.kr">sportal.or.kr</option>
	                                 <option value="" selected>직접입력</option>
	                              </select>
	                              <a href="javascript:;" onclick="chk_member_emailChk();" class="overlap_btn" style="color: #fff;">이메일 중복확인</a>
	                            </p>
                              <div id="check_member_email"></div>
                            </td>
                        </tr>
                        <tr>
                           <th scope="row" class="fs_1">광고성 메일 수신</th>
                           <td>
                              <p>
                                 <label> 
                                    <input type="radio" id="email_yn" name="email_yn" class="check" value="Y"> 수신함
                                 </label> 
                                 <label style="margin-left: 15px;"> 
                                    <input type="radio" id="email_yn" name="email_yn" class="check" value="N"> 수신안함
                                 </label>
                              </p>
                              <p class="c2">
						                                 ※ 주요 공지사항 및 알림 등은 설정에 관계 없이 발송되며,<br> 설정변경은 고객센터>회원정보 에서 변경 가능합니다.
                              </p>
                           </td>
                        </tr>
                        <tr>
                           <th scope="row"><b>*</b> 주소</th>
                           <td>
                              <p class="flex">
                                 <input type="text" id="zip_cd1" name="zip_cd1"
                                    class="input_j1 ws_4" readonly> - <input type="text"
                                    id="zip_cd2" name="zip_cd2" class="input_j1 ws_4" readonly>
                                 <a href="javascript:open_zipcode();" class="address_btn"
                                    style="padding: 10px;">주소검색</a>
                              </p>
                              <p style="margin-top: 8px;">
                                 <input type="text" id="addr1" name="addr1"
                                    class="input_j1 ws_6" readonly>
                              </p>
                           </td>
                        </tr>
                        <tr>
                           <th scope="row">상세주소</th>
                           <td><input type="text" id="addr2" name="addr2"
                              class="input_j1 ws_6"></td>
                        </tr>
                     </tbody>
                  </table>
               </div>
               <div class="j_btn1">
                  <input type="submit" value="가입하기" class="next_btn">
               </div>
            </form>
         </div>
         <!--     <h4 id="tabNavTitle0102"><a href="javascript:;" onclick = "move_tab();">사업자회원</a></h4> -->
      </div>
   </div>
   <form name="move_form" id="move_form" method="post"
      action="/join/join_3_1.do">
      <input type="hidden" name="member_nm"
         value="<%=request.getParameter("member_nm")%>" /> <input
         type="hidden" name="ipin" value="<%=request.getParameter("ipin")%>" />
   </form>
</body>
</html>