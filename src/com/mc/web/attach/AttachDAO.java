package com.mc.web.attach;

import java.io.File;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;
import com.mc.web.Globals;
import com.mc.web.MCMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class AttachDAO extends EgovAbstractMapper {
	@Resource(name = "globals")
	private Globals globals;
	
	public void regist(Map<String, String> params) {
		insert("attach.insert", params);
	}
	
	public MCMap getArticle(Map<String, String> params){
		return (MCMap) selectByPk("attach.article", params);
	}
	
	
	public void remove(Map<String, String> params) {
		MCMap article = getArticle(params);
		File file = new File(globals.getFILE_IN_PATH() + "/" + article.get("yyyy") + "/" + article.get("mm") + "/" + params.get("uuid"));
		if(file.exists())
			file.delete();
		delete("attach.delete", params);
	}
	
	public void remove(Map<String, String> params, String FilePath) {
		MCMap article = getArticle(params);
		File file = new File(FilePath + "/" + article.get("yyyy") + "/" + article.get("mm") + "/" + params.get("uuid"));
		if(file.exists())
			file.delete();
		delete("attach.delete", params);
	}
	
	public Object getList(Map<String, String> params){
		return list("attach.list", params);
	}
	
	public List getListAll(Map<String, Object> params){
		return list("attach.listAll", params);
	}
	
	public Object getListMore(Map<String, String> params){
		return list("attach.listMore", params);
	}	
	
	public void removeAll(Map<String, String> params) {
		List<MCMap> list = (List<MCMap>) getList(params);
		File file;
		for(MCMap article : list){
			file = new File(globals.getFILE_IN_PATH() + "/" + article.get("yyyy") + "/" + article.get("mm") + "/" + article.get("uuid"));
			if(file.exists())
				file.delete();
		}
		delete("attach.delete_all", params);
	}

	public MCMap getPreArticle(Map<String, String> params) {
		return (MCMap) selectByPk("attach.pre_article", params);
	}
}
