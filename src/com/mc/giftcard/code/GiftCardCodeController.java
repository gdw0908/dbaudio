package com.mc.giftcard.code;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mc.web.Globals;
import com.mc.web.MCMap;

@Controller
public class GiftCardCodeController {
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private GiftCardCodeService codeService;
	
	@Autowired
	private Globals globals;

	@ResponseBody
	@RequestMapping("/giftcard/code/updateOrder.do")	
	@Transactional(rollbackFor = { Exception.class })
	public Map updateCodeOrderSeq(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception {
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		MCMap member = (MCMap) session.getAttribute("member");
//		params.put("session_member_id", (String) member.get("member_id"));
//		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("ip", request.getRemoteHost());
		return codeService.updateCodeOrderSeq(params);
	}
}
