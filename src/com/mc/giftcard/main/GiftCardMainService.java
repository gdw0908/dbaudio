package com.mc.giftcard.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.Cacheable;
import com.mc.common.util.StringUtil;
import com.mc.giftcard.code.GiftCardCodeDAO;
import com.mc.giftcard.goods.part.GiftCardPartDAO;

@Service
public class GiftCardMainService {
	Logger logger = Logger.getLogger(this.getClass());

	private static List<String> names;
	
	@Autowired
	private GiftCardMainDAO mainDAO;
	
	@Autowired
	private GiftCardPartDAO partDAO;
	
	@Autowired
	private GiftCardCodeDAO codeDAO;

	@Cacheable(cacheName="mainCache")
	public Map menu_category() throws Exception {
		Map rst = new HashMap();
		rst.put("menu1", mainDAO.menu_category("050901001"));
		rst.put("menu_top1", mainDAO.menu_top("050901001"));
		rst.put("menu2", mainDAO.menu_category("050901002"));
		rst.put("menu_top2", mainDAO.menu_top("050901002"));
		rst.put("menu3", mainDAO.menu_category("050901003"));
		rst.put("menu_top3", mainDAO.menu_top("050901003"));
		rst.put("menu4", mainDAO.menu_category("050901004"));
		rst.put("menu_top4", mainDAO.menu_top("050901004"));
		rst.put("menu5", mainDAO.menu_category_nation("Y"));
		rst.put("menu_top5", mainDAO.menu_top_nation("Y"));
		rst.put("menu6", mainDAO.menu_category_nation("N"));
		rst.put("menu_top6", mainDAO.menu_top_nation("N"));
		rst.put("menu7", mainDAO.menu_category_sido());
		rst.put("main_popup", mainDAO.main_popup());
		return rst;
	}
	
	@Cacheable(cacheName="mainCache")
	public Map latest_part(Map params) throws Exception {
		params.put("client_yn", "Y");//사용자 페이지
		Map rstMap = new HashMap();
		
		if("1".equals(params.get("tab"))){//롯데
			params.put("carmakerseq", "1");
		}else if("2".equals(params.get("tab"))){//신세계
			params.put("carmakerseq", "2");
		}else if("3".equals(params.get("tab"))){//갤러리아
			params.put("carmakerseq", "3");
		}
		
		rstMap.put("list", partDAO.list(params));
		rstMap.put("pagination", partDAO.pagination(params));
		return rstMap;
	}
	
	public Map mainList(Map params, HttpServletRequest request) throws Exception {
		Map rst = new HashMap();
		List list = null;
		list = (List)mainDAO.notice();
		List boardList = new ArrayList();
		for(int x=0; x < list.size(); x++){
			Map map = new HashMap();
			map = (Map)list.get(x);
			String title = (String)map.get("title");
			map.put("title", StringUtil.clearXSS(title,""));
			boardList.add(map);
		}
		rst.put("notice", boardList);
		rst.put("banner_popup", mainDAO.banner_popup());
		rst.put("quick_popup", mainDAO.quick_popup());
		rst.put("service", mainDAO.service());
		rst.put("mdpart", mainDAO.mdpart());
		rst.put("cooperation", mainDAO.cooperation());
		rst.put("newPartsList", mainDAO.newPartsList());
		rst.put("mobile_popup", mainDAO.mobile_popup());
		rst.put("cart_total", mainDAO.getCartListCount((String)params.get("member_id")));
		rst.put("category", partDAO.carmaker(params));
		
		String part_view_seq = "";
		String part_view_seq_orderby = "";
		Cookie[] cook = request.getCookies();
		if(cook!=null){
			for(int i=0;i<cook.length;i++){//전송된 쿠키이름 얻어오기
				String name=cook[i].getName();//쿠키이름에 item이 포함되어 있다면
				if(name.indexOf("part_view")!=-1){//해당 value얻어오기
					String part_view = cook[i].getValue();
					if(part_view != null && part_view != ""){
						part_view_seq += part_view+",";
						part_view_seq_orderby += part_view+","+i+",";
					}
				}
			}
		}
		if(part_view_seq.length() > 0){
			part_view_seq = part_view_seq.substring(0,part_view_seq.length()-1);
			part_view_seq_orderby = part_view_seq_orderby.substring(0,part_view_seq_orderby.length()-1);
			params.put("part_view_seq", part_view_seq);
			params.put("part_view_seq_orderby", part_view_seq_orderby);
			rst.put("part_view_list", mainDAO.part_view_list(params));
		}		
		return rst;
	}
	
	public Map headerList() throws Exception {
		Map rst = new HashMap();
		rst.put("top_popup", mainDAO.top_popup());
		rst.put("layer_popup", mainDAO.layer_popup());
		rst.put("total_goods_cnt", mainDAO.total_goods_cnt().getStrNullVal("totalcount", "0"));
		
		Map rst2 = new HashMap();
		rst.put("category", partDAO.carmaker(rst2));		
		return rst;
	}
	
	public Map mobile_header() throws Exception {
		Map rst = new HashMap();
		rst.put("total_goods_cnt", mainDAO.total_goods_cnt().getStrNullVal("totalcount", "0"));
		return rst;
	}

	public Map mymenu(Map params) {
		Map rstMap = new HashMap();
		//rstMap.put("notice", mainDAO.mymenu_count("4"));
		rstMap.put("carallbaro", mainDAO.mymenu_carallbaro_count());//중고부품문의로 변경
		rstMap.put("application", mainDAO.mymenu_count("6"));
		params.put("client_yn", "Y");//사용자 페이지
		
		//codeDAO 리스트 받아올때 keyword로 인한 오류로 인한 추가 2015.03.31
		String keyword = "";
		String condition = "";
		if(params.get("keyword") != null){
			keyword = (String)params.get("keyword"); 
		}
		if(params.get("condition") != null){
			condition = (String)params.get("condition");
		}
		params.remove("keyword");
		params.remove("condition");
				
		if(!StringUtil.isEmptyByParam((String)params.get("menu"))){
			if("menu1".equals(params.get("menu"))){
				params.put("part1", "050901001");
				params.put("upcodeno", "050901001");
				rstMap.put("part2", codeDAO.oldCodeList(params));
			}else if("menu2".equals(params.get("menu"))){
				params.put("part1", "050901002");
				params.put("upcodeno", "050901002");
				rstMap.put("part2", codeDAO.oldCodeList(params));
			}else if("menu3".equals(params.get("menu"))){
				params.put("part1", "050901003");
				params.put("upcodeno", "050901003");
				rstMap.put("part1", codeDAO.oldCodeList(params));
			}else if("menu4".equals(params.get("menu"))){
				params.put("part1", "050901004");
				params.put("upcodeno", "050901004");
				rstMap.put("part2", codeDAO.oldCodeList(params));
			}else if("menu5".equals(params.get("menu"))){
				params.put("nation", "Y");
			}else if("menu6".equals(params.get("menu"))){
				params.put("nation", "N");
			}
		}
		rstMap.put("carmaker", partDAO.carmaker(params));
		if(!StringUtil.isEmptyByParam((String)params.get("carmakerseq"))){
			rstMap.put("carmodel", partDAO.carmodel(params));
		}
		if(!StringUtil.isEmptyByParam((String)params.get("cargradeseq"))){
			rstMap.put("cargrade", partDAO.cargrade(params));
		}
		
		params.put("code_group_seq", "38");
		rstMap.put("grade", codeDAO.codeList(params));
		rstMap.put("sido", codeDAO.sido(params));
		if(!StringUtil.isEmptyByParam((String)params.get("sigungu"))){
			rstMap.put("sigungu", codeDAO.sigungu(params));
		}
		if(!StringUtil.isEmptyByParam((String)params.get("dong"))){
			rstMap.put("dong", codeDAO.dong(params));
		}
		
		//검색시 condition이 없어서 오류로 인한 추가 2015.03.31 
		if(!keyword.equals("")){
			params.put("keyword", keyword);
			if(!condition.equals("")){
				params.put("condition", condition);
			}else{
				params.put("condition", "a.SEARCH_TAG");
			}				
		}
		return rstMap;
	}

	@Cacheable(cacheName="mainCache")
	public Map all_menu() {
		Map rst = new HashMap();
		rst.put("menu1", mainDAO.all_menu("050901001"));
		rst.put("menu2", mainDAO.all_menu("050901002"));
		rst.put("menu3", mainDAO.all_menu("050901003"));
		rst.put("menu4", mainDAO.all_menu("050901004"));
		rst.put("menu5", mainDAO.menu_category_nation("Y"));
		rst.put("menu6", mainDAO.menu_category_nation("N"));
		rst.put("menu7", mainDAO.menu_category_sido());
		Map rst2 = new HashMap();
		rst.put("category", partDAO.carmaker(rst2));		
		return rst;
	}


	public Map mobile_latest_part(Map params) throws Exception {
		Map rstMap = new HashMap();
		if(params.get("tab").equals("1")){
			params.put("select_type", "item");
			rstMap.put("list", mainDAO.mobile_mdpart(params));
			params.put("select_type", "pageinfo");
			rstMap.put("pagination", mainDAO.mobile_mdpart(params));
		}else{
			params.put("select_type", "item");
			rstMap.put("list", mainDAO.mobile_newpart(params));
			params.put("select_type", "pageinfo");
			rstMap.put("pagination", mainDAO.mobile_newpart(params));
			params.put("select_type", "total");
			rstMap.put("total", mainDAO.mobile_newpart(params));
		}
		return rstMap;
	}

	public Map autocomplete(String prefix) {
		Map rst = new HashMap();
		List matches = new ArrayList();
		if("".equals(prefix)){
			rst.put("data", matches);
			return rst;
		}
		names = mainDAO.autocomplete();
		String prefix_upper = prefix.toUpperCase();
		for (int i = 0; i < names.size(); i++) {
			String name = (String) names.get(i);
			if(name == null) continue;
			String name_upper_case = name.toUpperCase();
			if (name_upper_case.startsWith(prefix_upper)) {
				matches.add(name);
			}
			if(matches.size()>=10){
				break;
			}
		}
		rst.put("data", matches);
		return rst;
	}
}