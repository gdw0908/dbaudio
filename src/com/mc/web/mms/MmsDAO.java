package com.mc.web.mms;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class MmsDAO extends EgovAbstractMapper {
	@Resource(name="sqlSession_mms")
	public void setSuperSqlMapClient(SqlSessionFactory sqlSession){
		super.setSqlSessionFactory(sqlSession);
	}

	public String getNextval() {
		return selectByPk("mms.nextval",null).toString();
	}
	
	public int sms_write(Map<String, String> params){
		return update("mms.sms_insert", params);
	}
	
	public int mms_write(Map<String, String> params){
		return update("mms.mms_insert", params);
	}
	
	public Object list(Map<String, String> params) throws Exception{
		return list("mms.list", params);
	}
	
	public Map page_info(Map<String, String> params){
		return (Map) selectByPk("mms.page_info", params);
	}
}
