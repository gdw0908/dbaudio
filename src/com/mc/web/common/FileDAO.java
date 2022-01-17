package com.mc.web.common;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class FileDAO extends EgovAbstractMapper {

	
	public List list(Map<String, String> params){
		return list("attach.list", params);
	}
	
	public int insert(Map<String, String> params){
		return update("attach.insert", params);
	}
	
	public int delete_all(Map<String, String> params){
		return update("attach.delete_all", params);
	}
}
