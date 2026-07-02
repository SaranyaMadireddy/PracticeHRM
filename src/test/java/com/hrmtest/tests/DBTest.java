package com.hrmtest.tests;

import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.hrmtest.base.BaseClass;
import com.hrmtest.pages.HomePage;
import com.hrmtest.pages.LoginPage;
import com.hrmtest.utilities.DBConnections;
import com.hrmtest.utilities.DataProviders;

public class DBTest extends BaseClass{
	
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages() {
		loginPage=new LoginPage(getDriver());
		homePage=new HomePage(getDriver());
	}
	
	@Test(dataProvider="DBTesting",dataProviderClass=DataProviders.class)
	public  void verifyEmployeeDetailsTest(String empId,String empName) {
		
		SoftAssert soft=new SoftAssert();
		
		loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
		homePage.pimTab();
		homePage.enterEmpNameinSearch(empName);
		homePage.search();
		
		Map<String,String> empdata=DBConnections.getEmployeeDetails(empId);
		
		String firstname=empdata.get("empFirstName");
		String middlename=empdata.get("empMiddleName");
		String lastname=empdata.get("empLastName");
		
		String firstnameAndMiddleName =firstname+" "+middlename;
		
		soft.assertTrue(	homePage.verifyEmployeeFirstNameAndMiddleName(firstnameAndMiddleName),"FirstName and middleName mismatch");
		
		soft.assertTrue(	homePage.verifyEmployeeLastName(lastname),"LastName mismatch");
		
		soft.assertAll();
		
		System.out.println("Test completed");
		
		
	}

}
