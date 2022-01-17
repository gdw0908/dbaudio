package com.mc.web.mms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MmsService {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private MmsDAO mmsDAO;
	
	@Autowired
	private MmsInfoDAO mmsInfoDAO;
	
	public Map write(Map params) throws Exception {
		Map rstMap = new HashMap();
		
		String raw = (String)params.get("tran_msg");
		int rawLength = 0;
		if(raw != null){
			byte[] rawBytes = null;
			try {
				rawBytes = raw.getBytes("EUC-KR");
			} catch (Exception e) {
				e.printStackTrace();
			}
			rawLength = rawBytes.length;
		}
		if(rawLength <= 80){//SMS전송
			params.put("em_tran_seq", mmsDAO.getNextval());
			params.put("tran_type", "4");
			mmsDAO.sms_write(params);
			rstMap.put("rst", "1");
		}else if(rawLength >= 80){ //MMS전송
			params.put("em_tran_seq", mmsDAO.getNextval());
			params.put("tran_type", "6");
			mmsDAO.mms_write(params);
			mmsDAO.sms_write(params);
			rstMap.put("rst", "1");
		}else{
			rstMap.put("rst", "0");
		}
		return rstMap;
	}
	
	public Map list(Map params){
		Map rstMap = new HashMap();
		try{
			rstMap.put("rst", "1");
			rstMap.put("list", mmsDAO.list(params));
			rstMap.put("page_info", mmsDAO.page_info(params));
		}catch(Exception e){
			rstMap.put("list", new ArrayList());
			rstMap.put("page_info", new HashMap());
		}
		return rstMap;
	}
	
	public void acMMS_cartno(Map params){//관리자,협력사
		try{
			Map adminInfo = mmsInfoDAO.admin_cell();
			//협력사 문자 발송
			Map mmsSendMap = new HashMap();
			Map mmsInfo = (Map) mmsInfoDAO.cartno_cooperation_cell(params);
			String mmsMessage = (String)params.get("mmsMessage");
			String mmsProductnm = (String)mmsInfo.get("productnm");
			mmsSendMap.put("tran_phone", mmsInfo.get("sms_tel"));//받는사람
			mmsSendMap.put("tran_callback", "1544-6444");//보내는사람				
			mmsSendMap.put("tran_msg", mmsProductnm+mmsMessage);//메세지
			write(mmsSendMap);
			//관리자 문자 발송
			if(adminInfo.get("sms_yn").equals("Y")){
				mmsSendMap.put("tran_phone", adminInfo.get("cell"));
				write(mmsSendMap);
			}
		}catch(Exception e){
			
		}
	}
	
	public void acMMS_orderno(Map params){//관리자,협력사
		try{
			List mmsList = mmsInfoDAO.orderno_cooperation_cell(params);
			Map adminInfo = mmsInfoDAO.admin_cell();
			for(int x = 0; x < mmsList.size(); x++){
				Map mmsSendMap = new HashMap();
				Map mmsInfo = (Map) mmsList.get(x);
				String mmsMessage = (String)params.get("mmsMessage");
				String mmsProductnm = (String)mmsInfo.get("productnm");
				mmsSendMap.put("tran_phone", mmsInfo.get("sms_tel"));//받는사람
				mmsSendMap.put("tran_callback", "1544-6444");//보내는사람				
				mmsSendMap.put("tran_msg", mmsProductnm+mmsMessage);//메세지
				write(mmsSendMap);
				if(adminInfo.get("sms_yn").equals("Y")){
					mmsSendMap.put("tran_phone", adminInfo.get("cell"));
					write(mmsSendMap);
				}
			}
		}catch(Exception e){
			
		}
	}
	
	public void userMMS_cartno(Map params){//사용자
		try{
			Map mmsSendMap = new HashMap();
			Map mmsInfo = (Map) mmsInfoDAO.cartno_cooperation_cell(params);
			String mmsMessage = (String)params.get("mmsMessage");
			String mmsProductnm = (String)mmsInfo.get("productnm");
			mmsSendMap.put("tran_phone", mmsInfo.get("cell"));//받는사람
			mmsSendMap.put("tran_callback", "1544-6444");//보내는사람				
			mmsSendMap.put("tran_msg", mmsProductnm+mmsMessage);//메세지
			write(mmsSendMap);
		}catch(Exception e){
			
		}
	}
	
	public void userMMS_orderno(Map params){
		try{
			List mmsList = mmsInfoDAO.orderno_cooperation_cell(params);
			for(int x = 0; x < mmsList.size(); x++){
				Map mmsSendMap = new HashMap();
				Map mmsInfo = (Map) mmsList.get(x);
				String mmsMessage = (String)params.get("mmsMessage");
				String mmsProductnm = (String)mmsInfo.get("productnm");
				mmsSendMap.put("tran_phone", mmsInfo.get("cell"));//받는사람
				mmsSendMap.put("tran_callback", "1544-6444");//보내는사람				
				mmsSendMap.put("tran_msg", mmsProductnm+mmsMessage);//메세지
				write(mmsSendMap);
			}
		}catch(Exception e){
			
		}
	}
	
	public void guestMMS(Map params){//비회원
		try{
			Map mmsSendMap = new HashMap();
			mmsSendMap.put("tran_phone", params.get("guestPhone"));//받는사람
			mmsSendMap.put("tran_callback", "1544-6444");//보내는사람				
			mmsSendMap.put("tran_msg", params.get("mmsMessage"));//메세지
			write(mmsSendMap);
		}catch(Exception e){
			
		}
	}
	
	public void guestVirMMS(Map params){//무통장
		try{
			Map mmsInfo = (Map) mmsInfoDAO.orderCell(params);
			if("GUEST".equals(mmsInfo.get("member_type"))){
				Map mmsSendMap = new HashMap();
				mmsSendMap.put("tran_phone", mmsInfo.get("rehp"));//받는사람
				mmsSendMap.put("tran_callback", "1544-6444");//보내는사람				
				mmsSendMap.put("tran_msg", params.get("mmsMessage"));//메세지
				write(mmsSendMap);
			}
		}catch(Exception e){
			
		}
	}
	
	

}