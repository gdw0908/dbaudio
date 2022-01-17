package com.mc.giftcard.login;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mc.web.Globals;
import com.mc.common.util.Encryption;
import com.mc.common.util.StringUtil;
import com.mc.common.util.Util;
import com.mc.web.MCMap;


@Controller
@RequestMapping({"/giftcard/login/login.do", "/giftcard/mobile/login/login.do"})
public class GiftCardUserLoginController {
	
	@Autowired
	private GiftCardLoginService loginService;
	
	@Autowired
	private GiftCardLoginDAO LoginDAO;
	
	@Resource(name = "globals")
	private Globals globals;
	
	
	@RequestMapping(params="!mode")
	public String loginFrm(ModelMap model, @RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session){
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member != null){
			return "/giftcard/login/login";
    	}
		
		return "/giftcard/login/login";
	}

	@RequestMapping(params="mode=logout")
	public String logout(ModelMap model, @RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session){
		session.removeAttribute("member");
		session.invalidate();
		request.setAttribute("redirect", "/giftcard/login/login.do");
		return "message";
	}
	
	@RequestMapping(params="mode=proc", method=RequestMethod.POST) 
	public String user_proc(ModelMap model, @RequestParam Map<String, String> params, @CookieValue("JSESSIONID") String cookie, HttpServletRequest request, HttpSession session) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member != null || (cookie == "" || cookie == null)){
			request.setAttribute("message", Globals.ABNORMAL_MSG);
    		request.setAttribute("redirect", "/");
			return "message";
    	}
		
		params.put("member_id", StringUtil.clearXSS(params.get("member_id"), "").replaceAll("'", "").replaceAll(";", "").replaceAll("-", ""));
		//params.put("member_pw", StringUtil.clearXSS(params.get("member_pw"), "").replaceAll("'", "").replaceAll(";", "").replaceAll("-", ""));
		params.put("member_pw", Encryption.stringEncryption(StringUtil.clearXSS(params.get("member_pw"), "").replaceAll("'", "").replaceAll(";", "").replaceAll("-", ""), ""));
		
		
		if(params.get("member").equals("1"))
		{
			//회원 로그인
			member = (MCMap) LoginDAO.getMemberByIdPwUser(params);
		}
		/*
		else
		{
			//비회원 로그인
			member = LoginDAO.getArticle("member.getNoNUserMemberByIdPw", params);
		}
		*/
		
		
		if(member == null){
			request.setAttribute("message", "아이디와 비밀번호가 일치하지 않습니다.");
			request.setAttribute("redirect", "/giftcard/login/login.do?method=login");
			return "message";
		}else{
			
			LoginDAO.update("member.updateMemberLastLogin", member);
			session.setMaxInactiveInterval(60 * 60);
    		session.setAttribute("member", member);
    		session.setAttribute("sessionCom_nm", (String)member.get("com_seq"));
    		
    		if(!StringUtil.isEmptyByParam(params, "returnURL")){
    			return "redirect:"+params.get("returnURL");
//    			request.setAttribute("redirect", params.get("returnURL"));
//    			return "message";
    		}else{
    			return "redirect:/giftcard/index.do";
    		}
    		
		}
	}
	
	
	@RequestMapping(params="mode=mlogin")
	public String mobile(ModelMap model, @RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session){
		
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("params", params);
		return "/giftcard/mobile/login/login";
	}
	
	@RequestMapping(params="mode=mnomember")
	public String mnomember(ModelMap model, @RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session){
		
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("params", params);
		return "/giftcard/mobile/login/nomember";
	}
	
	@RequestMapping(params="mode=m_buy_login")
	public String m_buy_login(ModelMap model, @RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session){
		
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("params", params);
		return "/giftcard/mobile/login/buy_login";
	}
	
	@RequestMapping(params="mode=m_buy_nomember")
	public String m_buy_nomember(ModelMap model, @RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session){
		
		request.setAttribute("servletPath", request.getServletPath());
		String header = request.getHeader("referer");
		if(!"".equals(header) && null != header){
			session.setAttribute("returnURL", header);
		}
		request.setAttribute("params", params);
		return "/giftcard/mobile/login/buy_nomember";
	}
	
	@RequestMapping(params="mode=mProc")
	public String mobileProc(ModelMap model, @RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session,@CookieValue("JSESSIONID") String cookie){
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member != null || (cookie == "" || cookie == null)){
			request.setAttribute("message", Globals.ABNORMAL_MSG);
    		request.setAttribute("redirect", "/");
			return "message";
    	}
		
		params.put("member_id", StringUtil.clearXSS(params.get("member_id"), "").replaceAll("'", "").replaceAll(";", "").replaceAll("-", ""));
		//params.put("member_pw", StringUtil.clearXSS(params.get("member_pw"), "").replaceAll("'", "").replaceAll(";", "").replaceAll("-", ""));
		params.put("member_pw", Encryption.stringEncryption(StringUtil.clearXSS(params.get("member_pw"), "").replaceAll("'", "").replaceAll(";", "").replaceAll("-", ""), ""));
		
		
		if(params.get("member").equals("1"))
		{
			//회원 로그인
			member = (MCMap) LoginDAO.getMemberByIdPwUser(params);
		}
		/*
		else
		{
			//비회원 로그인
			member = LoginDAO.getArticle("member.getNoNUserMemberByIdPw", params);
		}
		*/
		
		
		if(member == null){
			request.setAttribute("message", "아이디와 비밀번호가 일치하지 않습니다.");
			request.setAttribute("redirect", "/giftcard/mobile/login/login.do?mode=mobile");
			return "message";
		}else{
			
			LoginDAO.update("member.updateMemberLastLogin", member);
			session.setMaxInactiveInterval(60 * 60);
    		session.setAttribute("member", member);
    		
    		if(!StringUtil.isEmptyByParam(params, "returnURL")){
    			return "redirect:"+params.get("returnURL");
//    			request.setAttribute("redirect", params.get("returnURL"));
//    			return "message";
    		}else{
    			return "redirect:/mobile";
    		}
    		
		}
	}
	
	
	@RequestMapping(params="mode=mlogout")
	public String mlogout(ModelMap model, @RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session){
		session.removeAttribute("member");
		session.invalidate();
		request.setAttribute("redirect", "/mobile");
		return "message";
	}
	
}
