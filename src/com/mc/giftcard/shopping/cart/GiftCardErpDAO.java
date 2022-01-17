package com.mc.giftcard.shopping.cart;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class GiftCardErpDAO extends EgovAbstractMapper {

	@Resource(name="sqlSession_erp")
	public void setSuperSqlMapClient(SqlSessionFactory sqlSession){
		super.setSqlSessionFactory(sqlSession);
	}
	
	public int erp_order(Map params){
		return update("erp.erp_order", params);
	}
	
	public int erp_order_upt(Map params){
		return update("erp.erp_order_upt", params);
	}
	
	public String erp_orderno(){
		return (String) selectByPk("erp.erp_orderno", null);
	}
	
	public int erp_orderdtl(Map params){
		return update("erp.erp_orderdtl", params);
	}
	
	public int erp_orderdtl_upt(Map params){
		return update("erp.erp_orderdtl_upt", params);
	}
	
	public int erp_orderdtl_cancel(Map params){
		return update("erp.erp_orderdtl_cancel", params);
	}
	
	public int erp_orderdtl_return(Map params){
		return update("erp.erp_orderdtl_return", params);
	}
	
	public int erp_orderdtl_refund(Map params){
		return update("erp.erp_orderdtl_refund", params);
	}
}
