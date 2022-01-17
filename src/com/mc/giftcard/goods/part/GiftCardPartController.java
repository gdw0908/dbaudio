package com.mc.giftcard.goods.part;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * 
 *
 * @Description : 
 * @ClassName   : com.mc.web.goods.part.PartController.java
 * @Modification Information
 *
 * @author 이창기
 * @since 2015. 3. 11.
 * @version 1.0 *  
 * Copyright (C)  All right reserved.
 */
@Controller
public class GiftCardPartController { 
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private GiftCardPartService partService;
	
	@RequestMapping("/giftcard/goods/list.do")
	public String list(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map params) throws Exception{
		model.addAttribute("data", partService.list(params));
		return "giftcard/goods/list";
	}
	
	@RequestMapping("/giftcard/goods/view.do")
	public String view(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map params) throws Exception{
		Map data = partService.view(params);
		int stock_cnt = 0;
		if(data.get("view") != null){
			Map m = (Map)data.get("view");
			if(m.get("stock_num") != null){
				stock_cnt = Integer.valueOf((String)m.get("stock_num"));
			}			
		}
		if(stock_cnt <= 0){
			request.setAttribute("message", "이 상품의 재고가 더이상 없습니다.");
			return "message";
		}
		model.addAttribute("otherList", partService.other_list(params)); //함께 구매한
		model.addAttribute("data", data);
		return "giftcard/goods/view";
	}
	
	@RequestMapping("/giftcard/popup/goods/other_list.do")
	public String other_list(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map params) throws Exception{
		model.addAttribute("data", partService.other_list(params));
		return "giftcard/goods/other_list";
	}
	
	@RequestMapping("/giftcard/popup/goods/photo.do")
	public String photo(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map params) throws Exception{
		model.addAttribute("data", partService.photo(params));
		return "giftcard/goods/photo";
	}
}
