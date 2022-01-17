package com.mc.web.application;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

	@Autowired
	private ApplicationDAO dao;

	public Map controllData() throws Exception {
		Map rstMap = new HashMap();
		rstMap.put("total_parts", dao.totalcount());
		rstMap.put("notice", dao.notice());
		rstMap.put("top_banner", dao.top_banner());
		rstMap.put("event_banner", dao.event_banner());
		rstMap.put("recommend_parts", dao.partlist());
		return rstMap;
	}
}
