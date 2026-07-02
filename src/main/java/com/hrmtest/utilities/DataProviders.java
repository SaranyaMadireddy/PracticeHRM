package com.hrmtest.utilities;

import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviders {
	
	public final String filepPath= System.getProperty("user.dir")+"/src/test/resources/TestData/TestData.xlsx";
	
	@DataProvider(name="ValidLogin")
	public Object[][] validLogin() {
		return getSheetData("ValidLogin");
	}
	
	@DataProvider(name="InvalidLogin")
	public Object[][] invalidLogin() {
		return getSheetData("InvalidLogin");
	}
	
	@DataProvider(name="DBTesting")
	public Object[][] DBTest() {
		return getSheetData("DBTest");
	}
	
	public Object[][] getSheetData(String sheetName){
		
	List<String[]> sheetdata=ExcelReaderUtility.getSheetData(filepPath, sheetName);
			
		Object[][] data=new Object[sheetdata.size()][sheetdata.get(0).length];
		
		for(int i=0;i<sheetdata.size();i++) {
			
			data[i]= sheetdata.get(i);
		}
		
		return data;	
	}

}
