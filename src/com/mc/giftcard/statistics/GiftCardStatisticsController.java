package com.mc.giftcard.statistics;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mc.common.util.StringUtil;
import com.mc.giftcard.calculate.GiftCardCalculateDAO;
import com.mc.web.MCMap;

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

@Controller
public class GiftCardStatisticsController {
Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private GiftCardCalculateDAO calculateDAO;
	
	@RequestMapping("/giftcard/admin/statistics/statistics.do")
	public String statistics(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session) throws Exception{
		
		request.setAttribute("list", calculateDAO.statistics(params));
		request.setAttribute("params", params);

		return "/giftcard/admin/statistics/statistics";
	}
	
	@RequestMapping("/giftcard/admin/statistics/statistics_excel.do")
	public void statistics_excel(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException, RowsExceededException, WriteException{
		
		List<MCMap> list = calculateDAO.statistics(params);
		
		OutputStream output = response.getOutputStream();
		response.setContentType("application/xls");
		response.setHeader("Content-Disposition","attachment; filename=statistics.xls");
		
		
		WritableWorkbook writebook = Workbook.createWorkbook(output);
		WritableSheet writeSheet = writebook.createSheet("??????", 0);

		WritableCellFormat headerFormat = new WritableCellFormat(); // ?????? ??? ?????? ??????
		WritableCellFormat dataFormat = new WritableCellFormat(); // ????????? ??? ?????? ??????

		headerFormat.setAlignment(Alignment.CENTRE); // ??? ????????? ??????
		headerFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // ??? ?????? ????????? ??????
		headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // ????????? ????????????????????? ??????
		headerFormat.setBackground(Colour.GRAY_25);
		
		dataFormat.setAlignment(Alignment.CENTRE); // ??? ????????? ??????
		dataFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // ??? ?????? ????????? ??????
		dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // ????????? ????????????????????? ??????
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
		
		String[] header = {"?????????", "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "??????"};
		if (header != null && header.length > 0)
		{
			for(int x=0;x<header.length;x++){
				Label headerLabels =new Label(x, i, header[x], headerFormat); // ????????? ????????? ?????? ????????? A?????? E?????? ??????
				writeSheet.addCell(headerLabels); // ?????? ??????
			}
			i++;
		}
		try {
			for(MCMap map : list){
				int cell = 0;
				int u_cnt = map.getIntNullVal("u_cnt", 0);
				int c_cnt = map.getIntNullVal("c_cnt", 0);
				int u_qty = map.getIntNullVal("u_qty", 0);
				int c_qty = map.getIntNullVal("c_qty", 0);
				int u_sum = map.getIntNullVal("u_sum", 0);
				int c_sum = map.getIntNullVal("c_sum", 0);
				int refund_cash_cnt = map.getIntNullVal("refund_cash_cnt", 0);
				int refund_card_cnt = map.getIntNullVal("refund_card_cnt", 0);
				int refund_cash_qty = map.getIntNullVal("refund_cash_qty", 0);
				int refund_card_qty = map.getIntNullVal("refund_card_qty", 0);
				int refund_cash_sum = map.getIntNullVal("refund_cash_sum", 0);
				int refund_card_sum = map.getIntNullVal("refund_card_sum", 0);
				
				writeSheet.addCell(new Label(cell++, i, StringUtil.isNullDef((String)map.get("com_nm"), "??????"), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(u_cnt + c_cnt), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(u_qty + c_qty), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(u_sum + c_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(refund_cash_cnt + refund_card_cnt), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(refund_cash_qty + refund_card_qty), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand(refund_cash_sum + refund_card_sum), dataFormat));
				writeSheet.addCell(new Label(cell++, i, StringUtil.getThousand((u_sum + c_sum) - (refund_cash_sum + refund_card_sum)), dataFormat));
				
				i++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		writebook.write(); // ????????? ????????? ?????? ????????? ?????? ??????
		writebook.close(); // ?????? ??? ??????????????? ?????? ??????
	}
	
	
}
