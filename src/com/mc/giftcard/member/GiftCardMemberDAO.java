package com.mc.giftcard.member;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class GiftCardMemberDAO extends EgovAbstractMapper {
	
	public Map view(Map<String, String> params){
		return (Map) selectByPk("member.view", params);
	}
	
	public List basongjiList(Map<String, String> params){
		return list("member.basongjiList", params);
	}
	
	public int write(Map<String, String> params){
		return update("member.write", params);
	}
	
	public int insertBasongji(Map<String, String> params){
		return update("member.insertBasongji", params);
	}
	
	public int modify(Map<String, String> params){
		return update("member.modify", params);
	}
	
	public int basongjiRemove(Map<String, String> params){
		return delete("member.basongjiRemove", params);
	}
	
	public int del(Map<String, String> params){
		return update("member.del", params);
	}
	
	public int passwordInit(Map<String, String> params){
		return update("member.passwordInit", params);
	}
	
	public void insertMemberData(Map<String, String> params) {
		insert("member.insertMemberData",  params);
	}
	
	public Map findIDPasswdList(Map<String, String> params){
		return (Map) selectByPk("member.findIDPasswdList", params);
	}
	
	public Map findPwPasswdList(Map<String, String> params){
		return (Map) selectByPk("member.findPwPasswdList", params);
	}
	
}
