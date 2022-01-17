package com.mc.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mc.web.MCMap;

public class AdminLoginInterceptor extends HandlerInterceptorAdapter{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {//ajax호출일 경우
		HttpSession session = request.getSession();
		String requestURI = request.getRequestURI().toString();
		String method = request.getMethod();
		String ajax = "";
		if(request.getHeader("X-requested-With") != null){
			ajax = request.getHeader("X-requested-With");
			ajax = ajax.toLowerCase();
		}
		boolean returnValue = true;
		if(requestURI.indexOf("/admin/") > -1 && method.equals("POST") && requestURI.indexOf("/admin/login.do") == -1 && "xmlhttprequest".equals(ajax)){
			MCMap member = (MCMap) session.getAttribute("member");
			if(member == null){
				returnValue = false;
				response.sendError(999, "Session Time Out");								
	    	}
		}
		return returnValue;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {//Servlet호출일 경우
		HttpSession session = request.getSession();
		String requestURI = request.getRequestURI().toString();
		String method = request.getMethod();
		String ajax = "";
		if(request.getHeader("X-requested-With") != null){
			ajax = request.getHeader("X-requested-With");
			ajax = ajax.toLowerCase();
		}
		boolean returnValue = true;
		if(requestURI.indexOf("/admin/") > -1 && method.equals("POST") && requestURI.indexOf("/admin/login.do") == -1 && !"xmlhttprequest".equals(ajax)){
			MCMap member = (MCMap) session.getAttribute("member");
			if(member == null){
				returnValue = false;
				modelAndView.addObject("message", "세션이 끊어졌습니다.<br>로그인 후 다시 시도해 주세요.<br>로그인페이지로 이동합니다.");
				modelAndView.addObject("top","/giftcard/admin/login.do");
				modelAndView.setViewName("/message");
	    	}
		}
	}
}
