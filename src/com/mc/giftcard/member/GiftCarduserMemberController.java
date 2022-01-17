package com.mc.giftcard.member;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mc.common.util.Encryption;
import com.mc.common.util.StringUtil;
import com.mc.web.Globals;
import com.mc.web.MCMap;
import com.mc.web.mail.MailService;
import com.mc.web.mms.MmsService;
/**
 * 
 * @Description : 
 * @ClassName   : com.mc.web.member.MemberController.java
 * @Modification Information
 *
 * @author 유민기
 * @since 2015. 2. 12.
 * @version 1.0 *  
 * Copyright (C)  All right reserved.
 */
@Controller
@RequestMapping({"/giftcard/join/join_4.do", "/giftcard/join/id_search_2.do", "/giftcard/join/pw_search_2.do", "/giftcard/mobile/join/join_4.do", "/giftcard/mobile/join/id_search_ok.do", "/giftcard/mobile/join/pw_search_ok.do"})
public class GiftCarduserMemberController {
	
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private MmsService sms;

	@Autowired
	private GiftCardMemberDAO MemberDAO;
	
	@Autowired
	private MailService mailService;
	
	@RequestMapping(params="mode=insert", method=RequestMethod.POST) 
	public String user_proc(ModelMap model, @RequestParam Map<String, String> params, @CookieValue("JSESSIONID") String cookie, HttpServletRequest request, HttpSession session) throws Exception{
		
		params.put("ip", request.getRemoteHost());
		if(!StringUtil.isEmptyByParam(params, "tel1") || !StringUtil.isEmptyByParam(params, "tel2")) params.put("tel", params.get("tel1") + "-" + params.get("tel2") + "-" + params.get("tel3"));
		params.put("email", params.get("email1") + "@" + params.get("email2"));
		params.put("zip_cd", params.get("zip_cd1") + "-" + params.get("zip_cd2"));
		params.put("member_pw", Encryption.stringEncryption((String)params.get("member_pw"), ""));
		
		if(!StringUtil.isEmptyByParam(params, "cell1")) params.put("cell",params.get("cell1") + "-" + params.get("cell2") + "-" + params.get("cell3"));
		if(!StringUtil.isEmptyByParam(params, "staff_tel1")) params.put("staff_tel",params.get("staff_tel1") + "-" + params.get("staff_tel2") + "-" + params.get("staff_tel3"));
		if(!StringUtil.isEmptyByParam(params, "busi_no1")) params.put("busi_no",params.get("busi_no1") + "-" + params.get("busi_no2") + "-" + params.get("busi_no3"));
		
		MemberDAO.insertMemberData(params);
		
		return "/giftcard/join/join_4";
	}
	
	@RequestMapping(params="mode=search", method=RequestMethod.POST) 
	public String id_search(ModelMap model, @RequestParam Map<String, String> params, @CookieValue("JSESSIONID") String cookie, HttpServletRequest request, HttpSession session) throws Exception{
		
		MCMap article = (MCMap) MemberDAO.findIDPasswdList(params);
		request.setAttribute("article", article);
		request.setAttribute("params", params);
		return "/giftcard/join/id_search_2";
	}
	
	//비밀번호 찾기
	@RequestMapping(params="mode=pw_search", method=RequestMethod.POST) 
	public String pw_search(ModelMap model, @RequestParam Map<String, String> params, @CookieValue("JSESSIONID") String cookie, HttpServletRequest request, HttpSession session) throws Exception{
		
		MCMap article = (MCMap) MemberDAO.findPwPasswdList(params);
		
		if(article != null) // 회원정보가 있으면
		{
			params.put("session_member_seq", "");
			params.put("session_member_nm", "");
			params.put("ip", request.getRemoteHost());
			params.put("member_seq",(String) article.get("member_seq"));
			model.addAttribute("member_seq", (String) article.get("member_seq"));
			
			return "/giftcard/join/pw_search_1";
		} else {
			request.setAttribute("article", article);
			request.setAttribute("params", params);
			return "/giftcard/join/pw_search_2";
			
		}
		
	}
	
	//비밀번호 업데이트 추가
	@RequestMapping(params="mode=pw_update", method=RequestMethod.POST) 
	public String pw_update(ModelMap model, @RequestParam Map<String, String> params, @CookieValue("JSESSIONID") String cookie, HttpServletRequest request, HttpSession session) throws Exception{
		
		params.put("session_member_seq", "");
	    params.put("session_member_nm", "");
	    params.put("ip", request.getRemoteHost());
	    
	    //비밀번호 암호화
	    params.put("member_pw", Encryption.stringEncryption(StringUtil.clearXSS(params.get("member_pw"), "").replaceAll("'", "").replaceAll(";", "").replaceAll("-", ""), ""));
		
		int result = MemberDAO.passwordInit(params);
		
		if(result == 1) {
			request.setAttribute("servletPath", request.getServletPath());
			request.setAttribute("requestURI", request.getRequestURI());
			request.setAttribute("requestURL", request.getRequestURL());
			
			request.setAttribute("message", "비밀번호 변경이 완료 되었습니다.");
			request.setAttribute("redirect", "/giftcard/login/login.do");
			return "message";
			
		} else {
			request.setAttribute("message", "비밀번호 변경 오류입니다.");
			return "message";
			
		}
	}
	
	@RequestMapping(params="mode=minsert", method=RequestMethod.POST) 
	public String minsert(ModelMap model, @RequestParam Map<String, String> params, @CookieValue("JSESSIONID") String cookie, HttpServletRequest request, HttpSession session) throws Exception{
		
		params.put("ip", request.getRemoteHost());
		
		if(!StringUtil.isEmptyByParam(params, "tel1") || !StringUtil.isEmptyByParam(params, "tel2")) params.put("tel", params.get("tel1") + "-" + params.get("tel2") + "-" + params.get("tel3"));
		params.put("email", params.get("email1") + "@" + params.get("email2"));
		params.put("zip_cd", params.get("zip_cd1") + "-" + params.get("zip_cd2"));
		params.put("member_pw", Encryption.stringEncryption((String)params.get("member_pw"), ""));
		
		if(!StringUtil.isEmptyByParam(params, "cell1")) params.put("cell",params.get("cell1") + "-" + params.get("cell2") + "-" + params.get("cell3"));
		if(!StringUtil.isEmptyByParam(params, "staff_tel1")) params.put("staff_tel",params.get("staff_tel1") + "-" + params.get("staff_tel2") + "-" + params.get("staff_tel3"));
		if(!StringUtil.isEmptyByParam(params, "busi_no1")) params.put("busi_no",params.get("busi_no1") + "-" + params.get("busi_no2") + "-" + params.get("busi_no3"));
		
		MemberDAO.insertMemberData(params);
		
		return "/giftcard/mobile/join/join_4";
	}
	
	@RequestMapping(params="mode=msearch", method=RequestMethod.POST) 
	public String mobile_search(ModelMap model, @RequestParam Map<String, String> params, @CookieValue("JSESSIONID") String cookie, HttpServletRequest request, HttpSession session) throws Exception{
		
		MCMap article = (MCMap) MemberDAO.findIDPasswdList(params);
		request.setAttribute("article", article);
		request.setAttribute("params", params);
		return "/giftcard/mobile/join/id_search_ok";
	}
	
	@RequestMapping(params="mode=mpw_search", method=RequestMethod.POST) 
	public String mpw_search(ModelMap model, @RequestParam Map<String, String> params, @CookieValue("JSESSIONID") String cookie, HttpServletRequest request, HttpSession session) throws Exception{
		
		if(!StringUtil.isEmptyByParam((String)params.get("auth_member_nm")))
		{
			if(!params.get("auth_member_nm").equals(params.get("auth_member_nm_1")))
			{
				request.setAttribute("message", "인증받은 이름과 입력하신 이름이 일치하지 않습니다.");
				request.setAttribute("redirect", "/giftcard/mobile/join/pw_search_usr.do");
				return "message";
			}
		}
		
		if(!StringUtil.isEmptyByParam(params, "busi_no1")) params.put("busi_no",params.get("busi_no1") + "-" + params.get("busi_no2") + "-" + params.get("busi_no3"));
		
		MCMap article = (MCMap) MemberDAO.findPwPasswdList(params);
		
		if(article != null) // 회원정보가 있으면
		{
			String initPw = StringUtil.createPasword(5);

			params.put("session_member_seq", "");
			params.put("session_member_nm", "");
			params.put("ip", request.getRemoteHost());
			params.put("member_pw", Encryption.stringEncryption(initPw, ""));
			params.put("member_seq",(String) article.get("member_seq"));
			
			MemberDAO.passwordInit(params);

			//SMS 발송 로직 필요
			if(!StringUtil.isEmptyByParam(article, "cell")) //개인은 핸드폰 번호가 있고 기업은 핸드폰번호가 없음
			{
				
				//params.put("tran_phone", (String)article.get("cell"));
				params.put("tran_phone", (String)article.get("cell"));
				params.put("tran_callback","1566-6444");
				params.put("tran_msg", "초기화된 비밀번호는 " + initPw + " 입니다.\n 반드시 회원정보 수정에서 비밀번호를 수정해주세요.");
				sms.write(params);
			}
			else
			{
				//이메일 발송 로직 필요
				params.put("from_email","partsmoa@insun.com");		//보내는사람 이메일주소
				params.put("from_nm","Parts Moa");					//보내는사람 이름
				params.put("title","초기화된 비밀번호 입니다.");		//제목
				params.put("conts","초기화된 비밀번호는 " + initPw + " 입니다.\n 반드시 회원정보 수정에서 비밀번호를 수정해주세요.");			//내용
				params.put("to_email", (String)article.get("email"));		//받는사람 이메일
				log.info("============================= Mail Send Start ==============================");
				mailService.sendUser(params);
				log.info("============================= Mail Send End ==============================");
			}
		}
		
		request.setAttribute("article", article);
		request.setAttribute("params", params);
		return "/giftcard/mobile/join/pw_search_ok";
	}

}
