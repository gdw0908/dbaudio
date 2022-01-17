package com.mc.giftcard.goods.spell;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.mc.common.util.ExcelUtil;
import com.mc.web.MCMap;
import com.mc.web.common.POIExcelDownloadService;
/**
 * 
 *
 * @Description : 
 * @ClassName   : com.mc.web.goods.spell.AdminSpellController.java
 * @Modification Information
 *
 * @author 이창기
 * @since 2015. 3. 18.
 * @version 1.0 *  
 * Copyright (C)  All right reserved.
 */
@Controller
@RequestMapping("/giftcard/admin/goods/spell/index.do")
public class GiftCardAdminSpellController {
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private GiftCardSpellService spellService;
	
	@Autowired
	private POIExcelDownloadService excelDownloadService;
	
	@ResponseBody
	@RequestMapping(params="mode=view")
	@Transactional(rollbackFor = { Exception.class })
	public Map view(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		return spellService.view(params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=modify")
	@Transactional(rollbackFor = { Exception.class })
	public Map modify(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> jsonObject) throws Exception{
		Map params = (JSONObject) JSONValue.parse((String)jsonObject.get("jData"));	//JSON으로 보내준 데이터를 사용
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("session_member_id", (String) member.get("member_id"));
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		params.put("ip", request.getRemoteHost());
		return spellService.modify(params);
	}

	@RequestMapping(params="mode=excelDown")
	public void excelDown(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		List<MCMap> list = spellService.excelDown(params);
		for(MCMap m : list){
			if(m.containsKey("trans_id")){
				m.put("trans_id", "모바일");
			}else{
				m.put("trans_id", "PC");
			}
		}
		String filename = java.net.URLEncoder.encode("주문","UTF-8");
		OutputStream output = response.getOutputStream();
		response.setContentType("application/xls");
		response.setHeader("Content-Disposition","attachment; filename="+filename+".xls");
		String[][] header = {
				{"주문상태", "주문일", "주문자", "상품명", "상품위치", "결제금액", "주문번호", "상품ERP코드", "공급업체", "결제유형","결제방법"}, 
				{"status_nm","orderdate","receiver","productnm","part_location","actual_price","orderno","erp_code","com_nm","paytyp_nm","trans_id"},
				{"", "date", "", "", "", "bill", "", "", "","",""},
				{"3000", "5000", "2500", "8000", "6000", "3000", "4000", "5000", "5000","3000","3000"}
			};
		
		excelDownloadService.excelDownload(output, list, "주문내역", header);
	}
}
