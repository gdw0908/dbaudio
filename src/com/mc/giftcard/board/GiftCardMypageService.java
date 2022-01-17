package com.mc.giftcard.board;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mc.common.util.StringUtil;
import com.mc.web.attach.AttachDAO;
import com.mc.web.common.FileDAO;
import com.mc.web.common.FileUtil;
import com.mc.common.util.StringUtil;;

@Service
public class GiftCardMypageService {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private GiftCardBoardDAO boardDAO;
	
	@Autowired
	private FileDAO fileDAO;
	
	@Autowired
	private FileUtil fileUtil;
	
	@Autowired
	private AttachDAO attachDAO;
	
	@Value("#{config['upload.temp']}")
	private String TEMP_PATH;
	@Value("#{config['upload.board']}")
	private String UPLOAD_PATH;

	
	/*
	 * 1 : 공지사항 ( 관리자 )
	 * 2 : 자주묻는질문 ( 관리자 )
	 * 3 : 협력사문의 ( 비회원 )
	 * 4 : 1:1문의 = 중고부품견적문의 = 카올바로( 비회원 )
	 * 5 : 구매후기 ( 회원 )
	 * 6 : 국산차 부품문의 ( 비회원 )
	 * 7 : 자원관리 ( 협력사 )
	 * 8 : 수입차 부품문의 ( 비회원 )
	 * 9 : 관리자게시판 ( 관리자 전용 )
	 * 10 : 회원사 공지 게시판 ( 관리자 전용 )
	 * 11 : 카올바로 정비사례 보러가기 ( 관리자 전용 )
	 * 12 : 파츠모아 이야기 ( 관리자 )
	 */
	public void boardSelect(Map params,String folder) throws Exception{
		String path = (String)params.get("path");
		if(path.equals("notice")){
			params.put("board_seq", "1");
		}else if(path.equals("QA")){
			params.put("board_seq", "2");
		}else if(path.equals("forces")){
			params.put("board_seq", "3");
		}else if(path.equals("mantoman")){
			params.put("board_seq", "4");
		}else if(path.equals("late")){
			params.put("board_seq", "5");
		}else if(path.equals("application")){
			params.put("board_seq", "6");
		}else if(path.equals("application_import")){
			params.put("board_seq", "8");
		}else if(path.equals("mantoman_late")){
			params.put("board_seq", "11");
		}else if(path.equals("story")){
			params.put("board_seq", "12");
		}
		
		String[] folderName = folder.split("/");
		if(folderName[1].equals("mobile")){
			params.put("folder", "/giftcard/mobile/mypage");
			params.put("img", "mobile/");
		}else if(folderName[1].equals("pop")){
			params.put("folder", "/giftcard/pop/mypage");
			params.put("img", "");
		}else{
			params.put("folder", "/giftcard/mypage");
			params.put("img", "");
		}
	}
	
	public String commentPcMobile(String folder) throws Exception{
		String[] folderName = folder.split("/");
		String result = "";
		if(folderName[1].equals("mobile")){
			result = "mobile/";
		}
		return result;
	}
	
	public Map list(Map params) throws Exception{
		Map rstMap = new HashMap();
		List list = null;
		
		if("12".equals(params.get("board_seq"))){
			if(StringUtil.isEmptyByParam(params, "cpage")){
				params.put("cpage", "1");
			}
			params.put("rows", "12");
		}else{
			if(StringUtil.isEmptyByParam(params, "cpage"))
				params.put("cpage", "1");
			if(StringUtil.isEmptyByParam(params, "rows"))
				params.put("rows", "10");
		}
		params.put("table_nm", "NSH_ARTICLE");
		if(Integer.parseInt((String)params.get("board_seq")) == 2){
			if(StringUtil.isEmptyByParam(params, "group_code"))
				params.put("group_code", "1");
			if(StringUtil.isEmptyByParam(params, "cate_seq"))
				params.put("cate_seq", "0");
			list = (List)boardDAO.list_category(params);
			List boardList = new ArrayList();
			for(int x=0; x < list.size(); x++){
				Map map = new HashMap();
				map = (Map)list.get(x);
				String title = (String)map.get("title");
				map.put("title", StringUtil.clearXSS(title,""));
				boardList.add(map);
			}
			rstMap.put("list", boardList);
			rstMap.put("pageInfo", boardDAO.page_info_category(params));
			rstMap.put("category", boardDAO.categoryList(params));
		}else{
			list = (List)boardDAO.list(params);
			List boardList = new ArrayList();
			for(int x=0; x < list.size(); x++){
				Map map = new HashMap();
				map = (Map)list.get(x);
				String title = (String)map.get("title");
				map.put("title", StringUtil.clearXSS(title,""));
				boardList.add(map);
			}
			rstMap.put("list", boardList);
			rstMap.put("pageInfo", boardDAO.page_info(params));
		}		
		return rstMap;		
	}
	
	public Map view(Map params) throws Exception {
		Map rstMap = new HashMap();
		Map view = boardDAO.view(params);
		view.put("conts", StringUtil.clearXSS((String)view.get("conts")));
		view.put("title", StringUtil.clearXSS((String)view.get("title")));
		rstMap.put("view", view);
		List list = (List) boardDAO.commentList(params);
		List commentList = new ArrayList();
		for(int x=0; x < list.size(); x++){
			Map map = new HashMap();
			map = (Map)list.get(x);
			String conts = (String)map.get("conts");
			map.put("conts", StringUtil.clearXSS(conts,"br"));
			commentList.add(map);
		}
		rstMap.put("comment",commentList);
		params.put("table_seq", params.get("article_seq"));
		params.put("table_nm", "NSH_ARTICLE");
		rstMap.put("files", fileDAO.list(params));
		return rstMap;
	}
	
	public Map write(List<MultipartFile> attachList, Map params) throws Exception {
		Map rstMap = new HashMap();
		List<Map<String,String>> fileList = new ArrayList<Map<String,String>>();
		if(attachList != null){
			fileList = fileUtil.upload(attachList, 10, (String)params.get("upload_path"));
			for (int i = 0; i < fileList.size(); i++) {
				Map m = (Map) fileList.get(i);
				m.put("order_seq", i+1);
				m.put("table_nm", "NSH_ARTICLE");
				m.put("table_seq", params.get("article_seq"));//mysql auto_increment 의 값을 가져온 값
				m.put("session_member_id", params.get("session_member_group_seq"));
				m.put("session_member_seq", params.get("session_member_seq"));
				m.put("session_member_nm", params.get("session_member_nm"));
				m.put("ip", params.get("ip"));
				//String path = request.getSession().getServletContext().getRealPath(UPLOAD_PATH+"/"+m.get("yyyy")+"/"+m.get("mm"));
				fileDAO.insert(m);
				//fileUtil.copy(request.getSession().getServletContext().getRealPath(TEMP_PATH) + File.separator + m.get("uuid"), path + File.separator + m.get("uuid"),path);
			}
		}	
		
		boardDAO.write(params);
		rstMap.put("rst", "1");
		return rstMap;
	}
	
	public Map modify(List<MultipartFile> attachList, List<String> delAttachList, Map params) throws Exception {
		Map rstMap = new HashMap();
		List<Map<String,String>> fileList = new ArrayList<Map<String,String>>();
		boardDAO.modify(params);
		params.put("table_seq", params.get("article_seq"));
		params.put("table_nm", "NSH_ARTICLE");
		if(attachList != null){
			fileList = fileUtil.upload(attachList, 10, (String)params.get("upload_path"));
			for (int i = 0; i < fileList.size(); i++) {
				Map m = (Map) fileList.get(i);
				m.put("order_seq", i+1);
				m.put("table_nm", "NSH_ARTICLE");
				m.put("table_seq", params.get("article_seq"));//mysql auto_increment 의 값을 가져온 값
				m.put("session_member_id", params.get("session_member_group_seq"));
				m.put("session_member_seq", params.get("session_member_seq"));
				m.put("session_member_nm", params.get("session_member_nm"));
				m.put("ip", params.get("ip"));
				fileDAO.insert(m);
			}
		}
		Map delMap = new HashMap();
		if(delAttachList != null){
			for (int i = 0; i < delAttachList.size(); i++) {
				delMap.put("uuid", delAttachList.get(i));
				attachDAO.remove(delMap,(String)params.get("upload_path"));
			}
		}
		rstMap.put("rst", "1");
		return rstMap;
	}
	
	public Map del(Map params) throws Exception {
		Map rstMap = new HashMap();
		boardDAO.del(params);
		rstMap.put("rst", "1");
		return rstMap;
	}

	public Map commentWrite(Map params) throws Exception {
		Map rstMap = new HashMap();
		params.put("conts", ((String)params.get("conts")).replaceAll("\n", "<br/>"));
		boardDAO.commentWrite(params);
		boardDAO.articleCommentCnt(params);
		rstMap.put("rst", "1");
		List list = (List) boardDAO.commentList(params);
		List commentList = new ArrayList();
		for(int x=0; x < list.size(); x++){
			Map map = new HashMap();
			map = (Map)list.get(x);
			String conts = (String)map.get("conts");
			map.put("conts", StringUtil.clearXSS(conts,"br"));
			commentList.add(map);
		}
		rstMap.put("comment",commentList);
		rstMap.put("article_seq", params.get("article_seq"));
		rstMap.put("session_id", params.get("reg_id"));
		rstMap.put("servletPath", params.get("servletPath"));
		rstMap.put("boolean", "true");
		rstMap.put("img", commentPcMobile((String)params.get("servletPath")));
		return rstMap;
	}

	public Map commentReply(Map params) throws Exception {
		Map rstMap = new HashMap();
		Map commentInfo = new HashMap();
		commentInfo = boardDAO.commentReplyInfo(params);
		params.put("grp", commentInfo.get("grp"));
		params.put("seq", commentInfo.get("seq"));
		boardDAO.commentReplyInfoUpdate(params);
		params.put("reply_id", commentInfo.get("reg_id"));		
		params.put("article_seq", commentInfo.get("article_seq"));
		params.put("lvl", String.valueOf((Integer.parseInt((String)commentInfo.get("lvl"))+1)));		
		params.put("seq", String.valueOf((Integer.parseInt((String)commentInfo.get("seq"))+1)));
		params.put("conts", ((String)params.get("conts")).replaceAll("\n", "<br/>"));
		boardDAO.commentWrite(params);
		boardDAO.articleCommentCnt(params);
		rstMap.put("rst", "1");
		List list = (List) boardDAO.commentList(params);
		List commentList = new ArrayList();
		for(int x=0; x < list.size(); x++){
			Map map = new HashMap();
			map = (Map)list.get(x);
			String conts = (String)map.get("conts");
			map.put("conts", StringUtil.clearXSS(conts,"br"));
			commentList.add(map);
		}
		rstMap.put("comment",commentList);
		rstMap.put("article_seq", params.get("article_seq"));
		rstMap.put("session_id", params.get("reg_id"));
		rstMap.put("servletPath", params.get("servletPath"));
		rstMap.put("boolean", "true");
		rstMap.put("img", commentPcMobile((String)params.get("servletPath")));
		return rstMap;
	}

	public Map commentDel(Map params) throws Exception {
		Map rstMap = new HashMap();
		Map commentInfo = new HashMap();
		commentInfo = boardDAO.commentReplyInfo(params);
		if(params.get("reg_id").equals(commentInfo.get("reg_id"))){
			params.put("grp", commentInfo.get("grp"));
			params.put("seq", commentInfo.get("seq"));
			params.put("lvl", commentInfo.get("lvl"));
			params.put("article_seq", commentInfo.get("article_seq"));
			boardDAO.commentDelReply(params);
			boardDAO.commentDel(params);
			boardDAO.articleCommentCnt(params);
			params.put("reply_id", commentInfo.get("reg_id"));		
			params.put("article_seq", commentInfo.get("article_seq"));
			boardDAO.articleCommentCnt(params);
			rstMap.put("rst", "1");
			List list = (List) boardDAO.commentList(params);
			List commentList = new ArrayList();
			for(int x=0; x < list.size(); x++){
				Map map = new HashMap();
				map = (Map)list.get(x);
				String conts = (String)map.get("conts");
				map.put("conts", StringUtil.clearXSS(conts,"br"));
				commentList.add(map);
			}
			rstMap.put("comment",commentList);
			rstMap.put("article_seq", params.get("article_seq"));
			rstMap.put("session_id", params.get("reg_id"));
			rstMap.put("servletPath", params.get("servletPath"));
			rstMap.put("boolean", "true");
			rstMap.put("img", commentPcMobile((String)params.get("servletPath")));
		}else{
			rstMap.put("rst", "2");
			rstMap.put("boolean", "false");
		}
		return rstMap;
	}

	public Map commentReplyUpdate(Map params) throws Exception {
		Map rstMap = new HashMap();
		Map commentInfo = new HashMap();
		commentInfo = boardDAO.commentReplyInfo(params);
		if(params.get("reg_id").equals(commentInfo.get("reg_id"))){
			params.put("conts", ((String)params.get("conts")).replaceAll("\n", "<br/>"));
			boardDAO.commentReplyUpdate(params);
			params.put("article_seq", commentInfo.get("article_seq"));
			rstMap.put("rst", "1");
			List list = (List) boardDAO.commentList(params);
			List commentList = new ArrayList();
			for(int x=0; x < list.size(); x++){
				Map map = new HashMap();
				map = (Map)list.get(x);
				String conts = (String)map.get("conts");
				map.put("conts", StringUtil.clearXSS(conts,"br"));
				commentList.add(map);
			}
			rstMap.put("comment",commentList);
			rstMap.put("article_seq", commentInfo.get("article_seq"));
			rstMap.put("session_id", params.get("reg_id"));
			rstMap.put("servletPath", params.get("servletPath"));
			rstMap.put("boolean", "true");
			rstMap.put("img", commentPcMobile((String)params.get("servletPath")));
		}else{
			rstMap.put("rst", "2");
			rstMap.put("boolean", "false");
		}
		return rstMap;
	}
	
	
	
	public Map resourcesWrite(List<MultipartFile> attachList, Map params) throws Exception {
		Map rstMap = new HashMap();
		List list = (List) params.get("resources");
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				Map m = (Map) list.get(i);
				m.put("article_seq", params.get("article_seq"));
				boardDAO.resourcesItemInsert(m);
			}
		}
		if(!("8".equals(params.get("session_member_group_seq")) || "1".equals(params.get("session_member_group_seq")))){
			Map com_parent = (Map)boardDAO.selectByPk("article.cooperation_parentSeq", params);
			String title = "";
			if(com_parent != null){
				if(com_parent.get("parent_seq") != null){
					title = (String)com_parent.get("parent_seq");
				}else{
					throw new Exception("NotCooperation");
				}
			}else{
				throw new Exception("NotCooperation");
			}
			params.put("title",title);
		}
		List<Map<String,String>> fileList = new ArrayList<Map<String,String>>();
		if(attachList != null){
			fileList = fileUtil.upload(attachList, 10, (String)params.get("upload_path"));
			for (int i = 0; i < fileList.size(); i++) {
				Map m = (Map) fileList.get(i);
				m.put("order_seq", i+1);
				m.put("table_nm", "NSH_ARTICLE");
				m.put("table_seq", params.get("article_seq"));//mysql auto_increment 의 값을 가져온 값
				m.put("session_member_id", params.get("session_member_group_seq"));
				m.put("session_member_seq", params.get("session_member_seq"));
				m.put("session_member_nm", params.get("session_member_nm"));
				m.put("ip", params.get("ip"));
				//String path = request.getSession().getServletContext().getRealPath(UPLOAD_PATH+"/"+m.get("yyyy")+"/"+m.get("mm"));
				fileDAO.insert(m);
				//fileUtil.copy(request.getSession().getServletContext().getRealPath(TEMP_PATH) + File.separator + m.get("uuid"), path + File.separator + m.get("uuid"),path);
			}
		}	
		
		boardDAO.write(params);
		rstMap.put("rst", "1");
		return rstMap;
	}
	
	public Map resourcesModify(List<MultipartFile> attachList, List<String> delAttachList, Map params) throws Exception {
		Map rstMap = new HashMap();
		boardDAO.modify(params);
		List list = (List) params.get("resources");
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				Map m = (Map) list.get(i);
				m.put("article_seq", params.get("article_seq"));
				if(m.get("del_yn") == null){
					boardDAO.resourcesItemInsert(m);
				}else{
					boardDAO.resourcesItemUpdate(m);
				}
			}
		}
		
		List<Map<String,String>> fileList = new ArrayList<Map<String,String>>();
		params.put("table_seq", params.get("article_seq"));
		params.put("table_nm", "NSH_ARTICLE");
		if(attachList != null){
			fileList = fileUtil.upload(attachList, 10, (String)params.get("upload_path"));
			for (int i = 0; i < fileList.size(); i++) {
				Map m = (Map) fileList.get(i);
				m.put("order_seq", i+1);
				m.put("table_nm", "NSH_ARTICLE");
				m.put("table_seq", params.get("article_seq"));//mysql auto_increment 의 값을 가져온 값
				m.put("session_member_id", params.get("session_member_group_seq"));
				m.put("session_member_seq", params.get("session_member_seq"));
				m.put("session_member_nm", params.get("session_member_nm"));
				m.put("ip", params.get("ip"));
				fileDAO.insert(m);
			}
		}
		Map delMap = new HashMap();
		if(delAttachList != null){
			for (int i = 0; i < delAttachList.size(); i++) {
				delMap.put("uuid", delAttachList.get(i));
				attachDAO.remove(delMap,(String)params.get("upload_path"));
			}
		}
		rstMap.put("rst", "1");
		return rstMap;
	}
}