package com.mc.giftcard.shopping.cart;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
		String RETURNURL = "http://192.168.0.60:8085" + "/danal/CPCGI.do?SERVICETYPE="+SERVICETYPE;
		String CANCELURL = "http://192.168.0.60:8085" + "/danal/Cancel.do";

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
	
	
}
