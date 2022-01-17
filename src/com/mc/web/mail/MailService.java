package com.mc.web.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ser.std.StringCollectionSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mc.web.MCMap;
import com.mc.web.common.FileUtil;
import com.mc.web.common.mail.SendMailThread;
import com.mc.web.common.mail.SendMailThreadUser;
import com.mc.common.util.ExcelUtil;

@Service
public class MailService {
	
	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private MailTemplateDAO mailTemplateDAO;
	
	@Autowired
	private MailTargetDAO mailTargetDAO;
	
	@Autowired
	private MailSendDAO mailSendDAO;
	
	@Autowired
	private FileUtil fileUtil;
	
	@Autowired
	private SendMailThread sendMailThread;
	
	@Autowired
	private SendMailThreadUser sendMailThreadUser;
	
	@Value("#{config['upload.temp']}")
	private String TEMP_PATH;
	@Value("#{config['upload.mail']}")
	private String MAIL_PATH;
	
	/*===================템플릿 START===================*/
	public Map templateList(Map params) throws Exception {	//템플릿 리스트
		Map rstMap = new HashMap();
		if(params.get("cpage") == null || "".equals(params.get("cpage")))
			params.put("cpage","1");
		if(params.get("rows") == null || "".equals(params.get("rows")))
			params.put("rows","10");
		rstMap.put("list", mailTemplateDAO.getList(params));
		rstMap.put("pageinfo", mailTemplateDAO.getPageInfo(params));
		rstMap.put("param", params);
		return rstMap;
	}
	
	public int templateWrite(Map params) throws Exception { //템플릿 등록
		Map rstMap = new HashMap();
		return mailTemplateDAO.regist(params); 
	}
	
	public Map templateView(Map params) throws Exception { //템플릿 수정폼
		Map rstMap = new HashMap();
		rstMap.put("view", mailTemplateDAO.getArticle(params));
		return rstMap;
	}
	
	public Map templateModify(Map params) throws Exception { //템플릿 수정 업데이트
		Map rstMap = new HashMap();
		rstMap = (Map)mailTemplateDAO.getArticle(params);
		String seq = (String)rstMap.get("reg_seq");
		rstMap.clear();
		if(seq.equals(params.get("session_member_seq"))){
			rstMap.put("rst", mailTemplateDAO.modify(params));
		}else{
			rstMap.put("rst", "0");
			rstMap.put("message", "수정 권한이 없습니다.");
		}
		return rstMap;
	}
	
	public Map templateDelete(Map params) throws Exception { //템플릿 삭제
		Map rstMap = new HashMap();
		rstMap.put("rst", mailTemplateDAO.delete(params));
		rstMap.put("message", "삭제가 완료되었습니다.");
		return rstMap;
	}
	/*===================템플릿 END===================*/
	
	
	/*===================타겟 START===================*/
	public Map targetList(Map params) throws Exception {	//타겟 리스트
		Map rstMap = new HashMap();
		if(params.get("cpage") == null || "".equals(params.get("cpage")))
			params.put("cpage","1");
		if(params.get("rows") == null || "".equals(params.get("rows")))
			params.put("rows","10");
		
		rstMap.put("list", mailTargetDAO.getList(params));
		rstMap.put("pageinfo", mailTargetDAO.getPageInfo(params));
		rstMap.put("param", params);
		return rstMap;
	}
	
	public int targetWrite(Map params,HttpServletRequest request) throws Exception { //타겟 등록
		Map rstMap = new HashMap();
		String tg_seq = mailTargetDAO.maxseq(params);
		int nTargetCnt = 0;
		if("1".equals(params.get("target_cd")) && params.get("tg_xls_file_nm") != null && !"".equals(params.get("tg_xls_file_nm"))){
			String path = request.getSession().getServletContext().getRealPath(MAIL_PATH);
			fileUtil.copy(request.getSession().getServletContext().getRealPath(TEMP_PATH) + File.separator + params.get("tg_xls_file_nm"), path + File.separator + params.get("tg_xls_file_nm"),path);
			ExcelUtil eu = new ExcelUtil(path, (String)params.get("tg_xls_file_nm"));
			List<Map<String, String>> list = eu.getExcelData();
			Map<String, String> param;
			for (Map<String, String> m : list) {
				param = new HashMap<String, String>();
				param.put("tg_seq", tg_seq);
				param.put("name", m.get("0"));
				param.put("email", m.get("1"));
				mailTargetDAO.registTarget(param);
			}
			nTargetCnt = list.size();
		}else if("2".equals(params.get("target_cd"))){
			String conts = (String) params.get("tg_input_str");
			String [] sArray = conts.split("\n");
			String [] sSubArray;
			Map<String, String> param;
			for (String article : sArray) {
				sSubArray = article.split(";");
				param = new HashMap<String, String>();
				param.put("tg_seq", tg_seq);
				param.put("email", sSubArray[0]);
				if("".equals(sSubArray[1]))
					param.put("name", "");
				else	
					param.put("name", sSubArray[1]);
				mailTargetDAO.registTarget(param);
			}
			nTargetCnt = sArray.length;
		}else if("4".equals(params.get("target_cd"))){
			Map tg_chk_cd = (Map)params.get("tg_chk_cd");
			if(tg_chk_cd.get("0") != null && tg_chk_cd.get("0").equals(true)){
				params.put("tg_chk_cd", "0");
			}else{
				String tg_chk_cds = "";
				if(tg_chk_cd.get("2") != null &&tg_chk_cd.get("2").equals(true)){
					tg_chk_cds += "2,";
				}
				if(tg_chk_cd.get("3") != null &&tg_chk_cd.get("3").equals(true)){
					tg_chk_cds += "3,";
				}
				if(tg_chk_cd.get("4") != null &&tg_chk_cd.get("4").equals(true)){
					tg_chk_cds += "4,";
				}
				if(!tg_chk_cds.equals("")){
					tg_chk_cds = tg_chk_cds.substring(0, tg_chk_cds.length()-1);
				}
				params.put("tg_chk_cd", tg_chk_cds);
			}
			nTargetCnt = Integer.parseInt(mailTargetDAO.target_count(params));
		}
		
		params.put("tg_seq", tg_seq);
		params.put("tg_cnt", String.valueOf(nTargetCnt));
		return mailTargetDAO.regist(params); 
	}
	
	public Map targetView(Map params) throws Exception { //타겟 수정폼
		Map rstMap = new HashMap();
		rstMap.put("view", mailTargetDAO.getArticle(params));
		return rstMap;
	}
	
	public int targetModify(Map params,HttpServletRequest request) throws Exception { //템플릿 수정 업데이트
		Map rstMap = new HashMap();
		String tg_seq = (String)params.get("tg_seq");
		int nTargetCnt = 0;
		if("1".equals(params.get("target_cd"))){
			if(params.get("tg_xls_file_nm") != null && !"".equals(params.get("tg_xls_file_nm"))){
				if(params.get("old_tg_xls_file_nm") != null){
					if(!(params.get("old_tg_xls_file_nm")).equals(params.get("tg_xls_file_nm"))){
						mailTargetDAO.deleteTarget(params);
						params.put("tg_chk_cd", "");//초기화
						params.put("tg_input_str", "");//초기화
						String path = request.getSession().getServletContext().getRealPath(MAIL_PATH);
						fileUtil.copy(request.getSession().getServletContext().getRealPath(TEMP_PATH) + File.separator + params.get("tg_xls_file_nm"), path + File.separator + params.get("tg_xls_file_nm"),path);
						ExcelUtil eu = new ExcelUtil(path, (String)params.get("tg_xls_file_nm"));
						List<Map<String, String>> list = eu.getExcelData();
						Map<String, String> param;
						for (Map<String, String> m : list) {
							param = new HashMap<String, String>();
							param.put("tg_seq", tg_seq);
							param.put("name", m.get("0"));
							param.put("email", m.get("1"));
							mailTargetDAO.registTarget(param);
						}
						nTargetCnt = list.size();
						params.put("tg_cnt", String.valueOf(nTargetCnt));
					}
				}else{
					if(params.get("tg_xls_file_nm") != null){
						mailTargetDAO.deleteTarget(params);
						params.put("tg_chk_cd", "");//초기화
						params.put("tg_input_str", "");//초기화
						String path = request.getSession().getServletContext().getRealPath(MAIL_PATH);
						fileUtil.copy(request.getSession().getServletContext().getRealPath(TEMP_PATH) + File.separator + params.get("tg_xls_file_nm"), path + File.separator + params.get("tg_xls_file_nm"),path);
						ExcelUtil eu = new ExcelUtil(path, (String)params.get("tg_xls_file_nm"));
						List<Map<String, String>> list = eu.getExcelData();
						Map<String, String> param;
						for (Map<String, String> m : list) {
							param = new HashMap<String, String>();
							param.put("tg_seq", tg_seq);
							param.put("name", m.get("0"));
							param.put("email", m.get("1"));
							mailTargetDAO.registTarget(param);
						}
						nTargetCnt = list.size();
						params.put("tg_cnt", String.valueOf(nTargetCnt));
					}
				}
			}else{
				params.put("tg_xls_file_nm",params.get("old_tg_xls_file_nm"));
			}
		}else if("2".equals(params.get("target_cd"))){
			String conts = (String) params.get("tg_input_str");
			params.put("tg_chk_cd", "");//초기화
			params.put("tg_xls_file_nm", "");//초기화
			String [] sArray = conts.split("\n");
			String [] sSubArray;
			Map<String, String> param;
			mailTargetDAO.deleteTarget(params);
			for (String article : sArray) {
				sSubArray = article.split(";");
				param = new HashMap<String, String>();
				param.put("tg_seq", tg_seq);
				param.put("email", sSubArray[0]);
				if("".equals(sSubArray[1]))
					param.put("name", "");
				else	
					param.put("name", sSubArray[1]);
				mailTargetDAO.registTarget(param);
			}
			nTargetCnt = sArray.length;
			params.put("tg_cnt", String.valueOf(nTargetCnt));
		}else if("4".equals(params.get("target_cd"))){
			params.put("tg_xls_file_nm", "");//초기화
			params.put("tg_input_str", "");//초기화
			Map tg_chk_cd = (Map)params.get("tg_chk_cd");
			if(tg_chk_cd.get("0") != null && tg_chk_cd.get("0").equals(true)){
				params.put("tg_chk_cd", "0");
			}else{
				String tg_chk_cds = "";
				if(tg_chk_cd.get("2") != null &&tg_chk_cd.get("2").equals(true)){
					tg_chk_cds += "2,";
				}
				if(tg_chk_cd.get("3") != null &&tg_chk_cd.get("3").equals(true)){
					tg_chk_cds += "3,";
				}
				if(tg_chk_cd.get("4") != null &&tg_chk_cd.get("4").equals(true)){
					tg_chk_cds += "4,";
				}
				if(!tg_chk_cds.equals("")){
					tg_chk_cds = tg_chk_cds.substring(0, tg_chk_cds.length()-1);
				}
				params.put("tg_chk_cd", tg_chk_cds);
			}
			nTargetCnt = Integer.parseInt(mailTargetDAO.target_count(params));
			params.put("tg_cnt", String.valueOf(nTargetCnt));
		}
		
		params.put("tg_seq", tg_seq);
		return mailTargetDAO.modify(params);
	}
	
	public Map targetDelete(Map params) throws Exception { //타겟 삭제
		Map rstMap = new HashMap();
		mailTargetDAO.delete(params);
		mailTargetDAO.deleteTarget(params);
		rstMap.put("rst","1");		
		return rstMap;
	}
	/*===================타겟 END===================*/
	
	
	/*===================전송 START===================*/
	public Map sendList(Map params) throws Exception {	//전송 리스트
		Map rstMap = new HashMap();
		if(params.get("cpage") == null || "".equals(params.get("cpage")))
			params.put("cpage","1");
		if(params.get("rows") == null || "".equals(params.get("rows")))
			params.put("rows","10");
		rstMap.put("list", mailSendDAO.getList(params));
		rstMap.put("pageinfo", mailSendDAO.getPageInfo(params));
		rstMap.put("param", params);
		return rstMap;
	}
	
	public Map sendWrite(Map params) throws Exception { //전송 등록
		Map rstMap = new HashMap();
		rstMap.put("rst", mailSendDAO.regist(params));
		return rstMap;
	}
	
	public Map sendView(Map params) throws Exception { //전송 수정폼
		Map rstMap = new HashMap();
		rstMap.put("view", mailSendDAO.getArticle(params));
		return rstMap;
	}
	
	public Map sendModify(Map params) throws Exception { //전송 수정 업데이트
		Map rstMap = new HashMap();
		rstMap = (Map)mailSendDAO.getArticle(params);
		String seq = (String)rstMap.get("reg_seq");
		rstMap.clear();
		if(seq.equals(params.get("session_member_seq"))){
			rstMap.put("rst", mailSendDAO.modify(params));
		}else{
			rstMap.put("rst", "0");
			rstMap.put("message", "수정 권한이 없습니다.");
		}
		return rstMap;
	}
	
	
	public Map sendDelete(Map params) throws Exception { //전송 삭제
		Map rstMap = new HashMap();
		rstMap.put("rst", mailSendDAO.delete(params));
		rstMap.put("message", "삭제가 완료되었습니다.");
		return rstMap;
	}
	
	
	
	public void send(Map params) throws Exception{ // 이메일 발송
		Map rstMap = new HashMap();
		List targetList = null;
		MCMap sendItem = (MCMap) mailSendDAO.getArticle(params);
		MCMap targetItem = (MCMap) mailTargetDAO.getArticle(sendItem);
		MCMap templetItem = (MCMap) mailTemplateDAO.getArticle(sendItem);
		mailSendDAO.updateStart(params);	//발송시작 업데이트
		if("4".equals(targetItem.get("target_cd"))){
			targetList = mailTargetDAO.mb_info(targetItem);
		}else{
			targetList = mailTargetDAO.targetList(sendItem);
		}
		
		sendMailThread.setTargetList(targetList);
		sendMailThread.setFromEmail((String) sendItem.get("from_email"));
		sendMailThread.setFromNm((String) sendItem.get("from_nm"));
		sendMailThread.setTitle((String) sendItem.get("title"));
		sendMailThread.setConts((String) templetItem.get("conts"));
		sendMailThread.setSeq(sendItem.get("sd_seq").toString());
		sendMailThread.run();
	}
	
	public void sendUser(Map params) throws Exception{ // 이메일 발송
		Map rstMap = new HashMap();
		sendMailThreadUser.setFromEmail((String) params.get("from_email"));//보내는사람 이메일주소
		sendMailThreadUser.setFromNm((String) params.get("from_nm"));//보내는사람 이름
		sendMailThreadUser.setTitle((String) params.get("title"));//제목
		sendMailThreadUser.setConts((String) params.get("conts"));//내용
		sendMailThreadUser.setToEmail((String) params.get("to_email"));//받는사람 이메일
		sendMailThreadUser.run();
	}
	
	/*===================전송 END===================*/
	

}
