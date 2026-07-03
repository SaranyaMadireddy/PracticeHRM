package com.hrmtest.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.hrmtest.actiondriver.ActionDriver;
import com.hrmtest.base.BaseClass;

public class LoginPage {
	
	private ActionDriver actionDriver;

	public LoginPage(WebDriver driver) {
//		actionDriver=new ActionDriver(driver);
		this.actionDriver=BaseClass.getActionDriver();
	}
	
//	locators
	private By username=By.name("username");
	private By password=By.name("password");
	private By loginButton = By.xpath("//button[text()=' Login ']");
	private By errorMessage=By.xpath("//p[text()='Invalid credentials']");
	
	
//Methods
	
	public void login(String userName,String passWord) {
		actionDriver.enterText(username, userName);
		actionDriver.enterText(password, passWord);
		actionDriver.click(loginButton);
	}
	
	public boolean verifyErrorMessageisDisplayed() {
		return actionDriver.isDisplayed(errorMessage);
	}
	
	public boolean verifyErrorMessage(String expectedMessage) {
		 return actionDriver.compareText(errorMessage, expectedMessage);
	}
	
	public String getErrorMessage() {
	return actionDriver.getText(errorMessage);
	}
	
	
}
