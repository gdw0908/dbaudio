package com.mc.giftcard.shopping.order_state;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mc.common.util.Encryption;
import com.mc.common.util.StringUtil;
import com.mc.common.util.Util;
import com.mc.giftcard.shopping.cart.GiftCardCartDAO;
import com.mc.giftcard.shopping.cart.GiftCardErpDAO;
import com.mc.web.Globals;
import com.mc.web.MCMap;
import com.mc.web.mms.MmsInfoDAO;
import com.mc.web.mms.MmsService;

import aegis.pgclient.PgClientBean40;
import kr.co.allthegate.mobile.AGSMobile;
import net.sf.json.JSONObject;

@Service
public class GiftCardStateService {
	
	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private GiftCardStateDAO stateDAO;
	
	@Autowired
	private GiftCardErpDAO erpDAO;
	
	@Autowired
	private GiftCardCartDAO cartDAO;
	
	@Autowired
	private Globals globals;
	
	@Autowired
	private MmsInfoDAO mmsInfoDAO;
	
	@Autowired
	private MmsService mmsService;
	
	public Map list(Map params) throws Exception {
		Map rstMap = new HashMap();
		rstMap.put("list", stateDAO.list(params));
		return rstMap;
	}

	public Map nomember_view(Map params) {
		Map rstMap = new HashMap();
		rstMap.put("list", cartDAO.orderResultList(params));
		rstMap.put("resultInfo", cartDAO.orderResultInfo(params));
		return rstMap;
	}

	public int check_nonmember(Map params) {
		params.put("passwd", Encryption.stringEncryption((String) params.get("passwd"), ""));
		return stateDAO.check_nonmember(params);
	}
	
	public int check_my_cart(Map params) {
		return stateDAO.check_my_cart(params);
	}

	public Map cancel_view(Map params) {
		return stateDAO.cancel_view(params);
	}
	
	public Map pay_cancel(Map params) {
		Map rstMap = new HashMap();
		MCMap info = stateDAO.pay_info(params);
		if(info == null){	//결제정보가 없는경우
			rstMap.put("rst", "-1");
			rstMap.put("msg", "결재정보가 없습니다. 확인후 다시 시도해 주시기 바랍니다.");
			return rstMap;
		}else if("card".equals(info.getStrNull("paytyp"))){//신용카드만 즉시 취소 가능
			/**********************************************************************************************
			*
			* 파일명 : AGS_cancel_ing.jsp
			* 최종수정일자 : 2007/04/25
			* 
			* 올더게이트 플러그인에서 리턴된 데이타를 받아서 소켓취소요청을 합니다.
			*
			* Copyright  AEGIS ENTERPRISE.Co.,Ltd. All rights reserved.
			*
			**********************************************************************************************/ 

			/****************************************************************************
			*
			* [1] 올더게이트 결제취소시 사용할 로컬 통신서버 IP/Port 번호
			*
			* ENCTYPE	: 0:안심클릭,일반결제 2:ISP
			* LOCALADDR	: PG서버와 통신을 담당하는 암호화Process가 위치해 있는 IP 
			* LOCALPORT	: 포트
			*
			****************************************************************************/

			String LOCALADDR = "220.85.12.3";
			int LOCALPORT = 29760;
			String ENCTYPE = "0";
			 
			/****************************************************************************
			*
			* 승인/취소에 사용될 클래스 객체 생성
			*
			****************************************************************************/

			PgClientBean40 agspay = new PgClientBean40( LOCALADDR, LOCALPORT );

			/****************************************************************************
			*
			* [2] AGS_cancel.html 로 부터 넘겨받을 데이타
			*
			****************************************************************************/

			/*공통사용*/
			agspay.setValue("AgsPayHome", globals.getAGSPAY_PATH());		//올더게이트 결제설치 디렉토리 (상점에 맞게 수정)
			agspay.setValue("log","true");							//true : 로그기록, false : 로그기록안함.
			agspay.setValue("logLevel","INFO");						//로그레벨 : DEBUG, INFO, WARN, ERROR, FATAL (해당 레벨이상의 로그만 기록됨)
			agspay.setValue("Type", "Cancel");						//고정값(수정불가)

			agspay.setValue("StoreId", globals.getSTORE_ID());		//상점아이디
			agspay.setValue("AuthTy", (String) info.get("paytyp"));			//결제형태
			agspay.setValue("SubTy", (String) info.get("subty"));			//서브결제형태
			agspay.setValue("rApprNo",(String) info.get("rapprno"));			//승인번호
			agspay.setValue("rApprTm",(String) info.get("rapprtm"));			//승인일자
			agspay.setValue("rDealNo",(String) info.get("rdealno"));			//거래번호
			agspay.setValue("PartCancelAmt", (String)info.get("cancel_amt"));	//부분취소금액

			/****************************************************************************
			*
			* [4] 올더게이트 결제서버로 결제를 요청합니다.
			*
			****************************************************************************/
			agspay.startPay();

			/****************************************************************************
			*
			* [5] 취소요청결과에 따른 상점DB 저장 및 기타 필요한 처리작업을 수행하는 부분입니다.
			*
			* 신용카드결제 취소결과가 정상적으로 수신되었으므로 DB 작업을 할 경우 
			* 결과페이지로 데이터를 전송하기 전 이부분에서 하면된다.
			*
			* 여기서 DB 작업을 해 주세요.
			* 취소성공여부 : agspay.getResult("rCancelSuccYn") (성공:y 실패:n)
			* 취소결과메시지 : agspay.getResult("rCancelResMsg")
			*
			****************************************************************************/		
				
			if(agspay.getResult("rCancelSuccYn").equals("y") )
			{ 
				// 결제취소에 따른 처리부분
				logger.info("신용카드 승인취소가 성공처리되었습니다. [" + agspay.getResult("rCancelSuccYn")+"]"+ agspay.getResult("rCancelResMsg") );
				stateDAO.pay_cancel(params);
				//재고 수량 복구
				params.put("item_seq", info.get("item_seq"));
				params.put("qty", info.get("qty"));
				stateDAO.qty_cancel(params);
				if("partsmoa".equals(globals.getSTORE_ID())){
					erpDAO.erp_orderdtl_cancel(params);
				}
				
				rstMap.put("rst", "1");
				//문자 보내기
				params.put("mmsMessage", " 상품의 주문 및 결제가 취소 되었습니다.");
				mmsService.acMMS_cartno(params);
				return rstMap;
			}
			else
			{
				// 결제취소실패에 따른 상점처리부분
				logger.info("승인취소가 실패처리되었습니다. [" + agspay.getResult("rCancelSuccYn")+"]"+ agspay.getResult("rCancelResMsg") );
				rstMap.put("rst", "-1");
				rstMap.put("msg", "승인취소가 실패처리되었습니다. [" + agspay.getResult("rCancelSuccYn")+"]"+ agspay.getResult("rCancelResMsg") );
				return rstMap;
			}
		}else{
			// 결제취소요청
			stateDAO.m_pay_cancel(params);
			//문자 보내기
			params.put("mmsMessage", " 상품의 주문 및 결제 취소를 요청하였습니다.");
			mmsService.acMMS_cartno(params);
			//재고 수량 복구
//			params.put("item_seq", info.get("item_seq"));
//			params.put("qty", info.get("qty"));
//			stateDAO.qty_cancel(params);
			rstMap.put("rst", "1");
			return rstMap;
		}
	}
	
	public Map return_send(Map params) {
		Map rstMap = new HashMap();
		rstMap.put("rst", stateDAO.return_send(params));
		return rstMap;
	}
	
	public Map exchange_send(Map params) {
		Map rstMap = new HashMap();
		rstMap.put("rst", stateDAO.exchange_send(params));
		return rstMap;
	}
	
	public Map order_cancel(Map params) {
		Map rstMap = new HashMap();
		stateDAO.order_cancel(params);
		//재고 수량 복구
		MCMap info = stateDAO.pay_info(params);
		params.put("item_seq", info.get("item_seq"));
		params.put("qty", info.get("qty"));
		stateDAO.qty_cancel(params);
		rstMap.put("rst", "1");
		return rstMap;
	}
	
	public Map refunds_send(Map params) {
		Map rstMap = new HashMap();
		rstMap.put("rst", stateDAO.refunds_send(params));
		/* 환불요청 문자 발송*/
		params.put("mmsMessage", " 상품의 환불신청이 접수되었습니다.");
		mmsService.acMMS_cartno(params);
		return rstMap;
	}

	public Map track(Map params) {
		Map rstMap = new HashMap();
		Map map = stateDAO.track(params);
		if(map == null){
			rstMap.put("rst", "-1");
			rstMap.put("msg", "잘못된 호출입니다.");
		}else if(StringUtil.isEmptyByParam(map, "delivery") || StringUtil.isEmptyByParam(map, "ch_c_no")){
			rstMap.put("rst", "-2");
			rstMap.put("msg", "송장번호 미입력 상태입니다.");
		}else{
			String url = "";
			//구현불가
			if("77777777777".equals(map.get("delivery"))){
				url = (String)map.get("delivery_url") + (String)map.get("ch_c_no");
				
				Document doc = null;
				try {
//					doc = Jsoup.connect(url).get();
					doc = Jsoup.parse(getHttpClient(url));
				} catch (IOException e) {
					e.printStackTrace();
				}
				Elements content = doc.select(".view");
				
				rstMap.put("rst", "1");
			}else{
				rstMap.put("rst", "2");
				rstMap.put("data", map);
			}
		}
		return rstMap;
	}
	
	public String getHttpClient(String url) throws ClientProtocolException, IOException{
		HttpClient httpClient = new DefaultHttpClient(); 
		httpClient.getParams().setIntParameter("http.connection.timeout", 5000);

        HttpGet httpGet = new HttpGet(url); 
        HttpResponse httpResponse = httpClient.execute(httpGet); 
         
        return EntityUtils.toString(httpResponse.getEntity());
	}
	
	public Map m_pay_cancel(Map params) {
		Map rstMap = new HashMap();
		MCMap info = stateDAO.m_pay_info(params);
		
		params.put("orderno", info.get("orderno"));
		MCMap info_count = stateDAO.m_info_count(params);
		
		if(Integer.parseInt((String)info_count.get("count")) == 1) // 구입한 상품이 1개 이면 결제 진행
		{

			if(info == null){	//결제정보가 없는경우
				rstMap.put("rst", "-1");
				rstMap.put("msg", "결재정보가 없습니다. 확인후 다시 시도해 주시기 바랍니다.");
				return rstMap;
			}else if("card".equals(info.getStrNull("paytyp"))){//신용카드만 즉시 취소 가능{
				
				///////////////////////////////////////////////////////////////////////////////////////////////////
				//
				// 올더게이트 모바일 카드 결제취소 페이지
				//
				///////////////////////////////////////////////////////////////////////////////////////////////////
				
				String tracking_id = (String)info.get("trans_id");
				String transaction = (String)info.get("trans");
				String SendNo = (String)info.get("rdealno");
				String AdmNo = (String)info.get("rapprno");
				String AdmDt = (String)info.get("rapprtm");
				String Store_OrdNo = (String)info.get("orderno");
				String store_id = Util.getProperty("store_id");
				String log_path = Util.getProperty("mobile_path");
				
				AGSMobile mobile = new AGSMobile(store_id, tracking_id, transaction, log_path);
				HashMap<String, Object> ret = new HashMap<String, Object>();
				mobile.setLogging(true);	//true : 로그기록, false : 로그기록안함.
				
				ret = mobile.cancel(AdmNo, AdmDt, SendNo, "");
				JSONObject data = ((JSONObject)ret.get("data"));

				if(ret.get("status").equals("ok")){	// 승인성공

					// 결제취소에 따른 처리부분
					logger.info("신용카드 승인취소가 성공처리되었습니다. [" + ret.get("status") +"]"+ ret.get("message") );
					stateDAO.pay_cancel(params);
					//재고 수량 복구
					params.put("item_seq", info.get("item_seq"));
					params.put("qty", info.get("qty"));
					stateDAO.qty_cancel(params);
					if("partsmoa".equals(globals.getSTORE_ID())){
						erpDAO.erp_orderdtl_cancel(params);
					}
					
					rstMap.put("rst", "1");
					//문자 보내기
					params.put("mmsMessage", " 상품의 주문 및 결제가 취소 되었습니다.");
					mmsService.acMMS_cartno(params);
					return rstMap;
				}
				else
				{	
					// 결제취소실패에 따른 상점처리부분
					logger.info("신용카드 승인취소가 실패처리되었습니다. [" + ret.get("status") +"]"+ ret.get("message") );
					rstMap.put("rst", "-1");
					rstMap.put("msg", "신용카드 승인취소가 실패처리되었습니다. [" + ret.get("status") +"]"+ ret.get("message") );
					return rstMap;
				}
			}
			else //무통장, 무통장(에스크로)
			{
				
				// 결제취소에 따른 처리부분
				stateDAO.m_pay_cancel(params);
				
				/* 환불요청 문자 발송*/
				params.put("mmsMessage", " 상품의 주문 및 결제 취소를 요청하였습니다.");
				mmsService.acMMS_cartno(params);
				
				//재고 수량 복구
//				params.put("item_seq", info.get("item_seq"));
//				params.put("qty", info.get("qty"));
//				stateDAO.qty_cancel(params);
				rstMap.put("rst", "1");
				return rstMap;
				
			}
		}
		else
		{
			// 결제취소에 따른 처리부분
			stateDAO.m_pay_cancel(params);
			
			/* 환불요청 문자 발송*/
			params.put("mmsMessage", " 상품의 주문 및 결제 취소를 요청하였습니다.");
			mmsService.acMMS_cartno(params);
			
			//재고 수량 복구
//			params.put("item_seq", info.get("item_seq"));
//			params.put("qty", info.get("qty"));
//			stateDAO.qty_cancel(params);
			rstMap.put("rst", "1");
			return rstMap;
		}
		
	}
	
	
	public List bankList(Map params) {
		return stateDAO.list("code.codeList", params);
	}
}