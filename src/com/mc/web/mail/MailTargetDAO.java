package com.mc.web.mail;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class MailTargetDAO extends EgovAbstractMapper {
	
	public List getList(Map<String, String> params) {
		return list("mail_target.list", params);
	}
	
	public List targetList(Map<String, String> params) {
		return list("mail_target.targetList", params);
	}
	
	public Object getArticle(Map<String, String> params) {
		return selectByPk("mail_target.article", params);
	}
	
	public Object getPageInfo(Map<String, String> params) {
		return selectByPk("mail_target.pageInfo", params);
	}
	
	public String maxseq(Map<String, String> params) {
		return (String) selectByPk("mail_target.maxseq", params);
	}
	
	public int regist(Map<String, String> params) {
		return insert("mail_target.regist", params);
	}
	
	public void registTarget(Map<String, String> params) {
		insert("mail_target.registTarget", params);
	}
	
	public void delete(Map<String, String> params) {
		update("mail_target.delete", params);
	}
	
	public void deleteTarget(Map<String, String> params) {
		delete("mail_target.deleteTarget", params);
	}
	
	public int modify(Map<String, String> params) {
		return update("mail_target.modify", params);
	}
	
	public List getQueryList(Map<String, String> params) {
		return list("mail_target.getQueryList", params);
	}

	public String target_count(Map<String, String> params) {
		return (String) selectByPk("mail_target.target_count",params);
	}
	
	public List mb_info(Map<String, String> params) {
		return list("mail_target.mb_info",params);
	}

	public String mb_List(Map<String, String> params) {
		return (String) selectByPk("mail_target.mb_List",params);
	}

}
