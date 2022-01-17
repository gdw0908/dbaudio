package com.mc.giftcard.goods.part;

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
import org.springframework.web.multipart.MultipartFile;

import com.mc.web.MCMap;
/**
 * 
 *
 * @Description : 
 * @ClassName   : com.mc.web.goods.part.PartController.java
 * @Modification Information
 *
 * @author 이창기
 * @since 2015. 2. 25.
 * @version 1.0 *  
 * Copyright (C)  All right reserved.
 */
@Controller
@RequestMapping("/giftcard/admin/goods/part/index.do")
public class GiftCardAdminPartController {
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private GiftCardPartService partService;
	
	@ResponseBody
	@RequestMapping(params="mode=list")
	public Map adminlist(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		return partService.adminlist(params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=view")
	@Transactional(rollbackFor = { Exception.class })
	public Map view(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		return partService.view(params);
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
		return partService.write(request, params);
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
		return partService.modify(request, params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=del")
	@Transactional(rollbackFor = { Exception.class })
	public Map del(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_id", (String) member.get("member_id"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("ip", request.getRemoteHost());
		return partService.del(params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=excelUpload")
	@Transactional(rollbackFor = { Exception.class })
	public Map excelUpload(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params, @RequestParam(value = "excelfile") MultipartFile excelfile) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_id", (String) member.get("member_id"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("session_com_seq", (String) member.get("com_seq"));
		params.put("ip", request.getRemoteHost());
		
		return partService.excelUpload(request, params, excelfile);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=updateCommonRate")
	@Transactional(rollbackFor = { Exception.class })
	public Map updateCommonRate(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_id", (String) member.get("member_id"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("ip", request.getRemoteHost());
		return partService.updateCommonRate(params);
	}
}
