package com.mc.giftcard.board;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mc.common.util.DateUtil;
import com.mc.web.MCMap;
import com.mc.web.common.FileDAO;
import com.mc.web.common.FileUtil;
import com.mc.web.common.POIExcelDownloadService;
/**
 *
 * @Description : 
 * @ClassName   : com.mc.web.board.BoardController.java
 * @Modification Information
 *
 * @author 오승택
 * @since 2015. 3. 13.
 * @version 1.0 *  
 * Copyright (C)  All right reserved.
 */
@Controller
@RequestMapping("/giftcard/admin/system/board/{path}/index.do")
public class GiftCardBoardController {
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private GiftCardBoardService boardService;
	
	@Autowired
	private GiftCardBoardDAO boardDAO;
	
	@Autowired
	private POIExcelDownloadService excelDownloadService;
	
	@ResponseBody
	@RequestMapping(params="mode=view")
	@Transactional(rollbackFor = { Exception.class })
	public Map view(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		return boardService.view(params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=write")
	@Transactional(rollbackFor = { Exception.class })
	public Map write(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_group_seq", (String) member.get("group_seq"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("ip", request.getRemoteHost());
		params.put("article_seq", boardDAO.getNextval());
		params.put("parent_seq", "0");
		return boardService.write(request, params);
	}	
	
	@ResponseBody
	@RequestMapping(params="mode=modify")
	@Transactional(rollbackFor = { Exception.class })
	public Map modify(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_group_seq", (String) member.get("group_seq"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("ip", request.getRemoteHost());
		return boardService.modify(request, params);
	}

	@ResponseBody
	@RequestMapping(params="mode=del")
	@Transactional(rollbackFor = { Exception.class })
	public Map del(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_group_seq", (String) member.get("group_seq"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("ip", request.getRemoteHost());
		return boardService.del(params);
	}
	
	
	@ResponseBody
	@RequestMapping(params="mode=categoryWrite")
	@Transactional(rollbackFor = { Exception.class })
	public Map categoryWrite(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		MCMap member = (MCMap) session.getAttribute("member");
		return boardService.categoryWrite(params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=categoryModify")
	@Transactional(rollbackFor = { Exception.class })
	public Map categoryModify(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		MCMap member = (MCMap) session.getAttribute("member");
		return boardService.categoryModify(params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=resourcesList")
	@Transactional(rollbackFor = { Exception.class })
	public Map resourcesList(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		return boardService.resourcesList(params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=resourcesWrite")
	@Transactional(rollbackFor = { Exception.class })
	public Map resourcesWrite(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_group_seq", (String) member.get("group_seq"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("ip", request.getRemoteHost());
		params.put("article_seq", boardDAO.getNextval());
		params.put("parent_seq", "0");
		return boardService.resourcesWrite(request, params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=resourcesModify")
	@Transactional(rollbackFor = { Exception.class })
	public Map resourcesModify(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_group_seq", (String) member.get("group_seq"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("ip", request.getRemoteHost());
		return boardService.resourcesModify(request, params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=resourcesView")
	@Transactional(rollbackFor = { Exception.class })
	public Map resourcesView(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_group_seq", (String) member.get("group_seq"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		return boardService.resourcesView(params);
	}
	
	
	
	@ResponseBody
	@RequestMapping(params="mode=commentList")
	@Transactional(rollbackFor = { Exception.class })
	public Map commentList(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_group_seq", (String) member.get("group_seq"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("reg_id", (String) member.get("member_id"));
		params.put("ip", request.getRemoteHost());
		return boardService.commentList(params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=commentInsert")
	@Transactional(rollbackFor = { Exception.class })
	public Map commentWrite(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_group_seq", (String) member.get("group_seq"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("reg_id", (String) member.get("member_id"));
		params.put("ip", request.getRemoteHost());
		return boardService.commentWrite(params);
	}
	
	
	@ResponseBody
	@RequestMapping(params="mode=commentReply")
	@Transactional(rollbackFor = { Exception.class })
	public Map commentReply(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_group_seq", (String) member.get("group_seq"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("reg_id", (String) member.get("member_id"));
		params.put("ip", request.getRemoteHost());
		return boardService.commentReply(params);
	}
	
	
	@ResponseBody
	@RequestMapping(params="mode=commentDel")
	@Transactional(rollbackFor = { Exception.class })
	public Map commentDel(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_group_seq", (String) member.get("group_seq"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("ip", request.getRemoteHost());
		return boardService.commentDel(params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=commentReplyUpdate")
	@Transactional(rollbackFor = { Exception.class })
	public Map commentReplyUpdate(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_group_seq", (String) member.get("group_seq"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("ip", request.getRemoteHost());
		return boardService.commentReplyUpdate(params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=commentReplyInfo")
	@Transactional(rollbackFor = { Exception.class })
	public Map commentReplyInfo(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_group_seq", (String) member.get("group_seq"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("ip", request.getRemoteHost());
		return boardService.commentReplyInfo(params);
	}
	
	
	
	@RequestMapping(params="mode=excelDown")
	public void excelDown(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		List firstList = boardService.resourcesExcel(params);
		List list = new ArrayList();
		
		for(int x = 0; x < firstList.size(); x++){
			Map hashMap = (Map) firstList.get(x);
			String replaceTag = (String)hashMap.get("item_info");
			replaceTag = replaceTag.replaceAll("&gt;", ">");
			replaceTag = replaceTag.replaceAll("&lt;", "<");
			hashMap.put("item_info",replaceTag);
			list.add(hashMap);
		}
		
		String filename = java.net.URLEncoder.encode(DateUtil.getCurrentDate()+"_자원관리","UTF-8");
		OutputStream output = response.getOutputStream();
		response.setContentType("application/xls");
		response.setHeader("Content-Disposition","attachment; filename="+filename+".xls");
		String[][] header = {
				{"주소", "업체명", "폐자원현황", "연락처", "반출희망일자", "반출완료일자", "비고", "상태"}, 
				{"full_address","com_nm","item_info","staff_tel","sdate","edate","conts","status_kr"},
				{"", "", "", "", "", "", "", ""},
				{"", "", "", "", "", "", "", ""}
			};
		
		excelDownloadService.excelDownload(output, list, "주문내역", header);
	}
	
	
}