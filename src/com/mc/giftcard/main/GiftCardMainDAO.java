package com.mc.giftcard.main;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.googlecode.ehcache.annotations.Cacheable;
import com.mc.web.MCMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class GiftCardMainDAO extends EgovAbstractMapper {
	public List menu_category(String param) {
		return list("main.menu_category", param);
	}
	public List menu_category_nation(String param) {
		return list("main.menu_category_nation", param);
	}
	public List menu_top(String param) {
		return list("main.menu_top", param);
	}
	public List menu_top_nation(String param) {
		return list("main.menu_top_nation", param);
	}
	public List menu_category_sido() {
		return list("main.menu_category_sido", null);
	}
	public List notice() {
		return list("main.notice", null);
	}
	@Cacheable(cacheName="popCache")
	public List top_popup() {
		return list("main.top_popup", null);
	}
	@Cacheable(cacheName="popCache")
	public List main_popup() {
		return list("main.main_popup", null);
	}
	@Cacheable(cacheName="popCache")
	public List banner_popup() {
		return list("main.banner_popup", null);
	}
	@Cacheable(cacheName="popCache")
	public List quick_popup() {
		return list("main.quick_popup", null);
	}
	@Cacheable(cacheName="popCache")
	public List layer_popup() {
		return list("main.layer_popup", null);
	}
	public List service() {
		return list("main.service", null);
	}
	public List mdpart() {
		return list("main.mdpart", null);
	}
	public List cooperation() {
		return list("main.cooperation", null);
	}
	public List part_view_list(Map param) {
		return list("main.part_view_list", param);
	}
	public List all_menu(String param) {
		return list("main.all_menu", param);
	}
	public List newPartsList() {
		return list("main.newPartsList", null);
	}
	@Cacheable(cacheName="popCache")
	public List mobile_popup() {
		return list("main.mobile_popup", null);
	}
	public Map mymenu_count(String param){
		return (Map) selectByPk("main.mymenu_count", param);
	}
	public Map mymenu_carallbaro_count(){
		return (Map) selectByPk("main.mymenu_carallbaro_count", null);
	}
	public List mobile_mdpart(Map param) {
		return list("main.mobile_mdpart", param);
	}

	@Cacheable(cacheName="autocompleteCache")
	public List autocomplete() {
		return list("main.autocomplete", null);
	}
	@Cacheable(cacheName="mainCache")
	public MCMap total_goods_cnt() {
		return (MCMap) selectByPk("part.pagination", null);
	}
	public List mobile_newpart(Map param) {
		return list("main.mobile_newpart", param);
	}
	public List mobile_newpart_total(Map param) {
		return list("main.mobile_newpart_total", param);
	}
	public Map getCartListCount(String param){
		return (Map) selectByPk("main.getCartListCount", param);
	}

	
}