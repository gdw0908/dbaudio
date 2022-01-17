package com.mc.web.test;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mc.web.MCMap;
import com.mc.web.common.FileUtil;

@Service
public class TestService {
	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private TestDAO testDAO;
	
	@Autowired
	private FileUtil fileUtil;
	
	@Value("#{config['upload.temp']}")
	private String TEMP_PATH;
	
	@Value("#{config['upload.board']}")
	private String UPLOAD_PATH;

	public MCMap test(Map<String, String> params) throws Exception {
		return testDAO.test(params);
	}

	public void add(Map<String, String> params) throws Exception {
		testDAO.add(params);
		testDAO.add(params);
	}
	
	public void thumb(HttpServletRequest request, HttpSession session) throws Exception{
		List list = testDAO.thumbList();
		for(int x=0;x < list.size();x++){
			Map m = (Map)list.get(x);
			fileUtil.compulsion_thumb(request.getSession().getServletContext().getRealPath((String)m.get("carall_uuid")),request.getSession().getServletContext().getRealPath((String)m.get("carall_uuid"))+"_thumb" , 172, 118, false);
		}
	}
}