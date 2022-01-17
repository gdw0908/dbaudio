package com.mc.giftcard.goods.part;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class GiftCardPartDAO extends EgovAbstractMapper {
	
	public List list(Map<String, String> params) {
		return list("part.list", params);
	}
	
	public Map pagination(Map<String, String> params) {
		return (Map) selectByPk("part.pagination", params);
	}
	
	public List other_list(Map<String, String> params) {
		return list("part.other_list", params);
	}
	
	public List photo(Map<String, String> params) {
		return list("part.photo", params);
	}
	
	public Map other_pagination(Map<String, String> params) {
		return (Map) selectByPk("part.other_pagination", params);
	}
	
	public Map view(Map<String, String> params){
		return (Map) selectByPk("part.view", params);
	}
	
	public int write(Map<String, String> params){
		return update("part.write", params);
	}
	
	public int modify(Map<String, String> params){
		return update("part.modify", params);
	}
	
	public int del(Map<String, String> params){
		return update("part.del", params);
	}
	
	public List carmaker(Map<String, String> params) {
		return list("old_code.carmaker", params);
	}
	
	public List carmodel(Map<String, String> params) {
		return list("old_code.carmodel", params);
	}
	
	public List cargrade(Map<String, String> params) {
		return list("old_code.cargrade", params);
	}

	public int updateCommonRate(Map params) {
		return update("cooperation.updateCommonRate", params);
	}
}
