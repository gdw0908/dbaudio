package com.mc.web.test;

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

import com.mc.web.Globals;


@Controller
public class TestController {
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private TestService testService;
	
	@Autowired
	private Globals globals;

	@RequestMapping("/test.do")
	public String test(ModelMap model, @RequestParam Map<String, String> params) throws Exception {
		log.info(params.get("aaa"));
		log.info(globals.DEVELOPE);
		
		model.addAttribute("json", testService.test(params));
		return "json";
	}

	@RequestMapping("/add.do")
	@Transactional(rollbackFor = { Exception.class })
	public String add(ModelMap model, @RequestParam Map<String, String> params)	throws Exception {
		testService.add(params);
		return "test/test";
	}
	
	@RequestMapping("/carallbaro_thumb.do")
	public void thumb(HttpServletRequest request, HttpSession session) throws Exception{
		testService.thumb(request, session);		
	}
}