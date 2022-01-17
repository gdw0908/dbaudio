package com.mc.giftcard.goods.spell;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mc.web.MCMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class GiftCardSpellDAO extends EgovAbstractMapper {
	
	public Map view(Map<String, String> params){
		return (Map) selectByPk("spell.view", params);
	}
	
	public int modify(Map<String, String> params){
		return update("spell.modify", params);
	}

	public List list(Map params) {
		return list("spell.list", params);
	}
	
	public MCMap pay_info(Map params) {
		return (MCMap) selectByPk("spell.pay_info", params);
	}
}
