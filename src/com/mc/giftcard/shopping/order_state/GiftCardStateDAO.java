package com.mc.giftcard.shopping.order_state;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mc.web.MCMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class GiftCardStateDAO extends EgovAbstractMapper {
	
	public List list(Map params){
		return list("state.list", params);
	}
	
	public int check_nonmember(Map params){
		return (Integer) selectByPk("state.check_nonmember", params);
	}
	
	public int check_my_cart(Map params){
		return (Integer) selectByPk("state.check_my_cart", params);
	}

	public Map cancel_view(Map params) {
		return (Map) selectByPk("state.cancel_view", params);
	}
	
	public MCMap pay_info(Map params) {
		return (MCMap) selectByPk("state.pay_info", params);
	}
	
	public MCMap m_pay_info(Map params) {
		return (MCMap) selectByPk("state.m_pay_info", params);
	}
	
	public MCMap m_info_count(Map params) {
		return (MCMap) selectByPk("state.m_info_count", params);
	}

	public Map getOrderData(Map params) {
		return (Map) selectByPk("state.getOrderData", params);
	}
	
	public Map track(Map params) {
		return (Map) selectByPk("state.track", params);
	}
	
	public int pay_cancel(Map params) {
		return update("state.pay_cancel", params);
	}
	
	public int m_pay_cancel(Map params) {
		return update("state.m_pay_cancel", params);
	}
	
	public int return_send(Map params) {
		return update("state.return_send", params);
	}
	
	public int exchange_send(Map params) {
		return update("state.exchange_send", params);
	}
	
	public int order_cancel(Map params) {
		return update("state.order_cancel", params);
	}
	
	public int refunds_send(Map params) {
		return update("state.refunds_send", params);
	}

	public int qty_cancel(Map params) {
		return update("state.qty_cancel", params);
	}
}
