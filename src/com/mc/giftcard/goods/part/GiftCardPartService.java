package com.mc.giftcard.goods.part;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.When;
import com.mc.common.util.StringUtil;
import com.mc.web.MCMap;
import com.mc.web.common.FileDAO;
import com.mc.web.common.FileUtil;

@Service
public class GiftCardPartService {
	
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
	private GiftCardPartDAO partDAO;
	
	@Autowired
	private GiftCardPartDAO codeDAO;
	
	public Map adminlist(Map params) throws Exception {
		Map rstMap = new HashMap();
		rstMap.put("list", partDAO.list(params));
		rstMap.put("pagination", partDAO.pagination(params));
		return rstMap;
	}
	
	public Map view(Map params) throws Exception {
		Map rstMap = new HashMap();
		rstMap.put("view", partDAO.view(params));
		params.put("table_nm", "GC_GOODS");
		params.put("table_seq", params.get("seq"));
		rstMap.put("files", fileDAO.list(params));
		return rstMap;
	}

	@TriggersRemove(cacheName="mainCache", removeAll=true, when=When.AFTER_METHOD_INVOCATION)
	public Map write(HttpServletRequest request, Map params) throws Exception {
		Map rstMap = new HashMap();
		
		partDAO.write(params);
		
		List list = (List) params.get("files");
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				Map m = (Map) list.get(i);
				m.put("order_seq", i+1);
				m.put("table_nm", "GC_GOODS");
				m.put("table_seq", params.get("parent_seq"));
				m.put("session_member_id", params.get("session_member_id"));
				m.put("session_member_seq", params.get("session_member_seq"));
				m.put("session_member_nm", params.get("session_member_nm"));
				m.put("ip", params.get("ip"));
				String path = request.getSession().getServletContext().getRealPath(UPLOAD_PATH + File.separator + m.get("yyyy") + File.separator + m.get("mm"));
				fileDAO.insert(m);
				fileUtil.copy(request.getSession().getServletContext().getRealPath(TEMP_PATH) + File.separator + m.get("uuid"), path + File.separator + m.get("uuid"),path);
				fileUtil.thumb(request.getSession().getServletContext().getRealPath(TEMP_PATH) + File.separator + (String) m.get("uuid"), path + File.separator + m.get("uuid")+"_thumb" , 172, 118, false);
			}
		}
		
		rstMap.put("rst", "1");
		return rstMap;
	}

	@TriggersRemove(cacheName="mainCache", removeAll=true, when=When.AFTER_METHOD_INVOCATION)
	public Map modify(HttpServletRequest request, Map params) throws Exception {
		Map rstMap = new HashMap();
		
		partDAO.modify(params);

		params.put("table_seq", params.get("item_seq"));
		params.put("table_nm", "GC_GOODS");
		fileDAO.delete_all(params);
		List list = (List) params.get("files");
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				Map m = (Map) list.get(i);
				m.put("order_seq", i+1);
				m.put("table_nm", "GC_GOODS");
				m.put("table_seq", params.get("item_seq"));
				m.put("session_member_id", params.get("session_member_id"));
				m.put("session_member_seq", params.get("session_member_seq"));
				m.put("session_member_nm", params.get("session_member_nm"));
				m.put("ip", params.get("ip"));
				String path = request.getSession().getServletContext().getRealPath(UPLOAD_PATH + File.separator + m.get("yyyy") + File.separator + m.get("mm"));
				fileDAO.insert(m);
				fileUtil.copy(request.getSession().getServletContext().getRealPath(TEMP_PATH) + File.separator + m.get("uuid"), path + File.separator + m.get("uuid"),path);
				fileUtil.thumb(request.getSession().getServletContext().getRealPath(TEMP_PATH) + File.separator + (String) m.get("uuid"), path + File.separator + m.get("uuid")+"_thumb" , 172, 118, false);
			}
		}
		
		list = (List) params.get("removeFiles");
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				Map m = (Map) list.get(i);
				fileUtil.delete(request.getSession().getServletContext().getRealPath(UPLOAD_PATH) + File.separator + m.get("yyyy") + File.separator + m.get("mm") + File.separator + m.get("uuid"));
				fileUtil.delete(request.getSession().getServletContext().getRealPath(UPLOAD_PATH) + File.separator + m.get("yyyy") + File.separator + m.get("mm") + File.separator + m.get("uuid")+"_thumb");
			}
		}
		
		rstMap.put("rst", "1");
		return rstMap;
	}
	
	@TriggersRemove(cacheName="mainCache", removeAll=true, when=When.AFTER_METHOD_INVOCATION)
	public Map del(Map params) throws Exception {
		Map rstMap = new HashMap();
		
		List list = (List) params.get("del");
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				Map m = (Map) list.get(i);
				m.put("session_member_id", params.get("session_member_id"));
				m.put("session_member_seq", params.get("session_member_seq"));
				m.put("session_member_nm", params.get("session_member_nm"));
				m.put("ip", params.get("ip"));
				partDAO.del(m);
			}
		}
		
		rstMap.put("rst", "1");
		return rstMap;
	}

	@TriggersRemove(cacheName="mainCache", removeAll=true, when=When.AFTER_METHOD_INVOCATION)
	public Map updateCommonRate(Map params) throws Exception {
		Map rstMap = new HashMap();
		
		partDAO.updateCommonRate(params);
		
		rstMap.put("rst", "1");
		return rstMap;
	}
	
	public Map list(Map params) throws Exception {
		params.put("client_yn", "Y");//사용자 페이지
		Map rstMap = new HashMap();
		if(!StringUtil.isEmptyByParam((String)params.get("menu"))){
			if("menu1".equals(params.get("menu"))){
				params.put("carmakerseq", "1");
			}else if("menu2".equals(params.get("menu"))){
				params.put("carmakerseq", "2");
			}else if("menu3".equals(params.get("menu"))){
				params.put("carmakerseq", "3");
			}
		}
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

		//검색시 condition이 없어서 오류로 인한 추가 2015.03.31 
		if(!keyword.equals("")){
			params.put("keyword", keyword);
			if(!condition.equals("")){
				params.put("condition", condition);
			}else{
				params.put("condition", "a.SEARCH_TAG");
			}				
		}
		Map rst2 = new HashMap();
		rstMap.put("category", partDAO.carmaker(rst2));		
		if(params.get("search_all_text") != null){
			String search_all_text = (String)params.get("search_all_text");
			String[] search_all_textList = null;
			search_all_textList = search_all_text.split(" ");
			params.put("search_all_text_arr",search_all_textList);
		}		
		rstMap.put("list", partDAO.list(params));
		rstMap.put("pagination", partDAO.pagination(params));
		return rstMap;
	}
	
	public Map other_list(Map params) throws Exception {
		Map rstMap = new HashMap();
		rstMap.put("list", partDAO.other_list(params));
		//rstMap.put("pagination", partDAO.other_pagination(params));
		return rstMap;
	}
	
	public Map photo(Map params) throws Exception {
		Map rstMap = new HashMap();
		rstMap.put("list", partDAO.photo(params));
		return rstMap;
	}

	public Map excelUpload(HttpServletRequest request, Map params, MultipartFile excelfile) throws IOException {
		Map rstMap = new HashMap();
		
		POIFSFileSystem fs = new POIFSFileSystem(excelfile.getInputStream());
		HSSFWorkbook workbook = new HSSFWorkbook(fs);
		
		HSSFSheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		
		MCMap data = new MCMap();
		for (int i = 1; i < rows; i++) {
			// 시트에 대한 행을 하나씩 추출
            HSSFRow row   = sheet.getRow(i);
			if (row != null) {
				//carmakerseq	carmodelseq	cargradeseq	productnm	caryyyy	color	grade	part1	part2	part3	user_pricing_yn	user_price	supplier_pricing_yn	supplier_price	sale_price	sale_sdate	sale_edate	discount_rate	fee_yn	fee_amount	stock_yn	stock_num	erp_code	search_tag	bestyn	eventyn	newyn	publicyn	recommyn	saleyn	planyn	approval	conts	service	carpartcode	insun_itemseq	inquiry_yn
				data.put("com_seq", 		params.get("session_com_seq")); 
				data.put("gubun", 		"1"); 
				data.put("carmakerseq", 		getCellValue(row.getCell(0))); 
				data.put("carmodelseq", 		getCellValue(row.getCell(1))); 
				data.put("cargradeseq", 		getCellValue(row.getCell(2))); 
				data.put("productnm", 			getCellValue(row.getCell(3))); 
				data.put("caryyyy", 			getCellValue(row.getCell(4))); 
				data.put("color", 				getCellValue(row.getCell(5))); 
				data.put("grade", 				getCellValue(row.getCell(6))); 
				data.put("part1", 				getCellValue(row.getCell(7))); 
				data.put("part2", 				getCellValue(row.getCell(8))); 
				data.put("part3", 				getCellValue(row.getCell(9))); 
				data.put("user_pricing_yn", 	getCellValue(row.getCell(10))); 
				data.put("user_price", 		getCellValue(row.getCell(11))); 
				data.put("supplier_pricing_yn",getCellValue(row.getCell(12))); 
				data.put("supplier_price", 	getCellValue(row.getCell(13))); 
				data.put("sale_price", 		getCellValue(row.getCell(14))); 
				data.put("sale_sdate", 		getCellValue(row.getCell(15))); 
				data.put("sale_edate", 		getCellValue(row.getCell(16))); 
				data.put("discount_rate", 		getCellValue(row.getCell(17))); 
				data.put("fee_yn", 			getCellValue(row.getCell(18))); 
				data.put("fee_amount", 		getCellValue(row.getCell(19))); 
				data.put("stock_yn", 			getCellValue(row.getCell(20))); 
				data.put("stock_num", 			getCellValue(row.getCell(21))); 
				data.put("erp_code", 			getCellValue(row.getCell(22))); 
				data.put("search_tag", 		getCellValue(row.getCell(23))); 
				data.put("bestyn", 			getCellValue(row.getCell(24))); 
				data.put("eventyn", 			getCellValue(row.getCell(25))); 
				data.put("newyn", 				getCellValue(row.getCell(26))); 
				data.put("publicyn", 			getCellValue(row.getCell(27))); 
				data.put("recommyn", 			getCellValue(row.getCell(28))); 
				data.put("saleyn", 			getCellValue(row.getCell(29))); 
				data.put("planyn", 			getCellValue(row.getCell(30))); 
				data.put("approval", 			getCellValue(row.getCell(31))); 
				data.put("conts", 				getCellValue(row.getCell(32))); 
				data.put("service", 			getCellValue(row.getCell(33))); 
				data.put("carpartcode", 		getCellValue(row.getCell(34))); 
				data.put("insun_itemseq", 		getCellValue(row.getCell(35))); 
				data.put("inquiry_yn", 		getCellValue(row.getCell(36)));
				data.put("car_yn", 			getCellValue(row.getCell(37)));
				data.put("files", 			getCellValue(row.getCell(38)));
				data.put("session_member_id", 			params.get("session_member_id"));
				
				/*partDAO.write(data); 
				
				String[] files = StringUtil.split(data.getStrNull("files").replaceAll("[\n \t]", ""), ",");
				
				if(files != null){
					for (int j = 0; j < files.length; j++) {
						String filename = files[j];
						Map m = new MCMap();
						m.put("order_seq", j+1);
						m.put("table_nm", "GC_GOODS");
						m.put("table_seq", data.get("parent_seq"));
						m.put("uuid", UUID.randomUUID().toString());
						m.put("attach_nm", filename);
						m.put("yyyy", DateUtil.getTime("yyyy"));
						m.put("mm", DateUtil.getTime("MM"));
						m.put("session_member_id", params.get("session_member_id"));
						m.put("session_member_seq", params.get("session_member_seq"));
						m.put("session_member_nm", params.get("session_member_nm"));
						m.put("ip", params.get("ip"));
						String path = request.getSession().getServletContext().getRealPath(UPLOAD_PATH + File.separator + m.get("yyyy") + File.separator + m.get("mm"));
						fileDAO.insert(m);
						fileUtil.copy(request.getSession().getServletContext().getRealPath("/upload/temp")+ File.separator + filename, path + File.separator + m.get("uuid"), path);
					}
				}*/
			}
		}
		
		/*int sheetNum = workbook.getNumberOfSheets();
		for (int k = 0; k < sheetNum; k++) {
			System.out.println("Sheet Number : "+k);
			System.out.println("Sheet Name : " + workbook.getSheetName(k));
			HSSFSheet sheet = workbook.getSheetAt(k);
			
			int rows = sheet.getPhysicalNumberOfRows();
			for (int r = 0; r < rows; r++) {
			   HSSFRow row   = sheet.getRow(r);
			   System.out.println("Row : "+row.getRowNum());
			   
			   int cells = row.getPhysicalNumberOfCells();
			   for (short c = 0; c < cells; c++) {
			       HSSFCell cell  = row.getCell(c);
			       int celltype = cell.getCellType();
			   }
			   
			}
		}*/
		
		return rstMap;
	}
	
	public String getCellValue(HSSFCell cell){
		String value = null;
		if (cell != null) {
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_FORMULA:
				value = "" + cell.getCellFormula();
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				value = Long.toString((long)cell.getNumericCellValue()) + ""; // double
				break;
			case HSSFCell.CELL_TYPE_STRING:
				value = "" + cell.getStringCellValue(); // String
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				value = "";
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				value = "" + cell.getBooleanCellValue(); // boolean
				break;
			case HSSFCell.CELL_TYPE_ERROR:
				value = "" + cell.getErrorCellValue(); // byte
				break;
			default:
			}
		}
		return value;
	}
	
}