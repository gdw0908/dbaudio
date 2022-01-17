package com.mc.web.mail;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class MailSendDAO extends EgovAbstractMapper {
	
	public Object getList(Map<String, String> params) {
		return list("mail_send.list", params);
	}
	
	public Map getPageInfo(Map<String, String> params) {
		return (Map) selectByPk("mail_send.pageInfo", params);
	}
	
	public Object getArticle(Map<String, String> params) {
		return selectByPk("mail_send.article", params);
	}
	
	public int regist(Map<String, String> params) {
		return insert("mail_send.regist", params);
	}
	
	public int delete(Map<String, String> params) {
		return update("mail_send.delete", params);
	}
	
	public int modify(Map<String, String> params) {
		return update("mail_send.modify", params);
	}
	
	public void updateStart(Map<String, String> params) {
		update("mail_send.updateStart", params);
	}
	
	public void updateEnd(String params) {
		update("mail_send.updateEnd", params);
	}

}
