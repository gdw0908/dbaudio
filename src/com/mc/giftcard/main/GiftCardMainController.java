package com.mc.giftcard.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mc.giftcard.shopping.cart.GiftCardCartService;
import com.mc.web.Globals;
import com.mc.web.MCMap;

@Controller
public class GiftCardMainController {
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private GiftCardMainService mainService;
	
	@Autowired
	private GiftCardCartService cartService;

	@Autowired
	private Globals globals;
	
	@RequestMapping("/giftcard/inc/gnb.do")	
	public String updateCodeOrderSeq(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception {
		model.addAttribute("main", mainService.menu_category());
		return "/giftcard/inc/gnb";
	}
	
	@RequestMapping("/giftcard/popup/latest_part.do")	
	public String latest_part(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception {
		model.addAttribute("data", mainService.latest_part(params));
		return "/giftcard/latest_part";
	}
	
	@RequestMapping("/giftcard/inc/header.do")	
	public String header(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception {
		model.addAttribute("list", mainService.headerList());
		params.put("sessionid", session.getId());
		MCMap member = (MCMap) session.getAttribute("member");
		if(member != null){
			params.put("session_member_id", (String) member.get("member_id"));
		}
		model.addAttribute("cartCnt", cartService.list(params));	
		
		return "/giftcard/inc/header";
	}

	@RequestMapping("/giftcard/inc/all_menu_box.do")	
	public String all_menu(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception {
		model.addAttribute("data", mainService.all_menu());
		return "/giftcard/inc/all_menu_box";
	}
	
	@RequestMapping("/giftcard/index.do")	
	public String main(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception {
		model.addAttribute("list", mainService.mainList(params,request));
		return "/giftcard/index";
	}

	@ResponseBody
	@RequestMapping("/giftcard/autocomplete.do")
	public Map autocomplete(ModelMap model, HttpServletRequest request, @RequestParam Map<String, String> params, @RequestParam(value = "prefix", required = false, defaultValue = "") String prefix) throws Exception {
		return mainService.autocomplete(prefix);
	}
}
