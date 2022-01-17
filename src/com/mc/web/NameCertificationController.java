package com.mc.web;


import gov.mogaha.gpin.sp.proxy.GPinProxy;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mc.web.Globals;
import com.mc.common.util.Req;
import com.mc.common.util.StringUtil;
import com.mc.common.util.Util;

@Controller
@RequestMapping("/NameCertification.do")
public class NameCertificationController {
	
	
	@Resource(name = "globals")
	private Globals globals;
	
	@Value("#{config['nice.siteId']}")
	private String NICE_SITE_ID;
	
	@Value("#{config['nice.sitePassword']}")
	private String NICE_SITE_PASSWORD;
	
	@Value("#{config['home.url.ssl']}")
	private String HOME_URL_SSL;
	
	@Value("#{config['home.url']}")
	private String HOME_URL;
	
	@Value("#{config['develope']}")
	private String DEVELOPE;

	@RequestMapping(params="mode=phone")
	public String phone(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{

		MCMap phone_cert_data = new MCMap();
		phone_cert_data.put("returnUrl", Req.getValue(request, "returnUrl", ""));
		phone_cert_data.put("returnSiteId", Req.getValue(request, "return_site_id", ""));
		phone_cert_data.put("menu_id", Req.getValue(request, "menu_id", ""));
		phone_cert_data.put("mode", Req.getValue(request, "returnMode", ""));
		phone_cert_data.put("member_id", Req.getValue(request, "member_id", ""));
		phone_cert_data.put("member_nm", Req.getValue(request, "member_nm", ""));
		session.setAttribute("phone_cert_data", phone_cert_data);
		
		NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();
	    
		String sSiteCode = NICE_SITE_ID;				   // NICE로부터 부여받은 사이트 코드
	    String sSitePassword = NICE_SITE_PASSWORD;			 // NICE로부터 부여받은 사이트 패스워드
	    String sRequestNumber = "REQ0000000001";        	// 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로 
	                                                    	// 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
	    sRequestNumber = niceCheck.getRequestNO(sSiteCode);
	  	session.setAttribute("REQ_SEQ" , sRequestNumber);	// 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.
	  	
	   	String sAuthType = "";      	// 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서
	   	String popgubun 	= "N";		//Y : 취소버튼 있음 / N : 취소버튼 없음
		String customize 	= "";			//없으면 기본 웹페이지 / Mobile : 모바일페이지
			
	    // CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
	    String sReturnUrl = HOME_URL_SSL+"/NameCertification.do?mode=phoneSuccess";      // 성공시 이동될 URL
	    String sErrorUrl = HOME_URL_SSL+"/NameCertification.do?mode=phoneFail";          // 실패시 이동될 URL

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
	    if( iReturn == 0 )
	    {
	        sEncData = niceCheck.getCipherData();
	    }
	    else if( iReturn == -1)
	    {
	        sMessage = "암호화 시스템 에러입니다.";
	    }    
	    else if( iReturn == -2)
	    {
	        sMessage = "암호화 처리오류입니다.";
	    }    
	    else if( iReturn == -3)
	    {
	        sMessage = "암호화 데이터 오류입니다.";
	    }    
	    else if( iReturn == -9)
	    {
	        sMessage = "입력 데이터 오류입니다.";
	    }    
	    else
	    {
	        sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
	    }
		
		request.setAttribute("json", "{\"sEncData\" : \"" + sEncData + "\", \"sMessage\" : \"" + sMessage + "\"}");
		return "json";
	}
	
	@RequestMapping(params="mode=phoneSuccess")
	public String phoneSuccess(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		/*실명인증 로직*/
		MCMap phone_cert_data = (MCMap) session.getAttribute("phone_cert_data");
		String returnUrl = String.valueOf(phone_cert_data.get("returnUrl"));
		String returnSiteId = String.valueOf(phone_cert_data.get("returnSiteId"));
		String menu_id = String.valueOf(phone_cert_data.get("menu_id"));
		String mode = String.valueOf(phone_cert_data.get("mode"));
		String member_id = String.valueOf(phone_cert_data.get("member_id"));	//비밀번호찾기
		String member_nm = String.valueOf(phone_cert_data.get("member_nm"));	//비밀번호찾기
	    session.removeAttribute("phone_cert_data");
		
		NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();

	    String sEncodeData = StringUtil.requestReplace(request.getParameter("EncodeData"), "encodeData");
	    
	    String sSiteCode = NICE_SITE_ID;				   // NICE로부터 부여받은 사이트 코드
	    String sSitePassword = NICE_SITE_PASSWORD;			 // NICE로부터 부여받은 사이트 패스워드

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
	    String sMobileNo = "";       // 휴대폰번호
	    String sMessage = "실명인증에 성공하였습니다.";
	    String sPlainData = "";
	    

	    
	    int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);

	    if( iReturn == 0 )
	    {
	        sPlainData = niceCheck.getPlainData();
	        sCipherTime = niceCheck.getCipherDateTime();	//복호화한 시간
	        // 데이타를 추출합니다.
	        java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);
	        
	        sRequestNumber  = (String)mapresult.get("REQ_SEQ");			//CP 요청번호
	        sResponseNumber = (String)mapresult.get("RES_SEQ");			//처리결과 고유번호 (NICE에서 부여)
	        sAuthType 			= (String)mapresult.get("AUTH_TYPE");	//인증수단 - M : 휴대폰 / C : 신용카드 / X : 공인인증서
	        sName 					= (String)mapresult.get("NAME");	//성명
	        sBirthDate 			= (String)mapresult.get("BIRTHDATE");	//생년월일 - YYYYMMDD
	        sGender 				= (String)mapresult.get("GENDER");	//성별 코드 - 0:여성 / 1:남성
	        sNationalInfo  	= (String)mapresult.get("NATIONALINFO");	//국적 정보 - 0:내국인 / 1:외국인
	        sDupInfo 				= (String)mapresult.get("DI");		//개인 회원의 중복가입여부 확인을 위해 사용되는 값(크기: 64byte)
	        sConnInfo 			= (String)mapresult.get("CI");			//주민등록번호와 1:1로 매칭되는 고유키(크기: 88byte)
	        sMobileNo	=	(String)mapresult.get("MOBILE_NO");			// 휴대폰번호(안됨)
	        // 휴대폰 번호 : MOBILE_NO => (String)mapresult.get("MOBILE_NO");
	        // 이통사 정보 : MOBILE_CO => (String)mapresult.get("MOBILE_CO");
			// checkplus_success 페이지에서 결과값 null 일 경우, 관련 문의는 관리담당자에게 하시기 바랍니다.        
	        String session_sRequestNumber = (String)session.getAttribute("REQ_SEQ");
		    
	        if(!sRequestNumber.equals(session_sRequestNumber))
	        {
	            sMessage = "세션값이 다릅니다. 올바른 경로로 접근하시기 바랍니다.";
	            request.setAttribute("redirect", "close");
	            sResponseNumber = "";
	            sAuthType = "";
	        }else{
	        	/*MCMap real_name = new MCMap();
	        	real_name.put("ipin", StringUtil.isNullDef(sDupInfo, "non_member"));
				real_name.put("ci", sConnInfo);
				real_name.put("virtualNo", "");
				real_name.put("name", sName);
				real_name.put("birth", sBirthDate);
				real_name.put("phone", sMobileNo);
				real_name.put("sex", sGender);
				
				session.setAttribute("real_name", real_name);*/
	        	MCMap  member = new MCMap();
	        	member.put("member_id", "non_member");
	        	member.put("member_nm", sName);
	        	member.put("dept", "비회원");
	        	member.put("group_seq", "7");
	        	member.put("dept", "비회원");
	        	member.put("ci", sConnInfo);
	        	member.put("di", sDupInfo);
	    		session.setAttribute("mc_member", member);

	    		String url = HOME_URL;
	    		if(!("".equals(returnSiteId) || "1".equals(returnSiteId) || "null".equals(returnSiteId))){
	    			url += "/s0"+returnSiteId;
	    		}
	    		if("".equals(menu_id)){
					request.setAttribute("opener", url);
	    		}else{
	    			request.setAttribute("opener", url+"?menu_id="+menu_id);
	    		}
	        }
	    }
	    else if( iReturn == -1)
	    {
	        sMessage = "복호화 시스템 에러입니다.";
	        request.setAttribute("redirect", "close");
	    }    
	    else if( iReturn == -4)
	    {
	        sMessage = "복호화 처리오류입니다.";
	        request.setAttribute("redirect", "close");
	    }    
	    else if( iReturn == -5)
	    {
	        sMessage = "복호화 해쉬 오류입니다.";
	        request.setAttribute("redirect", "close");
	    }    
	    else if( iReturn == -6)
	    {
	        sMessage = "복호화 데이터 오류입니다.";
	        request.setAttribute("redirect", "close");
	    }    
	    else if( iReturn == -9)
	    {
	        sMessage = "입력 데이터 오류입니다.";
	        request.setAttribute("redirect", "close");
	    }    
	    else if( iReturn == -12)
	    {
	        sMessage = "사이트 패스워드 오류입니다.";
	        request.setAttribute("redirect", "close");
	    }    
	    else
	    {
	        sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
	        request.setAttribute("redirect", "close");
	    }
		
	    request.setAttribute("message", sMessage);
	    return "message";
	}
	
	@RequestMapping(params="mode=phoneFail")
	public String phoneFail(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();

	    String sEncodeData = StringUtil.requestReplace(request.getParameter("EncodeData"), "encodeData");
	    String sReserved1  = StringUtil.requestReplace(request.getParameter("param_r1"), "");
	    String sReserved2  = StringUtil.requestReplace(request.getParameter("param_r2"), "");
	    String sReserved3  = StringUtil.requestReplace(request.getParameter("param_r3"), "");

	    String sSiteCode = NICE_SITE_ID;				   // NICE로부터 부여받은 사이트 코드
	    String sSitePassword = NICE_SITE_PASSWORD;			 // NICE로부터 부여받은 사이트 패스워드

	    String sCipherTime = "";					// 복호화한 시간
	    String sRequestNumber = "";				// 요청 번호
	    String sErrorCode = "";						// 인증 결과코드
	    String sAuthType = "";						// 인증 수단
	    String sMessage = "";
	    String sPlainData = "";
	    
	    int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);

	    if( iReturn == 0 )
	    {
	        sPlainData = niceCheck.getPlainData();
	        sCipherTime = niceCheck.getCipherDateTime();
	        
	        // 데이타를 추출합니다.
	        java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);
	        
	        sRequestNumber 	= (String)mapresult.get("REQ_SEQ");
	        sErrorCode 			= (String)mapresult.get("ERR_CODE");
	        sAuthType 			= (String)mapresult.get("AUTH_TYPE");
	    }
	    else if( iReturn == -1)
	    {
	        sMessage = "복호화 시스템 에러입니다.";
	    }    
	    else if( iReturn == -4)
	    {
	        sMessage = "복호화 처리오류입니다.";
	    }    
	    else if( iReturn == -5)
	    {
	        sMessage = "복호화 해쉬 오류입니다.";
	    }    
	    else if( iReturn == -6)
	    {
	        sMessage = "복호화 데이터 오류입니다.";
	    }    
	    else if( iReturn == -9)
	    {
	        sMessage = "입력 데이터 오류입니다.";
	    }    
	    else if( iReturn == -12)
	    {
	        sMessage = "사이트 패스워드 오류입니다.";
	    }    
	    else
	    {
	        sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
	    }
		
	    request.setAttribute("message", sMessage);
	    request.setAttribute("redirect", "close");
	    return "message";
	}
	@Autowired
    private ServletContext servletContext;

	@RequestMapping(params="mode=ipinSuccess")
	public String ipinSuccess(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws Exception{
		String sMessage = "실명인증에 실패하였습니다.";
		
//		실명인증 로직
		MCMap ipin_location = (MCMap) session.getAttribute("ipin_location");
		String returnUrl = String.valueOf(ipin_location.get("gpinAuthRetPage"));
		String returnSiteId = String.valueOf(ipin_location.get("returnSiteId"));
		String menu_id = String.valueOf(ipin_location.get("menu_id"));
		String mode = String.valueOf(ipin_location.get("mode"));
		String member_id = String.valueOf(ipin_location.get("member_id"));	//비밀번호찾기
		String member_nm = String.valueOf(ipin_location.get("member_nm"));	//비밀번호찾기
		String gpinUserIP = String.valueOf(ipin_location.get("gpinUserIP"));
	    session.removeAttribute("ipin_location");
		
		response.addHeader("Cache-Control", "private");

	    GPinProxy proxy = GPinProxy.getInstance(servletContext);

	    String parameter = request.getParameter("versionRequest");
	    if(parameter != null && "versionRequest".equals(parameter) )
	    {
	        try
	        {
	        	//log.debug(proxy.getSPVersion());
	        }
	        catch(Exception e)
	        {
	            // 에러에 대한 처리는 이용기관에 맞게 처리할 수 있습니다.
	            e.printStackTrace();
	        }
	        request.setAttribute("message", sMessage);
	        request.setAttribute("redirect", "close");
	        return "message";
	    }
	    
	    boolean result = false;

		// 요청한 사용자 IP와 응답받는 사용자 IP를 비교한다.
	    boolean ipCheck = request.getRemoteAddr().equals(gpinUserIP);
	    if(ipCheck){
	        try{
	            result = proxy.parseSAMLResponse(request, session);
	        }
	        catch(Exception e){
	            e.printStackTrace();
	        }
	        
	    }else{
	    	sMessage = "IP인증에 실패하였습니다.";
	        request.setAttribute("redirect", "close");
	    }
	    
	    if(result){
	    	
	    	/*MCMap real_name = new MCMap();
	    	real_name.put("ipin", session.getAttribute("dupInfo"));
	    	real_name.put("ci", "");
			real_name.put("virtualNo", session.getAttribute("virtualNo"));
			real_name.put("name", session.getAttribute("realName"));
			real_name.put("birth", session.getAttribute("birthDate"));
			real_name.put("sex", session.getAttribute("sex"));*/
			
        	MCMap  member = new MCMap();
        	member.put("member_id", "non_member");
        	member.put("member_nm", session.getAttribute("realName"));
        	member.put("dept", "비회원");
        	member.put("group_seq", "7");
        	member.put("dept", "비회원");
        	//member.put("ipin", session.getAttribute("dupInfo"));
        	member.put("di", session.getAttribute("dupInfo"));
    		session.setAttribute("mc_member", member);
			
    		String url = HOME_URL;
    		if(!("".equals(returnSiteId) || "1".equals(returnSiteId) || "null".equals(returnSiteId))){
    			url += "/s0"+returnSiteId;
    		}
    		if("".equals(menu_id)){
				request.setAttribute("opener", url);
    		}else{
    			request.setAttribute("opener", url+"?menu_id="+menu_id);
    		}
				
	    	sMessage = "본인인증에 성공하였습니다.";
	    }
		
	    request.setAttribute("message", sMessage);
		return "message";
	}
	
}
