package com.mc.giftcard.category;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.When;
import com.mc.common.util.StringUtil;
import com.mc.web.MCMap;

@Controller
@RequestMapping("/giftcard/admin/goods/inventory/inventory_cate3.do")
public class GiftCardCategoryNameController {
	

	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private GiftCardCategoryNameDAO CategoryCarNameDAO;
	
	@RequestMapping(params="!method")
	public String _default(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		return list(params, request, session);
	}
	
	@RequestMapping(params="method=list")
	public String list(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		if(StringUtil.isEmptyByParam(params, "cpage")) params.put("cpage", "1");
		params.put("rows", "10");
		
		MCMap view = (MCMap) CategoryCarNameDAO.getViewList(params);
		MCMap lastest_seq = (MCMap) CategoryCarNameDAO.getCarNameLastestIndex(params);
		
		request.setAttribute("lastest_seq", lastest_seq);
		request.setAttribute("view", view);
		request.setAttribute("page_info", CategoryCarNameDAO.getPageInfo(params));
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("comInfo", CategoryCarNameDAO.getComInfoList(params));
		request.setAttribute("list", CategoryCarNameDAO.getCarNameList(params));
		request.setAttribute("params", params);

		return "/giftcard/admin/goods/inventory/inventory_cate3";
	}
	
	
	@RequestMapping(params="method=insert")
	@TriggersRemove(cacheName="mainCache", removeAll=true, when=When.AFTER_METHOD_INVOCATION)
	public String insert(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		CategoryCarNameDAO.getCarNameInsertRow(params);

		if(StringUtil.isEmptyByParam(params, "cpage")) params.put("cpage", "1");
		params.put("rows", "10");
		
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("page_info", CategoryCarNameDAO.getPageInfo(params));
		request.setAttribute("params", params);
		
		request.setAttribute("message", "등록되었습니다.");
		request.setAttribute("redirect", "/giftcard/admin/goods/inventory/inventory_cate3.do");
		return "message";
	}
	
	
	@RequestMapping(params="method=delete")
	@TriggersRemove(cacheName="mainCache", removeAll=true, when=When.AFTER_METHOD_INVOCATION)
	public String delete(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		CategoryCarNameDAO.updateCarNameState(params);
		
		if(StringUtil.isEmptyByParam(params, "cpage")) params.put("cpage", "1");
		params.put("rows", "10");
		
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("page_info", CategoryCarNameDAO.getPageInfo(params));
		request.setAttribute("params", params);
		
		request.setAttribute("message", "삭제되었습니다.");
		request.setAttribute("redirect", "/giftcard/admin/goods/inventory/inventory_cate3.do");
		return "message";
		
	}
	
	@RequestMapping(params="method=update")
	@TriggersRemove(cacheName="mainCache", removeAll=true, when=When.AFTER_METHOD_INVOCATION)
	public String update(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		if(params.get("qry_method").equals("qry_update"))
		
			CategoryCarNameDAO.getUpdateCarName(params);
		else
			CategoryCarNameDAO.CarNameInsertQry(params);
			
		if(StringUtil.isEmptyByParam(params, "cpage")) params.put("cpage", "1");
		params.put("rows", "10");
		
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("page_info", CategoryCarNameDAO.getPageInfo(params));
		request.setAttribute("params", params);
		
		request.setAttribute("message", "수정되었습니다.");
		request.setAttribute("redirect", "/giftcard/admin/goods/inventory/inventory_cate3.do");
		return "message";
	}


}
