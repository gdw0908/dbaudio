package com.mc.common.util;

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.mc.web.Globals;

@Component
public class PaymentUtil {
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private Globals globals;
	/**
	 * 계좌 검증(계좌번호, 예금주 조회)
	 * @param jsonData
	 * @return
	 * @throws Exception
	 */
	public String settleAccntReq( JSONObject jsonData) throws Exception {
		
		URL url = new URL(globals.getVC_SETTLE_ACCNT_URL());
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		HttpApiUtil http = new HttpApiUtil(httpCon);
		
		http.request("POST", "Authorization", globals.getVC_ACCTAPI_KEY(), jsonData);
		String res = http.response();
		
		return res;
	}
	/**
	 * 가상계좌번호 요청
	 * @param jsonData
	 * @return
	 * @throws Exception
	 */
	public String vactWithDrawReq( JSONObject jsonData) throws Exception {
		
		URL url = new URL(globals.getVC_VACT_WITHDRAWGET_URL());
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		HttpApiUtil http = new HttpApiUtil(httpCon);
		
		http.request("POST", "Authorization", globals.getVC_ACCTAPI_KEY(), jsonData);
		String res = http.response();
		
		return res;
	}
	/**
	 * 가상계좌번호 출금정보 등록
	 * @param jsonData
	 * @return
	 * @throws Exception
	 */
	public String vactInfoRegReq( JSONObject jsonData) throws Exception {
		
		URL url = new URL(globals.getVC_VACT_REG_URL());
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		HttpApiUtil http = new HttpApiUtil(httpCon);
		
		http.request("POST", "Authorization", globals.getVC_ACCTAPI_KEY(), jsonData);
		String res = http.response();
		
		return res;
	}
}
