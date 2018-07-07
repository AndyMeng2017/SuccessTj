/**   
 * @Title: ExcelHelp.java 
 * @Package com.sinovoice.csr.manager.service.test 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 姜英朕  
 * @date 2014年1月1日 下午5:03:36 
 */

package com.hcicloud.sap.common.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.*;


/**
 * @ClassName: ExcelHelp
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author jiangyingzhen
 * @date 2014年1月1日 下午5:03:36
 * 
 */
public class ExcelHelp {

	/**
	 * 读取excel
	 * 
	 * @param excelFile
	 * @param excelFileFileName
	 * @return
	 * @throws Exception
	 */
	public static List<List<List<String>>> getExcelData(InputStream inputStream, String excelFileFileName) {
		Workbook book = null;
		try {
			book = createWorkBook(inputStream,excelFileFileName);
		} catch (Exception e) {
			throw new RuntimeException("Excel文件出错，请检查文档格式");
		}
		List<List<List<String>>> retList = new ArrayList<List<List<String>>>();
		try {
			for (int i = 0; i < book.getNumberOfSheets(); i++) {
				Sheet sheet = book.getSheetAt(i);
				if (sheet == null) {
					retList.add(null);
				}
				else {
					retList.add(ReadSheet(sheet));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("读取文件数据出错，请检查文档内数据");
		}

		return retList;
	}

	/**
	 * 读取sheet
	 * 
	 * @param sheet
	 * @return
	 * @throws Exception
	 */
	// 把一个sheet转换为数据
	public static List<List<String>> ReadSheet(Sheet sheet) throws Exception {

		// 要读取的数据列
		List<List<String>> retList = new ArrayList<List<String>>();
		
		if(sheet.getLastRowNum()==0)
			return retList;
		//以第一行表头为准，表头为多少列就为多少列。
		List<String> titleRowDate = new ArrayList<String>();
		Row firstros = sheet.getRow(0);
		Iterator<Cell> iterator = firstros.cellIterator();
		while(iterator.hasNext())
		{
			Cell cell=iterator.next();
			if(cell != null&&!"".equals(trimCellValue(cell.getStringCellValue())))
				titleRowDate.add(trimCellValue(cell.getStringCellValue()));
		}
		retList.add(titleRowDate);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			// 第一行数据
			List<String> rowDate = new ArrayList<String>();
			Row ros = sheet.getRow(i);
			if (ros != null) {
				//按照标题列数计算
				for(int j=0;j<titleRowDate.size();j++)
				{
					Cell cell=ros.getCell(j);
					if(cell==null)
						rowDate.add("");
					else{
						cell.setCellType(Cell.CELL_TYPE_STRING);
						rowDate.add(trimCellValue(cell.getStringCellValue()));
					}
					
				}
			}
			retList.add(rowDate);
		}
		return retList;
	}
	
	
	public static String trimCellValue(String cellvalue){
		if(cellvalue==null)
			cellvalue="";
		else
		cellvalue= StringUtil.trimC(cellvalue).replace("\r", "").replace("\n","");
		return cellvalue;
	}

	/**
	 * 根据文件类型生成workbook
	 * 
	 * @param is
	 * @param excelFileFileName
	 * @return
	 * @throws Exception
	 */
	// 判断文件类型
	public static Workbook createWorkBook(InputStream is,
                                          String excelFileFileName) throws Exception {
		if (excelFileFileName.toLowerCase().endsWith("xls")) {
			return new HSSFWorkbook(is);
		}
		if (excelFileFileName.toLowerCase().endsWith("xlsx")) {
			return new XSSFWorkbook(is);
		}
		return null;
	}
	/**
	 * 此方法用于创建一个新的Workbook并将分页的数据循环追加至excel内容
	 * @param map 封装的表格数据 其中第一个list为列宽 第二个list为列名
	 * @param wb 创建的workbook对象
	 * @param isCreatSheet 第一次调用传true生成第一个sheet页 第二次以后传false会自动向wb中增加sheet
	 * @param hasSerialNumber 是否添加 “序号” 列
	 * @return
	 */
	public static void CreateExcelByPage(Map<String, List<List<String>>> map, Workbook wb , int maxRow, boolean isCreatSheet, boolean hasSerialNumber) {
		Set<String> keys = map.keySet();
		Iterator<String> iterator = keys.iterator();
		String key = iterator.next();			
		List<List<String>> sheetList = map.get(key); //表数据
		//设置表头
		List<String> firstData = sheetList.get(0);
		sheetList.remove(0); 
		List<String> secondData = sheetList.get(0);
		sheetList.remove(0);
		if(hasSerialNumber){
			firstData.add(0, "25");
			secondData.add(0, "序号");
		}
		Sheet sheet ;
		Map<String, CellStyle> stylemap = createStyles(wb);
		if(isCreatSheet){
			sheet = wb.createSheet(key);
			for(int i=0;i<firstData.size();i++)
			{
				//70为两个像素
				short width=(short)(70*Integer.parseInt(firstData.get(i)));
				sheet.setColumnWidth((short)i,width);
			}
			//创建表头
			Row row = sheet.createRow(0);
			for (int j = 0; j < secondData.size(); j++) {
				Cell cell = row.createCell(j);
				cell.setCellValue(secondData.get(j));
				cell.setCellStyle(stylemap.get("title"));
				cell.setCellType(Cell.CELL_TYPE_STRING);
			}
			
		}
		int sheetNum = wb.getNumberOfSheets();	//获取sheet页总数量
		sheet = wb.getSheetAt(sheetNum-1); //获取最后一个sheet页
		int rowNum = sheet.getLastRowNum()+1; //获取总行数
		//遍历sheet数据
		// 遍历sheet数据
		List<String> rowList = new ArrayList<String>();
		for (int i = 0; i < sheetList.size(); i++) {
			rowNum = sheet.getLastRowNum()+1;
			if(rowNum > maxRow){//sheet页已满 创建新sheet  设置表头
				sheetNum = wb.getNumberOfSheets();
				sheet = wb.createSheet(key+sheetNum);
				for(int k=0;k<firstData.size();k++)
				{
					//70为两个像素
					short width=(short)(70*Integer.parseInt(firstData.get(k)));
					sheet.setColumnWidth((short)k,width);
				}
				//创建表头
				Row row = sheet.createRow(0);
				for (int j = 0; j < secondData.size(); j++) {
					Cell cell = row.createCell(j);
					cell.setCellValue(secondData.get(j));
					cell.setCellStyle(stylemap.get("title"));
					cell.setCellType(Cell.CELL_TYPE_STRING);
				}
				rowNum = sheet.getLastRowNum()+1;
			}
			
			rowList = sheetList.get(i);
			Row row = sheet.createRow(rowNum);
			row.setHeight((short) (2 * 356));
			int firstColumn ; //1表示有序号列 0表示没序号列
			if(hasSerialNumber){ //设置序号列
				Cell cell = row.createCell(0);
				cell.setCellValue(rowNum);
				cell.setCellStyle(stylemap.get("default"));
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				firstColumn = 1;
			}else{
				firstColumn = 0;
			}
			for (int j = 0; j < rowList.size(); j++) {
				Cell cell = row.createCell(j+firstColumn);
				cell.setCellValue(rowList.get(j)!=null?rowList.get(j):"");
				cell.setCellStyle(stylemap.get("default"));
				cell.setCellType(Cell.CELL_TYPE_STRING);
			}
		}
		sheetList.clear();
		map.clear();
		rowList.clear();
	}
	
	
	
	// 创建并生成excel
	public static Workbook CreateExcel(Map<String, List<List<String>>> map, int ishasColumnWidth) {
		Workbook wb = new HSSFWorkbook();// 创建工作薄
		Set<String> keys = map.keySet();
		Iterator<String> iterator = keys.iterator();
		Map<String, List<List<String>>> sheetMap = new HashMap<String, List<List<String>>>();
		
		// 循环创建sheet
		while (iterator.hasNext()) {
			String key = iterator.next();			
			List<List<String>> sheetList = map.get(key);
			int count = 65534;
			if(sheetList.size()>count+2){
				List<String> firstData = sheetList.get(0);
				sheetList.remove(0);
				List<String> secondData = sheetList.get(0);
				sheetList.remove(0);
				int size = sheetList.size();
				for (int i = 0; size>i*count; i++) {
					List<List<String>> subSheetList = new ArrayList<List<String>>();
					subSheetList.add(firstData);
					subSheetList.add(secondData);
					int endIndex = size-(i+1)*count>0?(i+1)*count:size;
					for (int j = i*count; j < endIndex; j++) {
						subSheetList.add(sheetList.get(j));
					}
					sheetMap.put(key + Integer.toString(i), subSheetList);
				}
			}else{
				sheetMap.put(key, map.get(key));
			}
		}
		
		keys = sheetMap.keySet();
		iterator = keys.iterator();	
		Map<String, CellStyle> stylemap = createStyles(wb);
		while (iterator.hasNext()) {
			String sheetname = iterator.next();
			// 创建sheet
			Sheet workSheet = wb.createSheet(sheetname);
			List<List<String>> contentList = sheetMap.get(sheetname);
			//如果第一行是ishasColumnWidth
			if(ishasColumnWidth==1)
			{
				List<String> widthList=contentList.get(0);
				contentList.remove(0);
				for(int i=0;i<widthList.size();i++)
				{
					//70为两个像素
					short width=(short)(70*Integer.parseInt(widthList.get(i)));
					workSheet.setColumnWidth((short)i,width);
				}
			}
			
			
			// 获取sheet数据
			
			// 遍历sheet数据
			for (int i = 0; i < contentList.size(); i++) {
				List<String> rowList = contentList.get(i);
				Row row = workSheet.createRow(i);
				row.setHeight((short) (2 * 356));
				
				CellStyle cellStyle;
				if(i==0)
					cellStyle=stylemap.get("title");
				else {
					cellStyle=stylemap.get("default");
				}
				
				for (int j = 0; j < rowList.size(); j++) {
					Cell cell = row.createCell(j);
					cell.setCellValue(rowList.get(j));
					cell.setCellStyle(cellStyle);
					cell.setCellType(Cell.CELL_TYPE_STRING);
				}
			}
		}				
		return wb;
	}

	//获取表头数据
	private static Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

		// ----------------------标题样式---------------------------
		CellStyle cell_header_title = wb.createCellStyle();
		Font font_header_title = wb.createFont();
		font_header_title.setBoldweight(Font.BOLDWEIGHT_BOLD);//
		font_header_title.setFontHeight((short) (12 * 20));
		font_header_title.setFontName("Times New Roman");//
		cell_header_title.setFont(font_header_title);
		cell_header_title.setAlignment(CellStyle.ALIGN_CENTER);//
		cell_header_title.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cell_header_title.setWrapText(true);
		cell_header_title.setBorderBottom(CellStyle.BORDER_THIN);
		cell_header_title.setBorderLeft(CellStyle.BORDER_THIN);
		cell_header_title.setBorderTop(CellStyle.BORDER_THIN);
		cell_header_title.setBorderRight(CellStyle.BORDER_THIN);
		styles.put("title", cell_header_title);

		// -----------------------数据样式---------------------------

		CellStyle cell_data_default = wb.createCellStyle();
		Font font_data_default = wb.createFont();
		font_data_default.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		font_data_default.setFontHeight((short) (10 * 20));
		font_data_default.setFontName("Arial Narrow");
		cell_data_default.setFont(font_data_default);
		cell_data_default.setAlignment(CellStyle.ALIGN_CENTER);
		cell_data_default.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cell_data_default.setWrapText(true);
		cell_data_default.setBorderBottom(CellStyle.BORDER_THIN);
		cell_data_default.setBorderLeft(CellStyle.BORDER_THIN);
		cell_data_default.setBorderTop(CellStyle.BORDER_THIN);
		cell_data_default.setBorderRight(CellStyle.BORDER_THIN);
		styles.put("default", cell_data_default);
		return styles;
	}

}
