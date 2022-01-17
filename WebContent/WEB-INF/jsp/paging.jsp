<%@page import="java.util.Map"%>
<%@page import="com.mc.common.util.Util"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	Map params = (Map<String, String>) request.getAttribute("params");
	Map page_info = (Map<String, String>) request.getAttribute("page_info");
	int cpage = Integer.parseInt(Util.removePrimeNumber((String) params.get("cpage")));
	int totalpage = Integer.parseInt(Util.removePrimeNumber((String) page_info.get("totalpage")));
	String servletPath = (String) request.getAttribute("servletPath");
%>

<div class="paging">
<span id="pagingWrap">
		<%
			int start = (cpage - 1) / 10 * 10 + 1;
			int end = start + 9 < totalpage? start + 9: totalpage;
			if(start - 1 > 0){
		%>
		<a class="p_first" href="<%=servletPath %>?cpage=<%=start - 1 %>&amp;codeno=${article.codeno }&amp;condition=${params.condition }&amp;keyword=${params.keyword } &amp;parttyp=${params.parttyp }&amp;sellinfo=${params.sellinfo }&amp;delyn=${params.delyn }&amp;show=${params.show}&amp;read=true&amp;carmakerseq=${params.carmakerseq}&amp;carmodelseq=${params.carmodelseq}&amp;cargradeseq=${params.cargradeseq}&amp;caryyyy=${params.caryyyy}&amp;color=${params.color}&amp;grade=${params.grade}&amp;part1=${params.part1}&amp;part2=${params.part2}&amp;part3=${params.part3}">
		<!-- <img src="/images/main/btn_bbs_front.png" title="맨 앞으로" /> -->
		&lt;&lt
		</a>	
		<%
			}
			if(cpage > 1){
		%>
				<a class="p_first2" href="<%=servletPath %>?cpage=<%=cpage - 1 %>&amp;codeno=${article.codeno }&amp;condition=${params.condition }&amp;keyword=${params.keyword }&amp;parttyp=${params.parttyp }&amp;sellinfo=${params.sellinfo }&amp;delyn=${params.delyn }&amp;show=${params.show}&amp;read=true&amp;carmakerseq=${params.carmakerseq}&amp;carmodelseq=${params.carmodelseq}&amp;cargradeseq=${params.cargradeseq}&amp;caryyyy=${params.caryyyy}&amp;color=${params.color}&amp;grade=${params.grade}&amp;part1=${params.part1}&amp;part2=${params.part2}&amp;part3=${params.part3}">
				<!-- <img src="/images/main/btn_bbs_prev.png" title="이전으로" />  -->
				&lt;
				</a>
		<%
			}
			%>
			<%
			for(int i = start; i <=end ; i++){
				if(i != cpage){
		%>
					<a href="<%=servletPath %>?cpage=<%=i %>&amp;codeno=${article.codeno }&amp;condition=${params.condition }&amp;keyword=${params.keyword }&amp;parttyp=${params.parttyp }&amp;sellinfo=${params.sellinfo }&amp;delyn=${params.delyn }&amp;show=${params.show}&amp;read=true&amp;carmakerseq=${params.carmakerseq}&amp;carmodelseq=${params.carmodelseq}&amp;cargradeseq=${params.cargradeseq}&amp;caryyyy=${params.caryyyy}&amp;color=${params.color}&amp;grade=${params.grade}&amp;part1=${params.part1}&amp;part2=${params.part2}&amp;part3=${params.part3}" ><%=i %></a>
		<%
					continue;
				}
		%>
				<a class="on" href="<%=servletPath %>?cpage=<%=i %>&amp;codeno=${article.codeno }&amp;condition=${params.condition }&amp;keyword=${params.keyword }&amp;parttyp=${params.parttyp }&amp;sellinfo=${params.sellinfo }&amp;delyn=${params.delyn }&amp;show=${params.show}&amp;read=true&amp;carmakerseq=${params.carmakerseq}&amp;carmodelseq=${params.carmodelseq}&amp;cargradeseq=${params.cargradeseq}&amp;caryyyy=${params.caryyyy}&amp;color=${params.color}&amp;grade=${params.grade}&amp;part1=${params.part1}&amp;part2=${params.part2}&amp;part3=${params.part3}"><%=i %></a>
		<%
			}
			%>
			<%
			if(cpage < totalpage){
		%>
				<a class="p_last" href="<%=servletPath %>?cpage=<%=cpage + 1 %>&amp;codeno=${article.codeno }&amp;condition=${params.condition }&amp;keyword=${params.keyword }&amp;parttyp=${params.parttyp }&amp;sellinfo=${params.sellinfo }&amp;delyn=${params.delyn }&amp;show=${params.show}&amp;read=true&amp;carmakerseq=${params.carmakerseq}&amp;carmodelseq=${params.carmodelseq}&amp;cargradeseq=${params.cargradeseq}&amp;caryyyy=${params.caryyyy}&amp;color=${params.color}&amp;grade=${params.grade}&amp;part1=${params.part1}&amp;part2=${params.part2}&amp;part3=${params.part3}">
				<!-- <img src="/images/main/btn_bbs_next.png" title="다음으로" /> -->
				&gt;
				</a>
		<%
			}
			if(end  < totalpage){
		%>
				<a class="p_last2" href="<%=servletPath %>?cpage=<%=end + 1 %>&amp;codeno=${article.codeno }&amp;condition=${params.condition }&amp;keyword=${params.keyword }&amp;parttyp=${params.parttyp }&amp;sellinfo=${params.sellinfo }&amp;delyn=${params.delyn }&amp;show=${params.show}&amp;read=true&amp;carmakerseq=${params.carmakerseq}&amp;carmodelseq=${params.carmodelseq}&amp;cargradeseq=${params.cargradeseq}&amp;caryyyy=${params.caryyyy}&amp;color=${params.color}&amp;grade=${params.grade}&amp;part1=${params.part1}&amp;part2=${params.part2}&amp;part3=${params.part3}">
				<!-- <img src="/images/main/btn_bbs_last.png" title="맨 뒤로" />  -->
				&gt;&gt;
				</a>
		<%
			}
		%>
		</span>
                        </div>