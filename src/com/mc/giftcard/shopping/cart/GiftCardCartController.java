package com.mc.giftcard.shopping.cart;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mc.common.util.HttpApiUtil;
import com.mc.common.util.StringUtil;
import com.mc.web.Globals;
import com.mc.web.MCMap;
import com.mc.web.common.ShorturlController;
import com.mc.common.util.PaymentUtil;

/**
 * 
 *
 * @Description :
 * @ClassName : com.mc.web.shopping.cart.CartController.java
 * @Modification Information
 *
 * @author 이창기
 * @since 2015. 3. 15.
 * @version 1.0 * Copyright (C) All right reserved.
 */
@Controller
@RequestMapping({ "/giftcard/mypage/shopping/cart/{index}.do" })
public class GiftCardCartController {
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private Globals globals;

	@Autowired
	private GiftCardCartService cartService;
	
	@Autowired
	private PaymentUtil paymentUtil;

	@RequestMapping(params = "!mode")
	public String index(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@CookieValue(value = "cart_list", required = false, defaultValue = "") String cookie,
			@RequestParam Map params) throws Exception {
		params.put("sessionid", session.getId());
		MCMap member = (MCMap) session.getAttribute("member");
		if (member != null) {
			params.put("session_member_id", member.get("member_id"));
		}
		if("Y".equals(StringUtil.nvl(	(String)params.get("agree"), ""))) {
			session.setAttribute("agree", params.get("agree"));
			session.setAttribute("use_chk", params.get("use_chk"));
			session.setAttribute("ps_chk", params.get("ps_chk"));
		}
		model.addAttribute("data", cartService.list(params));
		return "/giftcard/mypage/shopping/cart/list";
	}

	@RequestMapping(params = "mode=add_cart")
	@Transactional(rollbackFor = { Exception.class })
	public String add_cart(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map params)
			throws Exception {
		params.put("sessionid", session.getId());
		MCMap member = (MCMap) session.getAttribute("member");
		if (member != null) {
			params.put("session_member_id", member.get("member_id"));
			params.put("session_group_seq", member.get("group_seq"));
		}
		Map rstMap = cartService.add_cart(params);
		if ("-1".equals(rstMap.get("rst"))) {
			request.setAttribute("message", rstMap.get("msg"));
			return "message";
		}
		return "redirect:/giftcard/mypage/shopping/cart/index.do?cart_no=" + params.get("cart_no");
		//return cartService.add_cart(params);
		
	}
	@ResponseBody
	@RequestMapping(params = "mode=add_cartAjax")
	@Transactional(rollbackFor = { Exception.class })
	public Map add_cartAjax(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map params)
			throws Exception {
		params.put("sessionid", session.getId());
		MCMap member = (MCMap) session.getAttribute("member");
		if (member != null) {
			params.put("session_member_id", member.get("member_id"));
			params.put("session_group_seq", member.get("group_seq"));
		}
		Map rstMap = cartService.add_cart(params);
		if ("-1".equals(rstMap.get("rst"))) {
			request.setAttribute("message", rstMap.get("msg"));
		}
		return rstMap;
	}

	@RequestMapping(params = "mode=direct_order")
	@Transactional(rollbackFor = { Exception.class })
	public String direct_order(ModelMap model, HttpServletRequest request, HttpSession session,
			@RequestParam Map params) throws Exception {
		String returnurl = "/giftcard/mypage/shopping/cart/step2";
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("sessionid", session.getId());// 비회원용 세션아이디

		if (member != null) {
			params.put("session_member_id", member.get("member_id"));
			params.put("session_group_seq", member.get("group_seq"));
			params.put("member_seq", member.get("member_seq"));
		}
		Map rstMap = cartService.direct_order(params);
		if ("-1".equals(rstMap.get("rst"))) {
			request.setAttribute("message", "상품의 재고수량을 확인하시기 바랍니다.");
			request.setAttribute("redirect", "/giftcard/mypage/shopping/cart/index.do");
			return "message";
		}
		if ("-2".equals(rstMap.get("rst"))) {
			request.setAttribute("message", rstMap.get("msg"));
			return "message";
		}
		if (member == null) { 
			if ("Y".equals(StringUtil.nvl((String)session.getAttribute("agree"),""))) {
				if (!("Y".equals(StringUtil.nvl((String)session.getAttribute("use_chk"),"")) && "Y".equals(StringUtil.nvl((String)session.getAttribute("ps_chk"),"")))) {
					request.setAttribute("message", "약관에 동의해 주시기 바랍니다.");
					request.setAttribute("params", params);
					return "message";
				} else {
					returnurl = "/giftcard/mypage/shopping/cart/nomember";
				}
			} else if (params.containsKey("agree")) {
				if (!("Y".equals(params.get("use_chk")) && "Y".equals(params.get("ps_chk")))) {
					request.setAttribute("message", "약관에 동의해 주시기 바랍니다.");
					request.setAttribute("params", params);
					return "message";
				} else {
					session.setAttribute("agree", params.get("agree"));
					session.setAttribute("use_chk", params.get("use_chk"));
					session.setAttribute("ps_chk", params.get("ps_chk"));
					returnurl = "/giftcard/mypage/shopping/cart/nomember";
				}
			} else if (member == null) {
				request.setAttribute("params", params);
				request.setAttribute("redirect", "/giftcard/login/login_2.do?mode=guest");
				return "message";
			}
		}
		model.addAttribute("data", rstMap);
		return returnurl;
	}

	@ResponseBody
	@RequestMapping(params = "mode=remove_cart")
	@Transactional(rollbackFor = { Exception.class })
	public Map remove_cart(ModelMap model, HttpServletRequest request, HttpSession session,
			@RequestParam Map<String, Object> jsonObject) throws Exception {
		Map params = (JSONObject) JSONValue.parse((String) jsonObject.get("jData")); // JSON으로 보내준 데이터를 사용
		return cartService.remove_cart(params);
	}

	@RequestMapping(params = "mode=step2")
	@Transactional(rollbackFor = { Exception.class })
	public String step2(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map params,
			@RequestParam(value = "chk", defaultValue = "") String[] chk) throws Exception {
		String returnurl = "/giftcard/mypage/shopping/cart/step2";
		MCMap member = (MCMap) session.getAttribute("member");
		if (chk.length <= 0) {
			request.setAttribute("message", "결제하실 상품을 1개이상 선택하셔야 합니다.");
			request.setAttribute("redirect", "/giftcard/mypage/shopping/cart/index.do");
			return "message";
		}
		if (member == null) {
			if ("Y".equals(StringUtil.nvl((String)session.getAttribute("agree"),""))) {
				if (!("Y".equals(StringUtil.nvl((String)session.getAttribute("use_chk"),"")) && "Y".equals(StringUtil.nvl((String)session.getAttribute("ps_chk"),"")))) {
					request.setAttribute("message", "약관에 동의해 주시기 바랍니다.");
					request.setAttribute("params", params);
					request.setAttribute("redirect", "/giftcard/login/login_2.do?mode=guest");
					return "message";
				} else {
					returnurl = "/giftcard/mypage/shopping/cart/nomember";
				}
			} else  if (params.containsKey("agree")) {
				if (!("Y".equals(params.get("use_chk")) && "Y".equals(params.get("ps_chk")))) {
					request.setAttribute("message", "약관에 동의해 주시기 바랍니다.");
					request.setAttribute("params", params);
					return "message";
				} else {
					returnurl = "/giftcard/mypage/shopping/cart/nomember";
				}
			} else if (member == null) {
				request.setAttribute("params", params);
				request.setAttribute("redirect", "/giftcard/login/login_2.do?mode=guest");
				return "message";
			}
		}else if (member != null) {
			params.put("session_member_id", member.get("member_id"));
			params.put("member_seq", member.get("member_seq"));
		}
		params.put("sessionid", session.getId());// 비회원용 세션아이디

		params.put("cart_list", Arrays.asList(chk));
		Map rstMap = cartService.step2(params, chk);
		if ("-1".equals(rstMap.get("rst")) || "-2".equals(rstMap.get("rst"))) {
			request.setAttribute("message", "상품의 재고수량을 확인하시기 바랍니다.");
			request.setAttribute("redirect", "/giftcard/mypage/shopping/cart/index.do");
			return "message";
		}

		model.addAttribute("data", rstMap);
		return returnurl;
	}

	/*
	 * @RequestMapping(params="mode=pay_ing")
	 * 
	 * @Transactional(rollbackFor = { Exception.class }) public String
	 * pay_int(ModelMap model, HttpServletRequest request, HttpSession
	 * session, @RequestParam Map params, @PathVariable("index") String
	 * index, @RequestParam(value="cart_no", required=true, defaultValue="")
	 * String[] cart_no, @RequestParam(value="message", required=false,
	 * defaultValue="[]") String[] message) throws Exception{
	 * 
	 * 
	 * if(StringUtil.isEmpty(request.getParameter("devTest")) ) {
	 * 
	 * request.setAttribute("message", "현재 결제 개발중입니다. 다음으로 그냥 넘어감");
	 * request.setAttribute("redirect",
	 * "/giftcard/mypage/shopping/cart/"+index+".do?mode=pay_result");
	 * 
	 * return "message"; }else if(request.getParameter("devTest").equals("T") ) {
	 * request.setAttribute("message", "현재 결제 개발중입니다. 다음으로 그냥 넘어감");
	 * request.setAttribute("redirect",
	 * "/giftcard/mypage/shopping/cart/"+index+".do?mode=pay_result");
	 * 
	 * return "message"; }
	 *//**********************************************************************************************
		 *
		 * 파일명 : AGS_pay_ing.jsp 최종수정일자 : 2009/5/12
		 *
		 * 올더게이트 플러그인에서 리턴된 데이타를 받아서 소켓결제요청을 합니다.
		 *
		 * Copyright AEGIS ENTERPRISE.Co.,Ltd. All rights reserved.
		 *
		 *
		 * ※ 유의사항 ※ 1. "|"(파이프) 값은 결제처리 중 구분자로 사용하는 문자이므로 결제 데이터에 "|"이 있을경우 결제가 정상적으로
		 * 처리되지 않습니다.(수신 데이터 길이 에러 등의 사유)
		 **********************************************************************************************/
	/*
	 * request.setCharacterEncoding("UTF-8");
	 * 
	 *//****************************************************************************
		 *
		 * [1] 올더게이트 결제시 사용할 로컬 통신서버 IP/Port 번호
		 *
		 * ENCTYPE : 결제 종류에 따른 구분(0:안심클릭,일반결제 2:ISP) LOCALADDR : PG서버와 통신을 담당하는
		 * 암호화Process가 위치해 있는 IP LOCALPORT : 포트
		 *
		 ****************************************************************************/
	/*
	 * 
	 * String LOCALADDR = "220.85.12.3"; int LOCALPORT = 29760; String ENCTYPE =
	 * "0";
	 * 
	 *//****************************************************************************
		 *
		 * [2]승인/취소에 사용될 클래스 객체 생성
		 *
		 ****************************************************************************/
	/*
	 * 
	 * PgClientBean40 agspay = new PgClientBean40( LOCALADDR, LOCALPORT );
	 * 
	 *//****************************************************************************
		 *
		 * [3] AGS_pay.html 로 부터 넘겨받을 데이타
		 *
		 ****************************************************************************/
	/*
	 * 
	 * 공통사용 agspay.setValue("AgsPayHome", globals.getAGSPAY_PATH()); //올더게이트 결제설치
	 * 디렉토리 (상점에 맞게 수정) agspay.setValue("log","true"); //true : 로그기록, false :
	 * 로그기록안함. agspay.setValue("logLevel","INFO"); //로그레벨 : DEBUG, INFO, WARN,
	 * ERROR, FATAL (해당 레벨이상의 로그만 기록됨) agspay.setValue("UseNetCancel","true");
	 * //true : 망취소 사용. false: 망취소 미사용 agspay.setValue("Type", "Pay"); //고정값(수정불가)
	 * agspay.setValue("AuthTy", request.getParameter("AuthTy")); //결제형태
	 * agspay.setValue("SubTy", request.getParameter("SubTy")); //서브결제형태
	 * agspay.setValue("StoreId", request.getParameter("StoreId")); //상점아이디
	 * agspay.setValue("OrdNo", request.getParameter("OrdNo")); //주문번호
	 * agspay.setValue("Amt", request.getParameter("Amt")); //금액
	 * agspay.setValue("UserEmail", request.getParameter("UserEmail")); //주문자이메일
	 * agspay.setValue("ProdNm", request.getParameter("ProdNm")); //상품명
	 * 
	 * 신용카드&가상계좌사용 agspay.setValue("MallUrl", request.getParameter("MallUrl"));
	 * //상점URL주소 agspay.setValue("UserId", request.getParameter("UserId")); //회원아이디
	 * 
	 * 신용카드사용 agspay.setValue("OrdNm", request.getParameter("OrdNm")); //주문자명
	 * agspay.setValue("OrdPhone", request.getParameter("OrdPhone") ); //주문자연락처
	 * agspay.setValue("OrdAddr", request.getParameter("OrdAddr")); //주문자주소
	 * agspay.setValue("RcpNm", request.getParameter("RcpNm")); //수신자명
	 * agspay.setValue("RcpPhone", request.getParameter("RcpPhone") ); //수신자연락처
	 * agspay.setValue("DlvAddr", request.getParameter("DlvAddr")); //배송지주소
	 * agspay.setValue("Remark", request.getParameter("Remark")); //기타요구사항
	 * agspay.setValue("DeviId", request.getParameter("DeviId") ); //단말기아이디
	 * agspay.setValue("AuthYn", request.getParameter("AuthYn") ); //인증여부
	 * agspay.setValue("Instmt", request.getParameter("Instmt") ); //할부개월수
	 * agspay.setValue("UserIp",request.getRemoteAddr()); //회원 IP
	 * 
	 * 신용카드(ISP) agspay.setValue("partial_mm", request.getParameter("partial_mm") );
	 * //일반할부기간 agspay.setValue("noIntMonth", request.getParameter("noIntMonth") );
	 * //무이자할부기간 agspay.setValue("KVP_CURRENCY",
	 * request.getParameter("KVP_CURRENCY") ); //KVP_통화코드
	 * agspay.setValue("KVP_CARDCODE", request.getParameter("KVP_CARDCODE") );
	 * //KVP_카드사코드 agspay.setValue("KVP_SESSIONKEY",
	 * request.getParameter("KVP_SESSIONKEY") ); //KVP_SESSIONKEY
	 * agspay.setValue("KVP_ENCDATA", request.getParameter("KVP_ENCDATA") );
	 * //KVP_ENCDATA agspay.setValue("KVP_CONAME",
	 * request.getParameter("KVP_CONAME") ); //KVP_카드명 agspay.setValue("KVP_NOINT",
	 * request.getParameter("KVP_NOINT") ); //KVP_무이자=1 일반=0
	 * agspay.setValue("KVP_QUOTA", request.getParameter("KVP_QUOTA") ); //KVP_할부개월
	 * 
	 * 신용카드(안심) agspay.setValue("CardNo", request.getParameter("CardNo") ); //카드번호
	 * agspay.setValue("MPI_CAVV",request.getParameter("MPI_CAVV") ); //MPI_CAVV
	 * agspay.setValue("MPI_ECI",request.getParameter("MPI_ECI") ); //MPI_ECI
	 * agspay.setValue("MPI_MD64",request.getParameter("MPI_MD64") ); //MPI_MD64
	 * 
	 * 신용카드(일반) agspay.setValue("ExpMon", request.getParameter("ExpMon") );
	 * //유효기간(월) agspay.setValue("ExpYear", request.getParameter("ExpYear") );
	 * //유효기간(년) agspay.setValue("Passwd", request.getParameter("Passwd") ); //비밀번호
	 * agspay.setValue("SocId", request.getParameter("SocId") ); //주민등록번호/사업자등록번호
	 * 
	 * 계좌이체사용 agspay.setValue("ICHE_OUTBANKNAME",
	 * request.getParameter("ICHE_OUTBANKNAME")); //이체은행명
	 * agspay.setValue("ICHE_OUTACCTNO", request.getParameter("ICHE_OUTACCTNO") );
	 * //이체계좌번호 agspay.setValue("ICHE_OUTBANKMASTER",
	 * request.getParameter("ICHE_OUTBANKMASTER")); //이체계좌소유주
	 * agspay.setValue("ICHE_AMOUNT", request.getParameter("ICHE_AMOUNT") ); //이체금액
	 * 
	 * 핸드폰사용 agspay.setValue("HP_SERVERINFO", request.getParameter("HP_SERVERINFO")
	 * ); //SERVER_INFO(핸드폰결제) agspay.setValue("HP_HANDPHONE",
	 * request.getParameter("HP_HANDPHONE") ); //HANDPHONE(핸드폰결제)
	 * agspay.setValue("HP_COMPANY", request.getParameter("HP_COMPANY") );
	 * //COMPANY(핸드폰결제) agspay.setValue("HP_ID", request.getParameter("HP_ID") );
	 * //HP_ID(핸드폰결제) agspay.setValue("HP_SUBID", request.getParameter("HP_SUBID")
	 * ); //HP_SUBID(핸드폰결제) agspay.setValue("HP_UNITType",
	 * request.getParameter("HP_UNITType") ); //HP_UNITType(핸드폰결제)
	 * agspay.setValue("HP_IDEN", request.getParameter("HP_IDEN") );
	 * //HP_IDEN(핸드폰결제) agspay.setValue("HP_IPADDR",
	 * request.getParameter("HP_IPADDR") ); //HP_IPADDR(핸드폰결제)
	 * 
	 * ARS사용 agspay.setValue("ARS_NAME", request.getParameter("ARS_NAME"));
	 * //ARS_NAME(ARS결제) agspay.setValue("ARS_PHONE",
	 * request.getParameter("ARS_PHONE") ); //ARS_PHONE(ARS결제)
	 * 
	 * 가상계좌사용 agspay.setValue("VIRTUAL_CENTERCD",
	 * request.getParameter("VIRTUAL_CENTERCD")); //은행코드
	 * agspay.setValue("VIRTUAL_DEPODT", request.getParameter("VIRTUAL_DEPODT") );
	 * //입금예정일 agspay.setValue("ZuminCode", request.getParameter("ZuminCode") );
	 * //주민번호 agspay.setValue("MallPage", request.getParameter("MallPage")); //상점
	 * 입/출금 통보 페이지 agspay.setValue("VIRTUAL_NO", request.getParameter("VIRTUAL_NO")
	 * ); //가상계좌번호
	 * 
	 * 이지스에스크로사용 agspay.setValue("ES_SENDNO", request.getParameter("ES_SENDNO") );
	 * //이지스에스크로(전문추적번호)
	 * 
	 * 계좌이체(소켓) 결제 사용 변수 agspay.setValue("ICHE_SOCKETYN",
	 * request.getParameter("ICHE_SOCKETYN")); //계좌이체(소켓) 사용 여부
	 * agspay.setValue("ICHE_POSMTID", request.getParameter("ICHE_POSMTID"));
	 * //계좌이체(소켓) 이용기관주문번호 agspay.setValue("ICHE_FNBCMTID",
	 * request.getParameter("ICHE_FNBCMTID")); //계좌이체(소켓) FNBC거래번호
	 * agspay.setValue("ICHE_APTRTS", request.getParameter("ICHE_APTRTS"));
	 * //계좌이체(소켓) 이체 시각 agspay.setValue("ICHE_REMARK1",
	 * request.getParameter("ICHE_REMARK1")); //계좌이체(소켓) 기타사항1
	 * agspay.setValue("ICHE_REMARK2", request.getParameter("ICHE_REMARK2"));
	 * //계좌이체(소켓) 기타사항2 agspay.setValue("ICHE_ECWYN",
	 * request.getParameter("ICHE_ECWYN")); //계좌이체(소켓) 에스크로여부
	 * agspay.setValue("ICHE_ECWID", request.getParameter("ICHE_ECWID")); //계좌이체(소켓)
	 * 에스크로ID agspay.setValue("ICHE_ECWAMT1", request.getParameter("ICHE_ECWAMT1"));
	 * //계좌이체(소켓) 에스크로결제금액1 agspay.setValue("ICHE_ECWAMT2",
	 * request.getParameter("ICHE_ECWAMT2")); //계좌이체(소켓) 에스크로결제금액2
	 * agspay.setValue("ICHE_CASHYN", request.getParameter("ICHE_CASHYN"));
	 * //계좌이체(소켓) 현금영수증발행여부 agspay.setValue("ICHE_CASHGUBUN_CD",
	 * request.getParameter("ICHE_CASHGUBUN_CD")); //계좌이체(소켓) 현금영수증구분
	 * agspay.setValue("ICHE_CASHID_NO", request.getParameter("ICHE_CASHID_NO"));
	 * //계좌이체(소켓) 현금영수증신분확인번호
	 * 
	 * 계좌이체-텔래뱅킹(소켓) 결제 사용 변수 agspay.setValue("ICHEARS_SOCKETYN",
	 * request.getParameter("ICHEARS_SOCKETYN")); //텔레뱅킹계좌이체(소켓) 사용 여부
	 * agspay.setValue("ICHEARS_ADMNO", request.getParameter("ICHEARS_ADMNO"));
	 * //텔레뱅킹계좌이체 승인번호 agspay.setValue("ICHEARS_POSMTID",
	 * request.getParameter("ICHEARS_POSMTID")); //텔레뱅킹계좌이체 이용기관주문번호
	 * agspay.setValue("ICHEARS_CENTERCD",
	 * request.getParameter("ICHEARS_CENTERCD")); //텔레뱅킹계좌이체 은행코드
	 * agspay.setValue("ICHEARS_HPNO", request.getParameter("ICHEARS_HPNO"));
	 * //텔레뱅킹계좌이체 휴대폰번호 agspay.setValue("AGS_HASHDATA",
	 * request.getParameter("AGS_HASHDATA")); // 전역 해쉬 변수
	 * 
	 *//****************************************************************************
		 *
		 * [4] 올더게이트 결제서버로 결제를 요청합니다.
		 *
		 ****************************************************************************/
	/*
	 * 
	 * agspay.startPay();
	 * 
	 *//****************************************************************************
		 *
		 * [5] 결제결과에 따른 상점DB 저장 및 기타 필요한 처리작업을 수행하는 부분입니다.
		 *
		 * 아래의 결과값들을 통하여 각 결제수단별 결제결과값을 사용하실 수 있습니다.
		 * 
		 * -- 공통사용 -- 업체ID : agspay.getResult("rStoreId") 주문번호 :
		 * agspay.getResult("rOrdNo") 상품명 : agspay.getResult("rProdNm") 거래금액 :
		 * agspay.getResult("rAmt") 성공여부 : agspay.getResult("rSuccYn") (성공:y 실패:n) 결과메시지
		 * : agspay.getResult("rResMsg")
		 *
		 * 1. 신용카드
		 * 
		 * 전문코드 : agspay.getResult("rBusiCd") 거래번호 : agspay.getResult("rDealNo") 승인번호 :
		 * agspay.getResult("rApprNo") 할부개월 : agspay.getResult("rInstmt") 승인시각 :
		 * agspay.getResult("rApprTm") 카드사코드 : agspay.getResult("rCardCd")
		 *
		 * 2.계좌이체(인터넷뱅킹/텔레뱅킹) 에스크로주문번호 : agspay.getResult("ES_SENDNO") (에스크로 결제시)
		 *
		 * 3.가상계좌 가상계좌의 결제성공은 가상계좌발급의 성공만을 의미하며 입금대기상태로 실제 고객이 입금을 완료한 것은 아닙니다. 따라서 가상계좌
		 * 결제완료시 결제완료로 처리하여 상품을 배송하시면 안됩니다. 결제후 고객이 발급받은 계좌로 입금이 완료되면 MallPage(상점 입금통보
		 * 페이지(가상계좌))로 입금결과가 전송되며 이때 비로소 결제가 완료되게 되므로 결제완료에 대한 처리(배송요청 등)은 MallPage에
		 * 작업해주셔야 합니다. 결제종류 : agspay.getResult("rAuthTy") (가상계좌 일반 : vir_n 유클릭 : vir_u
		 * 에스크로 : vir_s) 승인일자 : agspay.getResult("rApprTm") 가상계좌번호 :
		 * agspay.getResult("rVirNo")
		 *
		 * 4.핸드폰결제 핸드폰결제일 : agspay.getResult("rHP_DATE") 핸드폰결제 TID :
		 * agspay.getResult("rHP_TID")
		 *
		 * 5.ARS결제 ARS결제일 : agspay.getResult("rHP_DATE") ARS결제 TID :
		 * agspay.getResult("rHP_TID")
		 *
		 ****************************************************************************/
	/*
	 * String cancelReq = "false"; if(agspay.getResult("rSuccYn").equals("y")){
	 * params.put("rdealno", agspay.getResult("rDealNo"));//거래번호
	 * params.put("rapprno", agspay.getResult("rApprNo"));//승인번호
	 * params.put("rinstmt", agspay.getResult("rInstmt"));//할부개월
	 * params.put("rapprtm", agspay.getResult("rApprTm"));//승인시각
	 * 
	 * params.put("cart_no_arr", cart_no); params.put("msg_arr", message);
	 * params.put("orderno", params.get("OrdNo"));//주문번호 params.put("userid",
	 * params.get("UserId"));//회원아이디 params.put("email",
	 * params.get("UserEmail"));//이메일 params.put("zipcd",
	 * params.get("zip1")+"-"+params.get("zip2"));//우편번호 params.put("receiver",
	 * params.get("receiver"));//수취인 params.put("cell",
	 * params.get("cell1")+"-"+params.get("cell2")+"-"+params.get("cell3"));//받는이
	 * 휴대폰 params.put("tel",
	 * params.get("tel1")+"-"+params.get("tel2")+"-"+params.get("tel3"));//받는이 휴대폰
	 * params.put("paytyp", params.get("AuthTy"));//결재타입 params.put("subty",
	 * params.get("SubTy"));//서브결재형태 params.put("payamt", params.get("Amt"));//결재금액
	 * 
	 * MCMap member = (MCMap) session.getAttribute("member"); if(member != null){
	 * params.put("group_seq", (String) member.get("group_seq"));
	 * params.put("memberLogin", "Y"); }else{ params.put("memberLogin", "N");
	 * params.put("guestPhone",
	 * params.get("m_cell1")+"-"+params.get("m_cell2")+"-"+params.get("m_cell3")); }
	 * 
	 * if( agspay.getResult("AuthTy").equals("virtual") ){ //가상계좌결제의 경우 입금이 완료되지 않은
	 * 입금대기상태(가상계좌 발급성공)이므로 상품을 배송하시면 안됩니다. cartService.success(params); }else{ //
	 * 결제성공에 따른 상점처리부분 try{ cartService.success(params);
	 * cartService.erp_order(params); }catch(Exception e){ cancelReq = "true"; if(
	 * cancelReq.equals("true") ) { agspay.setValue("Type", "Cancel"); // 고정
	 * agspay.setValue("CancelMsg", "DB FAIL"); // 취소사유 agspay.startPay(); } throw
	 * e; } //out.println ("결제가 성공처리되었습니다. [" + agspay.getResult("rSuccYn") + "]" +
	 * agspay.getResult("rResMsg")); } }else{ // 결제실패에 따른 상점처리부분
	 * log.error("결제가 실패처리되었습니다. [" + agspay.getResult("rSuccYn") + "]" +
	 * agspay.getResult("rResMsg")); request.setAttribute("message",
	 * "결제가 실패처리되었습니다."); cancelReq = "true"; if( cancelReq.equals("true") ) {
	 * agspay.setValue("Type", "Cancel"); // 고정 agspay.setValue("CancelMsg",
	 * "DB FAIL"); // 취소사유 agspay.startPay(); } return "message"; }
	 * 
	 * 
	 *//*******************************************************************
		 * [6] 결제가 정상처리되지 못했을 경우 agspay.getResult("NetCancID") 값을 이용하여 결제결과에 대한 재확인요청을 할
		 * 수 있습니다.
		 * 
		 * 추가 데이터송수신이 발생하므로 결제가 정상처리되지 않았을 경우에만 사용하시기 바랍니다.
		 *
		 * 사용방법 : agspay.checkPayResult(agspay.getResult("NetCancID"));
		 * 
		 *******************************************************************/

	/*
	 * 
	 * 
	 * //agspay.setValue("Type", "Pay"); // 고정
	 * //agspay.checkPayResult(agspay.getResult("NetCancID"));
	 * 
	 * 
	 *//*******************************************************************
		 * [7] 상점DB 저장 및 기타 처리작업 수행실패시 강제취소
		 * 
		 * cancelReq : "true" 강제취소실행, "false" 강제취소실행안함.
		 *
		 * 결제결과에 따른 상점처리부분 수행 중 실패하는 경우 아래의 코드를 참조하여 거래를 취소할 수 있습니다. 취소성공여부 :
		 * agspay.getResult("rCancelSuccYn") (성공:y 실패:n) 취소결과메시지 :
		 * agspay.getResult("rCancelResMsg")
		 * 
		 * 유의사항 : 가상계좌(virtual)는 강제취소 기능이 지원되지 않습니다.
		 *******************************************************************//*
																				 * 
																				 * // 상점처리부분 수행실패시 cancelReq를 "true"로
																				 * 변경하여 // 결제취소를 수행되도록 할 수 있습니다. //
																				 * cancelReq의 "true"값으로 변경조건은 상점에서 판단하셔야
																				 * 합니다.
																				 * 
																				 * String cancelReq = "false";
																				 * 
																				 * if( cancelReq.equals("true") ) {
																				 * agspay.setValue("Type", "Cancel"); //
																				 * 고정 agspay.setValue("CancelMsg",
																				 * "DB FAIL"); // 취소사유
																				 * agspay.startPay(); }
																				 * 
																				 * 
																				 * model.addAttribute("agspay", agspay);
																				 * model.addAttribute("pathIndex",
																				 * index); return
																				 * "mypage/shopping/cart/pay_ing"; }
																				 */

	@RequestMapping(params="mode=pay_ing")
	@Transactional(rollbackFor = { Exception.class })
	public String pay_int(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map params, @PathVariable("index") String index, @RequestParam(value="cart_no", required=true, defaultValue="") String[] cart_no, @RequestParam(value="message", required=false, defaultValue="[]") String[] message) throws Exception{

		request.setCharacterEncoding("UTF-8");
		/*공통사용*/
		/****************************************************************************
		*
		*  결제결과에 따른 상점DB 저장 및 기타 필요한 처리작업을 수행하는 부분입니다.
		*
		*	아래의 결과값들을 통하여 각 결제수단별 결제결과값을 사용하실 수 있습니다.
		*
		*	1.가상계좌
		*	가상계좌의 결제성공은 가상계좌발급의 성공만을 의미하며 입금대기상태로 실제 고객이 입금을 완료한 것은 아닙니다.
		*	따라서 가상계좌 결제완료시 결제완료로 처리하여 상품을 배송하시면 안됩니다.
		*
		*	결제후 고객이 발급받은 계좌로 입금이 완료되면 MallPage(상점 입금통보 페이지(가상계좌))로 입금결과가 전송되며
		*	이때 비로소 결제가 완료되게 되므로 결제완료에 대한 처리(배송요청 등)은  MallPage에 작업해주셔야 합니다.
		*	결제종류 : agspay.getResult("rAuthTy") (가상계좌 일반 : vir_n 유클릭 : vir_u 에스크로 : vir_s)
		*	승인일자 : agspay.getResult("rApprTm")
		*	가상계좌번호 : agspay.getResult("rVirNo")
		*
		****************************************************************************/
		String cancelReq = "false";
		String rSuccYn = "y";
		
		
		if(StringUtil.nvl(rSuccYn, "y").equals("y")){ 
			/*
			 * params.put("rdealno", agspay.getResult("rDealNo"));//거래번호
			 * params.put("rapprno", agspay.getResult("rApprNo"));//승인번호
			 * params.put("rinstmt", agspay.getResult("rInstmt"));//할부개월
			 * params.put("rapprtm", agspay.getResult("rApprTm"));//승인시각
			 */			
			params.put("rapprno", params.get("rapprno"));//승인번호
			params.put("rdealno", params.get("rdealno"));//거래번호
			//params.put("rinstmt", "12");//할부개월
			params.put("rapprtm", params.get("rapprtm"));//승인시각
			
			params.put("cart_no_arr", cart_no);
			params.put("msg_arr", message);
			params.put("orderno", params.get("OrdNo"));//주문번호
			params.put("userid", params.get("UserId"));//회원아이디
			params.put("email", params.get("UserEmail"));//이메일
			params.put("zipcd", params.get("zip1")+"-"+params.get("zip2"));//우편번호
			params.put("receiver", params.get("receiver"));//수취인
			params.put("cell", params.get("cell1")+"-"+params.get("cell2")+"-"+params.get("cell3"));//받는이 휴대폰
			params.put("tel", params.get("tel1")+"-"+params.get("tel2")+"-"+params.get("tel3"));//받는이 휴대폰
			params.put("paytyp", StringUtil.nvl((String)params.get("AuthTy"), "virtual"));//결재타입
			params.put("subty", params.get("SubTy"));//서브결재형태
			params.put("payamt", params.get("Amt"));//결재금액

		
			MCMap member = (MCMap) session.getAttribute("member");
			if(member != null){
				params.put("group_seq", (String) member.get("group_seq"));
				params.put("memberLogin", "Y");
			}else{
				params.put("memberLogin", "N");
				params.put("guestPhone", params.get("m_cell1")+"-"+params.get("m_cell2")+"-"+params.get("m_cell3"));
			}
 
			try {
				//가상계좌결제의 경우 입금이 완료되지 않은 입금대기상태(가상계좌 발급성공)이므로 상품을 배송하시면 안됩니다.
				cartService.success(params);
			}catch(Exception e) { //결제 실패 
				cancelReq = "true";
				if( cancelReq.equals("true") )
				{
					//agspay.setValue("Type", "Cancel"); // 고정
					//agspay.setValue("CancelMsg", "DB FAIL"); // 취소사유
					//agspay.startPay();
				}
				throw e;
			}			
		}else{
			// 결제실패에 따른 상점처리부분
			//log.error("결제가 실패처리되었습니다. [" + agspay.getResult("rSuccYn") + "]" + agspay.getResult("rResMsg"));
			log.error("결제가 실패처리되었습니다. ");
			request.setAttribute("message", "결제가 실패처리되었습니다.");
			cancelReq = "true";
			if( cancelReq.equals("true") )
			{
				//agspay.setValue("Type", "Cancel"); // 고정
				//agspay.setValue("CancelMsg", "DB FAIL"); // 취소사유
				//agspay.startPay();
			}
			return "message";
		}
		

		/*******************************************************************
		* [6] 결제가 정상처리되지 못했을 경우 agspay.getResult("NetCancID") 값을 이용하여                                     
		* 결제결과에 대한 재확인요청을 할 수 있습니다.
		* 
		* 추가 데이터송수신이 발생하므로 결제가 정상처리되지 않았을 경우에만 사용하시기 바랍니다. 
		*
		* 사용방법 :
		* agspay.checkPayResult(agspay.getResult("NetCancID"));
		* 
		*******************************************************************/		
		//agspay.setValue("Type", "Pay"); // 고정
		//agspay.checkPayResult(agspay.getResult("NetCancID"));		
		/*******************************************************************
		* [7] 상점DB 저장 및 기타 처리작업 수행실패시 강제취소                                      
		*   
		* cancelReq : "true" 강제취소실행, "false" 강제취소실행안함.
		*
		* 결제결과에 따른 상점처리부분 수행 중 실패하는 경우    
		* 아래의 코드를 참조하여 거래를 취소할 수 있습니다.
		*	취소성공여부 : agspay.getResult("rCancelSuccYn") (성공:y 실패:n)
		*	취소결과메시지 : agspay.getResult("rCancelResMsg")
		*                                                      
		* 유의사항 :
		* 가상계좌(virtual)는 강제취소 기능이 지원되지 않습니다.
		*******************************************************************/		
		// 상점처리부분 수행실패시 cancelReq를 "true"로 변경하여 
		// 결제취소를 수행되도록 할 수 있습니다.
		// cancelReq의 "true"값으로 변경조건은 상점에서 판단하셔야 합니다.
		/*
		String cancelReq = "false";

		if( cancelReq.equals("true") )
		{
			agspay.setValue("Type", "Cancel"); // 고정
			agspay.setValue("CancelMsg", "DB FAIL"); // 취소사유
			agspay.startPay();
		}
		*/
		
		//model.addAttribute("agspay", agspay);
		model.addAttribute("pathIndex", index);
		model.addAttribute("params", params);
		return "/giftcard/mypage/shopping/cart/pay_ing";
	}
	
	@RequestMapping(params = "mode=pay_result")
	public String pay_result(ModelMap model, HttpServletRequest request, HttpSession session,
			@PathVariable("index") String index, @RequestParam Map params) throws Exception {
		String returnPath = "";
		model.addAttribute("data", cartService.pay_result(params));
		// TODO: 개발위해 주석 
		if("index".equals(index)){
			returnPath = "/giftcard/mypage/shopping/cart/pay_result";
		}else{
			returnPath = "/giftcard/mypage/shopping/cart/nomember_result";
		}
		return returnPath;
	}

	@ResponseBody
	@Transactional(rollbackFor = { Exception.class })
	@RequestMapping(params = "mode=virPayResult")
	public String virPayResult(ModelMap model, HttpServletRequest request, HttpSession session,
			@RequestParam Map params) throws Exception {
		/***************************************************************************************************************
		 * 올더게이트로 부터 입/출금 데이타를 받아서 상점에서 처리 한 후 올더게이트로 다시 응답값을 리턴한다. 업체에 맞게 수정하여 작업하면 된다.
		 ***************************************************************************************************************/
		/***********************************
		 * 올더게이트로 부터 넘겨 받는 값들 시작
		 *************************************/
		String trcode = request.getParameter("trcode"); // 거래코드
		String service_id = request.getParameter("service_id"); // 상점아이디
		String orderdt = request.getParameter("orderdt"); // 승인일자
		String virno = request.getParameter("virno"); // 가상계좌번호
		String deal_won = request.getParameter("deal_won"); // 입금액
		String ordno = request.getParameter("ordno"); // 주문번호
		String inputnm = request.getParameter("inputnm"); // 입금자명

		params.put("orderno", params.get("ordno"));// 주문번호
		params.put("rapprtm", params.get("orderdt"));// 승인시각

		log.info("무통장 계좌이체 승인시각 : " + params.get("orderdt"));
		/* 무통장주문 문자 */

		cartService.virAcctResult(params);
		//cartService.erp_order(params);
		/***************************************************************************************************************
		 * 상점에서 해당 거래에 대한 처리 db 처리 등....
		 *
		 * trcode = "1" ☞ 일반가상계좌 입금통보전문 (이지스효성 new 에스크로 포함) trcode = "2" ☞ 일반가상계좌 취소통보전문
		 * (이지스효성 new 에스크로 포함)
		 *
		 * ※ 에스크로가상계좌의 경우 입금자명 값은 통보전문에 들어가지 않습니다.
		 * 
		 * 
		 * 
		 ***************************************************************************************************************/

		/******************************************
		 * 처리 결과 리턴
		 ******************************************************/
		String rResMsg = "";
		String rSuccYn = "y";// 정상 : y 실패 : n
		// 정상처리 경우 거래코드|상점아이디|주문일시|가상계좌번호|처리결과|
		rResMsg = trcode + "|" + service_id + "|" + orderdt + "|" + virno + "|" + rSuccYn + "|";
		return rResMsg;
	}
	
	@ResponseBody
	@Transactional(rollbackFor = { Exception.class })
	@RequestMapping(params = "mode=virAcctResult")
	public String virAcctResult(ModelMap model, HttpServletRequest request, HttpSession session,
			@RequestParam Map params) throws Exception {
		/***************************************************************************************************************
		 * 올더게이트로 부터 입/출금 데이타를 받아서 상점에서 처리 한 후 올더게이트로 다시 응답값을 리턴한다. 업체에 맞게 수정하여 작업하면 된다.
		 ***************************************************************************************************************/

		/***********************************
		 * 올더게이트로 부터 넘겨 받는 값들 시작
		 *************************************/
		String trcode = request.getParameter("trcode"); // 거래코드
		String service_id = request.getParameter("service_id"); // 상점아이디
		String orderdt = request.getParameter("orderdt"); // 승인일자
		String virno = request.getParameter("virno"); // 가상계좌번호
		String deal_won = request.getParameter("deal_won"); // 입금액
		String ordno = request.getParameter("ordno"); // 주문번호
		String inputnm = request.getParameter("inputnm"); // 입금자명

		params.put("orderno", params.get("ordno"));// 주문번호
		params.put("rapprtm", params.get("orderdt"));// 승인시각

		log.info("무통장 계좌이체 승인시각 : " + params.get("orderdt"));
		/* 무통장주문 문자 */

		cartService.virAcctResult(params);
		cartService.erp_order(params);

		/***********************************
		 * 올더게이트로 부터 넘겨 받는 값들 끝
		 *************************************/

		/***************************************************************************************************************
		 * 상점에서 해당 거래에 대한 처리 db 처리 등....
		 *
		 * trcode = "1" ☞ 일반가상계좌 입금통보전문 (이지스효성 new 에스크로 포함) trcode = "2" ☞ 일반가상계좌 취소통보전문
		 * (이지스효성 new 에스크로 포함)
		 *
		 * ※ 에스크로가상계좌의 경우 입금자명 값은 통보전문에 들어가지 않습니다.
		 * 
		 * 
		 * 
		 ***************************************************************************************************************/

		/******************************************
		 * 처리 결과 리턴
		 ******************************************************/
		String rResMsg = "";
		String rSuccYn = "y";// 정상 : y 실패 : n
		// 정상처리 경우 거래코드|상점아이디|주문일시|가상계좌번호|처리결과|
		rResMsg = trcode + "|" + service_id + "|" + orderdt + "|" + virno + "|" + rSuccYn + "|";
		return rResMsg;
	}

	@ResponseBody
	@RequestMapping(params = "mode=qtyChange")
	@Transactional(rollbackFor = { Exception.class })
	public Map qtyChange(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map params)
			throws Exception {
		MCMap member = (MCMap) session.getAttribute("member");
		if (member != null) {
			params.put("session_member_id", (String) member.get("member_id"));
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			params.put("ip", request.getRemoteHost());
		}
		return cartService.qtyChange(params);
	}

	@ResponseBody
	@RequestMapping(params = "mode=changeCod")
	@Transactional(rollbackFor = { Exception.class })
	public Map changeCod(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map params)
			throws Exception {
		MCMap member = (MCMap) session.getAttribute("member");
		if (member != null) {
			params.put("session_member_id", (String) member.get("member_id"));
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			params.put("ip", request.getRemoteHost());
		}
		return cartService.changeCod(params);
	}

	@ResponseBody
	@RequestMapping(params = "mode=erp_order")
	@Transactional(rollbackFor = { Exception.class })
	public String erp_order(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestParam Map params) throws Exception {
		cartService.erp_order(params);
		return "OK";
	}

	@ResponseBody
	@RequestMapping(params = "mode=userAcctChk")
	@Transactional(rollbackFor = { Exception.class })
	public Map userAcctChk(ModelMap model, HttpServletRequest request, HttpSession session, @RequestParam Map params)
			throws Exception {
		MCMap member = (MCMap) session.getAttribute("member");
		Map rstMap = new HashMap();
		String res = "";
		String memberNm = (String)params.get("memberNm");
		if (member != null) {
			params.put("session_member_id", (String) member.get("member_id"));
			params.put("session_member_seq", (String) member.get("member_seq"));
			params.put("session_member_nm", (String) member.get("member_nm"));
			params.put("ip", request.getRemoteHost());
		}
		try {
			JSONObject jsonData = new JSONObject();
			jsonData.put("bankCd", params.get("bankCd"));
			jsonData.put("account", params.get("account"));			
			JSONObject jsonData2 = new JSONObject();
			jsonData2.put("accnt", jsonData);				
			
			res = paymentUtil.settleAccntReq(jsonData2); //예금주 조회
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse( res );
			JSONObject jsonObj = (JSONObject) obj;
			JSONObject jsonObj1 = (JSONObject)jsonObj.get("result");
			log.debug("처리결과==="+jsonObj1.get("resultCd"));
			
			rstMap.put("rst", false);
			if("0000".equals(jsonObj1.get("resultCd"))) {
				JSONObject jsonObj2 = (JSONObject)jsonObj.get("accnt");
				log.debug("처리결과 예금주명 ==" + jsonObj2.get("holder"));
				//계좌 검증!!!
				if(StringUtil.nvl(memberNm,"").equals(StringUtil.nvl((String)jsonObj2.get("holder"),""))) {
					rstMap.put("rst", "1");
				}
				// TODO: 일단 관리자만 통과 추후 이소스 제거 
				if("admin".equals(member.get("member_id"))) {
					rstMap.put("rst", true);
				}else {				
					rstMap.put("rst", false);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}		
		return rstMap;
	}
	
}
