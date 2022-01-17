package com.mc.web.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PathController {
	
	Logger log = Logger.getLogger(this.getClass());

	@RequestMapping("/{path}.do")
	public String path(@PathVariable String path, @RequestParam Map<String, String> params, HttpServletRequest request) throws Exception{
		return "/" + path;
	}
	@RequestMapping("/{path}/{page}.do")
	public String path(@PathVariable String path, @PathVariable String page, @RequestParam Map<String, String> params, HttpServletRequest request) throws Exception{
		return "/" + path + "/" + page;
	}
	@RequestMapping("/{path}/{path2}/{page}.do")
	public String path2(@PathVariable String path, @PathVariable String path2, @PathVariable String page, @RequestParam Map<String, String> params, HttpServletRequest request) throws Exception{
		return "/" + path + "/" + path2 + "/" + page;
	}
	@RequestMapping("/{path}/{path2}/{path3}/{page}.do")
	public String path3(@PathVariable String path, @PathVariable String path2, @PathVariable String path3, @PathVariable String page, @RequestParam Map<String, String> params, HttpServletRequest request) throws Exception{
		return "/" + path + "/" + path2  + "/" + path3  + "/" + page;
	}
	@RequestMapping("/{path}/{path2}/{path3}/{path4}/{page}.do")
	public String path3(@PathVariable String path, @PathVariable String path2, @PathVariable String path3, @PathVariable String path4, @PathVariable String page, @RequestParam Map<String, String> params, HttpServletRequest request) throws Exception{
		return "/" + path + "/" + path2  + "/" + path3 + "/" + path4  + "/" + page;
	}
	@RequestMapping("/{path}/{path2}/{path3}/{path4}/{path5}/{page}.do")
	public String path5(@PathVariable String path, @PathVariable String path2, @PathVariable String path3, @PathVariable String path4, @PathVariable String path5, @PathVariable String page, @RequestParam Map<String, String> params, HttpServletRequest request) throws Exception{
		return "/" + path + "/" + path2  + "/" + path3 + "/" + path4  + "/" + path5  + "/" + page;
	}
	
}
