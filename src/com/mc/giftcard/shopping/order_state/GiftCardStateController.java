package com.mc.giftcard.shopping.order_state;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mc.giftcard.shopping.cart.GiftCardCartService;
import com.mc.web.MCMap;
/**
 *
 * @Description : 
 * @ClassName   : com.mc.web.shopping.order_state.StateController.java
 * @Modification Information
 *
 * @author 이창기
 * @since 2015. 3. 19.
 * @version 1.0 *  
 * Copyright (C)  All right reserved.
 */
@Controller
@RequestMapping({"/giftcard/mypage/shopping/state/{index}.do", "/giftcard/popup/mypage/shopping/state/{index}.do", "/giftcard/mobile/mypage/shopping/state/{index}.do", "/mobile/popup/mypage/shopping/state/{index}.do"})
public class GiftCardStateController {
	Logger log = Logger.getLogger(this.getClass());
	
	@Value("#{config['agspay_path']}")
	private String AGSPAY_PATH;

	@Autowired
	private GiftCardStateService stateService;

	@Autowired
	private GiftCardCartService cartService;

	/**
	 * 
	 * Comment  : 회원 조회 
	 * @version : 1.0
	 * @tags    : @param model
	 * @tags    : @param request
	 * @tags    : @param response
	 * @tags    : @param session
	 * @tags    : @param cookie
	 * @tags    : @param params
	 * @tags    : @return
	 * @tags    : @throws Exception
	 * @date    : 2015. 3. 21.
	 *
	 */
	@RequestMapping(params="mode=list1")
	public String list1(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @CookieValue(value="cart_list", required=false, defaultValue="") String cookie, @RequestParam Map params) throws Exception{
		params.put("sessionid", session.getId());
		MCMap member = (MCMap) session.getAttribute("member");
		if(member != null){
			params.put("session_member_id", member.get("member_id"));
		}
		params.put("status_arr", "'99', '1', '6', '7', '8'");
		model.addAttribute("data", stateService.list(params));
		return "/giftcard/mypage/shopping/state/list";
	}
	
	@RequestMapping(params="mode=list2")
	public String list2(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @CookieValue(value="cart_list", required=false, defaultValue="") String cookie, @RequestParam Map params) throws Exception{
		params.put("sessionid", session.getId());
		MCMap member = (MCMap) session.getAttribute("member");
		if(member != null){
			params.put("session_member_id", member.get("member_id"));
		}
		params.put("status_arr", "'2','3','4','5','6','7','9','10','11','12','13','14','15','16','17','18'");
		model.addAttribute("data", stateService.list(params));
		return "/giftcard/mypage/shopping/state/list2";
	}
	
	@RequestMapping(params="mode=list3")
	public String list3(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @CookieValue(value="cart_list", required=false, defaultValue="") String cookie, @RequestParam Map params) throws Exception{
		params.put("sessionid", session.getId());
		MCMap member = (MCMap) session.getAttribute("member");
		if(member != null){
			params.put("session_member_id", member.get("member_id"));
		}
		params.put("status_arr", "'19', '20', '21', '22'");
		model.addAttribute("data", stateService.list(params));
		return "/giftcard/mypage/shopping/state/list3";
	}
	
	@RequestMapping(params="mode=list4")
	public String list4(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @CookieValue(value="cart_list", required=false, defaultValue="") String cookie, @RequestParam Map params) throws Exception{
		params.put("sessionid", session.getId());
		MCMap member = (MCMap) session.getAttribute("member");
		if(member != null){
			params.put("session_member_id", member.get("member_id"));
		}
		params.put("status_arr", "'23'");
		model.addAttribute("data", stateService.list(params));
		return "/giftcard/mypage/shopping/state/list4";
	}

	/**
	 * 
	 * Comment  : 비회원 조회
	 * @version : 1.0
	 * @tags    : @param model
	 * @tags    : @param request
	 * @tags    : @param response
	 * @tags    : @param session
	 * @tags    : @param cookie
	 * @tags    : @param params
	 * @tags    : @return
	 * @tags    : @throws Exception
	 * @date    : 2015. 3. 21.
	 *
	 */
	@RequestMapping(params="mode=nomember_view")
	public String nomember_view(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		
		if(stateService.check_nonmember(params) <= 0){
			request.setAttribute("message", "주문번호와 패스워드가 일치 하지 않습니다. 확인하시고 다시 이용하여 주시기 바랍니다.");
			return "message";
		}
		
		model.addAttribute("data", stateService.nomember_view(params));
		return "/giftcard/mypage/shopping/state/nomember_view";
	}

	@RequestMapping(params="mode=cancel_popup")
	public String cancel_popup(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		Map rst = new HashMap();
		rst.put("use_yn", "Y");
		rst.put("code_group_seq", "41");
		rst.put("condition", "val1");
		rst.put("keyword", "은행");
		model.addAttribute("bank",stateService.bankList(rst));
		model.addAttribute("item", stateService.cancel_view(params));
		return "/giftcard/mypage/shopping/state/cancel_popup";
	}
	
	@RequestMapping(params="mode=status_popup")
	public String status_popup(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		
		model.addAttribute("item", stateService.cancel_view(params));
		return "/giftcard/mypage/shopping/state/status_popup";
	}
	
	@RequestMapping(params="mode=pay_cancel")
	public String pay_cancel(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_id", member.get("member_id"));
		
		if(stateService.check_my_cart(params) <= 0){
			request.setAttribute("message", "정상적인 경로로 접근하여 주시기 바랍니다.");
			return "message";
		}

		Map rstMap = stateService.pay_cancel(params);
		
		if("-1".equals(rstMap.get("rst"))){
			request.setAttribute("message", rstMap.get("msg"));
			return "message";
		}
		return "/giftcard/mypage/shopping/state/cancel_result";
	}
	
	@RequestMapping(params="mode=nomember_pay_cancel")
	public String nomember_pay_cancel(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{

		if(stateService.check_nonmember(params) <= 0){
			request.setAttribute("message", "정상적인 경로로 접근하여 주시기 바랍니다.");
			return "message";
		}
		Map rstMap = stateService.pay_cancel(params);
		
		if("-1".equals(rstMap.get("rst"))){
			request.setAttribute("message", rstMap.get("msg"));
			return "message";
		}
		return "/giftcard/mypage/shopping/state/cancel_result";
	}

	@RequestMapping(params="mode=return_popup")
	public String return_popup(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		
		model.addAttribute("item", stateService.cancel_view(params));
		return "/giftcard/mypage/shopping/state/return_popup";
	}
	
	@RequestMapping(params="mode=return_send")
	public String return_send(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_id", member.get("member_id"));
		
		if(stateService.check_my_cart(params) <= 0){
			request.setAttribute("message", "정상적인 경로로 접근하여 주시기 바랍니다.");
			return "message";
		}

		Map rstMap = stateService.return_send(params);
		
		if("-1".equals(rstMap.get("rst"))){
			request.setAttribute("message", rstMap.get("msg"));
			return "message";
		}
		return "/giftcard/mypage/shopping/state/return_result";
	}
	
	@RequestMapping(params="mode=nomember_return_send")
	public String nomember_return_send(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		
		if(stateService.check_nonmember(params) <= 0){
			request.setAttribute("message", "정상적인 경로로 접근하여 주시기 바랍니다.");
			return "message";
		}

		Map rstMap = stateService.return_send(params);
		
		if("-1".equals(rstMap.get("rst"))){
			request.setAttribute("message", rstMap.get("msg"));
			return "message";
		}
		return "/giftcard/mypage/shopping/state/return_result";
	}
	
	@RequestMapping(params="mode=exchange_popup")
	public String exchange_popup(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		
		model.addAttribute("item", stateService.cancel_view(params));
		return "/giftcard/mypage/shopping/state/exchange_popup";
	}
	
	@RequestMapping(params="mode=exchange_send")
	public String exchange_send(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_id", member.get("member_id"));
		
		if(stateService.check_my_cart(params) <= 0){
			request.setAttribute("message", "정상적인 경로로 접근하여 주시기 바랍니다.");
			return "message";
		}

		Map rstMap = stateService.exchange_send(params);
		
		if("-1".equals(rstMap.get("rst"))){
			request.setAttribute("message", rstMap.get("msg"));
			return "message";
		}
		return "/giftcard/mypage/shopping/state/exchange_result";
	}
	
	@RequestMapping(params="mode=nomember_exchange_send")
	public String nomember_exchange_send(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		
		if(stateService.check_nonmember(params) <= 0){
			request.setAttribute("message", "정상적인 경로로 접근하여 주시기 바랍니다.");
			return "message";
		}

		Map rstMap = stateService.exchange_send(params);
		
		if("-1".equals(rstMap.get("rst"))){
			request.setAttribute("message", rstMap.get("msg"));
			return "message";
		}
		return "/giftcard/mypage/shopping/state/exchange_result";
	}
	
	@RequestMapping(params="mode=refunds_popup")
	public String refunds_popup(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		model.addAttribute("item", stateService.cancel_view(params));
		Map rst = new HashMap();
		rst.put("use_yn", "Y");
		rst.put("code_group_seq", "41");
		rst.put("condition", "val1");
		rst.put("keyword", "은행");
		model.addAttribute("bank",stateService.bankList(rst));
		return "/giftcard/mypage/shopping/state/refunds_popup";
	}
	
	@RequestMapping(params="mode=refunds_send")
	public String refunds_send(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_id", member.get("member_id"));
		
		if(stateService.check_my_cart(params) <= 0){
			request.setAttribute("message", "정상적인 경로로 접근하여 주시기 바랍니다.");
			return "message";
		}
		
		Map rstMap = stateService.refunds_send(params);
		
		if("-1".equals(rstMap.get("rst"))){
			request.setAttribute("message", rstMap.get("msg"));
			return "message";
		}
		return "/giftcard/mypage/shopping/state/refunds_result";
	}
	
	@RequestMapping(params="mode=track")
	public String track(ModelMap model, HttpServletRequest request, HttpServletResponse response, @RequestParam Map params) throws Exception{
		
		Map rstMap = stateService.track(params);
		
		if("-1".equals(rstMap.get("rst")) || "-2".equals(rstMap.get("rst"))){
			request.setAttribute("message", rstMap.get("msg"));
			request.setAttribute("redirect", "close");
			return "message";
		}
		model.addAllAttributes(rstMap);
		return "/giftcard/mypage/shopping/state/track";
	}
	
	@RequestMapping(params="mode=nomember_refunds_send")
	public String nomember_refunds_send(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		
		if(stateService.check_nonmember(params) <= 0){
			request.setAttribute("message", "정상적인 경로로 접근하여 주시기 바랍니다.");
			return "message";
		}
		
		Map rstMap = stateService.refunds_send(params);
		
		if("-1".equals(rstMap.get("rst"))){
			request.setAttribute("message", rstMap.get("msg"));
			return "message";
		}
		return "/giftcard/mypage/shopping/state/refunds_result";
	}
	
	@ResponseBody
	@RequestMapping(params="mode=order_cancel")
	@Transactional(rollbackFor = { Exception.class })
	public Map order_cancel(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_id", member.get("member_id"));
		
		if(stateService.check_my_cart(params) <= 0){
			Map rstMap = new HashMap();
			rstMap.put("msg", "정상적인 경로로 접근하여 주시기 바랍니다.");
			return rstMap;
		}

		return stateService.order_cancel(params);
	}
	
	
/* 모바일 시작 */
	
	@RequestMapping(params="mode=m_list1")
	public String m_list1(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @CookieValue(value="cart_list", required=false, defaultValue="") String cookie, @RequestParam Map params) throws Exception{
		params.put("sessionid", session.getId());
		MCMap member = (MCMap) session.getAttribute("member");
		if(member != null){
			params.put("session_member_id", member.get("member_id"));
		}
		params.put("status_arr", "'99', '1', '6', '7', '8'");
		model.addAttribute("data", stateService.list(params));
		return "/giftcard/mobile/mypage/shopping/state/list";
	}
	
	@RequestMapping(params="mode=m_list2")
	public String m_list2(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @CookieValue(value="cart_list", required=false, defaultValue="") String cookie, @RequestParam Map params) throws Exception{
		params.put("sessionid", session.getId());
		MCMap member = (MCMap) session.getAttribute("member");
		if(member != null){
			params.put("session_member_id", member.get("member_id"));
		}
		params.put("status_arr", "'2','3','4','5','6','7','9','10','11','12','13','14','15','16','17','18'");
		model.addAttribute("data", stateService.list(params));
		return "/mobile/mypage/shopping/state/list2";
	}
	
	@RequestMapping(params="mode=m_list3")
	public String m_list3(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @CookieValue(value="cart_list", required=false, defaultValue="") String cookie, @RequestParam Map params) throws Exception{
		params.put("sessionid", session.getId());
		MCMap member = (MCMap) session.getAttribute("member");
		if(member != null){
			params.put("session_member_id", member.get("member_id"));
		}
		params.put("status_arr", "'19', '20', '21', '22'");
		model.addAttribute("data", stateService.list(params));
		return "/mobile/mypage/shopping/state/list3";
	}
	
	@RequestMapping(params="mode=m_cancel_popup")
	public String m_cancel_popup(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		
		model.addAttribute("item", stateService.cancel_view(params));
		return "/mobile/mypage/shopping/state/cancel_popup";
	}
	
	@RequestMapping(params="mode=m_cancel_popup_result")
	public String m_cancel_popup_result(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		
		model.addAttribute("item", stateService.cancel_view(params));
		return "/mobile/mypage/shopping/state/cancel_popup_form";
	}
	
	@RequestMapping(params="mode=m_pay_cancel")
	public String m_pay_cancel(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_id", member.get("member_id"));
		
		if(stateService.check_my_cart(params) <= 0){
			request.setAttribute("message", "정상적인 경로로 접근하여 주시기 바랍니다.");
			return "message";
		}
		
		Map rstMap = stateService.m_pay_cancel(params);
		
		/*
		if("-1".equals(rstMap.get("rst"))){
			request.setAttribute("message", rstMap.get("msg"));
			return "message";
		}
		*/
		
		model.addAttribute("sayu", params.get("sayu"));
		model.addAttribute("item", stateService.cancel_view(params));
		
		return "/mobile/mypage/shopping/state/cancel_popup_result";
	}
	
	@RequestMapping(params="mode=m_exchange")
	public String m_exchange(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		
		model.addAttribute("item", stateService.cancel_view(params));
		return "/mobile/mypage/shopping/state/exchange";
	}
	
	@RequestMapping(params="mode=m_exchange_form")
	public String m_exchange_form(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		
		model.addAttribute("item", stateService.cancel_view(params));
		return "/mobile/mypage/shopping/state/exchange_form";
	}
	
	@RequestMapping(params="mode=m_exchange_result")
	public String m_exchange_result(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_id", member.get("member_id"));
		
		if(stateService.check_my_cart(params) <= 0){
			request.setAttribute("message", "정상적인 경로로 접근하여 주시기 바랍니다.");
			return "message";
		}

		Map rstMap = stateService.exchange_send(params);
		
		if("-1".equals(rstMap.get("rst"))){
			request.setAttribute("message", rstMap.get("msg"));
			return "message";
		}
		
		model.addAttribute("sayu", params.get("sayu"));
		model.addAttribute("item", stateService.cancel_view(params));
		return "/mobile/mypage/shopping/state/exchange_result";
	}
	
	@RequestMapping(params="mode=m_return")
	public String m_return(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		
		model.addAttribute("item", stateService.cancel_view(params));
		return "/mobile/mypage/shopping/state/return";
	}
	
	@RequestMapping(params="mode=m_return_form")
	public String m_return_form(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		
		model.addAttribute("item", stateService.cancel_view(params));
		return "/mobile/mypage/shopping/state/return_form";
	}
	
	@RequestMapping(params="mode=m_return_result")
	public String m_return_result(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_id", member.get("member_id"));
		
		if(stateService.check_my_cart(params) <= 0){
			request.setAttribute("message", "정상적인 경로로 접근하여 주시기 바랍니다.");
			return "message";
		}

		Map rstMap = stateService.return_send(params);
		
		if("-1".equals(rstMap.get("rst"))){
			request.setAttribute("message", rstMap.get("msg"));
			return "message";
		}
		
		model.addAttribute("sayu", params.get("sayu"));
		model.addAttribute("item", stateService.cancel_view(params));
		return "/mobile/mypage/shopping/state/exchange_result";
	}
	
	@RequestMapping(params="mode=m_refunds")
	public String m_refunds(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		
		model.addAttribute("item", stateService.cancel_view(params));
		return "/mobile/mypage/shopping/state/refunds";
	}
	
	@RequestMapping(params="mode=m_refunds_form")
	public String m_refunds_formm_refunds_form(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		model.addAttribute("item", stateService.cancel_view(params));
		Map rst = new HashMap();
		rst.put("use_yn", "Y");
		rst.put("code_group_seq", "41");
		rst.put("condition", "val1");
		rst.put("keyword", "은행");
		model.addAttribute("bank",stateService.bankList(rst));
		return "/mobile/mypage/shopping/state/refunds_form";
	}
	
	@RequestMapping(params="mode=m_refunds_result")
	public String m_refunds_send(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_id", member.get("member_id"));
		
		if(stateService.check_my_cart(params) <= 0){
			request.setAttribute("message", "정상적인 경로로 접근하여 주시기 바랍니다.");
			return "message";
		}
		
		Map rstMap = stateService.refunds_send(params);
		
		if("-1".equals(rstMap.get("rst"))){
			request.setAttribute("message", rstMap.get("msg"));
			return "message";
		}
		
		return "/mobile/mypage/shopping/state/refunds_result";
	}
	
	
	@RequestMapping(params="mode=m_nomember_view")
	public String m_nomember_view(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{
		
		if(stateService.check_nonmember(params) <= 0){
			request.setAttribute("message", "주문번호와 패스워드가 일치 하지 않습니다. 확인하시고 다시 이용하여 주시기 바랍니다.");
			return "message";
		}
		
		model.addAttribute("data", stateService.nomember_view(params));
		return "/mobile/mypage/shopping/state/nomember_view";
	}
	
	@RequestMapping(params="mode=m_nomember_pay_cancel")
	public String m_nomember_pay_cancel(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map params) throws Exception{

		if(stateService.check_nonmember(params) <= 0){
			request.setAttribute("message", "정상적인 경로로 접근하여 주시기 바랍니다.");
			return "message";
		}
		Map rstMap = stateService.m_pay_cancel(params);
		
		if("-1".equals(rstMap.get("rst"))){
			request.setAttribute("message", rstMap.get("msg"));
			return "message";
		}
		return "/mobile/mypage/shopping/state/cancel_popup_result";
	}
	
	/* 모바일 끝 */
	
}
