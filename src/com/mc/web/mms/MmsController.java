package com.mc.web.mms;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mc.web.MCMap;
import com.mc.web.common.FileDAO;
import com.mc.web.common.FileUtil;
/**
 *
 * @Description : 
 * @ClassName   : com.mc.web.mms.MmsController.java
 * @Modification Information
 *
 * @author 오승택
 * @since 2015. 3. 13.
 * @version 1.0 *  
 * Copyright (C)  All right reserved.
 */
@Controller
@RequestMapping("/admin/system/send/sms/index.do")
public class MmsController {
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private MmsService mmsService;
	
	@ResponseBody
	@RequestMapping(params="mode=sms_write")
	@Transactional(rollbackFor = { Exception.class })
	public Map write(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		return mmsService.write(params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=list")
	@Transactional(rollbackFor = { Exception.class })
	public Map list(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		if(jsonObject.get("cpage") == null){
			params.put("cpage", "1");
		}else{
			params.put("cpage", jsonObject.get("cpage"));
		}
		return mmsService.list(params);
	}
}