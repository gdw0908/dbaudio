package com.mc.web.common;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mc.web.MCMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class JsonDAO extends EgovAbstractMapper {
	public List getList(String queryId, Map<String, String> params){
		return list(queryId, params);
	}
	
	public MCMap getRequest(String queryId, Map<String, String> params){
		return (MCMap) selectByPk(queryId, params);
	}	
}
