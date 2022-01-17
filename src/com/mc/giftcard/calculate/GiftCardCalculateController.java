package com.mc.giftcard.calculate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mc.common.util.DateUtil;
import com.mc.common.util.StringUtil;
import com.mc.common.util.Util;
import com.mc.giftcard.schedule.GiftCardScheduleDAO;
import com.mc.web.MCMap;
import com.mc.web.common.POIExcelDownloadService;
import com.mc.web.schedule.ScheduleDAO;

@Controller
public class GiftCardCalculateController {
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private GiftCardCalculateDAO calculateDAO;
	
	@Autowired
	private GiftCardScheduleDAO scheduleDAO;
	
	@Autowired
	private POIExcelDownloadService excelDownloadService;
	
	@RequestMapping("/giftcard/admin/calculate/deadline.do")
	public String deadline(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		request.setAttribute("deadline_list", calculateDAO.get_deadline_list());
		
		if(!StringUtil.isEmptyByParam(params, "c_date")){
			params.put("type", "check");
			request.setAttribute("list", calculateDAO.select_nsh_calculate_group(params));
		}
		
		request.setAttribute("params", params);

		return "/giftcard/admin/calculate/deadline";
	}
	
	@RequestMapping("/giftcard/admin/calculate/deadline_proc.do")
	public String deadline_proc(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		MCMap member = (MCMap) session.getAttribute("member");
		params.put("reg_seq", (String) member.get("member_seq"));
		params.put("reg_nm", (String) member.get("member_nm"));
		params.put("reg_id", (String) member.get("member_id"));
		
		params.put("type", "check");
		List<MCMap> list = calculateDAO.select_com_list_group(params);
		String cooperations = "";
		int i = 0;
		for(MCMap map : list){
			i++;
			cooperations += map.getStr("com_nm");
			if(i < list.size())
				cooperations += ", ";
		}
		
		String c_date = params.get("c_date").replaceAll("'", "");
		params.put("period", c_date);
		params.put("days", Integer.toString(c_date.split(",").length));
		params.put("cooperations", cooperations);
		
		calculateDAO.insert_nsh_clulate_confirm(params);
		
		request.setAttribute("message", "정상적으로 마감처리되었습니다.");
		request.setAttribute("redirect", "/giftcard/admin/calculate/deadline.do");
		
		return "message";
	}
	
	@RequestMapping("/giftcard/admin/calculate/deadline_excel.do")
	public void deadline_excel(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException, RowsExceededException, WriteException{
		
		params.put("type", "check");
		List<MCMap> list = calculateDAO.select_nsh_calculate_group(params);
		
		OutputStream output = response.getOutputStream();
		response.setContentType("application/xls");
		response.setHeader("Content-Disposition","attachment; filename=calculate_deadline.xls");
		
		
		WritableWorkbook writebook = Workbook.createWorkbook(output);
		WritableSheet writeSheet = writebook.createSheet("마감정산", 0);

		WritableCellFormat headerFormat = new WritableCellFormat(); // 번호 셀 포멧 생성
		WritableCellFormat dataFormat = new WritableCellFormat(); // 데이터 셀 포멧 생성

		headerFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
		headerFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
		headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정
		headerFormat.setBackground(Colour.GRAY_25);
		
		dataFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
		dataFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
		dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정
		dataFormat.setWrap(true);
		
		
		writeSheet.setColumnView(0, 20); 
		writeSheet.setColumnView(1, 18); 
		writeSheet.setColumnView(2, 18); 
		writeSheet.setColumnView(3, 18); 
		writeSheet.setColumnView(4, 18); 
		writeSheet.setColumnView(5, 18); 
		writeSheet.setColumnView(6, 18); 
		writeSheet.setColumnView(7, 18); 
		writeSheet.setColumnView(8, 18); 
		writeSheet.setColumnView(9, 18); 
		writeSheet.setColumnView(10, 30); 
		writeSheet.setColumnView(11, 18); 
		
		CellView cv = new CellView();
		cv.setAutosize(true);
		

		writeSheet.addCell(new Label(0, 0, "조회된 일일정산데이터", headerFormat));
		writeSheet.addCell(new Label(1, 0, params.get("c_date").replaceAll("'", ""), dataFormat));
		
		int i = 1;
		
		String[] header = {"업체명", "판매금액(카드)", "판매금액(현금)", "반품/취소/환불(카드)", "반품/취소/환불(현금)", "판매총액", "주문총액", "공제내역(카드)", "공제내역(현금)", "공제내역(본사수수료)", "총정산액", "송금액"};
		if (header != null && header.length > 0)
		{
			for(int x=0;x<header.length;x++){
				Label headerLabels =new Label(x, i, header[x], headerFormat); // 데이터 포멧에 맞게 대문자 A에서 E까지 생성
				writeSheet.addCell(headerLabels); // 셀에 삽입
			}
			i++;
		}
		try {
			for(MCMap map : list){
				int cell = 0;
				int card = map.getIntNullVal("card", 0);
				int iche = map.getIntNullVal("iche", 0);
				int virtual = map.getIntNullVal("virtual", 0);
				int card_pg_commission = map.getIntNullVal("card_pg_commission", 0);
				int iche_pg_commission = map.getIntNullVal("iche_pg_commission", 0);
				int virtual_pg_commission = map.getIntNullVal("virtual_pg_commission", 0);
				int u_m_commission = map.getIntNullVal("u_m_commission", 0);
				int c_m_commission = map.getIntNullVal("c_m_commission", 0);
				int cancel_cash_sum = map.getIntNullVal("cancel_cash_sum", 0);
				int cancel_card_sum = map.getIntNullVal("cancel_card_sum", 0);
				int refund_cash_sum = map.getIntNullVal("refund_cash_sum", 0);
				int refund_card_sum = map.getIntNullVal("refund_card_sum", 0);
				int order_sum = map.getIntNullVal("order_sum", 0);
				int total = map.getIntNullVal("total", 0);
				
				writeSheet.addCell(new Label(cell++, i, StringUtil.isNullDef((String)map.get("com_nm"), "합계"), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(card), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(iche + virtual), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(cancel_card_sum + refund_card_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(cancel_cash_sum + refund_cash_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(order_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(card_pg_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(iche_pg_commission + virtual_pg_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(u_m_commission + c_m_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total) + "-" + StringUtil.getThousand(card_pg_commission + iche_pg_commission + virtual_pg_commission + u_m_commission + c_m_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total - (card_pg_commission + iche_pg_commission + virtual_pg_commission + u_m_commission + c_m_commission)), dataFormat));
				
				i++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		writebook.write(); // 준비된 정보를 엑셀 포멧에 맞게 작성
		writebook.close(); // 처리 후 메모리에서 해제 처리
	}
	
	@RequestMapping("/giftcard/admin/calculate/deadline_cancel.do")
	public String deadline_cancel(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		request.setAttribute("deadline_list", calculateDAO.get_deadline_list());
		
		return "/giftcard/admin/calculate/deadline_cancel";
	}
	
	@RequestMapping("/giftcard/admin/calculate/deadline_re_proc.do")
	public String deadline_re_proc(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		String date = Util.isNull(params.get("date"));
		
		if("".equals(date)){
			request.setAttribute("message", "정산일자를 선택해주세요.");
			request.setAttribute("redirect", "/giftcard/admin/calculate/deadline_cancel.do?date="+date);
			
			return "message";
		}
		
		if(scheduleDAO.getYesterdayCalcCnt(date) > 0){
			request.setAttribute("message", "이미 정산내역이 존재합니다.");
			request.setAttribute("redirect", "/giftcard/admin/calculate/deadline_cancel.do?date="+date);
			
			return "message";
		}
		
		List<MCMap> yesterdayCalclist = scheduleDAO.yesterdayCalcList("", date);	//결제
		//List<MCMap> yesterdayCancellist = scheduleDAO.yesterdayCalcList("C", date);	//취소
		//List<MCMap> yesterdayRefundclist = scheduleDAO.yesterdayCalcList("R", date);	//환불
		List<MCMap> yesterdayOrderlist = scheduleDAO.yesterdayCalcList("O", date);	//주문
		for(MCMap map : scheduleDAO.selectCooperationList(date)){
			String com_seq = map.getStr("com_seq");
			map.put("card", "0");
			map.put("card_cnt", "0");
			map.put("card_qty", "0");
			map.put("card_pg_commission", "0");
			map.put("iche", "0");
			map.put("iche_cnt", "0");
			map.put("iche_qty", "0");
			map.put("iche_pg_commission", "0");
			map.put("virtual", "0");
			map.put("virtual_cnt", "0");
			map.put("virtual_qty", "0");
			map.put("virtual_pg_commission", "0");
			map.put("hp", "0");
			map.put("hp_cnt", "0");
			map.put("hp_qty", "0");
			map.put("hp_pg_commission", "0");
			map.put("u_sum", "0");
			map.put("u_cnt", "0");
			map.put("u_qty", "0");
			map.put("u_m_commission", "0");
			map.put("c_sum", "0");
			map.put("c_cnt", "0");
			map.put("c_qty", "0");
			map.put("c_m_commission", "0");
			map.put("cancel_cash_sum", "0");
			map.put("cancel_cash_cnt", "0");
			map.put("cancel_cash_qty", "0");
			map.put("cancel_card_sum", "0");
			map.put("cancel_card_cnt", "0");
			map.put("cancel_card_qty", "0");
			map.put("refund_cash_sum", "0");
			map.put("refund_cash_cnt", "0");
			map.put("refund_cash_qty", "0");
			map.put("refund_card_sum", "0");
			map.put("refund_card_cnt", "0");
			map.put("refund_card_qty", "0");
			map.put("order_sum", "0");
			map.put("order_cnt", "0");
			map.put("order_qty", "0");
			map.put("total", "0");

			for(MCMap calc_map : yesterdayCalclist){
				if(com_seq.equals(calc_map.get("com_seq"))){
					map.put("card", calc_map.getStr("card"));
					map.put("card_cnt", calc_map.getStr("card_cnt"));
					map.put("card_qty", calc_map.getStr("card_qty"));
					map.put("card_pg_commission", calc_map.getStr("card_pg_commission"));
					map.put("iche", calc_map.getStr("iche"));
					map.put("iche_cnt", calc_map.getStr("iche_cnt"));
					map.put("iche_qty", calc_map.getStr("iche_qty"));
					map.put("iche_pg_commission", calc_map.getStr("iche_pg_commission"));
					map.put("virtual", calc_map.getStr("virtual"));
					map.put("virtual_cnt", calc_map.getStr("virtual_cnt"));
					map.put("virtual_qty", calc_map.getStr("virtual_qty"));
					map.put("virtual_pg_commission", calc_map.getStr("virtual_pg_commission"));
					map.put("hp", calc_map.getStr("hp"));
					map.put("hp_cnt", calc_map.getStr("hp_cnt"));
					map.put("hp_qty", calc_map.getStr("hp_qty"));
					map.put("hp_pg_commission", calc_map.getStr("hp_pg_commission"));
					map.put("u_sum", calc_map.getStr("u_sum"));
					map.put("u_cnt", calc_map.getStr("u_cnt"));
					map.put("u_qty", calc_map.getStr("u_qty"));
					map.put("u_m_commission", calc_map.getStr("u_m_commission"));
					map.put("c_sum", calc_map.getStr("c_sum"));
					map.put("c_cnt", calc_map.getStr("c_cnt"));
					map.put("c_qty", calc_map.getStr("c_qty"));
					map.put("c_m_commission", calc_map.getStr("c_m_commission"));
					map.put("total", calc_map.getStr("total"));
					break;
				}
			}
			
			/*for(MCMap cancel_map : yesterdayCancellist){
				if(com_seq.equals(cancel_map.get("com_seq"))){
					int cash_sum = cancel_map.getIntNumber("iche") + cancel_map.getIntNumber("virtual");
					int cash_cnt = cancel_map.getIntNumber("iche_cnt") + cancel_map.getIntNumber("virtual_cnt");
					int cash_qty = cancel_map.getIntNumber("iche_qty") + cancel_map.getIntNumber("virtual_qty");
					
					int card_sum = cancel_map.getIntNumber("card");
					int card_cnt = cancel_map.getIntNumber("card_cnt");
					int card_qty = cancel_map.getIntNumber("card_qty");
					
					map.put("cancel_cash_sum", cash_sum);
					map.put("cancel_cash_cnt", cash_cnt);
					map.put("cancel_cash_qty", cash_qty);
					map.put("cancel_card_sum", card_sum);
					map.put("cancel_card_cnt", card_cnt);
					map.put("cancel_card_qty", card_qty);
					break;
				}
			}
			
			for(MCMap refund_map : yesterdayRefundclist){
				if(com_seq.equals(refund_map.get("com_seq"))){
					int cash_sum = refund_map.getIntNumber("iche") + refund_map.getIntNumber("virtual");
					int cash_cnt = refund_map.getIntNumber("iche_cnt") + refund_map.getIntNumber("virtual_cnt");
					int cash_qty = refund_map.getIntNumber("iche_qty") + refund_map.getIntNumber("virtual_qty");
					
					int card_sum = refund_map.getIntNumber("card");
					int card_cnt = refund_map.getIntNumber("card_cnt");
					int card_qty = refund_map.getIntNumber("card_qty");
					
					map.put("refund_cash_sum", cash_sum);
					map.put("refund_cash_cnt", cash_cnt);
					map.put("refund_cash_qty", cash_qty);
					map.put("refund_card_sum", card_sum);
					map.put("refund_card_cnt", card_cnt);
					map.put("refund_card_qty", card_qty);
					break;
				}
			}*/
			
			for(MCMap order_map : yesterdayOrderlist){
				if(com_seq.equals(order_map.get("com_seq"))){
					int sum = order_map.getIntNumber("iche") + order_map.getIntNumber("virtual") + order_map.getIntNumber("card");
					int cnt = order_map.getIntNumber("iche_cnt") + order_map.getIntNumber("virtual_cnt") + order_map.getIntNumber("card_cnt");
					int qty = order_map.getIntNumber("iche_qty") + order_map.getIntNumber("virtual_qty") + order_map.getIntNumber("card_qty");
					
					map.put("order_sum", sum);
					map.put("order_cnt", cnt);
					map.put("order_qty", qty);
					break;
				}
			}
			
			scheduleDAO.insertNshCalculate(map);
		}
		request.setAttribute("message", "정상적으로 재정산처리되었습니다.");
		request.setAttribute("redirect", "/giftcard/admin/calculate/deadline_cancel.do");
		
		return "message";
	}
	
	@RequestMapping("/giftcard/admin/calculate/deadline_cancel_proc.do")
	public String deadline_cancel_proc(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		calculateDAO.delete_nsh_clulate(params);
		
		request.setAttribute("message", "정상적으로 취소처리되었습니다.");
		request.setAttribute("redirect", "/giftcard/admin/calculate/deadline_cancel.do");
		
		return "message";
	}
	
	@RequestMapping("/giftcard/admin/calculate/confirm_list.do")
	public String confirm_list(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		params.put("type", "confirm");
		request.setAttribute("com_list", calculateDAO.select_com_list_group(params));
		
		if(StringUtil.isEmptyByParam(params, "rows")) params.put("rows", "10");
		if(StringUtil.isEmptyByParam(params, "cpage")) params.put("cpage", "1");
		request.setAttribute("list", calculateDAO.confirm_list(params));
		request.setAttribute("page_info", calculateDAO.confirm_page_info(params));
		request.setAttribute("params", params);
		return "/giftcard/admin/calculate/confirm_list";
	}
	
	@RequestMapping("/giftcard/admin/calculate/confirm_view_detail.do")
	public String confirm_view_detail(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		String odate = (String)params.get("odate");
		String[] order_date = odate.split(",");
		odate = "";
		for(int x=0;x < order_date.length;x++){
			odate+= "'"+order_date[x]+"',";
		}
		if(odate.length() > 0){
			odate = odate.substring(0, odate.length()-1);
		}
		params.put("order_date", odate);
		if(params.get("type") == null){
			params.put("type","N");
		}
		request.setAttribute("list", calculateDAO.select_confirm_detail(params));
		request.setAttribute("params", params);
		return "/giftcard/admin/calculate/confirm_view_detail";
	}
	
	@RequestMapping("/giftcard/admin/calculate/confirm_view_detail_excel.do")
	public void confirm_view_detail_excel(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{
		String odate = (String)params.get("odate");
		String[] order_date = odate.split(",");
		odate = "";
		for(int x=0;x < order_date.length;x++){
			odate+= "'"+order_date[x]+"',";
		}
		if(odate.length() > 0){
			odate = odate.substring(0, odate.length()-1);
		}
		params.put("order_date", odate);
		List list = calculateDAO.select_confirm_detail_excel(params);
		String filename = "";
		if(params.get("type").equals("N")){
			filename = java.net.URLEncoder.encode("마감정산조회_판매","UTF-8");
		}else{
			filename = java.net.URLEncoder.encode("마감정산조회_취소환불","UTF-8");
		}
		OutputStream output = response.getOutputStream();
		response.setContentType("application/xls");
		response.setHeader("Content-Disposition","attachment; filename="+filename+".xls");
		if(params.get("type").equals("N")){
		String[][] header = {
				{"주문자", "상품명", "주문일(결제완료일)", "결제금액", "주문번호", "상품ERP코드", "결제유형", "PG수수료", "수수료율", "본사 수수료", "판매여부"}, 
				{"receiver","productnm", "pay_dt", "money", "orderno", "erp_code", "pay_type", "pg_commission", "commission_protage", "m_commission", "pay_yn"},
				{"", "", "", "bill", "", "", "", "bill", "", "bill", ""},
				{"3000", "9000", "3500", "3000", "5000", "6000", "3000", "5000", "5000", "5000", "5000"}
			};
		excelDownloadService.excelDownload(output, list, "주문내역", header);
		}else{
			String[][] header = {
					{"주문자", "상품명", "취소일(환불완료일)", "결제금액", "주문번호", "상품ERP코드", "결제유형", "PG수수료", "수수료율", "본사 수수료", "판매여부"}, 
					{"receiver","productnm", "pay_e_dt", "money", "orderno", "erp_code", "pay_type", "pg_commission", "commission_protage", "m_commission", "pay_yn"},
					{"", "", "", "bill", "", "", "", "bill", "", "bill", ""},
					{"3000", "9000", "3500", "3000", "5000", "6000", "3000", "5000", "5000", "5000", "5000"}
				};
			excelDownloadService.excelDownload(output, list, "취소내역", header);
		}
	}
	
	
	
	@RequestMapping("/giftcard/admin/calculate/confirm_view.do")
	public String confirm_view(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		request.setAttribute("vo", calculateDAO.select_confirm(params));
		
		params.put("type", "confirm");
		request.setAttribute("list", calculateDAO.select_nsh_calculate_group(params));
		
		request.setAttribute("params", params);
		
		return "/giftcard/admin/calculate/confirm_view";
	}
	
	@RequestMapping("/giftcard/admin/calculate/confirm_proc.do")
	public String confirm_proc(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		calculateDAO.rollback_nsh_clulate_confirm(params);
		
		request.setAttribute("message", "정상적으로 마감취소 처리되었습니다.");
		request.setAttribute("redirect", "/giftcard/admin/calculate/deadline.do");
		
		return "message";
	}
	
	@RequestMapping("/giftcard/admin/calculate/confirm_excel.do")
	public void confirm_excel(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException, RowsExceededException, WriteException{
		
		MCMap confirm = calculateDAO.select_confirm(params);
		
		params.put("type", "confirm");
		List<MCMap> list = calculateDAO.select_nsh_calculate_group(params);
		
		OutputStream output = response.getOutputStream();
		response.setContentType("application/xls");
		response.setHeader("Content-Disposition","attachment; filename=calculate_confirm.xls");
		
		
		WritableWorkbook writebook = Workbook.createWorkbook(output);
		WritableSheet writeSheet = writebook.createSheet("마감된정산", 0);

		WritableCellFormat headerFormat = new WritableCellFormat(); // 번호 셀 포멧 생성
		WritableCellFormat dataFormat = new WritableCellFormat(); // 데이터 셀 포멧 생성

		headerFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
		headerFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
		headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정
		headerFormat.setBackground(Colour.GRAY_25);
		
		dataFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
		dataFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
		dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정
		dataFormat.setWrap(true);
		
		
		writeSheet.setColumnView(0, 20); 
		writeSheet.setColumnView(1, 18); 
		writeSheet.setColumnView(2, 18); 
		writeSheet.setColumnView(3, 18); 
		writeSheet.setColumnView(4, 18); 
		writeSheet.setColumnView(5, 18); 
		writeSheet.setColumnView(6, 18); 
		writeSheet.setColumnView(7, 18); 
		writeSheet.setColumnView(8, 18); 
		writeSheet.setColumnView(9, 18); 
		writeSheet.setColumnView(10, 30); 
		writeSheet.setColumnView(11, 18); 
		
		CellView cv = new CellView();
		cv.setAutosize(true);
		
		String period = (String)confirm.get("period");

		writeSheet.addCell(new Label(0, 0, "정산일자", headerFormat));
		writeSheet.addCell(new Label(1, 0, period.replaceAll("'", ""), dataFormat));
		
		int i = 1;
		
		String[] header = {"업체명", "판매금액(카드)", "판매금액(현금)", "반품/취소/환불(카드)", "반품/취소/환불(현금)", "판매총액", "주문총액", "공제내역(카드)", "공제내역(현금)", "공제내역(본사수수료)", "총정산액", "송금액"};
		if (header != null && header.length > 0)
		{
			for(int x=0;x<header.length;x++){
				Label headerLabels =new Label(x, i, header[x], headerFormat); // 데이터 포멧에 맞게 대문자 A에서 E까지 생성
				writeSheet.addCell(headerLabels); // 셀에 삽입
			}
			i++;
		}
		try {
			for(MCMap map : list){
				int cell = 0;
				int card = map.getIntNullVal("card", 0);
				int iche = map.getIntNullVal("iche", 0);
				int virtual = map.getIntNullVal("virtual", 0);
				int card_pg_commission = map.getIntNullVal("card_pg_commission", 0);
				int iche_pg_commission = map.getIntNullVal("iche_pg_commission", 0);
				int virtual_pg_commission = map.getIntNullVal("virtual_pg_commission", 0);
				int u_m_commission = map.getIntNullVal("u_m_commission", 0);
				int c_m_commission = map.getIntNullVal("c_m_commission", 0);
				int cancel_cash_sum = map.getIntNullVal("cancel_cash_sum", 0);
				int cancel_card_sum = map.getIntNullVal("cancel_card_sum", 0);
				int refund_cash_sum = map.getIntNullVal("refund_cash_sum", 0);
				int refund_card_sum = map.getIntNullVal("refund_card_sum", 0);
				int order_sum = map.getIntNullVal("order_sum", 0);
				int total = map.getIntNullVal("total", 0);
				
				writeSheet.addCell(new Label(cell++, i, StringUtil.isNullDef((String)map.get("com_nm"), "합계"), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(card), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(iche + virtual), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(cancel_card_sum + refund_card_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(cancel_cash_sum + refund_cash_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(order_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(card_pg_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(iche_pg_commission + virtual_pg_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(u_m_commission + c_m_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total) + "-" + StringUtil.getThousand(card_pg_commission + iche_pg_commission + virtual_pg_commission + u_m_commission + c_m_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total - (card_pg_commission + iche_pg_commission + virtual_pg_commission + u_m_commission + c_m_commission)), dataFormat));
				
				i++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		writebook.write(); // 준비된 정보를 엑셀 포멧에 맞게 작성
		writebook.close(); // 처리 후 메모리에서 해제 처리
	}

	@RequestMapping("/giftcard/admin/calculate/day_search.do")
	public String day_search(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		String date = DateUtil.simpleDateFormat(DateUtil.getCurrentDateBDay(1), "yyyyMMdd", "yyyy-MM-dd");
		if(StringUtil.isEmptyByParam(params, "date")) params.put("date", date);
		
		request.setAttribute("list", calculateDAO.select_nsh_calculate_list(params));
		request.setAttribute("com_list", calculateDAO.select_com_list(params));
		
		request.setAttribute("sum", calculateDAO.select_nsh_calculate_sum(params));
		request.setAttribute("params", params);

		return "/giftcard/admin/calculate/day_search";
	}
	
	@RequestMapping("/giftcard/admin/calculate/day_excel.do")
	public void day_excel(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException, RowsExceededException, WriteException{
		
		String date = DateUtil.simpleDateFormat(DateUtil.getCurrentDateBDay(1), "yyyyMMdd", "yyyy-MM-dd");
		if(StringUtil.isEmptyByParam(params, "date")) params.put("date", date);
		
		
		List<MCMap> list = calculateDAO.select_nsh_calculate_list(params);
		
		OutputStream output = response.getOutputStream();
		response.setContentType("application/xls");
		response.setHeader("Content-Disposition","attachment; filename=calculate_day.xls");
		
		
		WritableWorkbook writebook = Workbook.createWorkbook(output);
		WritableSheet writeSheet = writebook.createSheet(params.get("date") + "일일정산", 0);

		WritableCellFormat headerFormat = new WritableCellFormat(); // 번호 셀 포멧 생성
		WritableCellFormat dataFormat = new WritableCellFormat(); // 데이터 셀 포멧 생성

		headerFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
		headerFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
		headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정
		headerFormat.setBackground(Colour.GRAY_25);
		
		dataFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
		dataFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
		dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정
		dataFormat.setWrap(true);
		
		writeSheet.setColumnView(0, 20); 
		writeSheet.setColumnView(1, 18); 
		writeSheet.setColumnView(2, 18); 
		writeSheet.setColumnView(3, 18); 
		writeSheet.setColumnView(4, 18); 
		writeSheet.setColumnView(5, 18); 
		writeSheet.setColumnView(6, 18); 
		writeSheet.setColumnView(7, 18); 
		writeSheet.setColumnView(8, 18); 
		writeSheet.setColumnView(9, 18); 
		writeSheet.setColumnView(10, 30); 
		writeSheet.setColumnView(11, 18); 
		
		CellView cv = new CellView();
		cv.setAutosize(true);	
		
		int i = 0;
		
		String[] header = {"업체명", "판매금액(카드)", "판매금액(현금)", "반품/취소/환불(카드)", "반품/취소/환불(현금)", "판매총액", "주문총액", "공제내역(카드)", "공제내역(현금)", "공제내역(본사수수료)", "총정산액", "송금액"};
		if (header != null && header.length > 0)
		{
			for(int x=0;x<header.length;x++){
				Label headerLabels =new Label(x, i, header[x], headerFormat); // 데이터 포멧에 맞게 대문자 A에서 E까지 생성
				writeSheet.addCell(headerLabels); // 셀에 삽입
			}
			i++;
		}
		try {
			for(MCMap map : list){
				int cell = 0;
				int card = map.getIntNullVal("card", 0);
				int iche = map.getIntNullVal("iche", 0);
				int virtual = map.getIntNullVal("virtual", 0);
				int card_pg_commission = map.getIntNullVal("card_pg_commission", 0);
				int iche_pg_commission = map.getIntNullVal("iche_pg_commission", 0);
				int virtual_pg_commission = map.getIntNullVal("virtual_pg_commission", 0);
				int u_m_commission = map.getIntNullVal("u_m_commission", 0);
				int c_m_commission = map.getIntNullVal("c_m_commission", 0);
				int cancel_cash_sum = map.getIntNullVal("cancel_cash_sum", 0);
				int cancel_card_sum = map.getIntNullVal("cancel_card_sum", 0);
				int refund_cash_sum = map.getIntNullVal("refund_cash_sum", 0);
				int refund_card_sum = map.getIntNullVal("refund_card_sum", 0);
				int order_sum = map.getIntNullVal("order_sum", 0);
				int total = map.getIntNullVal("total", 0);
				
				writeSheet.addCell(new Label(cell++, i, String.valueOf(map.get("com_nm")), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(card), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(iche + virtual), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(cancel_card_sum + refund_card_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(cancel_cash_sum + refund_cash_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(order_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(card_pg_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(iche_pg_commission + virtual_pg_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(u_m_commission + c_m_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total) + "-" + StringUtil.getThousand(card_pg_commission + iche_pg_commission + virtual_pg_commission + u_m_commission + c_m_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total - (card_pg_commission + iche_pg_commission + virtual_pg_commission + u_m_commission + c_m_commission)), dataFormat));
				
				i++;
			}
			
			if(StringUtil.isEmptyByParam(params, "com_seq")){
				MCMap map = calculateDAO.select_nsh_calculate_sum(params);
				int cell = 0;
				int card = map.getIntNullVal("card", 0);
				int iche = map.getIntNullVal("iche", 0);
				int virtual = map.getIntNullVal("virtual", 0);
				int card_pg_commission = map.getIntNullVal("card_pg_commission", 0);
				int iche_pg_commission = map.getIntNullVal("iche_pg_commission", 0);
				int virtual_pg_commission = map.getIntNullVal("virtual_pg_commission", 0);
				int u_m_commission = map.getIntNullVal("u_m_commission", 0);
				int c_m_commission = map.getIntNullVal("c_m_commission", 0);
				int cancel_cash_sum = map.getIntNullVal("cancel_cash_sum", 0);
				int cancel_card_sum = map.getIntNullVal("cancel_card_sum", 0);
				int refund_cash_sum = map.getIntNullVal("refund_cash_sum", 0);
				int refund_card_sum = map.getIntNullVal("refund_card_sum", 0);
				int order_sum = map.getIntNullVal("order_sum", 0);
				int total = map.getIntNullVal("total", 0);
				
				writeSheet.addCell(new Label(cell++, i, String.valueOf(map.get("com_nm")), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(card), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(iche + virtual), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(cancel_card_sum + refund_card_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(cancel_cash_sum + refund_cash_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(order_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(card_pg_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(iche_pg_commission + virtual_pg_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(u_m_commission + c_m_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total) + "-" + StringUtil.getThousand(card_pg_commission + iche_pg_commission + virtual_pg_commission + u_m_commission + c_m_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total - (card_pg_commission + iche_pg_commission + virtual_pg_commission + u_m_commission + c_m_commission)), dataFormat));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		writebook.write(); // 준비된 정보를 엑셀 포멧에 맞게 작성
		writebook.close(); // 처리 후 메모리에서 해제 처리
	}
	
	@RequestMapping("/giftcard/admin/calculate/week_search.do")
	public String week_search(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		params.put("type", "week");
		
		String year = DateUtil.getCurrentYearStr();
		String month = DateUtil.getCurrentMonthStr();
		int week = DateUtil.getCurrentWeek();
		
		if(StringUtil.isEmptyByParam(params, "year")) params.put("year", year);
		if(StringUtil.isEmptyByParam(params, "month")) params.put("month", month);
		if(StringUtil.isEmptyByParam(params, "week")) params.put("week", Integer.toString(week));
		
		List<MCMap> week_data = calculateDAO.get_week_data(params);
		for(MCMap map : week_data){
			int rownum = map.getIntNumber("rownum");
			int param_week = Integer.parseInt(params.get("week"));
			if(param_week == rownum){
				params.put("start_date", map.getStrNullVal("start_date", ""));
				params.put("end_date", map.getStrNullVal("end_date", ""));
				break;
			}
		}
		
		request.setAttribute("list", calculateDAO.select_nsh_calculate_group(params));
		request.setAttribute("com_list", calculateDAO.select_com_list_group(params));
		request.setAttribute("params", params);

		return "/giftcard/admin/calculate/week_search";
	}
	
	@RequestMapping("/giftcard/admin/calculate/week_excel.do")
	public void week_excel(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException, RowsExceededException, WriteException{
		
		params.put("type", "week");
		
		String year = DateUtil.getCurrentYearStr();
		String month = DateUtil.getCurrentMonthStr();
		int week = DateUtil.getCurrentWeek();
		
		if(StringUtil.isEmptyByParam(params, "year")) params.put("year", year);
		if(StringUtil.isEmptyByParam(params, "month")) params.put("month", month);
		if(StringUtil.isEmptyByParam(params, "week")) params.put("week", Integer.toString(week));
		
		List<MCMap> week_data = calculateDAO.get_week_data(params);
		for(MCMap map : week_data){
			int rownum = map.getIntNumber("rownum");
			int param_week = Integer.parseInt(params.get("week"));
			if(param_week == rownum){
				params.put("start_date", map.getStrNullVal("start_date", ""));
				params.put("end_date", map.getStrNullVal("end_date", ""));
				break;
			}
		}
		
		List<MCMap> list = calculateDAO.select_nsh_calculate_group(params);
		
		OutputStream output = response.getOutputStream();
		response.setContentType("application/xls");
		response.setHeader("Content-Disposition","attachment; filename=calculate_week.xls");
		
		
		WritableWorkbook writebook = Workbook.createWorkbook(output);
		WritableSheet writeSheet = writebook.createSheet(params.get("year") + "년" + params.get("month") + "월" + params.get("week") + "주정산", 0);

		WritableCellFormat headerFormat = new WritableCellFormat(); // 번호 셀 포멧 생성
		WritableCellFormat dataFormat = new WritableCellFormat(); // 데이터 셀 포멧 생성

		headerFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
		headerFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
		headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정
		headerFormat.setBackground(Colour.GRAY_25);
		
		dataFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
		dataFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
		dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정
		dataFormat.setWrap(true);
		
		writeSheet.setColumnView(0, 20); 
		writeSheet.setColumnView(1, 18); 
		writeSheet.setColumnView(2, 18); 
		writeSheet.setColumnView(3, 18); 
		writeSheet.setColumnView(4, 18); 
		writeSheet.setColumnView(5, 18); 
		writeSheet.setColumnView(6, 18); 
		writeSheet.setColumnView(7, 18); 
		writeSheet.setColumnView(8, 18); 
		writeSheet.setColumnView(9, 18); 
		writeSheet.setColumnView(10, 30); 
		writeSheet.setColumnView(11, 18); 
		
		CellView cv = new CellView();
		cv.setAutosize(true);	
		
		int i = 0;
		
		String[] header = {"업체명", "판매금액(카드)", "판매금액(현금)", "반품/취소/환불(카드)", "반품/취소/환불(현금)", "판매총액", "주문총액", "공제내역(카드)", "공제내역(현금)", "공제내역(본사수수료)", "총정산액", "송금액"};
		if (header != null && header.length > 0)
		{
			for(int x=0;x<header.length;x++){
				Label headerLabels =new Label(x, i, header[x], headerFormat); // 데이터 포멧에 맞게 대문자 A에서 E까지 생성
				writeSheet.addCell(headerLabels); // 셀에 삽입
			}
			i++;
		}
		try {
			for(MCMap map : list){
				int cell = 0;
				int card = map.getIntNullVal("card", 0);
				int iche = map.getIntNullVal("iche", 0);
				int virtual = map.getIntNullVal("virtual", 0);
				int card_pg_commission = map.getIntNullVal("card_pg_commission", 0);
				int iche_pg_commission = map.getIntNullVal("iche_pg_commission", 0);
				int virtual_pg_commission = map.getIntNullVal("virtual_pg_commission", 0);
				int u_m_commission = map.getIntNullVal("u_m_commission", 0);
				int c_m_commission = map.getIntNullVal("c_m_commission", 0);
				int cancel_cash_sum = map.getIntNullVal("cancel_cash_sum", 0);
				int cancel_card_sum = map.getIntNullVal("cancel_card_sum", 0);
				int refund_cash_sum = map.getIntNullVal("refund_cash_sum", 0);
				int refund_card_sum = map.getIntNullVal("refund_card_sum", 0);
				int order_sum = map.getIntNullVal("order_sum", 0);
				int total = map.getIntNullVal("total", 0);
				
				writeSheet.addCell(new Label(cell++, i, StringUtil.isNullDef((String)map.get("com_nm"), "합계"), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(card), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(iche + virtual), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(cancel_card_sum + refund_card_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(cancel_cash_sum + refund_cash_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(order_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(card_pg_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(iche_pg_commission + virtual_pg_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(u_m_commission + c_m_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total) + "-" + StringUtil.getThousand(card_pg_commission + iche_pg_commission + virtual_pg_commission + u_m_commission + c_m_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total - (card_pg_commission + iche_pg_commission + virtual_pg_commission + u_m_commission + c_m_commission)), dataFormat));
				
				i++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		writebook.write(); // 준비된 정보를 엑셀 포멧에 맞게 작성
		writebook.close(); // 처리 후 메모리에서 해제 처리
	}
	
	@RequestMapping("/giftcard/admin/calculate/month_search.do")
	public String month_search(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		params.put("type", "month");
		
		String year = DateUtil.getCurrentYearStr();
		String month = DateUtil.getCurrentMonthStr();
		
		if(StringUtil.isEmptyByParam(params, "year")) params.put("year", year);
		if(StringUtil.isEmptyByParam(params, "month")) params.put("month", month);
		
		request.setAttribute("list", calculateDAO.select_nsh_calculate_group(params));
		request.setAttribute("com_list", calculateDAO.select_com_list_group(params));
		request.setAttribute("params", params);

		return "/giftcard/admin/calculate/month_search";
	}
	
	@RequestMapping("/giftcard/admin/calculate/month_excel.do")
	public void month_excel(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException, RowsExceededException, WriteException{
		
		params.put("type", "month");
		
		String year = DateUtil.getCurrentYearStr();
		String month = DateUtil.getCurrentMonthStr();
		
		if(StringUtil.isEmptyByParam(params, "year")) params.put("year", year);
		if(StringUtil.isEmptyByParam(params, "month")) params.put("month", month);
		
		List<MCMap> list = calculateDAO.select_nsh_calculate_group(params);
		
		OutputStream output = response.getOutputStream();
		response.setContentType("application/xls");
		response.setHeader("Content-Disposition","attachment; filename=calculate_month.xls");
		
		
		WritableWorkbook writebook = Workbook.createWorkbook(output);
		WritableSheet writeSheet = writebook.createSheet(params.get("year") + "년" + params.get("month") + "월정산", 0);

		WritableCellFormat headerFormat = new WritableCellFormat(); // 번호 셀 포멧 생성
		WritableCellFormat dataFormat = new WritableCellFormat(); // 데이터 셀 포멧 생성

		headerFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
		headerFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
		headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정
		headerFormat.setBackground(Colour.GRAY_25);
		
		dataFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
		dataFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
		dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정
		dataFormat.setWrap(true);
		
		writeSheet.setColumnView(0, 20); 
		writeSheet.setColumnView(1, 18); 
		writeSheet.setColumnView(2, 18); 
		writeSheet.setColumnView(3, 18); 
		writeSheet.setColumnView(4, 18); 
		writeSheet.setColumnView(5, 18); 
		writeSheet.setColumnView(6, 18); 
		writeSheet.setColumnView(7, 18); 
		writeSheet.setColumnView(8, 18); 
		writeSheet.setColumnView(9, 18); 
		writeSheet.setColumnView(10, 30); 
		writeSheet.setColumnView(11, 18); 
		
		CellView cv = new CellView();
		cv.setAutosize(true);	
		
		int i = 0;
		
		String[] header = {"업체명", "판매금액(카드)", "판매금액(현금)", "반품/취소/환불(카드)", "반품/취소/환불(현금)", "판매총액", "주문총액", "공제내역(카드)", "공제내역(현금)", "공제내역(본사수수료)", "총정산액", "송금액"};
		if (header != null && header.length > 0)
		{
			for(int x=0;x<header.length;x++){
				Label headerLabels =new Label(x, i, header[x], headerFormat); // 데이터 포멧에 맞게 대문자 A에서 E까지 생성
				writeSheet.addCell(headerLabels); // 셀에 삽입
			}
			i++;
		}
		try {
			for(MCMap map : list){
				int cell = 0;
				int card = map.getIntNullVal("card", 0);
				int iche = map.getIntNullVal("iche", 0);
				int virtual = map.getIntNullVal("virtual", 0);
				int card_pg_commission = map.getIntNullVal("card_pg_commission", 0);
				int iche_pg_commission = map.getIntNullVal("iche_pg_commission", 0);
				int virtual_pg_commission = map.getIntNullVal("virtual_pg_commission", 0);
				int u_m_commission = map.getIntNullVal("u_m_commission", 0);
				int c_m_commission = map.getIntNullVal("c_m_commission", 0);
				int cancel_cash_sum = map.getIntNullVal("cancel_cash_sum", 0);
				int cancel_card_sum = map.getIntNullVal("cancel_card_sum", 0);
				int refund_cash_sum = map.getIntNullVal("refund_cash_sum", 0);
				int refund_card_sum = map.getIntNullVal("refund_card_sum", 0);
				int order_sum = map.getIntNullVal("order_sum", 0);
				int total = map.getIntNullVal("total", 0);
				
				writeSheet.addCell(new Label(cell++, i, StringUtil.isNullDef((String)map.get("com_nm"), "합계"), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(card), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(iche + virtual), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(cancel_card_sum + refund_card_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(cancel_cash_sum + refund_cash_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(order_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(card_pg_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(iche_pg_commission + virtual_pg_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(u_m_commission + c_m_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total) + "-" + StringUtil.getThousand(card_pg_commission + iche_pg_commission + virtual_pg_commission + u_m_commission + c_m_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total - (card_pg_commission + iche_pg_commission + virtual_pg_commission + u_m_commission + c_m_commission)), dataFormat));
				
				i++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		writebook.write(); // 준비된 정보를 엑셀 포멧에 맞게 작성
		writebook.close(); // 처리 후 메모리에서 해제 처리
	}
	
	@RequestMapping("/giftcard/admin/calculate/branch_search.do")
	public String branch_search(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		params.put("type", "branch");
		
		String year = DateUtil.getCurrentYearStr();
		if(StringUtil.isEmptyByParam(params, "branch")){
			int month_int = DateUtil.getCurrentMonth();
			
			if(month_int >= 1 && month_int <= 3)
				params.put("branch", "1");
			else if(month_int >= 4 && month_int <= 6)
				params.put("branch", "2");
			else if(month_int >= 7 && month_int <= 9)
				params.put("branch", "3");
			else
				params.put("branch", "4");
		}
		
		String branch = params.get("branch");
		if(branch.equals("1")){
			params.put("start_month", "01");
			params.put("end_month", "03");
		}else if(branch.equals("2")){
			params.put("start_month", "04");
			params.put("end_month", "06");
		}else if(branch.equals("3")){
			params.put("start_month", "07");
			params.put("end_month", "09");
		}else{
			params.put("start_month", "10");
			params.put("end_month", "12");
		}
		
		
		if(StringUtil.isEmptyByParam(params, "year")) params.put("year", year);
		
		request.setAttribute("list", calculateDAO.select_nsh_calculate_group(params));
		request.setAttribute("com_list", calculateDAO.select_com_list_group(params));
		request.setAttribute("params", params);

		return "/giftcard/admin/calculate/branch_search";
	}
	
	@RequestMapping("/giftcard/admin/calculate/branch_excel.do")
	public void branch_excel(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException, RowsExceededException, WriteException{
		
		params.put("type", "branch");
		
		String year = DateUtil.getCurrentYearStr();
		if(StringUtil.isEmptyByParam(params, "branch")){
			int month_int = DateUtil.getCurrentMonth();
			
			if(month_int >= 1 && month_int <= 3)
				params.put("branch", "1");
			else if(month_int >= 4 && month_int <= 6)
				params.put("branch", "2");
			else if(month_int >= 7 && month_int <= 9)
				params.put("branch", "3");
			else
				params.put("branch", "4");
		}
		
		String branch = params.get("branch");
		if(branch.equals("1")){
			params.put("start_month", "01");
			params.put("end_month", "03");
		}else if(branch.equals("2")){
			params.put("start_month", "04");
			params.put("end_month", "06");
		}else if(branch.equals("3")){
			params.put("start_month", "07");
			params.put("end_month", "09");
		}else{
			params.put("start_month", "10");
			params.put("end_month", "12");
		}
		
		
		if(StringUtil.isEmptyByParam(params, "year")) params.put("year", year);
		
		List<MCMap> list = calculateDAO.select_nsh_calculate_group(params);
		
		OutputStream output = response.getOutputStream();
		response.setContentType("application/xls");
		response.setHeader("Content-Disposition","attachment; filename=calculate_branch.xls");
		
		
		WritableWorkbook writebook = Workbook.createWorkbook(output);
		WritableSheet writeSheet = writebook.createSheet(params.get("year") + "년" + params.get("month") + "월" + params.get("branch") + "분기정산", 0);

		WritableCellFormat headerFormat = new WritableCellFormat(); // 번호 셀 포멧 생성
		WritableCellFormat dataFormat = new WritableCellFormat(); // 데이터 셀 포멧 생성

		headerFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
		headerFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
		headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정
		headerFormat.setBackground(Colour.GRAY_25);
		
		dataFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
		dataFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
		dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정
		dataFormat.setWrap(true);
		
		writeSheet.setColumnView(0, 20); 
		writeSheet.setColumnView(1, 18); 
		writeSheet.setColumnView(2, 18); 
		writeSheet.setColumnView(3, 18); 
		writeSheet.setColumnView(4, 18); 
		writeSheet.setColumnView(5, 18); 
		writeSheet.setColumnView(6, 18); 
		writeSheet.setColumnView(7, 18); 
		writeSheet.setColumnView(8, 18); 
		writeSheet.setColumnView(9, 18); 
		writeSheet.setColumnView(10, 30); 
		writeSheet.setColumnView(11, 18); 
		
		CellView cv = new CellView();
		cv.setAutosize(true);	
		
		int i = 0;
		
		String[] header = {"업체명", "판매금액(카드)", "판매금액(현금)", "반품/취소/환불(카드)", "반품/취소/환불(현금)", "판매총액", "주문총액", "공제내역(카드)", "공제내역(현금)", "공제내역(본사수수료)", "총정산액", "송금액"};
		if (header != null && header.length > 0)
		{
			for(int x=0;x<header.length;x++){
				Label headerLabels =new Label(x, i, header[x], headerFormat); // 데이터 포멧에 맞게 대문자 A에서 E까지 생성
				writeSheet.addCell(headerLabels); // 셀에 삽입
			}
			i++;
		}
		try {
			for(MCMap map : list){
				int cell = 0;
				int card = map.getIntNullVal("card", 0);
				int iche = map.getIntNullVal("iche", 0);
				int virtual = map.getIntNullVal("virtual", 0);
				int card_pg_commission = map.getIntNullVal("card_pg_commission", 0);
				int iche_pg_commission = map.getIntNullVal("iche_pg_commission", 0);
				int virtual_pg_commission = map.getIntNullVal("virtual_pg_commission", 0);
				int u_m_commission = map.getIntNullVal("u_m_commission", 0);
				int c_m_commission = map.getIntNullVal("c_m_commission", 0);
				int cancel_cash_sum = map.getIntNullVal("cancel_cash_sum", 0);
				int cancel_card_sum = map.getIntNullVal("cancel_card_sum", 0);
				int refund_cash_sum = map.getIntNullVal("refund_cash_sum", 0);
				int refund_card_sum = map.getIntNullVal("refund_card_sum", 0);
				int order_sum = map.getIntNullVal("order_sum", 0);
				int total = map.getIntNullVal("total", 0);
				
				writeSheet.addCell(new Label(cell++, i, StringUtil.isNullDef((String)map.get("com_nm"), "합계"), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(card), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(iche + virtual), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(cancel_card_sum + refund_card_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(cancel_cash_sum + refund_cash_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(order_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(card_pg_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(iche_pg_commission + virtual_pg_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(u_m_commission + c_m_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total) + "-" + StringUtil.getThousand(card_pg_commission + iche_pg_commission + virtual_pg_commission + u_m_commission + c_m_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total - (card_pg_commission + iche_pg_commission + virtual_pg_commission + u_m_commission + c_m_commission)), dataFormat));
				
				i++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		writebook.write(); // 준비된 정보를 엑셀 포멧에 맞게 작성
		writebook.close(); // 처리 후 메모리에서 해제 처리
	}
	
	@RequestMapping("/giftcard/admin/calculate/year_search.do")
	public String year_search(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		params.put("type", "year");
		
		String year = DateUtil.getCurrentYearStr();
		
		if(StringUtil.isEmptyByParam(params, "year")) params.put("year", year);
		
		request.setAttribute("list", calculateDAO.select_nsh_calculate_group(params));
		request.setAttribute("com_list", calculateDAO.select_com_list_group(params));
		request.setAttribute("params", params);

		return "/giftcard/admin/calculate/year_search";
	}
	
	@RequestMapping("/giftcard/admin/calculate/year_excel.do")
	public void year_excel(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException, RowsExceededException, WriteException{
		
		params.put("type", "year");
		
		String year = DateUtil.getCurrentYearStr();
		
		if(StringUtil.isEmptyByParam(params, "year")) params.put("year", year);
		
		List<MCMap> list = calculateDAO.select_nsh_calculate_group(params);
		
		OutputStream output = response.getOutputStream();
		response.setContentType("application/xls");
		response.setHeader("Content-Disposition","attachment; filename=calculate_year.xls");
		
		
		WritableWorkbook writebook = Workbook.createWorkbook(output);
		WritableSheet writeSheet = writebook.createSheet(params.get("year") + "년정산", 0);

		WritableCellFormat headerFormat = new WritableCellFormat(); // 번호 셀 포멧 생성
		WritableCellFormat dataFormat = new WritableCellFormat(); // 데이터 셀 포멧 생성

		headerFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
		headerFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
		headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정
		headerFormat.setBackground(Colour.GRAY_25);
		
		dataFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
		dataFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
		dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정
		dataFormat.setWrap(true);
		
		writeSheet.setColumnView(0, 20); 
		writeSheet.setColumnView(1, 18); 
		writeSheet.setColumnView(2, 18); 
		writeSheet.setColumnView(3, 18); 
		writeSheet.setColumnView(4, 18); 
		writeSheet.setColumnView(5, 18); 
		writeSheet.setColumnView(6, 18); 
		writeSheet.setColumnView(7, 18); 
		writeSheet.setColumnView(8, 18); 
		writeSheet.setColumnView(9, 18); 
		writeSheet.setColumnView(10, 30); 
		writeSheet.setColumnView(11, 18); 
		
		CellView cv = new CellView();
		cv.setAutosize(true);	
		
		int i = 0;
		
		String[] header = {"업체명", "판매금액(카드)", "판매금액(현금)", "반품/취소/환불(카드)", "반품/취소/환불(현금)", "판매총액", "주문총액", "공제내역(카드)", "공제내역(현금)", "공제내역(본사수수료)", "총정산액", "송금액"};
		if (header != null && header.length > 0)
		{
			for(int x=0;x<header.length;x++){
				Label headerLabels =new Label(x, i, header[x], headerFormat); // 데이터 포멧에 맞게 대문자 A에서 E까지 생성
				writeSheet.addCell(headerLabels); // 셀에 삽입
			}
			i++;
		}
		try {
			for(MCMap map : list){
				int cell = 0;
				int card = map.getIntNullVal("card", 0);
				int iche = map.getIntNullVal("iche", 0);
				int virtual = map.getIntNullVal("virtual", 0);
				int card_pg_commission = map.getIntNullVal("card_pg_commission", 0);
				int iche_pg_commission = map.getIntNullVal("iche_pg_commission", 0);
				int virtual_pg_commission = map.getIntNullVal("virtual_pg_commission", 0);
				int u_m_commission = map.getIntNullVal("u_m_commission", 0);
				int c_m_commission = map.getIntNullVal("c_m_commission", 0);
				int cancel_cash_sum = map.getIntNullVal("cancel_cash_sum", 0);
				int cancel_card_sum = map.getIntNullVal("cancel_card_sum", 0);
				int refund_cash_sum = map.getIntNullVal("refund_cash_sum", 0);
				int refund_card_sum = map.getIntNullVal("refund_card_sum", 0);
				int order_sum = map.getIntNullVal("order_sum", 0);
				int total = map.getIntNullVal("total", 0);
				
				writeSheet.addCell(new Label(cell++, i, StringUtil.isNullDef((String)map.get("com_nm"), "합계"), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(card), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(iche + virtual), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(cancel_card_sum + refund_card_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(cancel_cash_sum + refund_cash_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(order_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(card_pg_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(iche_pg_commission + virtual_pg_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(u_m_commission + c_m_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total) + "-" + StringUtil.getThousand(card_pg_commission + iche_pg_commission + virtual_pg_commission + u_m_commission + c_m_commission), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(total - (card_pg_commission + iche_pg_commission + virtual_pg_commission + u_m_commission + c_m_commission)), dataFormat));
				
				i++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		writebook.write(); // 준비된 정보를 엑셀 포멧에 맞게 작성
		writebook.close(); // 처리 후 메모리에서 해제 처리
	}
}
