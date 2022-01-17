package com.mc.giftcard.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mc.common.util.Encryption;
import com.mc.common.util.StringUtil;
import com.mc.web.Globals;
import com.mc.web.MCController;
import com.mc.web.MCMap;
import com.mc.giftcard.code.GiftCardCodeDAO;
import com.mc.web.mms.MmsService;
/**
 *
 * @Description : 
 * @ClassName   : com.mc.web.board.MypageController.java
 * @Modification Information
 *
 * @author 오승택
 * @since 2015. 3. 13.
 * @version 1.0 *  
 * Copyright (C)  All right reserved.
 */
@Controller
@RequestMapping({"/giftcard/mypage/{path}/index.do","/giftcard/mobile/mypage/{path}/index.do","/giftcard/pop/mypage/{path}/index.do"})
public class GiftCardMypageController extends MCController {
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private GiftCardMypageService mypageService;
	
	@Autowired
	private MmsService mmsService;
	
	@Autowired
	private GiftCardBoardDAO boardDAO;
	
	@Autowired
	private GiftCardCodeDAO codeDAO;
	
	private Encryption encryption;
	
	@Autowired
	private Globals globals;
	
	@Value("#{config['upload.board']}")
	private String UPLOAD_PATH;
	
	
	@RequestMapping(params="!mode")
	public String index(HttpServletRequest request, HttpSession session, @PathVariable("path") String path, @RequestParam Map<String, String> params) throws Exception{
		if("/giftcard/mobile/mypage/mantoman_late/index.do".equals(request.getRequestURI())){
			return "redirect:/giftcard/mobile/mypage/carallbaro/index.do?menu=menu2";
		}else{
			return list(request, session, path, params);
		}
	}
	
	@RequestMapping(params="mode=list")
	public String list(HttpServletRequest request, HttpSession session, @PathVariable("path") String path, @RequestParam Map<String, String> params) throws Exception{
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());		
		Map map = new HashMap();
		params.put("path", path);
		mypageService.boardSelect(params,request.getServletPath());
		if(params.get("board_seq").equals("6") || params.get("board_seq").equals("8")){
			MCMap member = (MCMap) session.getAttribute("member");
			if(member == null){
				params.put("status","N");
			}else{
				if(!(member.get("group_seq").equals("1") || member.get("group_seq").equals("3"))){
					params.put("status","N");
				}
			}
		}
		map = mypageService.list(params);
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member != null)
		{
			params.put("session_member_seq", (String)member.get("member_seq"));
			MCMap sessionData = (MCMap) boardDAO.getMemberInfo(params);
			request.setAttribute("sessionData", sessionData);
		}

		request.setAttribute("category", map.get("category"));
		request.setAttribute("list", map.get("list"));
		request.setAttribute("page_info", map.get("pageInfo"));
		request.setAttribute("params", params);
		return params.get("folder")+"/"+path+"/list";
	}
	
	@RequestMapping(params="mode=view")
	public String view(HttpServletRequest request, HttpSession session, @PathVariable("path") String path, @RequestParam Map<String, String> params) throws Exception{
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		Map map = new HashMap();
		params.put("path", path);
		mypageService.boardSelect(params,request.getServletPath());
		MCMap member = (MCMap) session.getAttribute("member");
		Map view = new HashMap();
		if(Integer.parseInt((String)params.get("board_seq")) == 6 || Integer.parseInt((String)params.get("board_seq")) == 8){//부품문의 게시판
			if(member == null){
				params.put("status","N");
			}else{
				if(!(member.get("group_seq").equals("1") || member.get("group_seq").equals("3"))){
					params.put("status","N");
				}
			}
			map = mypageService.view(params);
			view = (Map)map.get("view");
			if(view.get("status").equals("2")){//비공개
				if(member == null){
					if(view.get("reg_seq").equals("0")){
						
					}else{
						request.setAttribute("message", globals.PUBLIC_NO_ARTICLE);
						return "message";
					}
				}
				if(member != null && (member.get("group_seq").equals("1") || member.get("group_seq").equals("3"))){
					
				}else{
					if(member == null || view.get("reg_seq").equals("0")){
						if(params.get("pass") == null){ // 비밀번호가 없을 경우
							request.setAttribute("params", params);
							return params.get("folder")+"/"+path+"/Password";
						}else{
							String pass = encryption.stringEncryption((String)params.get("pass"),"MD5");
							params.put("pass",pass);
						}
						if(!view.get("pass").equals(params.get("pass"))){//비밀번호 틀릴경우
							request.setAttribute("message", globals.PERMISSION_DENIED);
							return "message";
						}
					}else{
						if(!member.get("member_id").equals(view.get("member_id")) && !member.get("group_seq").equals("1")){//관리자 권한 설정
							request.setAttribute("message", globals.PUBLIC_NO_ARTICLE);
							return "message";
						}
					}
				}
			}else if(view.get("status").equals("3")){//협력사 공개
				if(member == null){
					request.setAttribute("message", globals.PERMISSION_DENIED);
					return "message";
				}
				if(!(member.get("group_seq").equals("1") || member.get("group_seq").equals("3"))){
					request.setAttribute("message", globals.PERMISSION_DENIED);
					return "message";
				}
			}
		}else{
			map = mypageService.view(params);
			view = (Map)map.get("view");
			if(view.get("public_yn").equals("N")){//비공개 글일경우
				if(member != null && member.get("group_seq").equals("1")){
					
				}else{
					if(params.get("board_seq").equals("3") || params.get("board_seq").equals("4")){//협력사 문의 || 중고부품 정비견적
						if(view.get("reg_seq").equals("0")){ //비회원 글
							if(params.get("pass") == null){ // 비밀번호가 없을 경우
								request.setAttribute("params", params);
								return params.get("folder")+"/"+path+"/Password";
							}else{
								if(params.get("pass") != null){
									String pass = encryption.stringEncryption((String)params.get("pass"),"MD5");
									params.put("pass",pass);
								}
								if(!view.get("pass").equals(params.get("pass"))){
									request.setAttribute("message", globals.PERMISSION_DENIED);
									return "message";
								}
							}
						}else{
							if(member == null){
								request.setAttribute("message", globals.PUBLIC_NO_ARTICLE);
								return "message";
							}
							if(!member.get("member_id").equals(view.get("member_id"))){
								request.setAttribute("message", globals.PUBLIC_NO_ARTICLE);
								return "message";
							}
						}
					}else{
						if(member == null){
							request.setAttribute("message", globals.PUBLIC_NO_ARTICLE);
							return "message";
						}
						if(!member.get("member_id").equals(view.get("member_id"))){
							request.setAttribute("message", globals.PUBLIC_NO_ARTICLE);
							return "message";
						}
					}
				}
			}
		}
		boardDAO.view_count(params);
		map = mypageService.view(params);
		request.setAttribute("view", map.get("view"));
		request.setAttribute("files", map.get("files"));
		request.setAttribute("comment", map.get("comment"));
		request.setAttribute("params", params);
		return params.get("folder")+"/"+path+"/view";
	}
	
	@RequestMapping(params="mode=insertForm")
	@Transactional(rollbackFor = { Exception.class })
	public String write(HttpServletRequest request, HttpSession session, @PathVariable("path") String path, @RequestParam Map<String, String> params) throws Exception{
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		params.put("path", path);
		params.put("code_group_seq", "5");
		params.put("use_yn", "Y");
		request.setAttribute("code", codeDAO.codeList(params));
		request.setAttribute("params", params);
		mypageService.boardSelect(params,request.getServletPath());
		createToken(request);
		if(params.get("board_seq").equals("3") || params.get("board_seq").equals("4") || params.get("board_seq").equals("6") || params.get("board_seq").equals("8")){
			return params.get("folder")+"/"+path+"/insertForm";
		}else{
			MCMap member = (MCMap) session.getAttribute("member");
			if(member == null){
				request.setAttribute("message", globals.NOT_LOGIN);
				return "message";
			}
			return params.get("folder")+"/"+path+"/insertForm";
		}
	}
	
	
	
	@RequestMapping(params="mode=insert", method=RequestMethod.POST)
	@Transactional(rollbackFor = { Exception.class })
	public String write(HttpServletRequest request, HttpSession session, @PathVariable("path") String path, @RequestParam Map<String, String> params, @RequestParam(value = "attach", required = false) List<MultipartFile> attachList) throws Exception{
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		params.put("ip", request.getRemoteHost());
		params.put("parent_seq", "0");
		params.put("path", path);
		
		MCMap member = (MCMap) session.getAttribute("member");
		if(member == null){
			params.put("session_member_group_seq", "0");
			params.put("session_member_seq", "0");
			params.put("session_member_nm", params.get("member_nm"));
		}else{
			params.put("session_member_group_seq", (String) member.get("group_seq"));
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_nm", (String) member.get("member_nm"));
		}
		mypageService.boardSelect(params,request.getServletPath());
		if(params.get("board_seq").equals("3") || params.get("board_seq").equals("4") || params.get("board_seq").equals("6") || params.get("board_seq").equals("8")){
			params.put("article_seq", boardDAO.getNextval());
			if(params.get("pass") != null){
				String pass = encryption.stringEncryption((String)params.get("pass"),"MD5");
				params.put("pass",pass);
			}
		}else{
			if(member == null){
				request.setAttribute("redirect", request.getServletPath());
				request.setAttribute("message", globals.REQUIRED_LOGIN);
				return "message";
			}
			params.put("article_seq", boardDAO.getNextval());
		}
		params.put("upload_path",request.getSession().getServletContext().getRealPath(UPLOAD_PATH));
		if(params.get("charge_tel1") != null){
			params.put("charge_tel",params.get("charge_tel1")+"-"+params.get("charge_tel2")+"-"+params.get("charge_tel3"));
		}
		if(validationToken(request) == false){
			request.setAttribute("redirect", request.getServletPath());
			request.setAttribute("message", "이미 등록 되었습니다.");
			return "message";
		}else{
			mypageService.write(attachList, params);
			
			if(params.get("board_seq").equals("6")){//국산차
				Map mms = new HashMap();
				mms.put("tran_msg", "국산차 부품문의가 등록되었습니다.");
				mms.put("tran_phone", "010-4381-8833");
				mms.put("tran_callback", "1544-6444");
				mmsService.write(mms);
			}else if(params.get("board_seq").equals("8")){//수입차
				Map mms = new HashMap();
				mms.put("tran_msg", "수입차 부품문의가 등록되었습니다.");
				mms.put("tran_phone", "010-5068-8058");
				mms.put("tran_callback", "1544-6444");
				mmsService.write(mms);				
			}
			params.remove("parent_seq");
			return list(request, session, path, params);
		}
	}
	
	@RequestMapping(params="mode=modifyForm")
	@Transactional(rollbackFor = { Exception.class })
	public String modifyForm(HttpServletRequest request, HttpSession session, @PathVariable("path") String path, @RequestParam Map<String, String> params) throws Exception{
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		params.put("path", path);
		params.put("code_group_seq", "5");
		params.put("use_yn", "Y");
		request.setAttribute("code", codeDAO.codeList(params));
		mypageService.boardSelect(params,request.getServletPath());

		Map map = new HashMap();
		map = mypageService.view(params);
		Map view = new HashMap();
		view = (Map)map.get("view");
		if(params.get("board_seq").equals("3") || params.get("board_seq").equals("4") || params.get("board_seq").equals("6") || params.get("board_seq").equals("8")){
			if(view.get("reg_seq").equals("0")){
				if(params.get("pass") == null){
					request.setAttribute("params", params);
					return params.get("folder")+"/"+path+"/Password";
				}else{
					if(params.get("pass") != null){
						String pass = encryption.stringEncryption((String)params.get("pass"),"MD5");
						params.put("pass",pass);
					}
					if(!view.get("pass").equals(params.get("pass"))){
						request.setAttribute("message", globals.PERMISSION_DENIED);
						return "message";
					}
				}
			}else{
				MCMap member = (MCMap) session.getAttribute("member");
				if(member == null){
					request.setAttribute("message", globals.REQUIRED_LOGIN);
					return "message";
				}else{
					if(!member.get("member_id").equals(view.get("member_id"))){
						request.setAttribute("message", globals.PERMISSION_DENIED);
						return "message";
					}
				}
			}
		}else{
			MCMap member = (MCMap) session.getAttribute("member");
			if(member == null){
				request.setAttribute("message", globals.REQUIRED_LOGIN);
				return "message";
			}else{
				if(!member.get("member_id").equals(view.get("member_id"))){
					request.setAttribute("message", globals.PERMISSION_DENIED);
					return "message";
				}
			}
		}
		request.setAttribute("view", map.get("view"));
		request.setAttribute("files", map.get("files"));
		request.setAttribute("params", params);
		return params.get("folder")+"/"+path+"/modifyForm";
	}
	
	
	@RequestMapping(params="mode=modify", method=RequestMethod.POST)
	@Transactional(rollbackFor = { Exception.class })
	public String modify(HttpServletRequest request, HttpSession session, @PathVariable("path") String path, @RequestParam Map<String, String> params, @RequestParam(value = "attach", required = false) List<MultipartFile> attachList, @RequestParam(value = "delattach", required = false) List<String> delAttachList) throws Exception{
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		params.put("ip", request.getRemoteHost());
		params.put("path", path);
		mypageService.boardSelect(params,request.getServletPath());
		
		
		MCMap member = (MCMap) session.getAttribute("member");
		Map map = new HashMap();
		map = mypageService.view(params);
		Map checkAuth = new HashMap();
		checkAuth = (Map)map.get("view");
		if(params.get("board_seq").equals("3") || params.get("board_seq").equals("4") || params.get("board_seq").equals("6") || params.get("board_seq").equals("8")){//협력사 문의
			if(checkAuth.get("reg_seq").equals("0")){//비회원일 경우
				if(params.get("pass") != null){
					String pass = encryption.stringEncryption((String)params.get("pass"),"MD5");
					params.put("pass",pass);
				}
				if(!checkAuth.get("pass").equals(params.get("pass"))){//패스워드가 틀릴 경우
					request.setAttribute("message", globals.PERMISSION_DENIED);
					return "message";
				}
			}else{//회원일 경우
				if(member == null){//비로그인
					request.setAttribute("message", globals.REQUIRED_LOGIN);
					return "message";
				}else{//로그인
					if(!member.get("member_id").equals(checkAuth.get("member_id"))){//아이디가 틀릴 경우
						request.setAttribute("message", globals.PERMISSION_DENIED);
						return "message";
					}
				}
			}
		}else{//일반 게시판
			if(member == null){//비로그인
				request.setAttribute("message", globals.REQUIRED_LOGIN);
				return "message";
			}else{//로그인
				if(!member.get("member_id").equals(checkAuth.get("member_id"))){//아이디가 틀릴 경우
					request.setAttribute("message", globals.PERMISSION_DENIED);
					return "message";
				}
			}
		}
		
		if(member == null){
			params.put("session_member_group_seq", "0");
			params.put("session_member_seq", "0");
			params.put("session_member_nm", params.get("member_nm"));
		}else{
			params.put("session_member_group_seq", (String) member.get("group_seq"));
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_nm", (String) member.get("member_nm"));
		}
		params.put("upload_path",request.getSession().getServletContext().getRealPath(UPLOAD_PATH));
		if(params.get("charge_tel1") != null){
			params.put("charge_tel",params.get("charge_tel1")+"-"+params.get("charge_tel2")+"-"+params.get("charge_tel3"));
		}
		mypageService.modify(attachList, delAttachList, params);
		return list(request, session, path, params);
	}
	
	@RequestMapping(params="mode=del")
	@Transactional(rollbackFor = { Exception.class })
	public String del(ModelMap model, HttpServletRequest request, HttpSession session, @PathVariable("path") String path, @RequestParam Map<String, String> params) throws Exception{
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		params.put("ip", request.getRemoteHost());
		params.put("path", path);
		mypageService.boardSelect(params,request.getServletPath());
		
		
		MCMap member = (MCMap) session.getAttribute("member");
		Map map = new HashMap();
		map = mypageService.view(params);
		Map checkAuth = new HashMap();
		checkAuth = (Map)map.get("view");
		if(params.get("board_seq").equals("3") || params.get("board_seq").equals("4") || params.get("board_seq").equals("6") || params.get("board_seq").equals("8")){//협력사 문의
			if(checkAuth.get("reg_seq").equals("0")){//비회원일 경우
				if(params.get("pass") == null){
					request.setAttribute("params", params);
					return params.get("folder")+"/"+path+"/Password";
				}else{
					if(params.get("pass") != null){
						String pass = encryption.stringEncryption((String)params.get("pass"),"MD5");
						params.put("pass",pass);
					}
					if(!checkAuth.get("pass").equals(params.get("pass"))){//패스워드가 틀릴 경우
						request.setAttribute("message", globals.PERMISSION_DENIED);
						return "message";
					}
				}
			}else{//회원일 경우
				if(member == null){//비로그인
					request.setAttribute("message", globals.REQUIRED_LOGIN);
					return "message";
				}else{//로그인
					if(!member.get("member_id").equals(checkAuth.get("member_id"))){//아이디가 틀릴 경우
						request.setAttribute("message", globals.PERMISSION_DENIED);
						return "message";
					}
				}
			}
		}else{//일반 게시판
			if(member == null){//비로그인
				request.setAttribute("message", globals.REQUIRED_LOGIN);
				return "message";
			}else{//로그인
				if(!member.get("member_id").equals(checkAuth.get("member_id"))){//아이디가 틀릴 경우
					request.setAttribute("message", globals.PERMISSION_DENIED);
					return "message";
				}
			}
		}
		
		if(member == null){
			params.put("session_member_group_seq", "0");
			params.put("session_member_seq", "0");
			params.put("session_member_nm", (String)checkAuth.get("member_nm"));
		}else{
			params.put("session_member_group_seq", (String) member.get("group_seq"));
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_nm", (String) member.get("member_nm"));
		}
		params.put("upload_path",request.getSession().getServletContext().getRealPath(UPLOAD_PATH));
		if(params.get("charge_tel1") != null){
			params.put("charge_tel",params.get("charge_tel1")+"-"+params.get("charge_tel2")+"-"+params.get("charge_tel3"));
		}
		mypageService.del(params);
		return list(request, session, path, params);
	}
	
	@ResponseBody
	@RequestMapping(params="mode=commentInsert")
	@Transactional(rollbackFor = { Exception.class })
	public Map commentWrite(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		if(member != null){
			params.put("session_member_group_seq", (String) member.get("group_seq"));
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			params.put("reg_id", (String) member.get("member_id"));
			params.put("ip", request.getRemoteHost());
			params.put("servletPath", request.getServletPath());
			if(params.get("servletPath").indexOf("mantoman") > -1 && (params.get("session_member_group_seq").equals("3") || params.get("session_member_group_seq").equals("1"))){
				Map view = new HashMap();
				view.put("board_seq", "4");
				view.put("article_seq", params.get("article_seq"));
				view = boardDAO.view(view);
				if(view != null){
					Map mms = new HashMap();
					mms.put("tran_msg", "카올바로에 요청하신 견적에 대한 답글이 등록되었습니다 - 파츠모아");
					mms.put("tran_phone", view.get("charge_tel"));
					mms.put("tran_callback", "1544-6444");
					mmsService.write(mms);
				}
			}
			if(params.get("servletPath").indexOf("application") > -1 && (params.get("session_member_group_seq").equals("3") || params.get("session_member_group_seq").equals("1"))){
				Map view = new HashMap();
				if(params.get("servletPath").indexOf("import") > -1){
					view.put("board_seq", "8");
				}else{
					view.put("board_seq", "6");
				}				
				view.put("article_seq", params.get("article_seq"));
				view = boardDAO.view(view);
				if(view != null){
					Map mms = new HashMap();
					mms.put("tran_msg", "요청하신 문의에 대한 답글이 입력되었습니다 - 파츠모아");
					mms.put("tran_phone", view.get("charge_tel"));
					mms.put("tran_callback", "1544-6444");
					mmsService.write(mms);
				}
			}
			
			return mypageService.commentWrite(params);
		}else{
			Map rstMap = new HashMap();
			rstMap.put("boolean", "false");
			return rstMap;
		}
	}
	
	@ResponseBody
	@RequestMapping(params="mode=commentReply")
	@Transactional(rollbackFor = { Exception.class })
	public Map commentReply(HttpServletRequest request, HttpSession session, @PathVariable("path") String path, @RequestParam Map<String, String> params) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		if(member != null){
			params.put("session_member_group_seq", (String) member.get("group_seq"));
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			params.put("reg_id", (String) member.get("member_id"));
			params.put("ip", request.getRemoteHost());
			params.put("servletPath", request.getServletPath());
			return mypageService.commentReply(params);
		}else{
			Map rstMap = new HashMap();
			rstMap.put("boolean", "false");
			return rstMap;
		}
	}
	
	@ResponseBody
	@RequestMapping(params="mode=commentDel")
	@Transactional(rollbackFor = { Exception.class })
	public Map commentDel(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		if(member != null){
			params.put("session_member_group_seq", (String) member.get("group_seq"));
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			params.put("reg_id", (String) member.get("member_id"));
			params.put("ip", request.getRemoteHost());
			params.put("servletPath", request.getServletPath());
			return mypageService.commentDel(params);
		}else{
			Map rstMap = new HashMap();
			rstMap.put("rst", "1");
			rstMap.put("boolean", "false");
			return rstMap;
		}
	}
	
	@ResponseBody
	@RequestMapping(params="mode=commentReplyUpdate")
	@Transactional(rollbackFor = { Exception.class })
	public Map commentReplyUpdate(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		if(member != null){
			params.put("session_member_group_seq", (String) member.get("group_seq"));
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			params.put("reg_id", (String) member.get("member_id"));
			params.put("ip", request.getRemoteHost());
			params.put("servletPath", request.getServletPath());
			return mypageService.commentReplyUpdate(params);
		}else{
			Map rstMap = new HashMap();
			rstMap.put("rst", "1");
			rstMap.put("boolean", "false");
			return rstMap;
		}
	}
	
	@RequestMapping(params="mode=join")
	public String join(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/login/login.do");
			return "message";
    	}
		
		params.put("member_seq", (String)member.get("member_seq"));
		
		MCMap userData = (MCMap) boardDAO.getUserData(params);
		
		String[] tel = null;
		if(StringUtil.isEmptyByParam(userData, "tel")) tel = ((String) userData.get("tel")).split("-");
		String[] cell = ((String) userData.get("cell")).split("-");
		String[] email = ((String) userData.get("email")).split("@");
		String[] zip_code = ((String) userData.get("zip_cd")).split("-");
		
		if(StringUtil.isEmptyByParam(userData, "tel"))
		{
			request.setAttribute("tel", tel[0]);
			request.setAttribute("tel1", tel[1]);
			request.setAttribute("tel2", tel[2]);
		}
		
		request.setAttribute("cell", cell[0]);
		request.setAttribute("cell1", cell[1]);
		request.setAttribute("cell2", cell[2]);
		
		request.setAttribute("email", email[0]);
		request.setAttribute("email1", email[1]);
		
		request.setAttribute("zip_code1", zip_code[0]);
		if(zip_code.length > 1 ){
			request.setAttribute("zip_code2", zip_code[1]);
		}
		request.setAttribute("userData", userData);
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		
		return "/giftcard/mypage/member/list";
	}
	
	@RequestMapping(params="mode=userModify")
	public String userModify(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/login/login.do");
			return "message";
    	}
		params.put("ip", request.getRemoteHost());
		params.put("session_member_seq", (String)member.get("member_seq"));
		params.put("session_member_nm", (String)member.get("member_nm"));
		params.put("tel", params.get("tel") + "-" + params.get("tel1") + "-" + params.get("tel2"));
		params.put("cell", params.get("cell") + "-" + params.get("cell1") + "-" + params.get("cell2"));
		params.put("email", params.get("email") + "@" + params.get("email1"));
		params.put("zip_cd", params.get("zip_cd") + "-" + params.get("zip_cd1"));
		
		params.put("member_pw_df", encryption.stringEncryption((String)params.get("member_pw_df"),"MD5"));
		if(!StringUtil.isEmptyByParam(params, "member_pw")) params.put("member_pw", encryption.stringEncryption((String)params.get("member_pw"),"MD5"));
		
		
		int result = boardDAO.userDataUpdateStatus(params);
		
		if(result == 1)
		{
			request.setAttribute("servletPath", request.getServletPath());
			request.setAttribute("requestURI", request.getRequestURI());
			request.setAttribute("requestURL", request.getRequestURL());
			
			request.setAttribute("message", "회원정보가 수정되었습니다.");
			request.setAttribute("redirect", "/giftcard/mypage/member/index.do?mode=join");
			return "message";
		}
		else
		{
			request.setAttribute("message", "기존 비밀번호가 일치하지 않습니다. 기존 비밀번호를 확인하여 주세요.");
			request.setAttribute("redirect", "/giftcard/mypage/member/index.do?mode=join");
			return "message";
		}
		
	}
	
	
	@RequestMapping(params="mode=withdraw")
	public String withdraw(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/login/login.do");
			return "message";
    	}
		
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		
		return "/giftcard/mypage/member/withdraw1";
	}
	
	@RequestMapping(params="mode=withdraw1")
	public String withdraw1(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/login/login.do");
			return "message";
    	}
		
		params.put("session_member_seq", (String)member.get("member_seq"));
		params.put("session_member_id", (String)member.get("member_id"));
		params.put("member_pw", encryption.stringEncryption((String)params.get("member_pw"),"MD5"));
		MCMap userData = (MCMap) boardDAO.getUserData2(params);
		
		if(userData == null)
		{
			request.setAttribute("message", "비밀번호가 일치하지 않습니다. 다시 입력하여 주세요.");
    		request.setAttribute("redirect", "/giftcard/mypage/member/index.do?mode=withdraw");
			return "message";
		}
		else
		{
			request.setAttribute("servletPath", request.getServletPath());
			request.setAttribute("requestURI", request.getRequestURI());
			request.setAttribute("requestURL", request.getRequestURL());
			
			return "/giftcard/mypage/member/withdraw2";
		}
	}
	
	@RequestMapping(params="mode=withdraw2")
	public String withdraw2(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/login/login.do");
			return "message";
    	}
		
		params.put("session_member_seq", (String)member.get("member_seq"));
		params.put("session_member_id", (String)member.get("member_id"));
		
		boardDAO.withDrawMemberUpdate(params);
		
		session.removeAttribute("member");
		session.invalidate();

		return "/giftcard/mypage/member/withdraw3";
	}
	
	@RequestMapping(params="mode=myaddress")
	public String sendaddress(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/login/login.do");
			return "message";
    	}
		
		params.put("member_seq", (String)member.get("member_seq"));
		
		request.setAttribute("list", boardDAO.getSendAddressList(params));
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		
		return "/giftcard/mypage/member/myaddress";
	}
	
	@RequestMapping(params="mode=mydelete")
	public String mydelete(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/login/login.do");
			return "message";
    	}
		
		boardDAO.deleteMyadressData(params);
		
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		
		request.setAttribute("message", "삭제되었습니다.");
		request.setAttribute("redirect", "/giftcard/mypage/member/index.do?mode=myaddress");
		return "message";
	}
	
	@RequestMapping(params="mode=mydefault")
	public String mydefault(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/login/login.do");
			return "message";
    	}
		
		params.put("session_member_seq", (String)member.get("member_seq"));
		boardDAO.updateDefaultYnStatus(params);
		boardDAO.updateMyDefaultData(params);
		
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		
		request.setAttribute("message", "기본배송지로 등록 되었습니다.");
		request.setAttribute("redirect", "/giftcard/mypage/member/index.do?mode=myaddress");
		return "message";
	}
	
	@RequestMapping(params="mode=myaddressForm")
	public String myaddressForm(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/login/login.do");
			return "message";
    	}
		
		if(!StringUtil.isEmptyByParam(params, "seq")) // 수정
		{
			MCMap myaddress = (MCMap) boardDAO.getMyAddressView(params);
			
			String[] tel = ((String) myaddress.get("tel")).split("-");
			String[] cell = ((String) myaddress.get("cell")).split("-");
			String[] zip_code = ((String) myaddress.get("zip_cd")).split("-");
			
			request.setAttribute("tel", tel[0]);
			request.setAttribute("tel1", tel[1]);
			request.setAttribute("tel2", tel[2]);
			
			request.setAttribute("cell", cell[0]);
			request.setAttribute("cell1", cell[1]);
			request.setAttribute("cell2", cell[2]);
			
			request.setAttribute("zip_cd1", zip_code[0]);
			if(zip_code.length > 1){
				request.setAttribute("zip_cd2", zip_code[1]);
			}
			
			request.setAttribute("myaddress", myaddress);
			request.setAttribute("mode", "myaddressupdate");
			
		}
		else // 등록
		{
			params.put("session_member_seq", (String)member.get("member_seq"));
			MCMap myaddressTotal = (MCMap) boardDAO.getMyAddressTotal(params);
			request.setAttribute("myaddress", myaddressTotal);
			request.setAttribute("mode", "myaddressInsert");
		}
		
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		
		return "/giftcard/mypage/member/myaddress_form";
	}
	
	
	@RequestMapping(params="mode=myaddressInsert")
	public String myaddressInsert(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/login/login.do");
			return "message";
    	}
		
		params.put("session_member_seq", (String)member.get("member_seq"));
		params.put("ip", request.getRemoteHost());
		params.put("order_seq", "1");
		params.put("tel", params.get("tel") + "-" + params.get("tel1") + "-" + params.get("tel2"));
		params.put("cell", params.get("cell") + "-" + params.get("cell1") + "-" + params.get("cell2"));
		params.put("zip_cd", params.get("zip_cd1") + "-" + params.get("zip_cd2"));
		
		if(!StringUtil.isEmptyByParam(params, "default_yn"))
		{
			//신규 기본 배송지 저장을 위해 기본 배송지 초기화
			boardDAO.updateDefaultYnStatus(params);
		}
		
		boardDAO.insertMyAddressData(params);
		
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		
		request.setAttribute("message", "등록되었습니다.");
		request.setAttribute("redirect", "/giftcard/mypage/member/index.do?mode=myaddress");
		return "message";
	}
	
	@RequestMapping(params="mode=myaddressupdate")
	public String myaddressupdate(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/login/login.do");
			return "message";
    	}
		
		params.put("tel", params.get("tel") + "-" + params.get("tel1") + "-" + params.get("tel2"));
		params.put("cell", params.get("cell") + "-" + params.get("cell1") + "-" + params.get("cell2"));
		params.put("zip_cd", params.get("zip_cd1") + "-" + params.get("zip_cd2"));
		
		if(!StringUtil.isEmptyByParam(params, "default_yn"))
		{
			params.put("session_member_seq", (String)member.get("member_seq"));
			//신규 기본 배송지 저장을 위해 기본 배송지 초기화
			boardDAO.updateDefaultYnStatus(params);
			
		}
		
		boardDAO.updateMyAddressData(params);
		
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		
		request.setAttribute("message", "수정되었습니다.");
		request.setAttribute("redirect", "/giftcard/mypage/member/index.do?mode=myaddress");
		return "message";
	}
	
	@RequestMapping(params="mode=busi")
	public String busi(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/login/login.do");
			return "message";
    	}
		
		params.put("member_seq", (String)member.get("member_seq"));
		
		MCMap userData = (MCMap) boardDAO.getUserData(params);
		
		String[] busi_no = ((String) userData.get("busi_no")).split("-");
		String[] tel = ((String) userData.get("tel")).split("-");
		String[] staff_tel = ((String) userData.get("staff_tel")).split("-");
		String[] email = ((String) userData.get("email")).split("@");
		String[] zip_code = ((String) userData.get("zip_cd")).split("-");
		
		request.setAttribute("busi_no", busi_no[0]);
		request.setAttribute("busi_no1", busi_no[1]);
		request.setAttribute("busi_no2", busi_no[2]);
		request.setAttribute("tel", tel[0]);
		request.setAttribute("tel1", tel[1]);
		request.setAttribute("tel2", tel[2]);
		request.setAttribute("staff_tel", staff_tel[0]);
		request.setAttribute("staff_tel1", staff_tel[1]);
		request.setAttribute("staff_tel2", staff_tel[2]);
		request.setAttribute("email", email[0]);
		request.setAttribute("email1", email[1]);
		request.setAttribute("zip_code1", zip_code[0]);
		if(zip_code.length > 1){
			request.setAttribute("zip_code2", zip_code[1]);
		}
		request.setAttribute("userData", userData);
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		
		return "/giftcard/mypage/member/busi";
	}
	
	@RequestMapping(params="mode=busiModify")
	public String busiModify(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/login/login.do");
			return "message";
    	}
		params.put("ip", request.getRemoteHost());
		params.put("session_member_seq", (String)member.get("member_seq"));
		params.put("session_member_nm", (String)member.get("member_nm"));
		
		params.put("busi_no", params.get("busi_no") + "-" + params.get("busi_no1") + "-" + params.get("busi_no2"));
		params.put("tel", params.get("tel") + "-" + params.get("tel1") + "-" + params.get("tel2"));
		params.put("staff_tel", params.get("staff_tel") + "-" + params.get("staff_tel1") + "-" + params.get("staff_tel2"));
		params.put("email", params.get("email1") + "@" + params.get("email2"));
		params.put("zip_cd", params.get("zip_cd1") + "-" + params.get("zip_cd2"));
		
		params.put("member_pw_df", encryption.stringEncryption((String)params.get("member_pw_df"),"MD5"));
		if(!StringUtil.isEmptyByParam(params, "member_pw")) params.put("member_pw", encryption.stringEncryption((String)params.get("member_pw"),"MD5"));

		int result = boardDAO.busiDataUpdateStatus(params);
		
		if(result == 1)
		{
			request.setAttribute("servletPath", request.getServletPath());
			request.setAttribute("requestURI", request.getRequestURI());
			request.setAttribute("requestURL", request.getRequestURL());
			
			request.setAttribute("message", "회원정보가 수정되었습니다.");
			request.setAttribute("redirect", "/giftcard/mypage/member/index.do?mode=busi");
			return "message";
		}
		else
		{
			request.setAttribute("message", "기존 비밀번호가 일치하지 않습니다. 기존 비밀번호를 확인하여 주세요.");
			request.setAttribute("redirect", "/giftcard/mypage/member/index.do?mode=busi");
			return "message";
		}
		
	}
	
	@RequestMapping(params="mode=muser")
	public String muser(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/mobile/login/login.do?mode=mlogin");
			return "message";
    	}
		
		params.put("member_seq", (String)member.get("member_seq"));
		
		MCMap userData = (MCMap) boardDAO.getUserData(params);
		
		
		String[] tel = ((String) userData.get("tel")).split("-");
		String[] cell = ((String) userData.get("cell")).split("-");
		String[] email = ((String) userData.get("email")).split("@");
		String[] zip_code = ((String) userData.get("zip_cd")).split("-");
		
		request.setAttribute("tel", tel[0]);
		if(tel.length > 1){
			request.setAttribute("tel1", tel[1]);
		}
		if(tel.length > 2){
			request.setAttribute("tel2", tel[2]);
		}
		request.setAttribute("cell", cell[0]);
		if(cell.length > 1){
			request.setAttribute("cell1", cell[1]);
		}
		if(cell.length > 2){
			request.setAttribute("cell2", cell[2]);
		}
		
		if(email.length > 0){
			request.setAttribute("email", email[0]);
		}
		if(email.length > 1){
			request.setAttribute("email1", email[1]);
		}
		
		request.setAttribute("zip_code1", zip_code[0]);
		if(zip_code.length > 1){
			request.setAttribute("zip_code2", zip_code[1]);
		}
		
		request.setAttribute("userData", userData);
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		
		return "/giftcard/mobile/mypage/member/mypage";
	}
	
	@RequestMapping(params="mode=mbusi")
	public String mbusi(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/mobile/login/login.do?mode=mlogin");
			return "message";
    	}
		
		params.put("member_seq", (String)member.get("member_seq"));
		
		MCMap userData = (MCMap) boardDAO.getUserData(params);
		
		
		String[] tel = ((String) userData.get("tel")).split("-");
		String[] cell = ((String) userData.get("cell")).split("-");
		String[] email = ((String) userData.get("email")).split("@");
		String[] zip_code = ((String) userData.get("zip_cd")).split("-");
		
		request.setAttribute("tel", tel[0]);
		request.setAttribute("tel1", tel[1]);
		request.setAttribute("tel2", tel[2]);
		
		request.setAttribute("cell", cell[0]);
		request.setAttribute("cell1", cell[1]);
		request.setAttribute("cell2", cell[2]);
		
		request.setAttribute("email", email[0]);
		request.setAttribute("email1", email[1]);
		
		request.setAttribute("zip_code1", zip_code[0]);
		if(zip_code.length > 1){
			request.setAttribute("zip_code2", zip_code[1]);
		}
		
		request.setAttribute("userData", userData);
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		
		return "/giftcard/mobile/mypage/member/mypage";
	}
	
	@RequestMapping(params="mode=muserModify")
	public String muserModify(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/mobile/login/login.do?mode=mlogin");
			return "message";
    	}
		params.put("ip", request.getRemoteHost());
		params.put("session_member_seq", (String)member.get("member_seq"));
		params.put("session_member_nm", (String)member.get("member_nm"));
		params.put("tel", params.get("tel") + "-" + params.get("tel1") + "-" + params.get("tel2"));
		params.put("cell", params.get("cell") + "-" + params.get("cell1") + "-" + params.get("cell2"));
		params.put("email", params.get("email") + "@" + params.get("email1"));
		params.put("zip_cd", params.get("zip_cd") + "-" + params.get("zip_cd1"));
		
		params.put("member_pw_df", encryption.stringEncryption((String)params.get("member_pw_df"),"MD5"));
		if(!StringUtil.isEmptyByParam(params, "member_pw")) params.put("member_pw", encryption.stringEncryption((String)params.get("member_pw"),"MD5"));
		
		
		int result = boardDAO.userDataUpdateStatus(params);
		
		if(result == 1)
		{
			request.setAttribute("servletPath", request.getServletPath());
			request.setAttribute("requestURI", request.getRequestURI());
			request.setAttribute("requestURL", request.getRequestURL());
			
			request.setAttribute("message", "회원정보가 수정되었습니다.");
			request.setAttribute("redirect", "/giftcard/mobile/mypage/member/index.do?mode=muser");
			return "message";
		}
		else
		{
			request.setAttribute("message", "기존 비밀번호가 일치하지 않습니다. 기존 비밀번호를 확인하여 주세요.");
			request.setAttribute("redirect", "/giftcard/mobile/mypage/member/index.do?mode=muser");
			return "message";
		}
		
	}
	
	@RequestMapping(params="mode=mbusiModify")
	public String mbusiModify(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/mobile/login/login.do?mode=mlogin");
			return "message";
    	}
		params.put("ip", request.getRemoteHost());
		params.put("session_member_seq", (String)member.get("member_seq"));
		params.put("session_member_nm", (String)member.get("member_nm"));
		
		params.put("busi_no", params.get("busi_no") + "-" + params.get("busi_no1") + "-" + params.get("busi_no2"));
		params.put("tel", params.get("tel") + "-" + params.get("tel1") + "-" + params.get("tel2"));
		params.put("staff_tel", params.get("staff_tel") + "-" + params.get("staff_tel1") + "-" + params.get("staff_tel2"));
		params.put("email", params.get("email1") + "@" + params.get("email2"));
		params.put("zip_cd", params.get("zip_cd1") + "-" + params.get("zip_cd2"));
		
		params.put("member_pw_df", encryption.stringEncryption((String)params.get("member_pw_df"),"MD5"));
		if(!StringUtil.isEmptyByParam(params, "member_pw")) params.put("member_pw", encryption.stringEncryption((String)params.get("member_pw"),"MD5"));

		int result = boardDAO.busiDataUpdateStatus(params);
		
		if(result == 1)
		{
			request.setAttribute("servletPath", request.getServletPath());
			request.setAttribute("requestURI", request.getRequestURI());
			request.setAttribute("requestURL", request.getRequestURL());
			
			request.setAttribute("message", "회원정보가 수정되었습니다.");
			request.setAttribute("redirect", "/giftcard/mobile/mypage/member/index.do?mode=mbusi");
			return "message";
		}
		else
		{
			request.setAttribute("message", "기존 비밀번호가 일치하지 않습니다. 기존 비밀번호를 확인하여 주세요.");
			request.setAttribute("redirect", "/giftcard/mobile/mypage/member/index.do?mode=mbusi");
			return "message";
		}
		
	}
	
	@RequestMapping(params="mode=m_myaddress")
	public String m_myaddress(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/mobile/login/login.do?mode=mlogin");
			return "message";
    	}
		
		params.put("member_seq", (String)member.get("member_seq"));
		
		request.setAttribute("list", boardDAO.getSendAddressList(params));
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		
		return "/giftcard/mobile/mypage/member/myaddress";
	}
	
	@RequestMapping(params="mode=m_myaddressForm")
	public String m_myaddressForm(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/mobile/login/login.do?mode=mlogin");
			return "message";
    	}
		
		if(!StringUtil.isEmptyByParam(params, "seq")) // 수정
		{
			MCMap myaddress = (MCMap) boardDAO.getMyAddressView(params);
			
			String[] tel = ((String) myaddress.get("tel")).split("-");
			String[] cell = ((String) myaddress.get("cell")).split("-");
			String[] zip_code = ((String) myaddress.get("zip_cd")).split("-");
			
			request.setAttribute("tel", tel[0]);
			request.setAttribute("tel1", tel[1]);
			request.setAttribute("tel2", tel[2]);
			
			request.setAttribute("cell", cell[0]);
			request.setAttribute("cell1", cell[1]);
			request.setAttribute("cell2", cell[2]);
			
			request.setAttribute("zip_cd1", zip_code[0]);
			if(zip_code.length > 1){
				request.setAttribute("zip_cd2", zip_code[1]);
			}
			
			request.setAttribute("myaddress", myaddress);
			request.setAttribute("mode", "m_myaddressupdate");
			
		}
		else // 등록
		{
			params.put("session_member_seq", (String)member.get("member_seq"));
			MCMap myaddressTotal = (MCMap) boardDAO.getMyAddressTotal(params);
			request.setAttribute("myaddress", myaddressTotal);
			request.setAttribute("mode", "m_myaddressInsert");
		}
		
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		
		return "/giftcard/mobile/mypage/member/myaddress_form";
	}
	
	@RequestMapping(params="mode=m_myaddressupdate")
	public String m_myaddressupdate(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/mobile/login/login.do?mode=mlogin");
			return "message";
    	}
		
		params.put("tel", params.get("tel") + "-" + params.get("tel1") + "-" + params.get("tel2"));
		params.put("cell", params.get("cell") + "-" + params.get("cell1") + "-" + params.get("cell2"));
		params.put("zip_cd", params.get("zip_cd1") + "-" + params.get("zip_cd2"));
		
		if(!StringUtil.isEmptyByParam(params, "default_yn"))
		{
			params.put("session_member_seq", (String)member.get("member_seq"));
			//신규 기본 배송지 저장을 위해 기본 배송지 초기화
			boardDAO.updateDefaultYnStatus(params);
			
		}
		
		boardDAO.updateMyAddressData(params);
		
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		
		request.setAttribute("message", "수정되었습니다.");
		request.setAttribute("redirect", "/giftcard/mobile/mypage/member/index.do?mode=m_myaddress");
		return "message";
	}
	
	@RequestMapping(params="mode=m_myaddressInsert")
	public String m_myaddressInsert(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/mobile/login/login.do?mode=mlogin");
			return "message";
    	}
		
		params.put("session_member_seq", (String)member.get("member_seq"));
		params.put("ip", request.getRemoteHost());
		params.put("order_seq", "1");
		params.put("tel", params.get("tel") + "-" + params.get("tel1") + "-" + params.get("tel2"));
		params.put("cell", params.get("cell") + "-" + params.get("cell1") + "-" + params.get("cell2"));
		params.put("zip_cd", params.get("zip_cd1") + "-" + params.get("zip_cd2"));
		
		if(!StringUtil.isEmptyByParam(params, "default_yn"))
		{
			//신규 기본 배송지 저장을 위해 기본 배송지 초기화
			boardDAO.updateDefaultYnStatus(params);
		}
		
		boardDAO.insertMyAddressData(params);
		
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		
		request.setAttribute("message", "등록되었습니다.");
		request.setAttribute("redirect", "/giftcard/mobile/mypage/member/index.do?mode=m_myaddress");
		return "message";
	}
	
	@RequestMapping(params="mode=m_mydefault")
	public String m_mydefault(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/mobile/login/login.do?mode=mlogin");
			return "message";
    	}
		
		params.put("session_member_seq", (String)member.get("member_seq"));
		boardDAO.updateDefaultYnStatus(params);
		boardDAO.updateMyDefaultData(params);
		
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		
		request.setAttribute("message", "기본배송지로 등록 되었습니다.");
		request.setAttribute("redirect", "/giftcard/mobile/mypage/member/index.do?mode=m_myaddress");
		return "message";
	}
	
	@RequestMapping(params="mode=m_mydelete")
	public String m_mydelete(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/mobile/login/login.do?mode=mlogin");
			return "message";
    	}
		
		boardDAO.deleteMyadressData(params);
		
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		
		request.setAttribute("message", "삭제되었습니다.");
		request.setAttribute("redirect", "/giftcard/mobile/mypage/member/index.do?mode=m_myaddress");
		return "message";
	}
	
	@RequestMapping(params="mode=mwithdraw")
	public String mwithdraw(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/mobile/login/login.do?mode=mlogin");
			return "message";
    	}
		
		request.setAttribute("servletPath", request.getServletPath());
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("requestURL", request.getRequestURL());
		
		return "/giftcard/mobile/mypage/member/withdraw_pw";
	}
	
	@RequestMapping(params="mode=mwithdraw1")
	public String mwithdraw1(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/mobile/login/login.do?mode=mlogin");
			return "message";
    	}
		
		params.put("session_member_seq", (String)member.get("member_seq"));
		params.put("session_member_id", (String)member.get("member_id"));
		params.put("member_pw", encryption.stringEncryption((String)params.get("member_pw"),"MD5"));
		MCMap userData = (MCMap) boardDAO.getUserData2(params);
		
		if(userData == null)
		{
			request.setAttribute("message", "비밀번호가 일치하지 않습니다. 다시 입력하여 주세요.");
    		request.setAttribute("redirect", "/giftcard/mobile/mypage/member/index.do?mode=mwithdraw");
			return "message";
		}
		else
		{
			request.setAttribute("servletPath", request.getServletPath());
			request.setAttribute("requestURI", request.getRequestURI());
			request.setAttribute("requestURL", request.getRequestURL());
			
			return "/giftcard/mobile/mypage/member/withdraw_agree";
		}
	}
	
	@RequestMapping(params="mode=mwithdraw2")
	public String mwithdraw2(HttpServletRequest request, HttpSession session, @RequestParam Map<String, String> params) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		
		if(member == null){
			request.setAttribute("message", "로그인 후 이용가능합니다.");
    		request.setAttribute("redirect", "/giftcard/mobile/login/login.do?mode=mlogin");
			return "message";
    	}
		
		params.put("session_member_seq", (String)member.get("member_seq"));
		params.put("session_member_id", (String)member.get("member_id"));
		
		boardDAO.withDrawMemberUpdate(params);
		
		session.removeAttribute("member");
		session.invalidate();

		return "/giftcard/mobile/mypage/member/withdraw_result";
	}
}