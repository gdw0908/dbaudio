<%@ page language = "java" contentType = "text/html; charset=UTF-8"%>
<%@ page import="gov.mogaha.gpin.sp.proxy.*" %>
<%@ page import="com.mc.common.util.Req" %>
<%@ page import="com.mc.web.MCMap" %>
<%
	String requestUrl = request.getRequestURL().toString();
	if(!requestUrl.startsWith("https://")){
		requestUrl = requestUrl.replace("http://","https://");
		if(request.getQueryString() != null){
			requestUrl = requestUrl+"?"+request.getQueryString();
			response.sendRedirect(requestUrl);
		}
	}
%>
<%
	String returnUrl = "";
	if(request.getParameter("returnUrl")==null||request.getParameter("returnUrl").equals("")){
		returnUrl= request.getContextPath();
	}else{
		returnUrl = request.getContextPath()+request.getParameter("returnUrl");
	}
    /**
     * 사용자 본인인증을 요청하는 페이지입니다.
     * 회원가입, 게시판 글쓰기등 본인인증이 필요한 경우에 이 페이지를 호출하시면 됩니다.
     * 인증이 완료되면 session에 사용자정보가 설정됩니다.
     * 설정된 사용자 정보를 참조하는 방법은 Sample-AuthResponse를 참조하시기 바랍니다.
     */
    // 인증완료후 session에 저장된 사용자정보를 참조할 페이지, (이용기관 인증수신페이지와 다릅니다.)
    // TODO 이용기관에서 사용하실 페이지를 지정합니다.
    
    //System.out.println("-----------" + request.getRemoteAddr() + "-------------");
    
    MCMap ipin_location = new MCMap();
    ipin_location.put("gpinAuthRetPage", returnUrl);
    ipin_location.put("returnSiteId", Req.getValue(request, "return_site_id", ""));
    ipin_location.put("menu_id", Req.getValue(request, "menu_id", ""));
    ipin_location.put("mode", Req.getValue(request, "returnMode", ""));
    ipin_location.put("gpinUserIP", request.getRemoteAddr());
	session.setAttribute("ipin_location", ipin_location);

    GPinProxy proxy = GPinProxy.getInstance(this.getServletConfig().getServletContext());

    String requestHTML = "인증요청 메시지생성 실패";
    try
    {
        if (request.getParameter("Attr") != null)
        {
            requestHTML = proxy.makeAuthRequest(Integer.parseInt(request.getParameter("Attr")));
        }
        else
        {
            requestHTML = proxy.makeAuthRequest();
        }
    }
    catch(Exception e)
    {
        // 에러에 대한 처리는 이용기관에 맞게 처리할 수 있습니다.
        e.printStackTrace();
        out.println(e.getMessage());
    }
    // 인증 요청페이지를 생성하여 자동으로 공공I-PIN으로 forwarding 합니다.
    out.println(requestHTML);
%>
