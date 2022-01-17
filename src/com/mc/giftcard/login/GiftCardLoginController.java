package com.mc.giftcard.login;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mc.web.MCMap;

@Controller
@RequestMapping("/giftcard/admin/login.do")
public class GiftCardLoginController {
	
	@Autowired
	private GiftCardLoginService loginService;
	
	@RequestMapping(params="!mode")
	public String loginFrm(ModelMap model, @RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session){
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member != null){
			return "/giftcard/admin/index";
    	}
		
		return "/giftcard/admin/login";
	}

	@ResponseBody
	@RequestMapping(params="mode=superLogin")
	public Map superLogin(ModelMap model, @RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		return loginService.superLogin(session, params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=proc")
	public Map loginProc(ModelMap model, @RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		return loginService.login(session, params);
	}
	
	@RequestMapping(params="mode=logout")
	public String logout(ModelMap model, @RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session){
		session.invalidate();
		/*
		request.setAttribute("redirect", "/");
		return "message";
		*/
		return "/giftcard/admin/login";
	}
}
