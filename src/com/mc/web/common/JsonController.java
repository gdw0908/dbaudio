package com.mc.web.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mc.web.MCMap;

@RestController
public class JsonController {
	
	@Autowired
	private JsonDAO jsonDAO;
	
	@ResponseBody
	@RequestMapping("/json/list/{queryId}.do")
	public List list(@PathVariable String queryId,@RequestParam Map<String, String> params, HttpServletRequest request) throws Exception{
		return jsonDAO.getList(queryId, params);
	}

	@ResponseBody
	@RequestMapping("/json/request/{queryId}.do")
	public MCMap request(@PathVariable String queryId,@RequestParam Map<String, String> params, HttpServletRequest request) throws Exception{
		return jsonDAO.getRequest(queryId, params);
	}
	
	@ResponseBody
	@RequestMapping("/json/update/{queryId}.do")
	public Map<String, String> update(@PathVariable String queryId,@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		if(member != null){
			params.put("session_member_id", (String) member.get("member_id"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			params.put("ip", request.getRemoteHost());
		}
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("rst", Integer.toString(jsonDAO.update(queryId, params)));
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/json/insert/{queryId}.do")
	public Map<String, String> insert(@PathVariable String queryId,@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		if(member != null){
			params.put("session_member_id", (String) member.get("member_id"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			params.put("ip", request.getRemoteHost());
		}
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("rst", Integer.toString(jsonDAO.insert(queryId, params)));
		
		return map;
	}
}
