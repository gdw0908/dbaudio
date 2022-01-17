package com.mc.web.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mc.web.MCMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class ScheduleDAO extends EgovAbstractMapper {
	
	public int cartCleaning(){
		return delete("cart.cartCleaning", null);
	}
	
	public int getYesterdayCalcCnt(int day) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("day", Integer.toString(day));
		return getSqlSession().selectOne("calculate.get_yesterday_calc_cnt", params);
	}
	
	public int getYesterdayCalcCnt(String date) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("date", date);
		return getSqlSession().selectOne("calculate.get_yesterday_calc_cnt", params);
	}
	
	public List<MCMap> selectCooperationList(int day){
		Map<String, String> params = new HashMap<String, String>();
		params.put("day", Integer.toString(day));
		List<MCMap> list = getSqlSession().selectList("calculate.select_cooperation_list", params);
		
		return list;
	}
	
	public List<MCMap> selectCooperationList(String date){
		Map<String, String> params = new HashMap<String, String>();
		params.put("date", date);
		List<MCMap> list = getSqlSession().selectList("calculate.select_cooperation_list", params);
		
		return list;
	}
	
	public List<MCMap> yesterdayCalcList(String type, int day){
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", type);
		params.put("day", Integer.toString(day));
		List<MCMap> list = getSqlSession().selectList("calculate.yesterday_calc_list", params);
		
		return list;
	}
	
	public List<MCMap> yesterdayCalcList(String type, String date){
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", type);
		params.put("date", date);
		List<MCMap> list = getSqlSession().selectList("calculate.yesterday_calc_list", params);
		
		return list;
	}
	
	public int insertNshCalculate(Map<String, String> params){
		return insert("calculate.insert_nsh_calculate", params);
	}
}
