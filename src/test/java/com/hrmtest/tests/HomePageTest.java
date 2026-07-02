package com.hrmtest.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hrmtest.base.BaseClass;
import com.hrmtest.pages.HomePage;
import com.hrmtest.pages.LoginPage;
import com.hrmtest.utilities.DataProviders;
import com.hrmtest.utilities.ExtentManager;

public class HomePageTest extends BaseClass{
	private LoginPage loginPage;
	private HomePage homePage;
	
@BeforeMethod
public void setupPages() {
	loginPage=new LoginPage(getDriver());
	homePage=new HomePage(getDriver());
}

@Test(dataProvider = "ValidLogin",dataProviderClass = DataProviders.class)
public void verifyOrangeHRMlogo(String username,String password) {
	ExtentManager.logStep("Verifying Orange HRM Logo Test");
	ExtentManager.logStep("Logging in to Orange HRM");
	loginPage.login(username, password);
	ExtentManager.logStep("Logged in Successfully");
	homePage.verifyOrangeHRMlogo();
	ExtentManager.logStep("OrangeHRM logo is displayed");
	homePage.verifyAdminTab();
	homePage.logout();
	ExtentManager.logStep("Logged out successfully");
}

}
