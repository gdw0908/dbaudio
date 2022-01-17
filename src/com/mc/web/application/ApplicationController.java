package com.mc.web.application;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApplicationController {

	@Autowired
	private ApplicationService service;
	
	@ResponseBody
	@RequestMapping("/application/controllData.do")
	public Map controllData() throws Exception{
		return service.controllData(); 
	}
	
	
}
