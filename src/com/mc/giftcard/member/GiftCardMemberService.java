package com.mc.giftcard.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mc.common.util.Encryption;
import com.mc.common.util.StringUtil;
import com.mc.web.mail.MailService;

@Service
public class GiftCardMemberService {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private GiftCardMemberDAO memberDAO;
	
	@Autowired
	private MailService mailService;
	
	
	public Map view(Map params) throws Exception {
		Map rstMap = new HashMap();
		rstMap.put("view", memberDAO.view(params));
		rstMap.put("basongji", memberDAO.basongjiList(params));
		return rstMap;
	}
	
	public Map write(Map params) throws Exception {
		Map rstMap = new HashMap();
		if(!StringUtil.isEmpty((String)params.get("zip1")) && !StringUtil.isEmpty((String)params.get("zip2"))){
			params.put("zip_cd", params.get("zip1")+"-"+params.get("zip2"));
		}
		if(!StringUtil.isEmpty((String)params.get("tel1")) && !StringUtil.isEmpty((String)params.get("tel2")) && !StringUtil.isEmpty((String)params.get("tel3"))){
			params.put("tel", params.get("tel1")+"-"+params.get("tel2")+"-"+params.get("tel3"));
		}
		if(!StringUtil.isEmpty((String)params.get("cell1")) && !StringUtil.isEmpty((String)params.get("cell2")) && !StringUtil.isEmpty((String)params.get("cell3"))){
			params.put("cell", params.get("cell1")+"-"+params.get("cell2")+"-"+params.get("cell3"));
		}
		if(!StringUtil.isEmpty((String)params.get("email1")) && !StringUtil.isEmpty((String)params.get("email2"))){
			params.put("email", params.get("email1")+"@"+params.get("email2"));
		}
		if(!StringUtil.isEmpty((String)params.get("busi_no1")) && !StringUtil.isEmpty((String)params.get("busi_no2")) && !StringUtil.isEmpty((String)params.get("busi_no3"))){
			params.put("busi_no", params.get("busi_no1")+"-"+params.get("busi_no2")+"-"+params.get("busi_no3"));
		}
		if(StringUtil.isEmptyByParam(params, "member_nm")) params.put("member_nm", params.get("ceo_nm"));
		params.put("member_pw", Encryption.stringEncryption((String)params.get("member_pw"), ""));
		
		memberDAO.write(params);
		
		List list = (List) params.get("basongji");
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				Map m = (Map) list.get(i);
				m.put("order_seq", i+1);
				m.put("member_seq", params.get("member_seq"));
				m.put("session_member_id", params.get("session_member_id"));
				m.put("session_member_seq", params.get("session_member_seq"));
				m.put("session_member_nm", params.get("session_member_nm"));
				m.put("ip", params.get("ip"));
				if(!"".equals(m.get("zip1")) && !"".equals(m.get("zip2"))){
					m.put("zip_cd", m.get("zip1")+"-"+m.get("zip2"));
				}
				if(!"".equals(m.get("tel1")) && !"".equals(m.get("tel2")) && !"".equals(m.get("tel3"))){
					m.put("tel", m.get("tel1")+"-"+m.get("tel2")+"-"+m.get("tel3"));
				}
				if(!"".equals(m.get("cell1")) && !"".equals(m.get("cell2")) && !"".equals(m.get("cell3"))){
					m.put("cell", m.get("cell1")+"-"+m.get("cell2")+"-"+m.get("cell3"));
				}
				memberDAO.insertBasongji(m);
			}
		}
		
		rstMap.put("rst", "1");
		return rstMap;
	}
	
	public Map modify(Map params) throws Exception {
		Map rstMap = new HashMap();
		if(!StringUtil.isEmpty((String)params.get("zip1")) && !StringUtil.isEmpty((String)params.get("zip1"))){
			params.put("zip_cd", params.get("zip1")+"-"+params.get("zip2"));
		}
		if(!StringUtil.isEmpty((String)params.get("tel1")) && !StringUtil.isEmpty((String)params.get("tel2")) && !StringUtil.isEmpty((String)params.get("tel3"))){
			params.put("tel", params.get("tel1")+"-"+params.get("tel2")+"-"+params.get("tel3"));
		}
		if(!StringUtil.isEmpty((String)params.get("cell1")) && !StringUtil.isEmpty((String)params.get("cell2")) && !StringUtil.isEmpty((String)params.get("cell3"))){
			params.put("cell", params.get("cell1")+"-"+params.get("cell2")+"-"+params.get("cell3"));
		}
		if(!StringUtil.isEmpty((String)params.get("email1")) && !StringUtil.isEmpty((String)params.get("email2"))){
			params.put("email", params.get("email1")+"@"+params.get("email2"));
		}
		if(!StringUtil.isEmpty((String)params.get("busi_no1")) && !StringUtil.isEmpty((String)params.get("busi_no2")) && !StringUtil.isEmpty((String)params.get("busi_no3"))){
			params.put("busi_no", params.get("busi_no1")+"-"+params.get("busi_no2")+"-"+params.get("busi_no3"));
		}
		
		if(!StringUtil.isEmptyByParam(params, "member_pw")) 
		{
			params.put("member_pw", Encryption.stringEncryption((String)params.get("member_pw"), ""));
		}
			
		memberDAO.modify(params);
		
		memberDAO.basongjiRemove(params);
		List list = (List) params.get("basongji");
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				Map m = (Map) list.get(i);
				m.put("order_seq", i+1);
				m.put("member_seq", params.get("member_seq"));
				m.put("session_member_id", params.get("session_member_id"));
				m.put("session_member_seq", params.get("session_member_seq"));
				m.put("session_member_nm", params.get("session_member_nm"));
				m.put("ip", params.get("ip"));
				if(!"".equals(m.get("zip1")) && !"".equals(m.get("zip2"))){
					m.put("zip_cd", m.get("zip1")+"-"+m.get("zip2"));
				}
				if(!"".equals(m.get("tel1")) && !"".equals(m.get("tel2")) && !"".equals(m.get("tel3"))){
					m.put("tel", m.get("tel1")+"-"+m.get("tel2")+"-"+m.get("tel3"));
				}
				if(!"".equals(m.get("cell1")) && !"".equals(m.get("cell2")) && !"".equals(m.get("cell3"))){
					m.put("cell", m.get("cell1")+"-"+m.get("cell2")+"-"+m.get("cell3"));
				}
				memberDAO.insertBasongji(m);
			}
		}
		
		rstMap.put("rst", "1");
		return rstMap;
	}
	
	public Map del(Map params) throws Exception {
		Map rstMap = new HashMap();
		memberDAO.del(params);
		rstMap.put("rst", "1");
		return rstMap;
	}
	
	public Map passwordInit(Map params) throws Exception {
		Map rstMap = new HashMap();
		params.put("member_pw", StringUtil.createPasword(5));
		if(memberDAO.passwordInit(params) > 0){
			//TODO 메일이나 SMS발송 추가해줄것.

			params.put("from_email","partsmoa@insun.com");		//보내는사람 이메일주소
			params.put("from_nm","Parts Moa");					//보내는사람 이름
			params.put("title","초기화된 비밀번호 입니다.");		//제목
			params.put("conts","초기화된 비밀번호는 " + params.get("member_pw") + " 입니다.\n 반드시 회원정보 수정에서 비밀번호를 수정해주세요.");	//내용
			params.put("to_email", (String)params.get("email"));		//받는사람 이메일
			
			logger.info("============================= Mail Send Start ==============================");
			mailService.sendUser(params);
			logger.info("============================= Mail Send End ==============================");
			
			rstMap.put("rst", "1");
		}else{
			rstMap.put("msg", "변경에 실패하였습니다.");
			rstMap.put("rst", "-1");
		}
		return rstMap;
	}
	
}