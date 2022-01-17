package com.mc.web.temp;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mc.common.util.StringUtil;
import com.mc.web.MCMap;
import com.mc.web.common.FileDAO;
import com.mc.web.common.FileUtil;

@Service
public class TransferService {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Value("#{config['upload.temp']}")
	private String TEMP_PATH;
	
	@Value("#{config['upload.board']}")
	private String UPLOAD_PATH;
	
	@Autowired
	private FileDAO fileDAO;
	
	@Autowired
	private FileUtil fileUtil;

	@Autowired
	private TransferErpDAO transferErpDAO;
	
	@Autowired
	private TransferHomeDAO transferHomeDAO;
	
	public String transfer(Map params, HttpServletRequest request) throws Exception {
		
		List<MCMap> list = transferErpDAO.list(params);
		for (MCMap mc : list) {
			transferHomeDAO.write(mc);
			if(!"".equals(mc.getStrNull("filepath"))){
				String[] filepath = mc.getStrNull("filepath").split(",");
				for (int i = 0; i < filepath.length; i++) {
					Map m = new HashMap();
					String savefilenm = StringUtil.split(mc.getStrNull("savefilenm"), ",")[i];
					m.put("uuid", UUID.randomUUID().toString());
					m.put("attach_nm", StringUtil.split(mc.getStrNull("orgfilenm"), ",")[i]);
					m.put("order_seq", i+1);
					m.put("table_nm", "NSH_GOODS");
					m.put("table_seq", mc.getStr("item_seq"));
					m.put("session_member_id", "insun");
					m.put("session_member_seq", "30");
					m.put("session_member_nm", "인선모터스");
					m.put("yyyy", StringUtil.split(mc.getStr("filepath"), ",")[i].substring(0, 4));
					m.put("mm", StringUtil.split(mc.getStr("filepath"), ",")[i].substring(4, 6));
					m.put("ip", "127.0.0.1");
					String path = request.getSession().getServletContext().getRealPath(UPLOAD_PATH + File.separator + m.get("yyyy") + File.separator + m.get("mm"));
					fileDAO.insert(m);
					fileUtil.copy(request.getSession().getServletContext().getRealPath("/upload/oldimg")+ File.separator + StringUtil.split(mc.getStr("filepath"), ",")[i] + File.separator + savefilenm, path + File.separator + m.get("uuid"),path);
				}
			}
		}
		return "성공";
	}
	
	public String transfer_file(Map params, HttpServletRequest request) throws Exception {
		String r = "결과 : ";
		List<MCMap> insun_list = transferHomeDAO.insun_itemseqlist();
		for (MCMap mcMap : insun_list) {
			MCMap mc = transferErpDAO.ready_insunitem(mcMap);
			if(!"".equals(mc.getStrNull("filepath"))){
				int cnt = 0;
				r += "<br/>: item_seq : "+mc.getStrNull("item_seq")+", insun_itemseq : "+mc.getStrNull("insun_itemseq");
				String[] filepath = mc.getStrNull("filepath").split(",");
				for (int i = 0; i < filepath.length; i++) {
					Map m = new HashMap();
					String savefilenm = StringUtil.split(mc.getStrNull("savefilenm"), ",")[i];
					m.put("uuid", UUID.randomUUID().toString());
					m.put("attach_nm", StringUtil.split(mc.getStrNull("orgfilenm"), ",")[i]);
					m.put("order_seq", i+1);
					m.put("table_nm", "NSH_GOODS");
					m.put("table_seq", mc.getStr("item_seq"));
					m.put("session_member_id", "insun");
					m.put("session_member_seq", "30");
					m.put("session_member_nm", "인선모터스");
					m.put("yyyy", StringUtil.split(mc.getStr("filepath"), ",")[i].substring(0, 4));
					m.put("mm", StringUtil.split(mc.getStr("filepath"), ",")[i].substring(4, 6));
					m.put("ip", "127.0.0.1");
					String path = request.getSession().getServletContext().getRealPath(UPLOAD_PATH + File.separator + m.get("yyyy") + File.separator + m.get("mm"));
					int rst = fileUtil.copy(request.getSession().getServletContext().getRealPath("/upload/oldimg")+ File.separator + StringUtil.split(mc.getStr("filepath"), ",")[i] + File.separator + savefilenm, path + File.separator + m.get("uuid"),path);
					rst = fileUtil.thumb(path + File.separator + m.get("uuid"), path + File.separator + m.get("uuid") + "_thumb" , 172, 118, false);
					if(rst>0){//파일복사 성공한 경우에만 인서트
						fileDAO.insert(m);
						cnt++;
					}
				}
				r += "count : " + cnt;
			}
		}
		
		return r;
	}
	
	public String transfer_thumb(Map params, HttpServletRequest request) throws Exception {
		String responseText = "";
		try
		{
			List<MCMap> goods_image_list = transferHomeDAO.goods_image_list();
			int count = 0;
			String gubun = "";

			for (MCMap mcMap : goods_image_list) {
				
				String path = request.getSession().getServletContext().getRealPath(UPLOAD_PATH + File.separator + mcMap.get("yyyy") + File.separator + mcMap.get("mm"));
				//responseText += "<br>" + "Item.seq ==> " + count + " target ==>" + path + "/" + mcMap.get("uuid");
				
				int result = fileUtil.thumb(path + "/" + mcMap.get("uuid"), path + "/" + mcMap.get("uuid") + "_thumb" , 172, 118, false);
				
				if(result == 1)
				{
					//responseText += "\t : 성공";
					logger.info("Item.seq ==> " + count + " target ==>" + path + "/" + mcMap.get("uuid") + "\t 성공");
				}
				else
				{
					//responseText += "\t : 실패";
					logger.info("Item.seq ==> " + count + " target ==>" + path + "/" + mcMap.get("uuid") + "\t 실패");
				}
				
				count ++;
			}
		}
		catch(Exception e)
		{
			
		}
		
		finally
		{
			logger.info("변환완료");
			responseText = "변환 완료!! 변환 내용은 로그 참고";
		}

		return responseText;
	}
	
}