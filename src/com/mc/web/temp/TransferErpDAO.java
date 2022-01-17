package com.mc.web.temp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import com.mc.web.MCMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class TransferErpDAO extends EgovAbstractMapper {
	
	@Resource(name="sqlSession_erp")
	public void setSuperSqlMapClient(SqlSessionFactory sqlSession){
		super.setSqlSessionFactory(sqlSession);
	}
	
	public List<MCMap> list(Map<String, String> params){
		return list("transfer.list", params);
	}
	
	public MCMap ready_insunitem(Map<String, String> params){
		return (MCMap) selectByPk("transfer.ready_insunitem", params);
	}
}
