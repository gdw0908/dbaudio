<%@page import="java.util.Map"%>
<%@page import="com.mc.common.util.Util"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	Map params = (Map<String, String>) request.getAttribute("params");
	Map page_info = (Map<String, String>) request.getAttribute("page_info");
	int cpage = Integer.parseInt(Util.removePrimeNumber((String) params.get("cpage")));
	int rows = Integer.parseInt(Util.removePrimeNumber((String) params.get("rows")));
	int totalpage = Integer.parseInt(Util.removePrimeNumber((String) page_info.get("totalpage")));
	String condition = "";
	String keyword = "";
	if(params.get("condition") != null)
		condition = (String)params.get("condition");
	if(params.get("keyword") != null)
		keyword = (String) params.get("keyword");
	String servletPath = (String) request.getAttribute("servletPath");
%>              
<div class="paging">
	<span id="pagingWrap"> 
		<%
			int start = (cpage - 1) / 10 * 10 + 1;
			int end = start + 9 < totalpage? start + 9: totalpage;
			if(start - 1 > 0){
		%>
		<!-- <a class="p_first2" href="<%=servletPath %>?cpage=<%=start - 1 %>&amp;rows=<%=rows%>&amp;condition=<%=condition%>&amp;keyword=<%=keyword%>" onclick="return goPage(<%=start - 1 %>);">&lt;&lt;</a> -->
		<%
			}
			if(cpage > 1){
		%>
			<a class="p_first" href="<%=servletPath %>?cpage=<%=cpage - 1 %>&amp;rows=<%=rows%>&amp;condition=<%=condition%>&amp;keyword=<%=keyword%>" onclick="return goPage(<%=cpage - 1 %>);">이전</a> 
		<%
			}
			%>
			<%
			for(int i = start; i <=end ; i++){
				if(i != cpage){
		%>
					<a href="<%=servletPath %>?cpage=<%=i %>&amp;rows=<%=rows%>&amp;condition=<%=condition%>&amp;keyword=<%=keyword%>" onclick="return goPage(<%=i %>);"><%=i %></a>
		<%
					continue;
				}
		%>
				<b><a href="<%=servletPath %>?cpage=<%=i %>&amp;rows=<%=rows%>&amp;condition=<%=condition%>&amp;keyword=<%=keyword%>" onclick="return goPage(<%=i %>);"><%=i %></a></b>
		<%
			}
			%>
			<%
			if(cpage < totalpage){
		%>
				<a class="p_last" href="<%=servletPath %>?cpage=<%=cpage + 1 %>&amp;rows=<%=rows%>&amp;condition=<%=condition%>&amp;keyword=<%=keyword%>" onclick="return goPage(<%=cpage + 1 %>);">다음</a>
		<%
			}
			if(end  < totalpage){
		%>
				<!-- <a class="p_last2" href="<%=servletPath %>?cpage=<%=end + 1 %>&amp;rows=<%=rows%>&amp;condition=<%=condition%>&amp;keyword=<%=keyword%>" onclick="return goPage(<%=end + 1 %>);">&gt;&gt;</a> -->
		<%
			}
		%>
	</span>
</div>