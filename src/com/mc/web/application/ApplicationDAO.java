package com.mc.web.application;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class ApplicationDAO extends EgovAbstractMapper{
	
	public Map totalcount() throws Exception {
		return (Map)selectByPk("application.totalcount", null);
	}
	
	public List notice() throws Exception {
		return list("application.notice", null);
	}
	
	public List top_banner() throws Exception{
		return list("application.top_banner",null); 
	}
	
	public List event_banner() throws Exception{
		return list("application.event_banner",null); 
	}
	
	public List partlist() throws Exception{
		return list("application.partlist",null); 
	}
	
	
	
}