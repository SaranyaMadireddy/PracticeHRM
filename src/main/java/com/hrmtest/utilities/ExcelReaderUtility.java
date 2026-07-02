package com.hrmtest.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelReaderUtility {

	public static List<String[]> getSheetData(String filepath,String sheetName) {
		
		List<String[]> data=new ArrayList<>();
  		
		try(FileInputStream fis=new FileInputStream(filepath);Workbook workbook=new XSSFWorkbook(fis)){
			
			Sheet sheet=workbook.getSheet(sheetName);
			
			for(Row row:sheet) {
				if(row.getRowNum()==0)
					continue;
				List<String> rowNum=new ArrayList<>();
				for(Cell cell:row)
				{
					rowNum.add(getCellData(cell));
				}
				data.add(rowNum.toArray(new String[0]));
			}	
		} 
		catch (IOException e ) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	
	public static String getCellData(Cell cell) {
		if(cell==null) {
			return "";
		}
		switch(cell.getCellType()) {
			case STRING:
				return cell.getStringCellValue();
			case BOOLEAN:
			 return	String.valueOf(cell.getBooleanCellValue());
			case NUMERIC:
				if(DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue().toString();
				}
				return String.valueOf(cell.getNumericCellValue());
			default:
				return "";
			}
	}
}
