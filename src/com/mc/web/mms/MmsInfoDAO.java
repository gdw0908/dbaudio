package com.mc.web.mms;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class MmsInfoDAO extends EgovAbstractMapper {
	public Map admin_cell() throws Exception{
		return (Map) selectByPk("mmsInfo.admin_cell", null);
	} 
	
	public Map cartno_cooperation_cell(Map<String, String> params) throws Exception{
		return (Map) selectByPk("mmsInfo.cartno_cooperation_cell", params);
	}
	
	public List orderno_cooperation_cell(Map<String, String> params) throws Exception{
		return list("mmsInfo.orderno_cooperation_cell", params);
	}
	
	public Map orderCell(Map<String, String> params) throws Exception{
		return (Map) selectByPk("mmsInfo.orderno_cell", params);
	}
}
