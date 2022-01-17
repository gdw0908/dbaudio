package com.mc.web.test;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mc.web.MCMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class TestDAO extends EgovAbstractMapper {
	public MCMap test(Map<String, String> params) {
		return ((MCMap) selectByPk("Test.test", params));
	}

	public void add(Map<String, String> params) {
		insert("Test.add", params);
	}
	
	public List thumbList(){
		return list("Test.carallbaro_uuid", null);
	}
}