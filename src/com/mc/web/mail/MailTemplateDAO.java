package com.mc.web.mail;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class MailTemplateDAO extends EgovAbstractMapper {
	
	public List getList(Map<String, String> params) {
		return list("mail_templet.list", params);
	}
	
	public Object getPageInfo(Map<String, String> params) {
		return  selectByPk("mail_templet.pageInfo", params);
	}
	
	public Object getArticle(Map<String, String> params) {
		return selectByPk("mail_templet.article", params);
	}
	
	public int regist(Map<String, String> params) {
		return insert("mail_templet.regist", params);
	}
	
	public int delete(Map<String, String> params) {
		return update("mail_templet.delete", params);
	}
	
	public int modify(Map<String, String> params) {
		return update("mail_templet.modify", params);
	}

}
