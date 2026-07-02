package com.hrmtest.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hrmtest.base.BaseClass;
import com.hrmtest.pages.HomePage;
import com.hrmtest.pages.LoginPage;
import com.hrmtest.utilities.DataProviders;
import com.hrmtest.utilities.ExtentManager;

public class LoginPageTest extends BaseClass{
	private LoginPage loginPage;
	private HomePage homePage;

@BeforeMethod
public void setupPages() {
	loginPage=new LoginPage(getDriver());
	homePage=new HomePage(getDriver());
}

@Test(dataProvider = "ValidLogin",dataProviderClass = DataProviders.class)
public void validLoginTest(String username,String password) {
	ExtentManager.logStep("Verifying Valid login Test");
	ExtentManager.logStep("Logging in to Orange HRM");
	loginPage.login(username, password);
	ExtentManager.logStep("Logged in successfully");
	homePage.verifyAdminTab();
	homePage.logout();
	ExtentManager.logStep("Logged out successfully");
}

@Test(dataProvider = "InvalidLogin",dataProviderClass = DataProviders.class)
public void invalidLoginTest(String username,String password) {
	ExtentManager.logStep("Verifying InValid login Test");
	ExtentManager.logStep("Logging in to Orange HRM");
	loginPage.login(username, password);
	Assert.assertTrue(loginPage.verifyErrorMessage("Invalid credentials"),"Invalid Error Message");
	ExtentManager.logStep("Validation Successful");
	ExtentManager.logStep("Logged out successfully");
}

}
