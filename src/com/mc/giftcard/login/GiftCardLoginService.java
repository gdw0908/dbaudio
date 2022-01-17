package com.mc.giftcard.login;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mc.web.MCMap;

import com.mc.common.util.Encryption;

@Service("giftCardLoginService")
public class GiftCardLoginService {
	
	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private GiftCardLoginDAO loginDAO;
	

	public Map superLogin(HttpSession session, Map params) throws Exception {
		Map rstMap = new HashMap();
		MCMap member = (MCMap) session.getAttribute("member");
		if(member == null || !"1".equals(member.get("group_seq"))){
			rstMap.put("msg", "슈퍼관리자가 아닙니다.");
			rstMap.put("rst", "-1");
		}else{
			session.setAttribute("member", loginDAO.superLogin(params));
			session.setMaxInactiveInterval(60 * 60);
//    		session.getServletContext().setAttribute("member", member);
			rstMap.put("rst", "1");
		}
		return rstMap;
	}
	
	public Map login(HttpSession session, Map params) throws Exception {
		Map rstMap = new HashMap();
		//System.out.println(Encryption.stringEncryption((String)params.get("member_pw"), ""));
		params.put("member_pw",Encryption.stringEncryption((String)params.get("member_pw"), ""));
		Map member = loginDAO.getMemberByIdPw(params);
		if(member == null){
			rstMap.put("msg", "아이디와 비밀번호가 일치하지 않습니다.");
			rstMap.put("rst", "-1");
		}else{
			loginDAO.updateMemberLastLogin(member);
			session.setMaxInactiveInterval(60 * 60);
			session.setAttribute("member", member);
//    		session.getServletContext().setAttribute("member", member);
			rstMap.put("rst", "1");
		}
		return rstMap;
	}
}