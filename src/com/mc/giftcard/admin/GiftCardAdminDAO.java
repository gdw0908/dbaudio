package com.mc.giftcard.admin;

import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class GiftCardAdminDAO extends EgovAbstractMapper {
	public String order_cnt(Map params) {
		return (String) selectByPk("admin.order_cnt", params);
	}
	public String board_cnt(Map params) {
		return (String) selectByPk("admin.board_cnt", params);
	}
}