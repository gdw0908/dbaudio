<%@ page language="java" import="java.io.*,java.net.*" 
contentType="text/xml; charset=UTF-8"    
pageEncoding="UTF-8"%><%
URL url = new URL(request.getParameter("getUrl"));
URLConnection connection = url.openConnection();

connection.setDoOutput(true);
connection.setRequestProperty("CONTENT-TYPE","text/xml"); 

BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
String inputLine;
String buffer = "";

while ((inputLine = in.readLine()) != null){
	buffer += inputLine.trim();
}
in.close();
%><%=buffer%>