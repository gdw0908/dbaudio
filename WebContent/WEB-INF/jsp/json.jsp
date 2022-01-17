<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.JSONObject" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %><%
Map<String, String> obj = (Map)request.getAttribute("json");
String json = JSONObject.toJSONString(obj);
out.print(json);
%>