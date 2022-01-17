/*
 * Created on 2009. 1. 14.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.mc.common.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.CellView;
import jxl.Sheet;
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

import com.mc.common.util.Util;

/**
 * @author NEMS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExcelUtil {
	private WritableWorkbook writebook = null;
	private WritableSheet  writeSheet = null;

	private Workbook readbook = null;
	private Sheet readSheet = null;

	private int sheetNumber = 0;

	private String title;

	private String [] header;
	private List list;
	private String [] sum;

	private String dir;
	private String fileName;

	private int sheetCount = 0;
	private int columnCount = 0;
	private int rowCount = 0;

	private boolean init = false;
	// 0 : None, 1 : File not found
	private int initResult = 0;

	private ExcelUtil(){}

	public ExcelUtil(String dir, String fileName) throws Exception{
		this.dir = dir;
		this.fileName = fileName;

		initReadExcel();
	}

	public ExcelUtil(String dir, String fileName, int sheetNumber) throws Exception{
		this.dir = dir;
		this.fileName = fileName;
		this.sheetNumber = sheetNumber;

		initReadExcel();
	}

	public ExcelUtil(String dir, String fileName, List list, String title) throws Exception{
		this.dir = dir;
		this.fileName = fileName;
		this.title = title;
		this.list = list;

		FileUtil.createDir(dir);

		initWriteExcel();
	}

	public ExcelUtil(String dir, String fileName, List list, String [] header, String title) throws Exception{
		this.dir = dir;
		this.fileName = fileName;
		this.title = title;

		this.header = header;
		this.list = list;

		FileUtil.createDir(dir);

		initWriteHeaderExcel();
	}

	public ExcelUtil(String dir, String fileName, List list, String [] sum, String [] header, String title) throws Exception{
		this.dir = dir;
		this.fileName = fileName;
		this.title = title;

		this.header = header;
		this.list = list;
		this.sum = sum;

		FileUtil.createDir(dir);

		initWriteSumExcel();
	}

	private void initReadExcel() throws Exception{
		try
		{
			if(FileUtil.isFile(this.fileName, this.dir))
			{
				File exlFile = new File(this.dir, this.fileName);

				readbook = Workbook.getWorkbook(exlFile);
				readSheet = readbook.getSheet(sheetNumber);
				//sheet count
				this.sheetCount = readbook.getNumberOfSheets();
				// columns count
				this.columnCount = readSheet.getColumns();
				// row count
				this.rowCount = readSheet.getRows();

				this.init = true;
			}else{
				this.initResult = 1;
			}
		}
		catch(Exception e)
		{
			throw e;
		}finally{
			//if(readbook != null) readbook.close();
		}
	}
	/**
	 * 엑셀 저장
	 * @throws Exception 
	 */
	private void initWriteExcel() throws Exception
	{
		try
		{
			if(FileUtil.isFile(this.fileName, this.dir))
			{
				FileUtil.deleteFile(this.fileName, this.dir);
			}else{
				File exlFile = new File(this.dir, this.title+".xls");

				writebook = Workbook.createWorkbook(exlFile);
				writeSheet = writebook.createSheet(this.title, 0);

			     WritableCellFormat numberFormat = new WritableCellFormat(); // 번호 셀 포멧 생성
			     WritableCellFormat nameFormat = new WritableCellFormat(); // 이름 셀 포멧 생성
			     WritableCellFormat dataFormat = new WritableCellFormat(); // 데이터 셀 포멧 생성

			     numberFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
			     numberFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
			     numberFormat.setBorder(Border.ALL, BorderLineStyle.THICK); // 보더와 보더라인스타일 설정

			     nameFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
			     nameFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
			     nameFormat.setBorder(Border.BOTTOM, BorderLineStyle.HAIR); // 보더와 보더라인스타일 설정

			     dataFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
			     dataFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
			     dataFormat.setWrap(true);

			     String [] obj = null;

			     for(int i=0; i<this.list.size(); i++){
			     	obj = (String [])list.get(i);

			     	for(int x=0;x<obj.length;x++){
				     	Label nameLabels =new Label(x, i, obj[x], dataFormat); // 데이터 포멧에 맞게 대문자 A에서 E까지 생성
				     	writeSheet.addCell(nameLabels); // 셀에 삽입
				     	//Formula f = new Formula(1, 2, "=SUM(A2:A3)");
			     	}
			      }
			     // 라벨을 이용하여 해당 셀에 정보 넣기 끝
			     writebook.write(); // 준비된 정보를 엑셀 포멧에 맞게 작성
			     writebook.close(); // 처리 후 메모리에서 해제 처리

				this.init = true;
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
		     try {
		     	if(writebook != null)
		     		writebook.close(); // 처리 후 메모리에서 해제 처리
			} catch (Exception e) { ; }
		}
	}
	/**
	 * 헤더가 있는 경우
	 * @throws Exception 
	 */
	private void initWriteHeaderExcel() throws Exception
	{
		try
		{
			if(FileUtil.isFile(this.fileName, this.dir))
			{
				FileUtil.deleteFile(this.fileName, this.dir);
			}else{
				File exlFile = new File(this.dir+this.title+".xls");

				writebook = Workbook.createWorkbook(exlFile);
				writeSheet = writebook.createSheet(this.title, 0);

				WritableCellFormat headerFormat = new WritableCellFormat(); // 번호 셀 포멧 생성
				WritableCellFormat numberFormat = new WritableCellFormat(); // 번호 셀 포멧 생성
				WritableCellFormat nameFormat = new WritableCellFormat(); // 이름 셀 포멧 생성
				WritableCellFormat dataFormat = new WritableCellFormat(); // 데이터 셀 포멧 생성

				headerFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
				headerFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
				headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정
				headerFormat.setBackground(Colour.GRAY_25);

				numberFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
				numberFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
				numberFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정

				nameFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
				nameFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
				nameFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정

				dataFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
				dataFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
				dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정
				dataFormat.setWrap(true);

				CellView cv = new CellView();
				cv.setAutosize(true);

				String [] obj = null;

				int i = 0;
				if (header != null && header.length > 0)
				{
					for(int x=0;x<header.length;x++){
						Label headerLabels =new Label(x, i, header[x], headerFormat); // 데이터 포멧에 맞게 대문자 A에서 E까지 생성
						writeSheet.addCell(headerLabels); // 셀에 삽입
					}
					i++;
				}
				if (this.list != null && this.list.size() > 0)
				{
					for(; i<=this.list.size(); i++)
					{
						obj = (String [])list.get(i-1);
						for(int x=0;x<obj.length;x++){
							Label nameLabels =new Label(x, i, obj[x], dataFormat); // 데이터 포멧에 맞게 대문자 A에서 E까지 생성
							writeSheet.addCell(nameLabels); // 셀에 삽입
							//	Formula f = new Formula(1, 2, "=SUM(A2:A3)");
						}
					}
				}
				// 라벨을 이용하여 해당 셀에 정보 넣기 끝

				writebook.write(); // 준비된 정보를 엑셀 포멧에 맞게 작성
				writebook.close(); // 처리 후 메모리에서 해제 처리

				this.init = true;
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
		     try {
		     	if(writebook != null)
		     		writebook.close(); // 처리 후 메모리에서 해제 처리
			} catch (Exception e) { ; }
		}
	}
	/**
	 * 합계가 나와야 하는 경우
	 * @throws Exception 
	 */
	private void initWriteSumExcel() throws Exception
	{
		try
		{
			if(FileUtil.isFile(this.fileName, this.dir))
			{
				FileUtil.deleteFile(this.fileName, this.dir);
			}else{
				File exlFile = new File(this.dir+this.title+".xls");

				writebook = Workbook.createWorkbook(exlFile);
				writeSheet = writebook.createSheet(this.title, 0);

				WritableCellFormat headerFormat = new WritableCellFormat(); // 번호 셀 포멧 생성
				WritableCellFormat numberFormat = new WritableCellFormat(); // 번호 셀 포멧 생성
				WritableCellFormat nameFormat = new WritableCellFormat(); // 이름 셀 포멧 생성
				WritableCellFormat dataFormat = new WritableCellFormat(); // 데이터 셀 포멧 생성

				headerFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
				headerFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
				headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정
				headerFormat.setBackground(Colour.GRAY_25);

				numberFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
				numberFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
				numberFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정

				nameFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
				nameFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
				nameFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정

				dataFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
				dataFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
				dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 보더와 보더라인스타일 설정
				dataFormat.setWrap(true);

				CellView cv = new CellView();
				cv.setAutosize(true);

				String [] obj = null;

				int i = 0;
				if (header != null && header.length > 0)
				{
					for(int x=0;x<header.length;x++){
						Label headerLabels = new Label(x, i, header[x], headerFormat); // 데이터 포멧에 맞게 대문자 A에서 E까지 생성
						writeSheet.addCell(headerLabels); // 셀에 삽입
					}
					i++;
				}
				if (this.list != null && this.list.size() > 0)
				{
					// 내용
					for(; i<=this.list.size(); i++)
					{
						Object o = list.get(i-1).getClass();
						obj = (String [])list.get(i-1);
						for(int x=0;x<obj.length;x++){
							Label nameLabels = new Label(x, i, obj[x], dataFormat); // 데이터 포멧에 맞게 대문자 A에서 E까지 생성
							writeSheet.addCell(nameLabels); // 셀에 삽입
							// Formula f = new Formula(1, 2, "=SUM(A2:A3)");
							// writeSheet.addCell(f); // 셀에 삽입
						}
					}
				}
				if (this.sum != null && this.sum.length > 0)
				{
					for(int x=1;x<this.sum.length+2;x++){
						if(x == 1)
						{
							// 합산
							writeSheet.mergeCells(0, i, 1, i);
							Label nameLabels = new Label(0, i, "합계", headerFormat); // 데이터 포멧에 맞게 대문자 A에서 E까지 생성
							writeSheet.addCell(nameLabels);
						}else{
							Label sumLabels = new Label(x, i, this.sum[x-2], headerFormat); // 데이터 포멧에 맞게 대문자 A에서 E까지 생성
							writeSheet.addCell(sumLabels); // 셀에 삽입
						}
					}
				}

				writebook.write(); // 준비된 정보를 엑셀 포멧에 맞게 작성
				writebook.close(); // 처리 후 메모리에서 해제 처리

				this.init = true;
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
		     try {
		     	if(writebook != null)
		     		writebook.close(); // 처리 후 메모리에서 해제 처리
			} catch (Exception e) { ; }
		}
	}

	public void releaseReadExcel(){
		readbook.close();
	}

	public List<Map<String, String>> getExcelData(){

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> m = null;
		Cell cell = null;
		for(int x=1;x<this.rowCount;x++){
			m = new HashMap<String, String>();
			for(int y=0;y<this.columnCount;y++){
				cell = readSheet.getCell(y,x);
				m.put(String.valueOf(y), cell.getContents());
			}
			list.add(m);
		}

		return list;
	}

	/**
	 * @return Returns the columnCount.
	 */
	public int getColumnCount() {
		return columnCount;
	}
	/**
	 * @return Returns the init.
	 */
	public boolean isInit() {
		return init;
	}
	/**
	 * @return Returns the initResult.
	 */
	public int getInitResult() {
		return initResult;
	}
	/**
	 * @return Returns the rowCount.
	 */
	public int getRowCount() {
		return rowCount;
	}
	/**
	 * @return Returns the sheetCount.
	 */
	public int getSheetCount() {
		return sheetCount;
	}
}
