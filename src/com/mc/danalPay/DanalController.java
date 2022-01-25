package com.mc.danalPay;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mc.common.util.StringUtil;
import com.mc.web.Globals;

@Controller
@RequestMapping(value = "/danal")
public class DanalController {
	
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private Globals globals;
	
	@Value("${server.api.host}") 
	private String server_api_host;
	
	@RequestMapping("/Ready.do")
	public String Ready(@RequestParam Map<String, String> params, HttpServletRequest request,HttpSession session) throws Exception{
		
		String SERVICETYPE = params.get("SERVICETYPE");
		
		/*[ 필수 데이터 ]***************************************/
		Map REQ_DATA = new HashMap();
		Map RES_DATA = null;

		/******************************************************
		 *  RETURNURL 	: CPCGI페이지의 Full URL을 넣어주세요
		 *  CANCELURL 	: BackURL페이지의 Full URL을 넣어주세요
		 ******************************************************/
		String RETURNURL = "http://pairingpayments.co.kr" + "/danal/CPCGI.do?SERVICETYPE="+SERVICETYPE;
		String CANCELURL = "http://pairingpayments.co.kr" + "/danal/Cancel.do";

		/**************************************************
		 * SubCP 정보
		 **************************************************/
		REQ_DATA.put("SUBCPID", "");

		/**************************************************
		 * 결제 정보
		 **************************************************/
		
		String orderid = (String) request.getParameter("orderid");
		String amount = (String) request.getParameter("amount");
		REQ_DATA.put("AMOUNT", amount);
		
		ConcurrentHashMap<String, String> addMap = new ConcurrentHashMap<String, String>();
		addMap.put("orderid", orderid);
		addMap.put("amount", amount);
		//Constants.danalAmountMapList.add(addMap);
		globals.danalMap.put("amount", amount);
		//REQ_DATA.put("AMOUNT", DanalFunction.TEST_AMOUNT);
		REQ_DATA.put("CURRENCY", "410");	//통화코드 (원화: 410, 달러: 840)
		REQ_DATA.put("ITEMNAME", (String) request.getParameter("itemname"));
		REQ_DATA.put("USERAGENT", (String) request.getParameter("useragent"));
		REQ_DATA.put("ORDERID", orderid);
		//REQ_DATA.put("OFFERPERIOD", "2015102920151129");
		String dt = (String) request.getParameter("dt");
		REQ_DATA.put("OFFERPERIOD", dt + dt);

		/**************************************************
		 * 고객 정보
		 **************************************************/
		REQ_DATA.put("USERNAME", (String) request.getParameter("username")); // 구매자 이름
		REQ_DATA.put("USERID", StringUtil.nvl((String) request.getParameter("userid"),session.getId())); // 사용자 ID
		REQ_DATA.put("USEREMAIL", (String) request.getParameter("useremail")); // 소보법 email수신처

		/**************************************************
		 * URL 정보
		 **************************************************/
		REQ_DATA.put("CANCELURL", CANCELURL);
		REQ_DATA.put("RETURNURL", RETURNURL);

		/**************************************************
		 * 기본 정보
		 **************************************************/
		REQ_DATA.put("TXTYPE", "AUTH");
		REQ_DATA.put("SERVICETYPE", SERVICETYPE);
		
		if(SERVICETYPE.equals("BATCH"))
			REQ_DATA.put("ISBILL", "Y"); // N: 실제로 결제를 일으키지 않고 BillKey만 발급. Y: 실제로 거래를 일으키고 BillKey도 발급.
		
		REQ_DATA.put("ISNOTI", "N");
		REQ_DATA.put("BYPASSVALUE", "this=is;a=test;bypass=value"); // BILL응답 또는 Noti에서 돌려받을 값. '&'를 사용할 경우 값이 잘리게되므로 유의.

		//System.out.println("================REQ_DATA:"+REQ_DATA.toString()+"===============");
		RES_DATA = DanalFunction.CallCredit(REQ_DATA, true);
		
		String RETURNCODE = (String)RES_DATA.get("RETURNCODE");
		
		if ("0000".equals(RETURNCODE)) {
			request.setAttribute("STARTURL", RES_DATA.get("STARTURL"));
			request.setAttribute("STARTPARAMS", RES_DATA.get("STARTPARAMS"));
			return "/giftcard/danalPay/Ready";
		}else {
			request.setAttribute("RETURNCODE", RETURNCODE);
			request.setAttribute("RETURNMSG", RES_DATA.get("RETURNMSG"));
			return "/giftcard/danalPay/Error";
		}
	}

	@RequestMapping("/CPCGI.do")
	public String CPCGI(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		String SERVICETYPE = params.get("SERVICETYPE");
		
		String RES_STR = DanalFunction.toDecrypt((String) request.getParameter("RETURNPARAMS"));
		Map retMap = DanalFunction.str2data(RES_STR);

		String returnCode = (String) retMap.get("RETURNCODE");
		String returnMsg = (String) retMap.get("RETURNMSG");

		//*****  신용카드 인증결과 확인 *****************
		if (returnCode == null || !"0000".equals(returnCode)) {
			// returnCode가 없거나 또는 그 결과가 성공이 아니라면 실패 처리
			System.out.println("Authentication failed. " + returnMsg + "[" + returnCode + "]");
			request.setAttribute("RETURNCODE", returnCode);
			request.setAttribute("RETURNMSG", returnMsg);
			return "/giftcard/danalPay/Error";
		}
		
		/*[ 필수 데이터 ]***************************************/
		Map REQ_DATA = new HashMap();
		Map RES_DATA = new HashMap();

		/**************************************************
		 * 결제 정보
		 **************************************************/
		REQ_DATA.put("TID", (String) retMap.get("TID"));
		System.out.println("================TID:"+(String) retMap.get("TID")+"===============");
		
		String amount = "";
		String orderid = (String) retMap.get("ORDERID");
		/*for(ConcurrentHashMap<String, String> danalAmountMap : Constants.danalAmountMapList){
			if(danalAmountMap.get("orderid").equals(orderid)){
				amount = danalAmountMap.get("amount");
				//Constants.authNumberMapList.remove(orderid);
				break;
	 	   	}
		}*/
		amount=(String)globals.danalMap.get("amount");
		REQ_DATA.put("AMOUNT", amount);
		//REQ_DATA.put("AMOUNT", DanalFunction.TEST_AMOUNT); // 최초 결제요청(AUTH)시에 보냈던 금액과 동일한 금액을 전송

		/**************************************************
		 * 기본 정보
		 **************************************************/
		String TXTYPE = "BILL";
		if(SERVICETYPE.equals("BATCH"))
			TXTYPE = "ISSUEBILLKEY";
		
		REQ_DATA.put("TXTYPE", TXTYPE);
		REQ_DATA.put("SERVICETYPE", SERVICETYPE);

		RES_DATA = DanalFunction.CallCredit(REQ_DATA, false);
		
		if(!RES_DATA.containsKey("ISBILL")) RES_DATA.put("ISBILL", "");
		if(!RES_DATA.containsKey("BILLKEY")) RES_DATA.put("BILLKEY", "");
		if(!RES_DATA.containsKey("DISCOUNTAMOUNT")) RES_DATA.put("DISCOUNTAMOUNT", "");
		if(!RES_DATA.containsKey("TRXAMOUNT")) RES_DATA.put("TRXAMOUNT", "");
		
		String RETURNCODE = (String)RES_DATA.get("RETURNCODE");

		request.setAttribute("RETURNCODE", RETURNCODE);
		request.setAttribute("RETURNMSG", RES_DATA.get("RETURNMSG"));
		
		if ("0000".equals(RETURNCODE)) {
			request.setAttribute("RES_DATA", RES_DATA);
			return "/giftcard/danalPay/Success";
		} else 
			return "/giftcard/danalPay/Error";
	}
	
	@RequestMapping("/test.do")
	public String Billtestl(@RequestParam Map<String, String> params, HttpServletRequest request) throws Exception{
		return "/giftcard/danalPay/Success";
	}
	@RequestMapping("/BillCancel.do")
	public String BillCancel(@RequestParam Map<String, String> params, HttpServletRequest request) throws Exception{
		
		Map REQ_DATA = new HashMap();
		Map RES_DATA = new HashMap();
		
		/**************************************************
		 * 결제 정보
		 **************************************************/
		//REQ_DATA.put("TID", "xxxxxxxxxxxxx");
		REQ_DATA.put("TID", (String) request.getParameter("TID"));
		
		
		/**************************************************
		 * 기본 정보
		 **************************************************/
		REQ_DATA.put("CANCELTYPE", "C");
		REQ_DATA.put("AMOUNT", (String) request.getParameter("amount"));
		//REQ_DATA.put("AMOUNT", DanalFunction.TEST_AMOUNT);

		/**************************************************
		 * 취소 정보
		 **************************************************/
		REQ_DATA.put("CANCELREQUESTER", "CP_CS_PERSON");
		REQ_DATA.put("CANCELDESC", "Item not delivered");


		REQ_DATA.put("TXTYPE", "CANCEL");
		REQ_DATA.put("SERVICETYPE", "DANALCARD");

		
		RES_DATA = DanalFunction.CallCredit(REQ_DATA, false);
		
		String RETURNCODE = (String)RES_DATA.get("RETURNCODE");
		
		request.setAttribute("RETURNCODE", RETURNCODE);
		request.setAttribute("RETURNMSG", DanalFunction.data2str(RES_DATA));
		
		if ("0000".equals(RETURNCODE)) 
			return "/giftcard/danalPay/Cancel";
		else 
			return "/giftcard/danalPay/Error";
	}
	
	@RequestMapping("/DelBillkey.do")
	public String DelBillkey(@RequestParam Map<String, String> params, HttpServletRequest request) throws Exception{
		
		/*[ 필수 데이터 ]***************************************/
		Map REQ_DATA = new HashMap();
		Map RES_DATA = new HashMap();
		
		/**************************************************
		 * 결제 정보
		 **************************************************/
	  	REQ_DATA.put("BILLKEY", (String) request.getParameter("BILLKEY")); 
		
		/**************************************************
		 * 기본 정보
		 **************************************************/
		REQ_DATA.put("TXTYPE", "DELBILLKEY");
		REQ_DATA.put("SERVICETYPE", "BATCH");


		RES_DATA = DanalFunction.CallCredit(REQ_DATA, false);
		
		String RETURNCODE = (String)RES_DATA.get("RETURNCODE");
		
		request.setAttribute("RETURNCODE", RETURNCODE);
		request.setAttribute("RETURNMSG", DanalFunction.data2str(RES_DATA));
		
		if ("0000".equals(RETURNCODE)) 
			return "/giftcard/danalPay/Cancel";
		else 
			return "/giftcard/danalPay/Error";
	}
	
	@RequestMapping("/Cancel.do")
	public String Cancel(@RequestParam Map<String, String> params, HttpServletRequest request) throws Exception{
		
		return "/giftcard/danalPay/Cancel";
	}
	
	@RequestMapping("/ManualPay.do")
	public String ManualPay(@RequestParam Map<String, String> params, HttpServletRequest request) throws Exception{
		request.setAttribute("params", params);
		return "/giftcard/danalPay/ManualPay";
	}
	
	
	@RequestMapping("/PayExcute.do")
	public String PayExcute(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		String ord_nm = (String) params.get("ord_nm");
		String goods_nm = (String) params.get("goods_nm");
		
		String mobile = (String) params.get("mobile");
		String email = (String) params.get("email");
		String cert_num = (String) params.get("cert_num");
		String amt = (String) params.get("amt");
		
		String card_no = (String) params.get("card_no");
		String card_valid_mm = (String) params.get("card_valid_mm");
		String card_valid_yy = (String) params.get("card_valid_yy");
		String card_inst = (String) params.get("card_inst");
		
		if(card_inst.equals("1"))
		   card_inst = "00";
		else
		   card_inst = StringUtil.lpad(card_inst, 2, "0");
		
		params.put("card_inst", card_inst);
		
		String card_birth_dt = (String) params.get("card_birth_dt");
		String buyerAuthNum = card_birth_dt;
		String card_pw = (String) params.get("card_pw");
		
		String user_id = StringUtil.nvl((String) params.get("userid"),session.getId()); // 사용자 ID
		
		Map<String, Object> sendParam = new HashMap<String, Object>();
		
		/**************************************************
		 * 결제 정보
		 **************************************************/
		sendParam.put("AMOUNT", amt.replaceAll(",", ""));
		sendParam.put("CURRENCY", "410");
		sendParam.put("ITEMNAME", goods_nm);
		sendParam.put("ORDERID", cert_num);
		
		/**************************************************
		 * 고객 정보
		 **************************************************/
		sendParam.put("USERNAME", ord_nm);
		sendParam.put("USERPHONE", mobile);
		sendParam.put("USERID", user_id);
		sendParam.put("USERAGENT", "ONLINE"); //고정값
		sendParam.put("USEREMAIL", email); //고정값
		
		
		/**************************************************
		 * 카드 정보
		 **************************************************/
		sendParam.put("QUOTA", card_inst); //할부개월수. 일시불:00, 2개월:02...
		sendParam.put("ISREBILL", "N"); //고정값
		sendParam.put("BILLINFO", card_no); //카드번호
		sendParam.put("EXPIREPERIOD", card_valid_yy + card_valid_mm); //유효기간 YYMM
		sendParam.put("CARDPWD", card_pw); //비밀번호 앞 2자리
		sendParam.put("CARDAUTH", buyerAuthNum); //생년월일 YYMMDD
		sendParam.put("PAYTYP", "danalDr"); //고정값(페이구분(수기 직접결제))
		//sendParam.put("CPID", pg_mid);
		
		/**************************************************
		 * 기본 정보
		 **************************************************/
		sendParam.put("TXTYPE", "OTBILL");
		sendParam.put("SERVICETYPE", "KEYIN");
		
		log.error("danal_payment_proc:" + cert_num+" / sendParam:"+sendParam.toString());
		Map<String, Object> RES_DATA = DanalFunction.CallCredit(sendParam, true);
		log.error("danal_payment_proc:" + cert_num+" / RES_DATA:"+RES_DATA.toString());
		
		String RETURNCODE = (String) RES_DATA.get("RETURNCODE");
		String RETURNMSG = (String) RES_DATA.get("RETURNMSG");
		String transactionNo = (String)RES_DATA.get("TID");      //다날 거래키
		params.put("top_tid", transactionNo);
		
		if(RETURNCODE.equals("0000")) {
			request.setAttribute("RES_DATA", RES_DATA);
		} else {
		   	request.setAttribute("RETURNCODE", RETURNCODE);
			request.setAttribute("RETURNMSG", RETURNMSG);
			return "/giftcard/danalPay/Error";
		}		    	  
		
		return "/giftcard/danalPay/Success";
	}
	
}
