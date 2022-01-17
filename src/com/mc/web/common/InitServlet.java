package com.mc.web.common;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 *
 * @Description : 파일삭제 스케쥴러에서 웹루트 절대경로를 가져오기 위한 서블릿 클래스
 * 컨테이너 가동시 최초 한번 실행되서 경로를 MyDefaultContext 저장소에 저장해둠.
 * @ClassName   : com.vd.web.InitServlet.java
 * @Modification Information
 *
 * @author 이창기
 * @since 2014. 4. 6.
 * @version 1.0 *  
 * Copyright (C)  All right reserved.
 */
public class InitServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	public InitServlet() {
		super();
	}
	
	public void init(ServletConfig arg0) throws ServletException {
		super.init(arg0);
		MyDefaultContext con = MyDefaultContext.getInstance(getServletContext().getRealPath(""));
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
}