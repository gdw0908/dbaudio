package com.mc.web.test;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AjaxTestController {
	
	@RequestMapping("/test.json")
	public Map convertjson() throws Exception {
		Map map = new HashMap();
		map.put("aaa", "한글");
		return map;
	}

	@RequestMapping("/text.do")
	public String text() throws Exception {
		return "한글";
	}
}
