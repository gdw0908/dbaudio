package com.mc.giftcard.login;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.mc.web.MCMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Service("giftCardLoginDAO")
public class GiftCardLoginDAO extends EgovAbstractMapper {
	
	public Map getMemberByIdPw(Map<String, String> params){
		return (Map) selectByPk("member.getMemberByIdPw", params);
	}
	
	public Map getMemberByIdPwUser(Map<String, String> params){
		return (Map) selectByPk("member.getMemberByIdPwUser", params);
	}

	public MCMap getArticle(String queryId, Map<String, String> params){
		MCMap article = (MCMap) selectByPk(queryId, params);
		
		return article;
	}
	
	public Map superLogin(Map<String, String> params){
		return (Map) selectByPk("member.superLogin", params);
	}
	
	public int updateMemberLastLogin(Map<String, String> params){
		return update("member.updateMemberLastLogin", params);
	}
}
