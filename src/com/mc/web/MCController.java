package com.mc.web;

import javax.servlet.http.HttpServletRequest;

public abstract class MCController {
	protected void createToken(HttpServletRequest request){
		request.getSession().setAttribute(this.getClass().toString(), "token");
	}
	
	protected boolean validationToken(HttpServletRequest request){
		if(request.getSession().getAttribute(this.getClass().toString()) == null)
			return false;
		
		request.getSession().removeAttribute(this.getClass().toString());
		return true;
	}
}
