package com.mc.web.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mc.web.mail.MailTargetDAO;

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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mc.web.mail.MailTemplateDAO;
import com.mc.web.Globals;
import com.mc.web.MCController;
import com.mc.web.MCMap;
import com.mc.web.common.mail.SendMailThread;
/**
 *
 * @Description : 
 * @ClassName   : com.mc.web.mail.send.MailSendController.java
 * @Modification Information
 *
 * @author 오승택
 * @since 2015. 4. 1.
 * @version 1.0 *  
 * Copyright (C)  All right reserved.
 */
@Controller
@RequestMapping({"/admin/system/send/email/{path}/index.do","/user/main/index.do"})
public class MailController extends MCController
{
	@Resource(name = "globals")
	private Globals globals;
	
	@Autowired
	private MailService mailService;

	@Autowired
	private MailTargetDAO MailTargetDAO;

	@Autowired
	private MailTemplateDAO MailTempletDAO;

	@Autowired
	private SendMailThread SendMailThread;
	
	/*===================템플릿 START===================*/
	//템플릿 리스트
	@ResponseBody
	@RequestMapping(params="mode=template_list")
	@Transactional(rollbackFor = { Exception.class })
	public Map template_list(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		return mailService.templateList(params);
	}
	
	//템플릿 등록
	@ResponseBody
	@RequestMapping(params="mode=template_write")
	@Transactional(rollbackFor = { Exception.class })
	public Map template_write(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		Map rst = new HashMap();
		MCMap member = (MCMap)session.getAttribute("member");
		if(member == null){
			rst.put("rst","0");
			rst.put("message", globals.REQUIRED_LOGIN);
		}else{
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_id", (String) member.get("member_id"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			rst.put("rst", mailService.templateWrite(params));
		}
		return rst;
	}
	
	//템플릿 수정폼
	@ResponseBody
	@RequestMapping(params="mode=template_view")
	@Transactional(rollbackFor = { Exception.class })
	public Map template_view(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		return mailService.templateView(params);
	}
	
	//템플릿 수정 업데이트
	@ResponseBody
	@RequestMapping(params="mode=template_modify")
	@Transactional(rollbackFor = { Exception.class })
	public Map template_modify(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		Map rst = new HashMap();
		MCMap member = (MCMap)session.getAttribute("member");
		if(member == null){
			rst.put("rst","0");
			rst.put("message", globals.REQUIRED_LOGIN);
		}else{
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_id", (String) member.get("member_id"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			rst = mailService.templateModify(params);
		}
		return rst;
	}
	
	//템플릿 삭제
	@ResponseBody
	@RequestMapping(params="mode=template_delete")
	@Transactional(rollbackFor = { Exception.class })
	public Map template_delete(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		Map rst = new HashMap();
		MCMap member = (MCMap)session.getAttribute("member");
		if(member == null){
			rst.put("rst","0");
			rst.put("message", globals.REQUIRED_LOGIN);
		}else{
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_id", (String) member.get("member_id"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			rst = mailService.templateDelete(params);
		}
		return rst;
	}
	/*===================템플릿 END===================*/
	
	
	
	/*===================타겟 START===================*/
	//타겟 리스트
	@ResponseBody
	@RequestMapping(params="mode=target_list")
	@Transactional(rollbackFor = { Exception.class })
	public Map target_list(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		return mailService.targetList(params);
	}
	
	//타겟 등록 폼
	@ResponseBody
	@RequestMapping(params="mode=target_write_form")
	@Transactional(rollbackFor = { Exception.class })
	public Map target_write_form(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		createToken(request);
		Map rst = new HashMap();
		rst.put("rst", "true");
		return rst;
	}	
	
	//타겟 등록
	@ResponseBody
	@RequestMapping(params="mode=target_write")
	@Transactional(rollbackFor = { Exception.class })
	public Map target_write(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		Map rst = new HashMap();
		if(validationToken(request) == false){
			rst.put("rst","0");
			rst.put("message", "이미 등록 되었습니다.");
			return rst;
		}
		MCMap member = (MCMap)session.getAttribute("member");
		if(member == null){
			rst.put("rst","0");
			rst.put("message", globals.REQUIRED_LOGIN);
		}else{
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_id", (String) member.get("member_id"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			rst.put("rst", mailService.targetWrite(params,request));
		}
		return rst;
	}
	
	//타겟 수정폼
	@ResponseBody
	@RequestMapping(params="mode=target_view")
	@Transactional(rollbackFor = { Exception.class })
	public Map target_view(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		createToken(request);
		return mailService.targetView(params);
	}
	
	//타겟 수정 업데이트
	@ResponseBody
	@RequestMapping(params="mode=target_modify")
	@Transactional(rollbackFor = { Exception.class })
	public Map target_modify(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		Map rst = new HashMap();
		if(validationToken(request) == false){
			rst.put("rst","0");
			rst.put("message", "이미 등록 되었습니다.");
			return rst;
		}
		MCMap member = (MCMap)session.getAttribute("member");
		if(member == null){
			rst.put("rst","0");
			rst.put("message", globals.REQUIRED_LOGIN);
		}else{
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_id", (String) member.get("member_id"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			rst.put("rst", mailService.targetModify(params,request));
		}
		return rst;
	}
	
	//타겟 삭제
	@ResponseBody
	@RequestMapping(params="mode=target_delete")
	@Transactional(rollbackFor = { Exception.class })
	public Map target_delete(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		Map rst = new HashMap();
		MCMap member = (MCMap)session.getAttribute("member");
		if(member == null){
			rst.put("rst","0");
			rst.put("message", globals.REQUIRED_LOGIN);
		}else{
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_id", (String) member.get("member_id"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			rst = mailService.targetDelete(params);
		}
		return rst;
	}
	/*===================타겟 END===================*/
	
	
	
	
	
	
	//전송 리스트
	@ResponseBody
	@RequestMapping(params="mode=send_list")
	@Transactional(rollbackFor = { Exception.class })
	public Map send_list(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		return mailService.sendList(params);
	}
	
	//전송 등록
	@ResponseBody
	@RequestMapping(params={"mode=send_write"})
	@Transactional(rollbackFor=Exception.class)
	public Map send_write(@RequestParam Map<String, Object> jsonObject, HttpServletRequest request, HttpSession session) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		Map rst = new HashMap();
		MCMap member = (MCMap) session.getAttribute("member");
		if(member == null){
			rst.put("rst","0");
			rst.put("message", globals.REQUIRED_LOGIN);
		}else{
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_id", (String) member.get("member_id"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			rst.put("rst","1");
			rst.put("data", mailService.sendWrite(params));
		}
		return rst;
	}
	
	//전송 수정폼
	@ResponseBody
	@RequestMapping(params={"mode=send_view"})
	@Transactional(rollbackFor=Exception.class)
	public Map send_view(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		return mailService.sendView(params);
	}

	//전송 수정 업데이트
	@ResponseBody
	@RequestMapping(params={"mode=send_modify"})
	@Transactional(rollbackFor=Exception.class)
	public Map send_modify(@RequestParam Map<String, Object> jsonObject, HttpServletRequest request, HttpSession session) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		Map rst = new HashMap();
		MCMap member = (MCMap)session.getAttribute("member");
		if(member == null){
			rst.put("rst","0");
			rst.put("message", globals.REQUIRED_LOGIN);
		}else{
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_id", (String) member.get("member_id"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			rst = mailService.sendModify(params);
		}
		return rst;
	}
	
	//전송 삭제
	@ResponseBody
	@RequestMapping(params="mode=send_delete")
	@Transactional(rollbackFor = { Exception.class })
	public Map send_delete(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		Map rst = new HashMap();
		MCMap member = (MCMap)session.getAttribute("member");
		if(member == null){
			rst.put("rst","0");
			rst.put("message", globals.REQUIRED_LOGIN);
		}else{
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_id", (String) member.get("member_id"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			rst = mailService.sendDelete(params);
		}
		return rst;
	}
	
	//메일 전송
	@ResponseBody
	@RequestMapping(value="/user/main/index.do",params="mode=user_email_send")
	@Transactional(rollbackFor = { Exception.class })
	public Map send(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		Map rst = new HashMap();
		mailService.sendUser(params);
		rst.put("rst", "1");
		return rst;
	}
	
	//메일 전송
	@ResponseBody
	@RequestMapping(params="mode=sending")
	@Transactional(rollbackFor = { Exception.class })
	public Map sending(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		Map rst = new HashMap();
		MCMap member = (MCMap)session.getAttribute("member");
		if(member == null){
			rst.put("rst","0");
			rst.put("message", globals.REQUIRED_LOGIN);
		}else{
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_id", (String) member.get("member_id"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			mailService.send(params);
			rst.put("rst", "1");
		}
		return rst;
	}
}
