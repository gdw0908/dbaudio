package com.mc.giftcard.board;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mc.web.common.FileDAO;
import com.mc.web.common.FileUtil;

@Service
public class GiftCardBoardService {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private GiftCardBoardDAO boardDAO;
	
	@Autowired
	private FileDAO fileDAO;
	
	@Autowired
	private FileUtil fileUtil;
	
	@Value("#{config['upload.temp']}")
	private String TEMP_PATH;
	@Value("#{config['upload.board']}")
	private String UPLOAD_PATH;

	
	public Map view(Map params) throws Exception {
		Map rstMap = new HashMap();
		rstMap.put("view", boardDAO.view(params));
		params.put("table_nm", "NSH_ARTICLE");
		params.put("table_seq", params.get("article_seq"));
		rstMap.put("files", fileDAO.list(params));
		return rstMap;
	}
	
	public Map write(HttpServletRequest request, Map params) throws Exception {
		Map rstMap = new HashMap();
		
		List list = (List) params.get("files");
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				Map m = (Map) list.get(i);
				m.put("order_seq", i+1);
				m.put("table_nm", "NSH_ARTICLE");
				m.put("table_seq", params.get("article_seq"));//mysql auto_increment 의 값을 가져온 값
				m.put("session_member_id", params.get("session_member_group_seq"));
				m.put("session_member_seq", params.get("session_member_seq"));
				m.put("session_member_nm", params.get("session_member_nm"));
				m.put("ip", params.get("ip"));
				String path = request.getSession().getServletContext().getRealPath(UPLOAD_PATH+"/"+m.get("yyyy")+"/"+m.get("mm"));
				fileDAO.insert(m);
				fileUtil.copy(request.getSession().getServletContext().getRealPath(TEMP_PATH) + File.separator + m.get("uuid"), path + File.separator + m.get("uuid"),path);
				fileUtil.compulsion_thumb(request.getSession().getServletContext().getRealPath(TEMP_PATH) + File.separator + m.get("uuid"), path + File.separator + m.get("uuid")+"_thumb" , 172, 118, false);
			}
		}
		if(params.get("thumb") != null && !("").equals(params.get("thumb"))){
			String thumb = (String)params.get("thumb");
			String[] thumb_array = thumb.split("/");
			System.out.println(request.getSession().getServletContext().getRealPath(File.separator+thumb_array[1]+File.separator+thumb_array[2]));
			String filepath = request.getSession().getServletContext().getRealPath(File.separator+thumb_array[1]+File.separator+thumb_array[2])+File.separator + thumb_array[3];
			int resultInt = fileUtil.thumb(filepath, filepath+"_thumb" , 172, 118, false);
			if(resultInt == 1){
				params.put("thumb",params.get("thumb")+"_thumb");
			}
		}
		
		boardDAO.write(params);
		rstMap.put("rst", "1");
		return rstMap;
	}
	
	public Map modify(HttpServletRequest request, Map params) throws Exception {
		Map rstMap = new HashMap();
		
		if(params.get("thumb") != null && !("").equals(params.get("thumb"))){
			String thumb = (String)params.get("thumb");
			String[] thumb_array = thumb.split("/");
			System.out.println(request.getSession().getServletContext().getRealPath(File.separator+thumb_array[1]+File.separator+thumb_array[2]));
			String filepath = request.getSession().getServletContext().getRealPath(File.separator+thumb_array[1]+File.separator+thumb_array[2])+File.separator + thumb_array[3];
			int resultInt = fileUtil.thumb(filepath, filepath+"_thumb" , 172, 118, false);
			if(resultInt == 1){
				params.put("thumb",params.get("thumb")+"_thumb");
			}
		}
		
		
		boardDAO.modify(params);
		
		params.put("table_seq", params.get("article_seq"));
		params.put("table_nm", "NSH_ARTICLE");
		fileDAO.delete_all(params);
		List list = (List) params.get("files");
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				Map m = (Map) list.get(i);
				m.put("order_seq", i+1);
				m.put("table_nm", "NSH_ARTICLE");
				m.put("table_seq", params.get("article_seq"));
				m.put("session_member_id", params.get("session_member_id"));
				m.put("session_member_seq", params.get("session_member_seq"));
				m.put("session_member_nm", params.get("session_member_nm"));
				m.put("ip", params.get("ip"));
				String path = request.getSession().getServletContext().getRealPath(UPLOAD_PATH + File.separator + m.get("yyyy") + File.separator + m.get("mm"));
				fileDAO.insert(m);
				fileUtil.copy(request.getSession().getServletContext().getRealPath(TEMP_PATH) + File.separator + m.get("uuid"), path + File.separator + m.get("uuid"),path);
				fileUtil.compulsion_thumb(request.getSession().getServletContext().getRealPath(TEMP_PATH) + File.separator + m.get("uuid"), path + File.separator + m.get("uuid")+"_thumb" , 172, 118, false);
			}
		}
		
		list = (List) params.get("removeFiles");
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				Map m = (Map) list.get(i);
				fileUtil.delete(request.getSession().getServletContext().getRealPath(UPLOAD_PATH) + File.separator + m.get("yyyy") + File.separator + m.get("mm") + File.separator + m.get("uuid"));
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
	
	/*========== Resources Start ==========*/
	
	public List resourcesExcel(Map params) throws Exception {
		Map rstMap = new HashMap();
		Map cateMap = new HashMap();
		cateMap = (JSONObject) JSONValue.parse((String)params.get("cate_seq"));
		if(!cateMap.isEmpty()){
			List list = new ArrayList(cateMap.values());
			List getList = new ArrayList();
			for(int x=0; x < list.size(); x++){
				if(!list.get(x).equals(""))
					getList.add(list.get(x));
			}			
			if(getList.size() > 0){
				params.put("cate_seq_arr", getList);
			}
		}
		return boardDAO.resourcesExcel(params);
	}
	
	public Map resourcesList(Map params) throws Exception {
		Map rstMap = new HashMap();
		Map cateMap = new HashMap();
		if(params.get("user") == null ){
			cateMap = (Map)params.get("cate_seq");
			if(!cateMap.isEmpty()){
				List list = new ArrayList(cateMap.values());
				List getList = new ArrayList();
				for(int x=0; x < list.size(); x++){
					if(!list.get(x).equals(""))
						getList.add(list.get(x));
				}
				if(getList.size() > 0){
					params.put("cate_seq_arr", getList);
				}
			}
		}else{
			if(params.get("cate_seq") != null){
				String cate_seq = (String)params.get("cate_seq");
				if(!cate_seq.equals("")){
					List getList = new ArrayList();
					getList.add(cate_seq);
					params.put("cate_seq_arr", getList);
				}
			}
		}
		List<Map> list = (List<Map>)boardDAO.resourcesView(params);
		List fileList = new ArrayList();
		for(Map m : list){
			Map getFile = new HashMap();
			getFile.put("table_seq",m.get("article_seq"));
			getFile.put("table_nm","NSH_ARTICLE");
			fileList.add(fileDAO.list(getFile));
		}
		rstMap.put("list", list);
		rstMap.put("filelist", fileList);
		rstMap.put("pageinfo", boardDAO.resourcesListPageInfo(params));
		return rstMap;
	}
	
	public Map resourcesView(Map params) throws Exception {
		Map rstMap = new HashMap();
		rstMap.put("view", boardDAO.resourcesView(params));
		rstMap.put("resources", boardDAO.resourcesList(params));
		params.put("table_nm", "NSH_ARTICLE");
		params.put("table_seq", params.get("article_seq"));
		rstMap.put("files", fileDAO.list(params));
		return rstMap;
	}
	
	public Map resourcesWrite(HttpServletRequest request, Map params) throws Exception {
		Map rstMap = new HashMap();
		List list = (List) params.get("resources");
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				Map m = (Map) list.get(i);
				m.put("article_seq", params.get("article_seq"));
				boardDAO.resourcesItemInsert(m);
			}
		}
		if(!"8".equals(params.get("session_member_group_seq"))){
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
		List flist = (List) params.get("files");
		if(flist != null){
			for (int i = 0; i < flist.size(); i++) {
				Map m = (Map) flist.get(i);
				m.put("order_seq", i+1);
				m.put("table_nm", "NSH_ARTICLE");
				m.put("table_seq", params.get("article_seq"));//mysql auto_increment 의 값을 가져온 값
				m.put("session_member_id", params.get("session_member_group_seq"));
				m.put("session_member_seq", params.get("session_member_seq"));
				m.put("session_member_nm", params.get("session_member_nm"));
				m.put("ip", params.get("ip"));
				String path = request.getSession().getServletContext().getRealPath(UPLOAD_PATH+"/"+m.get("yyyy")+"/"+m.get("mm"));
				fileDAO.insert(m);
				fileUtil.copy(request.getSession().getServletContext().getRealPath(TEMP_PATH) + File.separator + m.get("uuid"), path + File.separator + m.get("uuid"),path);
			}
		}
		
		boardDAO.write(params);
		rstMap.put("rst", "1");
		return rstMap;
	}

	public Map resourcesModify(HttpServletRequest request, Map params) throws Exception {
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
		
		params.put("table_seq", params.get("article_seq"));
		params.put("table_nm", "NSH_ARTICLE");
		fileDAO.delete_all(params);
		List flist = (List) params.get("files");
		if(flist != null){
			for (int i = 0; i < flist.size(); i++) {
				Map m = (Map) flist.get(i);
				m.put("order_seq", i+1);
				m.put("table_nm", "NSH_ARTICLE");
				m.put("table_seq", params.get("article_seq"));
				m.put("session_member_id", params.get("session_member_id"));
				m.put("session_member_seq", params.get("session_member_seq"));
				m.put("session_member_nm", params.get("session_member_nm"));
				m.put("ip", params.get("ip"));
				String path = request.getSession().getServletContext().getRealPath(UPLOAD_PATH + File.separator + m.get("yyyy") + File.separator + m.get("mm"));
				fileDAO.insert(m);
				fileUtil.copy(request.getSession().getServletContext().getRealPath(TEMP_PATH) + File.separator + m.get("uuid"), path + File.separator + m.get("uuid"),path);
			}
		}
		
		flist = (List) params.get("removeFiles");
		if(flist != null){
			for (int i = 0; i < flist.size(); i++) {
				Map m = (Map) flist.get(i);
				fileUtil.delete(request.getSession().getServletContext().getRealPath(UPLOAD_PATH) + File.separator + m.get("yyyy") + File.separator + m.get("mm") + File.separator + m.get("uuid"));
			}
		}
		
		rstMap.put("rst", "1");
		return rstMap;
	}
	
	public Map resourcesDel(Map params) throws Exception {
		Map rstMap = new HashMap();
		boardDAO.del(params);
		rstMap.put("rst", "1");
		return rstMap;
	}
	/*========== Resources END ==========*/
	public Map categoryWrite(Map params) throws Exception {
		Map rstMap = new HashMap();
		boardDAO.categoryWrite(params);
		rstMap.put("rst", "1");
		return rstMap;
	}
	
	public Map categoryModify(Map params) throws Exception {
		Map rstMap = new HashMap();
		boardDAO.categoryModify(params);
		rstMap.put("rst", "1");
		return rstMap;
	}

	public Map commentWrite(Map params) {
		Map rstMap = new HashMap();
		params.put("conts", ((String)params.get("conts")).replaceAll("\n", "<br/>"));
		boardDAO.commentWrite(params);
		boardDAO.articleCommentCnt(params);
		rstMap.put("rst", "1");
		return rstMap;
	}

	public Map commentReply(Map params) {
		Map rstMap = new HashMap();
		Map commentInfo = new HashMap();
		commentInfo = boardDAO.commentReplyInfo(params);
		params.put("grp", commentInfo.get("grp"));
		params.put("seq", commentInfo.get("seq"));
		params.put("conts", ((String)params.get("conts")).replaceAll("\n", "<br/>"));
		boardDAO.commentReplyInfoUpdate(params);
		
		params.put("reply_id", commentInfo.get("reg_id"));		
		params.put("article_seq", commentInfo.get("article_seq"));
		params.put("lvl", String.valueOf((Integer.parseInt((String)commentInfo.get("lvl"))+1)));		
		params.put("seq", String.valueOf((Integer.parseInt((String)commentInfo.get("seq"))+1)));
		boardDAO.commentWrite(params);
		boardDAO.articleCommentCnt(params);
		rstMap.put("rst", "1");
		return rstMap;
	}

	public Map commentDel(Map params) {
		Map rstMap = new HashMap();
		Map commentInfo = new HashMap();
		commentInfo = boardDAO.commentReplyInfo(params);
		params.put("grp", commentInfo.get("grp"));
		params.put("seq", commentInfo.get("seq"));
		params.put("lvl", commentInfo.get("lvl"));
		params.put("article_seq", commentInfo.get("article_seq"));
		boardDAO.commentDelReply(params);
		boardDAO.commentDel(params);
		boardDAO.articleCommentCnt(params);
		rstMap.put("rst", "1");
		return rstMap;
	}

	public Map commentReplyUpdate(Map params) {
		Map rstMap = new HashMap();
		params.put("conts", ((String)params.get("conts")).replaceAll("\n", "<br/>"));
		boardDAO.commentReplyUpdate(params);
		rstMap.put("rst", "1");
		return rstMap;
	}
	
	public Map commentList(Map params){
		Map rstMap = new HashMap();
		List list = (List) boardDAO.commentList(params);
		List commentList = new ArrayList();
		for(int x=0; x < list.size(); x++){
			Map map = new HashMap();
			map = (Map)list.get(x);
			String conts = (String)map.get("conts");
			conts = conts.replaceAll("<br/>","\n");
			map.put("conts", conts);
			commentList.add(map);
		}
		rstMap.put("comment",commentList);
		return rstMap;
	}

	
	
	public Map commentReplyInfo(Map params){
		Map rstMap = new HashMap();
		rstMap = boardDAO.commentReplyInfo(params);
		String conts = (String)rstMap.get("conts");
		conts = conts.replaceAll("<br/>","\n");
		rstMap.put("conts", conts);
		return rstMap;
	}	
}