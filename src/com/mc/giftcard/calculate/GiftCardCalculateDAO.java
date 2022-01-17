package com.mc.giftcard.calculate;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mc.web.MCMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class GiftCardCalculateDAO extends EgovAbstractMapper {
	
	public List<MCMap> select_nsh_calculate_list(Map<String, String> params){
		List<MCMap> list = getSqlSession().selectList("calculate.select_nsh_calculate_list", params);
		
		return list;
	}
	
	public List<MCMap> select_nsh_calculate_group(Map<String, String> params){
		List<MCMap> list = getSqlSession().selectList("calculate.select_nsh_calculate_group", params);
		
		return list;
	}
	
	public List<MCMap> select_com_list(Map<String, String> params){
		List<MCMap> list = getSqlSession().selectList("calculate.select_com_list", params);
		
		return list;
	}
	
	public List<MCMap> select_com_list_group(Map<String, String> params){
		List<MCMap> list = getSqlSession().selectList("calculate.select_com_list_group", params);
		
		return list;
	}
	
	
	
	public MCMap select_nsh_calculate_sum(Map<String, String> params){
		return getSqlSession().selectOne("calculate.select_nsh_calculate_sum", params);
	}
	
	public List<MCMap> get_week_data(Map<String, String> params){
		List<MCMap> list = getSqlSession().selectList("calculate.get_week_data", params);
		
		return list;
	}
	
	public List<MCMap> get_deadline_list(){
		List<MCMap> list = getSqlSession().selectList("calculate.get_deadline_list");
		
		return list;
	}
	
	public void insert_nsh_clulate_confirm(Map<String, String> params){
		insert("calculate.insert_nsh_clulate_confirm", params);
		update("calculate.update_confirm", params);
	}
	
	public void rollback_nsh_clulate_confirm(Map<String, String> params){
		update("calculate.delete_nsh_clulate_confirm", params);
		delete("calculate.update_confirm_rollback", params);
	}
	
	public List<MCMap> confirm_list(Map<String, String> params){
		List<MCMap> list = getSqlSession().selectList("calculate.confirm_list", params);
		
		return list;
	}
	
	public MCMap confirm_page_info(Map<String, String> params){
		return getSqlSession().selectOne("calculate.confirm_page_info", params);
	}
	
	public MCMap select_confirm(Map<String, String> params){
		return getSqlSession().selectOne("calculate.select_confirm", params);
	}
	
	public List<MCMap> statistics(Map<String, String> params){
		List<MCMap> list = getSqlSession().selectList("calculate.statistics", params);
		
		return list;
	}
	
	public void delete_nsh_clulate(Map<String, String> params){
		update("calculate.delete_nsh_clulate", params);
	}
	
	public List<MCMap> select_confirm_detail(Map<String, String> params){
		List<MCMap> list = getSqlSession().selectList("calculate.view_detail", params);
		return list;
	}

	public List<MCMap> select_confirm_detail_excel(Map<String, String> params) {
		List<MCMap> list = getSqlSession().selectList("calculate.view_detail_excel", params);
		return list;
	}
}
