package com.mc.giftcard.goods.spell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mc.giftcard.code.GiftCardCodeDAO;
import com.mc.giftcard.shopping.cart.GiftCardErpDAO;
import com.mc.giftcard.shopping.order_state.GiftCardStateDAO;
import com.mc.web.Globals;
import com.mc.web.MCMap;
import com.mc.web.mms.MmsService;


@Service
public class GiftCardSpellService {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private Globals globals;
	
	@Autowired
	private GiftCardErpDAO erpDAO;

	@Autowired
	private GiftCardSpellDAO spellDAO;
	
	@Autowired
	private GiftCardCodeDAO codeDAO;

	@Autowired
	private GiftCardStateDAO stateDAO;
	
	@Autowired
	private MmsService mmsService;
	
	public List excelDown(Map params) throws Exception {
		return spellDAO.list(params);
	}
	
	public Map view(Map params) throws Exception {
		Map rstMap = new HashMap();
		rstMap.put("view", spellDAO.view(params));
		return rstMap;
	}
	
	public Map modify(Map params) throws Exception {
		Map rstMap = new HashMap();
		if("13".equals(params.get("status"))){	//교환신청중
			params.put("ch_dt", params.get("change_dt"));
		}else if("15".equals(params.get("status"))){	//교환제품 도착
			params.put("ch_d_dt", params.get("change_dt"));
		}else if("17".equals(params.get("status"))){	//교환제품 발송
			params.put("ch_b_dt", params.get("change_dt"));
		}else if("11".equals(params.get("status"))){	//반품제품도착일
			params.put("ban_d_dt", params.get("change_dt"));
			//재고 수량 복구
			MCMap info = spellDAO.pay_info(params);
			if(info != null && !"11".equals(info.getStrNull("status"))){
				params.put("item_seq", info.get("item_seq"));
				params.put("qty", info.get("qty"));
				//erp업데이트
				stateDAO.qty_cancel(params);
			}
		}else if("12".equals(params.get("status"))){	//반품완료일
			params.put("ban_c_dt", params.get("change_dt"));
			//재고 수량 복구
			MCMap info = spellDAO.pay_info(params);
			if(info != null && !"12".equals(info.getStrNull("status"))){
				params.put("item_seq", info.get("item_seq"));
				params.put("qty", info.get("qty"));
				stateDAO.qty_cancel(params);
				//erp업데이트
				if("partsmoa".equals(globals.getSTORE_ID())){
					erpDAO.erp_orderdtl_return(params);
				}
			}
		}else if("9".equals(params.get("status"))){	//반품배송일
			params.put("ban_b_dt", params.get("change_dt"));
		}else if("19".equals(params.get("status"))){	//환불신청일
			params.put("han_dt", params.get("change_dt"));
		}else if("21".equals(params.get("status"))){	//환불제품도착일
			params.put("han_d_dt", params.get("change_dt"));
		}else if("22".equals(params.get("status"))){	//환불완료일
			params.put("han_c_dt", params.get("change_dt"));
			if("partsmoa".equals(globals.getSTORE_ID())){
				erpDAO.erp_orderdtl_refund(params);
			}
		}else if("3".equals(params.get("status"))){	//결제취소신청일
			params.put("pay_c_dt", params.get("change_dt"));
		}else if("5".equals(params.get("status"))){	//결제취소완료일
			params.put("pay_e_dt", params.get("change_dt"));
			//재고 수량 복구
			MCMap info = spellDAO.pay_info(params);
			if(info != null && !"5".equals(info.getStrNull("status"))){
				params.put("item_seq", info.get("item_seq"));
				params.put("qty", info.get("qty"));
				//erp업데이트
				stateDAO.qty_cancel(params);
				if("partsmoa".equals(globals.getSTORE_ID())){
					erpDAO.erp_orderdtl_cancel(params);
				}
			}
		}else if("2".equals(params.get("status"))){	//주문취소일
			params.put("order_c_dt", params.get("change_dt"));
			//재고 수량 복구
			MCMap info = spellDAO.pay_info(params);
			if(info != null && !"2".equals(info.getStrNull("status"))){
				params.put("item_seq", info.get("item_seq"));
				params.put("qty", info.get("qty"));
				stateDAO.qty_cancel(params);
			}
		}else if("7".equals(params.get("status"))){	//배송중 문자 발송
			params.put("ba_dt", params.get("change_dt"));
			
			params.put("mmsMessage"," 상품의 발송이 시작되었습니다.");
			mmsService.userMMS_cartno(params);
		}
		
		spellDAO.modify(params);
		
		rstMap.put("rst", "1");
		return rstMap;
	}
}