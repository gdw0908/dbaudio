package com.mc.web.popupzone;

import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class PopupzoneDAO extends EgovAbstractMapper {

	public Map view(Map<String, String> params){
		return (Map) selectByPk("popupzone.view", params);
	}
	
	public int write(Map<String, String> params){
		return update("popupzone.insert", params);
	}
	
	public int modify(Map<String, String> params){
		return update("popupzone.update", params);
	}
	
	public int del(Map<String, String> params){
		return update("popupzone.delete", params);
	}
	
	public int mobileInit(){
		return update("popupzone.mobile_init", null);
	}
	
}
