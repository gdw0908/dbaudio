package com.mc.giftcard.admin;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GiftCardAdminService {
	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private GiftCardAdminDAO adminDAO;

	public Map header(Map params) throws Exception {
		Map rst = new HashMap();
		params.put("status", "1");
		rst.put("cnt1", adminDAO.order_cnt(params));
		params.put("status", "3");
		rst.put("cnt2", adminDAO.order_cnt(params));
		params.put("status", "9");
		rst.put("cnt3", adminDAO.order_cnt(params));
		params.put("status", "19");
		rst.put("cnt4", adminDAO.order_cnt(params));
		params.put("board_seq", "4");
		rst.put("cnt5", adminDAO.board_cnt(params));
		params.put("board_seq", "6");
		rst.put("cnt6", adminDAO.board_cnt(params));
		params.put("board_seq", "8");
		rst.put("cnt7", adminDAO.board_cnt(params));
		return rst;
	}
}