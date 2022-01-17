package com.mc.giftcard.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GiftCardCodeService {
	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private GiftCardCodeDAO codeDAO;

	public Map updateCodeOrderSeq(Map params) throws Exception {
		Map rstMap = new HashMap();
		List list = (List) params.get("list");
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				Map m = (Map) list.get(i);
//				m.put("session_member_id", params.get("session_member_id"));
//				m.put("session_member_nm", params.get("session_member_nm"));
				m.put("ip", params.get("ip"));
				codeDAO.updateCodeOrderSeq(m);
			}
		}
		rstMap.put("rst", "1");
		return rstMap;
	}
}