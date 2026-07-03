package com.hrmtest.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.hrmtest.actiondriver.ActionDriver;
import com.hrmtest.base.BaseClass;

public class HomePage {
	
	private ActionDriver actionDriver;

	public HomePage(WebDriver driver) {
//		actionDriver=new ActionDriver(driver);
		this.actionDriver=BaseClass.getActionDriver();
	}
	
	//locators
	private By adminTab=By.xpath("//span[text()='Admin']");
	private By orangeHRMlogo=By.className("oxd-brand-banner");
	private By user=By.className("oxd-userdropdown-tab");
	private By logout=By.xpath("//a[contains(text(),'Logout')]");
	private By pimTab=By.xpath("//span[text()='PIM']");
	private By employeeName=By.xpath("//div[@class='oxd-grid-item oxd-grid-item--gutters'][1]//input");
	//label[text()='Employee Name']/parent::div/following::div/div/div/input
	private By employeeFirstNameAndMiddleName=By.xpath("//div[@class='oxd-table-card'][1]/div/div[3]/div");
	private By employeeLastName=By.xpath("//div[@class='oxd-table-card'][1]/div/div[4]/div");
	private By searchButton=By.xpath("//button[text()=' Search ']");
	
	//Methods
	public boolean verifyAdminTab() {
	return	actionDriver.isDisplayed(adminTab);
	}
	
	public boolean verifyOrangeHRMlogo() {
		return actionDriver.isDisplayed(orangeHRMlogo);
	}
	
	public void logout() {
		actionDriver.click(user);
		actionDriver.click(logout);
	}
	
	public void pimTab() {
		actionDriver.click(pimTab);
	}
	
	public void search() {
		actionDriver.click(searchButton);
		actionDriver.scrollToElement(employeeFirstNameAndMiddleName);
	}
	
	public void enterEmpNameinSearch(String name) {
		actionDriver.enterText(employeeName, name);
	}
	
	public boolean verifyEmployeeFirstNameAndMiddleName(String firstNameAndMiddleName) {
		return actionDriver.compareText(employeeFirstNameAndMiddleName, firstNameAndMiddleName);
	}
	
	public boolean verifyEmployeeLastName(String lastName) {
		return actionDriver.compareText(employeeLastName, lastName);
	}
	
}
