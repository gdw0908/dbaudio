package com.mc.web.popupzone;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.When;
import com.mc.web.common.FileDAO;
import com.mc.web.common.FileUtil;

@Service
public class PopupzoneService {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private PopupzoneDAO popupzoneDAO;
	
	@Autowired
	private FileDAO fileDAO;
	
	@Autowired
	private FileUtil fileUtil;
	
	@Value("#{config['upload.temp']}")
	private String TEMP_PATH;
	@Value("#{config['upload.popup']}")
	private String UPLOAD_PATH;

	
	public Map view(Map params) throws Exception {
		Map rstMap = new HashMap();
		rstMap.put("view", popupzoneDAO.view(params));
		params.put("table_nm", "NSH_ARTICLE");
		params.put("table_seq", params.get("article_seq"));
		rstMap.put("files", fileDAO.list(params));
		return rstMap;
	}
	@TriggersRemove(cacheName={"popCache","mainCache"}, removeAll=true, when=When.AFTER_METHOD_INVOCATION)
	public Map write(HttpServletRequest request, Map params) throws Exception {
		Map rstMap = new HashMap();
		List list = (List) params.get("files");
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				Map m = (Map) list.get(i);
				String path = request.getSession().getServletContext().getRealPath(UPLOAD_PATH+"/"+m.get("yyyy")+"/"+m.get("mm"));
				fileUtil.copy(request.getSession().getServletContext().getRealPath(TEMP_PATH) + File.separator + m.get("uuid"), path + File.separator + m.get("uuid"),path);
				params.put("file_path", UPLOAD_PATH+"/"+m.get("yyyy")+"/"+m.get("mm")+"/"+m.get("uuid"));
			}
		}
		if(params.get("mobile") != null){
			String minit = (String)params.get("selecter") + (String)params.get("mobile");
			if(minit.equals("11")){
				popupzoneDAO.mobileInit();
			}
		}
		popupzoneDAO.write(params);
		rstMap.put("rst", "1");
		return rstMap;
	}
	@TriggersRemove(cacheName={"popCache","mainCache"}, removeAll=true, when=When.AFTER_METHOD_INVOCATION)
	public Map modify(HttpServletRequest request, Map params) throws Exception {
		Map rstMap = new HashMap();
		List list = (List) params.get("files");
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				Map m = (Map) list.get(i);
				String path = request.getSession().getServletContext().getRealPath(UPLOAD_PATH+"/"+m.get("yyyy")+"/"+m.get("mm"));
				fileUtil.copy(request.getSession().getServletContext().getRealPath(TEMP_PATH) + File.separator + m.get("uuid"), path + File.separator + m.get("uuid"),path);
				params.put("file_path", UPLOAD_PATH+"/"+m.get("yyyy")+"/"+m.get("mm")+"/"+m.get("uuid"));
			}
		}
		if(params.get("mobile") != null){
			String minit = (String)params.get("selecter") + (String)params.get("mobile");
			if(minit.equals("11")){
				popupzoneDAO.mobileInit();
			}
		}
		popupzoneDAO.modify(params);
		rstMap.put("rst", "1");
		return rstMap;
	}
	@TriggersRemove(cacheName={"popCache","mainCache"}, removeAll=true, when=When.AFTER_METHOD_INVOCATION)
	public Map del(Map params) throws Exception {
		Map rstMap = new HashMap();
		popupzoneDAO.del(params);
		rstMap.put("rst", "1");
		return rstMap;
	}
}