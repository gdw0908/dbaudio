package com.mc.giftcard.category;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mc.web.MCMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class GiftCardCategoryNameDAO extends EgovAbstractMapper {

	public Object getComInfoList(Map<String, String> params) {
		return list("category.getComInfoList", params);
	}
	
	public Object getCarNameList(Map<String, String> params) {
		return list("category.getCarNameList", params);
	}

	public Object getPageInfo(Map<String, String> params) {
		return selectByPk("category.getcarNamePageinfo", params);
	}
	
	public Object getViewList(Map<String, String> params) {
		return selectByPk("category.getcarNameViewList", params);
	}
	
	public void updateCarNameState(Map<String, String> params) {
		update("category.updateCarNameState", params);
	}
	
	public void getUpdateCarName(Map<String, String> params) {
		update("category.updateCarName", params);
	}
	
	public Object getCarNameLastestIndex(Map<String, String> params) {
		return selectByPk("category.getCarNameLastestIndex", params);
	}
	
	public void getCarNameInsertRow(Map<String, String> params) {
		insert("category.CarNameInsert", params);
	}
	
	public void CarNameInsertQry(Map<String, String> params) {
		insert("category.CarNameInsertQry", params);
	}
	
	
}
