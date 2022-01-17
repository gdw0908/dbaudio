package com.mc.web.temp;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 
 *
 * @Description : 데이터 이관 
 * @ClassName   : com.mc.web.temp.TransferController.java
 * @Modification Information
 *
 * @author 이창기
 * @since 2015. 4. 9.
 * @version 1.0 *  
 * Copyright (C)  All right reserved.
 */
@Controller
@RequestMapping("/transfer/index.do")
public class TransferController {
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TransferService transferService;
	
//	@ResponseBody
//	@RequestMapping(params="mode=transfer")
//	@Transactional(rollbackFor = { Exception.class })
//	public String transfer(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
//		return transferService.transfer(params, request);
//	}
	
	@ResponseBody
	@RequestMapping(params="mode=transfer_file")
	@Transactional(rollbackFor = { Exception.class })
	public String transfer_file(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		return transferService.transfer_file(params, request);
	}
	
//	@ResponseBody
//	@RequestMapping(params="mode=transfer_thumb")
//	@Transactional(rollbackFor = { Exception.class })
//	public String transfer_thumb(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
//		return transferService.transfer_thumb(params, request);
//	}
}
