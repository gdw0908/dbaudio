package com.mc.web.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mc.web.MCMap;

/**
 *
 * @Description : POI 엑셀 다운로드
 * @ClassName   : com.mc.common.util.POIExcelUtil.java
 * @Modification Information
 *
 * @author 이창기
 * @since 2015. 4. 2.
 * @version 1.0 *  
 * Copyright (C)  All right reserved.
 */
@Service
public class POIExcelDownloadService {
	
	@Value("#{config['home.url']}")
	private String HOME_URL;

	public void excelDownload(OutputStream output, List<MCMap> list, String title, String[][] header) throws Exception{
		HSSFWorkbook xlsxWb = new HSSFWorkbook(); // Excel 2007 이전 버전
		// Sheet 생성
		HSSFSheet sheet1 = xlsxWb.createSheet(title);
        DataFormat format = xlsxWb.createDataFormat();
        
//      Cell 스타일 생성
        HSSFFont tf = xlsxWb.createFont();
        tf.setFontHeightInPoints((short) 12);
        tf.setColor(HSSFColor.BLACK.index);
        tf.setFontName("맑은 고딕");
        tf.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //폰트굵게
        
        HSSFFont f = xlsxWb.createFont();
        f.setFontHeightInPoints((short) 10);
        f.setColor(HSSFColor.BLACK.index);
        f.setFontName("맑은 고딕");
        
        //기본 스타일
        CellStyle dcs = xlsxWb.createCellStyle();
        dcs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        dcs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        dcs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dcs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dcs.setBorderRight(HSSFCellStyle.BORDER_THIN);
        dcs.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dcs.setWrapText(true);//자동줄바꿈

        //타이틀 스타일
        CellStyle tcs = xlsxWb.createCellStyle();
        tcs.cloneStyleFrom(dcs);
        tcs.setFont(tf);
        tcs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        tcs.setFillPattern(CellStyle.SOLID_FOREGROUND);

        //기본 문자 스타일
        CellStyle cs = xlsxWb.createCellStyle();
        cs.cloneStyleFrom(dcs);
        cs.setFont(f);
        
        //날짜 스타일
        CellStyle dc = xlsxWb.createCellStyle();
        dc.cloneStyleFrom(dcs);
        dc.setDataFormat(format.getFormat("m/d/yy h:mm"));
        
        //숫자 스타일
        CellStyle nc = xlsxWb.createCellStyle();
        nc.cloneStyleFrom(dcs);
        nc.setDataFormat(format.getFormat("#,##0"));
        
        //화폐 스타일
        CellStyle bc = xlsxWb.createCellStyle();
        bc.cloneStyleFrom(dcs);
        bc.setDataFormat(format.getFormat("₩#,##0"));
         
        Row row = null;
        Cell cell = null;
        //----------------------------------------------------------
         
        // 첫 번째 줄
        row = sheet1.createRow(0);
        
        if (header[0] != null && header[0].length > 0){
			for(int i=0;i<header[0].length;i++){
				cell = row.createCell(i);
				cell.setCellValue(header[0][i]);
		        cell.setCellStyle(tcs);
			}
		}
        
        //두번째 줄부터 데이터 입력
        row = sheet1.createRow(1);
        for (MCMap mp : list) {
			for(int i=0;i<header[1].length;i++){
				cell = row.createCell(i);
				if("img".equals(header[2][i])){//이미지
					drawImage(sheet1, xlsxWb, sheet1.getLastRowNum(), "http://www.partsmoa.co.kr" + mp.getStrNull(header[1][i]));
//					drawImage(sheet1, xlsxWb, sheet1.getLastRowNum(), HOME_URL + mp.getStrNull(header[1][i]));
					row.setHeight((short)1500);
				}else if("date".equals(header[2][i])){//날짜
					cell.setCellValue(mp.getDate(header[1][i], "yyyy-MM-dd HH:mm:ss"));
					cell.setCellStyle(dc);
				}else if("bill".equals(header[2][i])){//화폐
					cell.setCellValue(mp.getIntNumber(header[1][i]));
					cell.setCellStyle(bc);
				}else if("int".equals(header[2][i])){//숫자
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(mp.getIntNumber(header[1][i]));
					cell.setCellStyle(nc);
				}else{//기본
					cell.setCellValue(mp.getStrNull(header[1][i]));
					cell.setCellStyle(cs);
				}
			}
			row = sheet1.createRow(sheet1.getLastRowNum()+1);
		}
        
        //컬럼 사이즈 조정
        for(int i=0;i<header[3].length;i++){
        	if(header[3][i].equals("")){
        		sheet1.autoSizeColumn(i);
        	}else{
        		sheet1.setColumnWidth(i, Integer.parseInt(header[3][i]));
        	}
        }
        xlsxWb.write(output);
	}
	
	private void drawImage(HSSFSheet sheet, HSSFWorkbook wb, int row, String thumb) throws IOException {
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

		HSSFClientAnchor anchor;
		anchor = new HSSFClientAnchor(0, 0, 0, 255, (short) 3, row, (short) 4, row);
		anchor.setAnchorType(2);
		int pictureIndex = loadPicture(thumb, wb);
		if(pictureIndex>0)
			patriarch.createPicture(anchor, pictureIndex); 
	}

	private int loadPicture(String path, HSSFWorkbook wb) throws IOException {
		
		HttpClient httpClient = new DefaultHttpClient(); 
		httpClient.getParams().setIntParameter("http.connection.timeout", 3000);

        HttpGet httpGet = new HttpGet(path); 
        HttpResponse httpResponse = httpClient.execute(httpGet); 
        byte[] byteData = EntityUtils.toByteArray(httpResponse.getEntity());
        
        int pictureIndex = wb.addPicture(byteData, HSSFWorkbook.PICTURE_TYPE_JPEG);
		return pictureIndex;
	}
}
