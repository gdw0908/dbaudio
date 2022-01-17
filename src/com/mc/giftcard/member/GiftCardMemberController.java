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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mc.web.MCMap;
/**
 * 
 * @Description : 
 * @ClassName   : com.mc.web.member.MemberController.java
 * @Modification Information
 *
 * @author 이창기
 * @since 2015. 2. 12.
 * @version 1.0 *  
 * Copyright (C)  All right reserved.
 */
@Controller
@RequestMapping("/giftcard/member/member.do")
public class GiftCardMemberController {
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private GiftCardMemberService memberService;
	
	@ResponseBody
	@RequestMapping(params="mode=view")
	@Transactional(rollbackFor = { Exception.class })
	public Map view(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		return memberService.view(params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=passwordInit")
	@Transactional(rollbackFor = { Exception.class })
	public Map passwordInit(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_id", (String) member.get("member_id"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("ip", request.getRemoteHost());
		return memberService.passwordInit(params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=write")
	@Transactional(rollbackFor = { Exception.class })
	public Map write(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_id", (String) member.get("member_id"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("ip", request.getRemoteHost());
		return memberService.write(params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=modify")
	@Transactional(rollbackFor = { Exception.class })
	public Map modify(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_id", (String) member.get("member_id"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("ip", request.getRemoteHost());
		return memberService.modify(params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=del")
	@Transactional(rollbackFor = { Exception.class })
	public Map del(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_id", (String) member.get("member_id"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("ip", request.getRemoteHost());
		return memberService.del(params);
	}
}
