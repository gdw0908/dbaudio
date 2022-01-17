package com.mc.giftcard.shopping.cart;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mc.web.MCMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class GiftCardCartDAO extends EgovAbstractMapper {
	
	public List cart_list(Map params){
		return list("cart.cart_list", params);
	}
	public List chk_cart_list(Map params){
		return list("cart.chk_cart_list", params);
	}
	public Map member_info(Map params){
		return (Map) selectByPk("cart.member_info", params);
	}
	public List member_basongji(Map params){
		return list("cart.member_basongji", params);
	}
	public int add_cart(Map params){
		return update("cart.add_cart", params);
	}
	public int add_cartQty(Map params){
		return update("cart.add_cartQty", params);
	}
	public int cart_stock(Map params){
		return (Integer) selectByPk("cart.cart_stock", params);
	}
	public int cart_qty_cartno(Map params){
		return (Integer) selectByPk("cart.cart_qty_cartno", params);
	}
	public int cart_stock_cartno(Map params){
		return (Integer) selectByPk("cart.cart_stock_cartno", params);
	}
	public int remove_cart(Map params){
		return update("cart.remove_cart", params);
	}
	public int updateQty(Map params){
		return update("cart.updateQty", params);
	}
	public int changeCod(Map params){
		return update("cart.changeCod", params);
	}
	public int minusStock(Map params){
		return update("cart.minusStock", params);
	}
	public int orderno_create(Map params){
		return update("cart.orderno_create", params);
	}
	public int order_insert(Map params){
		return update("cart.order_insert", params);
	}
	
	public String getCommission(Map params){
		return (String) selectByPk("cart.get_commission", params);
	}
	
	public int orderCart(Map params){
		return update("cart.orderCart", params);
	}
	public List orderResultList(Map params) {
		return list("cart.orderResultList", params);
	}
	public Map in_cart(Map params) {
		return (Map) selectByPk("cart.in_cart", params);
	}
	public Map orderResultInfo(Map params) {
		return (Map) selectByPk("cart.orderResultInfo", params);
	}
	public int actual_amount(Map params) {
		return (Integer) selectByPk("cart.actual_amount", params);
	}
	public List dir_cart(Map params) {
		return list("cart.dir_cart", params);
	}
	public MCMap inquiry_yn(Map params) {
		return (MCMap) selectByPk("cart.inquiry_yn", params);
	}
	
	public int virAcctResult1(Map params){
		return update("cart.virAcctResult1", params);
	}
	
	public int virAcctResult2(Map params){
		return update("cart.virAcctResult2", params);
	}
	public MCMap completePay(Map params) {
		return (MCMap) selectByPk("cart.completePay", params);
	}
	public List completePayList(Map params) {
		return list("cart.completePayList", params);
	}
}
