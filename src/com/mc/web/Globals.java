package com.mc.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.WebApplicationContext;

@Repository("globals")
public class Globals {
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Value("#{config['develope']}")
	public String DEVELOPE;

	@Value("#{config['agspay_path']}")
	private String AGSPAY_PATH;
	
	@Value("#{config['store_id']}")
	public String STORE_ID;
	
	@Value("#{config['home.url']}")
	public String HOME_URL;
	
	@Value("#{config['home.url.ssl']}")
	public String HOME_URL_SSL;
	
	@Value("#{config['upload.board']}")
	public String FILE_IN_PATH;
	
	@Value("#{config['upload.mail']}")
	public String MAIL_FILE_IN_PATH;


	//이메일정보
	@Value("#{config['email.ip']}")
	public String EMAIL_IP;
	
	@Value("#{config['email.port']}")
	public String EMAIL_PORT;
	
	@Value("#{config['email.id']}")
	public String EMAIL_ID;
	
	@Value("#{config['email.passwd']}")
	public String EMAIL_PASSWD;
	
	@Value("#{config['email.auth_yn']}")
	public String EMAIL_AUTH_YN;
	
	@Value("#{config['email.from_nm']}")
	public String EMAIL_FROM_NM;
	
	@Value("#{config['email.from_email']}")
	public String EMAIL_FROM_EMAIL;
	
	//PG수수료
	@Value("#{config['pg.commission.card1']}")
	public String PG_COMMISSION_CARD1;
	
	@Value("#{config['pg.commission.card2']}")
	public String PG_COMMISSION_CARD2;
	
	@Value("#{config['pg.commission.iche1']}")
	public String PG_COMMISSION_ICHE1;
	
	@Value("#{config['pg.commission.iche2']}")
	public String PG_COMMISSION_ICHE2;
	
	@Value("#{config['pg.commission.virtual']}")
	public String PG_COMMISSION_VIRTUAL;
	
	@Value("#{config['pg.commission.hp']}")
	public String PG_COMMISSION_HP;
	
	@Value("#{config['vc.acctApi.key']}")
	public String VC_ACCTAPI_KEY;
	
	@Value("#{config['vc.settle.accnt.url']}")
	public String VC_SETTLE_ACCNT_URL;
	
	@Value("#{config['vc.vact.withdrawGet.url']}")
	public String VC_VACT_WITHDRAWGET_URL;
	
	@Value("#{config['vc.vact.reg.url']}")
	public String VC_VACT_REG_URL;
	
	public String getPG_COMMISSION_CARD1() {
		return PG_COMMISSION_CARD1;
	}

	public String getPG_COMMISSION_CARD2() {
		return PG_COMMISSION_CARD2;
	}

	public String getPG_COMMISSION_ICHE1() {
		return PG_COMMISSION_ICHE1;
	}
	
	public String getPG_COMMISSION_ICHE2() {
		return PG_COMMISSION_ICHE2;
	}

	public String getPG_COMMISSION_VIRTUAL() {
		return PG_COMMISSION_VIRTUAL;
	}

	public String getPG_COMMISSION_HP() {
		return PG_COMMISSION_HP;
	}

	public String getHOME_URL_SSL() {
		return HOME_URL_SSL;
	}
	
	public String getHOME_URL() {
		return HOME_URL;
	}
	
	public String getFILE_IN_PATH() {
		return FILE_IN_PATH;
	}
	public void setFILE_IN_PATH(String fILE_IN_PATH) {
		FILE_IN_PATH = fILE_IN_PATH;
	}
	
	public String getSTORE_ID() {
		return STORE_ID;
	}
	
	public String getAGSPAY_PATH() {
		return AGSPAY_PATH;
	}

	public String getEMAIL_IP() {
		return EMAIL_IP;
	}

	public String getEMAIL_PORT() {
		return EMAIL_PORT;
	}

	public String getEMAIL_ID() {
		return EMAIL_ID;
	}

	public String getEMAIL_PASSWD() {
		return EMAIL_PASSWD;
	}

	public String getEMAIL_AUTH_YN() {
		return EMAIL_AUTH_YN;
	}

	public String getEMAIL_FROM_NM() {
		return EMAIL_FROM_NM;
	}

	public String getEMAIL_FROM_EMAIL() {
		return EMAIL_FROM_EMAIL;
	}

	public String getMAIL_FILE_IN_PATH() {
		return MAIL_FILE_IN_PATH;
	}

	public void setMAIL_FILE_IN_PATH(String mAIL_FILE_IN_PATH) {
		MAIL_FILE_IN_PATH = mAIL_FILE_IN_PATH;
	}	

	public String getVC_ACCTAPI_KEY() {
		return VC_ACCTAPI_KEY;
	}

	public void setVC_ACCTAPI_KEY(String vC_ACCTAPI_KEY) {
		VC_ACCTAPI_KEY = vC_ACCTAPI_KEY;
	}

	public String getVC_SETTLE_ACCNT_URL() {
		return VC_SETTLE_ACCNT_URL;
	}

	public void setVC_SETTLE_ACCNT_URL(String vC_SETTLE_ACCNT_URL) {
		VC_SETTLE_ACCNT_URL = vC_SETTLE_ACCNT_URL;
	}

	public String getVC_VACT_WITHDRAWGET_URL() {
		return VC_VACT_WITHDRAWGET_URL;
	}

	public void setVC_VACT_WITHDRAWGET_URL(String vC_VACT_WITHDRAWGET_URL) {
		VC_VACT_WITHDRAWGET_URL = vC_VACT_WITHDRAWGET_URL;
	}

	public String getVC_VACT_REG_URL() {
		return VC_VACT_REG_URL;
	}

	public void setVC_VACT_REG_URL(String vC_VACT_REG_URL) {
		VC_VACT_REG_URL = vC_VACT_REG_URL;
	}



	public static final String JOIN_MENU_ID = "0702000000";
	public static final String LOGIN_MENU_ID = "0701000000";
	public static final String ABNORMAL_MSG = "비정상적 접근입니다.";
	public static final String REQUIRED_LOGIN = "로그인 회원만 볼수있습니다.";
	public static final String NOT_LOGIN = "로그인 후 사용 할 수 있습니다.";
	public static final String PERMISSION_DENIED = "권한이 부족합니다.";
	public static final String PUBLIC_NO_ARTICLE = "비공개 글은 본인이 등록한 글만 열람이 가능합니다.";
	
	public static final List<String> IMAGE_EXTENTSIONS = Arrays.asList(new String[]{"gif", "jpg", "jpeg", "png", "bmp"});
	public static final List<String> HTML_EXTENTSIONS = Arrays.asList(new String[]{"htm", "html", "jsp", "do"});
	public static final List<String> BROWSER_EXTENTSIONS = Arrays.asList(new String[]{"chrome","firefox","safari","opera","iphone","ipod","android","blackberry","windows ce", "nokia","webos","opera mini","sonyericsson","opera mobi","iemobile"});	//브라우져
	
	public static Map danalMap= new HashMap();	//다날결제관련 상수 맵
}
