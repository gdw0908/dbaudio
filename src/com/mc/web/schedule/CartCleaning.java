package com.mc.web.schedule;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mc.web.MCMap;

/**
 *
 * @Description : 장바구니 정리
 * @ClassName   : com.mc.web.schedule.CartCleaning.java
 * @Modification Information
 *
 * @author 이창기
 * @since 2015. 3. 30.
 * @version 1.0 *  
 * Copyright (C)  All right reserved.
 */
@Controller
public class CartCleaning {
	protected final Logger logger = Logger.getLogger( this.getClass() );
	
	@Autowired
	private ScheduleDAO scheduleDAO;
	
	/**
	 * 
	 * Comment  : 일주일이상된 장바구니 삭제 
	 * @version : 1.0
	 * @tags    : 
	 * @date    : 2015. 3. 30.
	 *
	 */
	@Scheduled(cron="0 0 01 * * ?")//매일 새벽 1시 작업
	public void cartCleaning() {
		scheduleDAO.cartCleaning();
		logger.info("장바구니 정리 완료");
	}
	
	/**
	 * 
	 * Comment  : 정산
	 * @version : 1.0
	 * @tags    : 
	 * @date    : 2015. 4. 10.
	 *
	 */
	@Scheduled(cron = "0 1 0 * * ?")	//매일 새벽 12시 1분 반복(어제 매출현황 정산)
	@RequestMapping("/admin/yesterdayCalc.do")
	public void yesterdayCalc() throws Exception{
		
		
		
		/*for(int i = 24; i >= 1; i--){
			int days = i;
		}*/
		
		int days = 1;
		
		if(scheduleDAO.getYesterdayCalcCnt(days) > 0)
			return;
		
		List<MCMap> yesterdayCalclist = scheduleDAO.yesterdayCalcList("", days);
		List<MCMap> yesterdayCancellist = scheduleDAO.yesterdayCalcList("C", days);
		List<MCMap> yesterdayRefundclist = scheduleDAO.yesterdayCalcList("R", days);
		List<MCMap> yesterdayOrderlist = scheduleDAO.yesterdayCalcList("O", days);
		for(MCMap map : scheduleDAO.selectCooperationList(days)){
			String com_seq = map.getStr("com_seq");
			map.put("card", "0");
			map.put("card_cnt", "0");
			map.put("card_qty", "0");
			map.put("card_pg_commission", "0");
			map.put("iche", "0");
			map.put("iche_cnt", "0");
			map.put("iche_qty", "0");
			map.put("iche_pg_commission", "0");
			map.put("virtual", "0");
			map.put("virtual_cnt", "0");
			map.put("virtual_qty", "0");
			map.put("virtual_pg_commission", "0");
			map.put("hp", "0");
			map.put("hp_cnt", "0");
			map.put("hp_qty", "0");
			map.put("hp_pg_commission", "0");
			map.put("u_sum", "0");
			map.put("u_cnt", "0");
			map.put("u_qty", "0");
			map.put("u_m_commission", "0");
			map.put("c_sum", "0");
			map.put("c_cnt", "0");
			map.put("c_qty", "0");
			map.put("c_m_commission", "0");
			map.put("cancel_cash_sum", "0");
			map.put("cancel_cash_cnt", "0");
			map.put("cancel_cash_qty", "0");
			map.put("cancel_card_sum", "0");
			map.put("cancel_card_cnt", "0");
			map.put("cancel_card_qty", "0");
			map.put("refund_cash_sum", "0");
			map.put("refund_cash_cnt", "0");
			map.put("refund_cash_qty", "0");
			map.put("refund_card_sum", "0");
			map.put("refund_card_cnt", "0");
			map.put("refund_card_qty", "0");
			map.put("order_sum", "0");
			map.put("order_cnt", "0");
			map.put("order_qty", "0");
			map.put("total", "0");

			for(MCMap calc_map : yesterdayCalclist){
				if(com_seq.equals(calc_map.get("com_seq"))){
					map.put("card", calc_map.getStr("card"));
					map.put("card_cnt", calc_map.getStr("card_cnt"));
					map.put("card_qty", calc_map.getStr("card_qty"));
					map.put("card_pg_commission", calc_map.getStr("card_pg_commission"));
					map.put("iche", calc_map.getStr("iche"));
					map.put("iche_cnt", calc_map.getStr("iche_cnt"));
					map.put("iche_qty", calc_map.getStr("iche_qty"));
					map.put("iche_pg_commission", calc_map.getStr("iche_pg_commission"));
					map.put("virtual", calc_map.getStr("virtual"));
					map.put("virtual_cnt", calc_map.getStr("virtual_cnt"));
					map.put("virtual_qty", calc_map.getStr("virtual_qty"));
					map.put("virtual_pg_commission", calc_map.getStr("virtual_pg_commission"));
					map.put("hp", calc_map.getStr("hp"));
					map.put("hp_cnt", calc_map.getStr("hp_cnt"));
					map.put("hp_qty", calc_map.getStr("hp_qty"));
					map.put("hp_pg_commission", calc_map.getStr("hp_pg_commission"));
					map.put("u_sum", calc_map.getStr("u_sum"));
					map.put("u_cnt", calc_map.getStr("u_cnt"));
					map.put("u_qty", calc_map.getStr("u_qty"));
					map.put("u_m_commission", calc_map.getStr("u_m_commission"));
					map.put("c_sum", calc_map.getStr("c_sum"));
					map.put("c_cnt", calc_map.getStr("c_cnt"));
					map.put("c_qty", calc_map.getStr("c_qty"));
					map.put("c_m_commission", calc_map.getStr("c_m_commission"));
					map.put("total", calc_map.getStr("total"));
					break;
				}
			}
			
			for(MCMap cancel_map : yesterdayCancellist){
				if(com_seq.equals(cancel_map.get("com_seq"))){
					int cash_sum = cancel_map.getIntNumber("iche") + cancel_map.getIntNumber("virtual");
					int cash_cnt = cancel_map.getIntNumber("iche_cnt") + cancel_map.getIntNumber("virtual_cnt");
					int cash_qty = cancel_map.getIntNumber("iche_qty") + cancel_map.getIntNumber("virtual_qty");
					
					int card_sum = cancel_map.getIntNumber("card");
					int card_cnt = cancel_map.getIntNumber("card_cnt");
					int card_qty = cancel_map.getIntNumber("card_qty");
					
					map.put("cancel_cash_sum", cash_sum);
					map.put("cancel_cash_cnt", cash_cnt);
					map.put("cancel_cash_qty", cash_qty);
					map.put("cancel_card_sum", card_sum);
					map.put("cancel_card_cnt", card_cnt);
					map.put("cancel_card_qty", card_qty);
					break;
				}
			}
			
			for(MCMap refund_map : yesterdayRefundclist){
				if(com_seq.equals(refund_map.get("com_seq"))){
					int cash_sum = refund_map.getIntNumber("iche") + refund_map.getIntNumber("virtual");
					int cash_cnt = refund_map.getIntNumber("iche_cnt") + refund_map.getIntNumber("virtual_cnt");
					int cash_qty = refund_map.getIntNumber("iche_qty") + refund_map.getIntNumber("virtual_qty");
					
					int card_sum = refund_map.getIntNumber("card");
					int card_cnt = refund_map.getIntNumber("card_cnt");
					int card_qty = refund_map.getIntNumber("card_qty");
					
					map.put("refund_cash_sum", cash_sum);
					map.put("refund_cash_cnt", cash_cnt);
					map.put("refund_cash_qty", cash_qty);
					map.put("refund_card_sum", card_sum);
					map.put("refund_card_cnt", card_cnt);
					map.put("refund_card_qty", card_qty);
					break;
				}
			}
			
			for(MCMap order_map : yesterdayOrderlist){
				if(com_seq.equals(order_map.get("com_seq"))){
					int sum = order_map.getIntNumber("iche") + order_map.getIntNumber("virtual") + order_map.getIntNumber("card");
					int cnt = order_map.getIntNumber("iche_cnt") + order_map.getIntNumber("virtual_cnt") + order_map.getIntNumber("card_cnt");
					int qty = order_map.getIntNumber("iche_qty") + order_map.getIntNumber("virtual_qty") + order_map.getIntNumber("card_qty");
					
					map.put("order_sum", sum);
					map.put("order_cnt", cnt);
					map.put("order_qty", qty);
					break;
				}
			}
			
			scheduleDAO.insertNshCalculate(map);
		}
		
	}
}
