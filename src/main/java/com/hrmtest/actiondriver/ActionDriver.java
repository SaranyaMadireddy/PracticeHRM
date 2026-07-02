package com.hrmtest.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.hrmtest.base.BaseClass;
import com.hrmtest.utilities.ExtentManager;

public class ActionDriver {

	private WebDriverWait wait;
	private WebDriver driver;
	public static final Logger logger=BaseClass.logger;
	
	public ActionDriver(WebDriver driver) {
		this.driver=driver;
		int expwait=Integer.parseInt(BaseClass.getProp().getProperty("explicitwait"));
		wait=new WebDriverWait(driver,Duration.ofSeconds(expwait));
	}
	
	public void click(By by) {
		try {
			applyBorder(by, "green");
			waitForElementToBeClickable(by);
			WebElement element =driver.findElement(by);
			element.click();
			ExtentManager.logStep("Element is clicked:"+getElementDescription(by));
			logger.info("Clicked an element " + getElementDescription(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to click on Element:"+e.getMessage());
			ExtentManager.logFaliure(BaseClass.getDriver(),"Unable to click on Element:"+e.getMessage(),"Unable to click on Element");
		}
	}
	
	public void enterText(By by,String value) {
		try {
			waitForElementToBeClickable(by);
			WebElement element =driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			applyBorder(by, "green");
			ExtentManager.logStep("Entered text to element:"+getElementDescription(by));
			logger.info("Entered text to element:"+getElementDescription(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to Enter text:"+e.getMessage());
			ExtentManager.logFaliure(BaseClass.getDriver(),"Unable to Enter text:"+e.getMessage(),"Unable to Enter text");
		}
	}
	
	public boolean compareText(By by,String expectedText) {
		
	try {
		String actualText=	driver.findElement(by).getText();
		if(actualText.equalsIgnoreCase(expectedText)) {
			applyBorder(by, "green");
			logger.info("Compare Text passed "+actualText+ " equals "+expectedText);
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text passed "+actualText+ " equals "+expectedText, "Comape Text passed");
			return true;
		}else {
			applyBorder(by, "red");
			logger.error("Compare Text Failed "+actualText+ " not equals "+expectedText);
			ExtentManager.logFaliure(BaseClass.getDriver(),"Compare Text Failed "+actualText+ " not equals "+expectedText,"Compare Text Failed");
			return false;
		}
	} catch (Exception e) {
		applyBorder(by, "red");
		logger.error("Unable to Compare text:"+e.getMessage());
		ExtentManager.logFaliure(BaseClass.getDriver(),"Unable to Compare text:"+e.getMessage(),"Unable to Compare text");
		return false;
	}	
	}
	
	public String getText(By by) {
	try {
		applyBorder(by, "green");
		logger.info("Retrived text from element:"+getElementDescription(by));
		ExtentManager.logStep("Retrived text from element:"+getElementDescription(by));
		return	driver.findElement(by).getText();
	} catch (Exception e) {
		applyBorder(by, "red");
		logger.error("Unable to retrieve text:"+e.getMessage());
		ExtentManager.logFaliure(BaseClass.getDriver(),"Unable to retrieve text:"+e.getMessage(),"Unable to retrieve text");
		return "";
	}
	}
	
	public boolean isDisplayed(By by) {
		try {
			waitForVisibilityOfElement(by);
			applyBorder(by, "green");
			logger.info("Element is displayed:"+getElementDescription(by));
			ExtentManager.logStep("Element is displayed:"+getElementDescription(by));
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Element is not displayed:"+e.getMessage());
			ExtentManager.logFaliure(BaseClass.getDriver(),"Element is not displayed:"+e.getMessage(),"Element is not displayed");
			return false;
		}
	}
	
	public void waitForPageToLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			logger.info("Page loaded Successfully");
		} catch (Exception e) {
			logger.error("Page is not loaded within:"+timeOutInSec+e.getMessage());
		}
	}
	
	public void scrollToElement(By by) {
		try {
			WebElement element=driver.findElement(by);
			JavascriptExecutor js=(JavascriptExecutor)driver;
			js.executeScript("arguments[0].scrollIntoView(true);", element);
			applyBorder(by, "green");
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to scroll to element:"+e.getMessage());
		}
	}
	
	public void waitForElementToBeClickable(By by) {
			try {		
				wait.until(ExpectedConditions.elementToBeClickable(by));
			} catch (Exception e) {
				logger.error("Element is not clickable:"+e.getMessage());
			}
		
	}
	
	public  void waitForVisibilityOfElement(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			System.out.println("Element is not visible:"+e.getMessage());
		}
	}
	
	public String getElementDescription(By locator) {
		if(driver==null)
			return "driver is null";
		if(locator==null) {
			return "Locator is null";
		}
		
		try {
			WebElement element=driver.findElement(locator);
			
			String name =element.getDomAttribute("name");
			String id =element.getDomAttribute("id");
			String text =element.getText();
			String className =element.getDomAttribute("class");
			String placeholder =element.getDomAttribute("placeholder");
			
			if(isNotEmpty(name)) {
				return "Element with name:"+name;
			}else if(isNotEmpty(id)) {
				return "Element with id:"+id;
			}else if(isNotEmpty(text)) {
				return "Element with Text:"+text;
			}else if(isNotEmpty(className)) {
					return "Element with className:"+className;
			}else if(isNotEmpty(placeholder)) {
				return "Element with placeholder:"+placeholder;
			}
		} catch (Exception e) {
			logger.error("Unable to get Element Description:"+e.getMessage());
		}
		return "Unable to get Element Description";			
	
	}
	
	private boolean isNotEmpty(String value) {
		return value!=null && !value.isEmpty();
	}
	
	private String truncate(String text,int maxlength) {
		if(text.length()<maxlength) {
			return text;
		}
		return text.substring(0,maxlength);
	}
	
	public void applyBorder(By by ,String color) {
		try {
			JavascriptExecutor js=(JavascriptExecutor)driver;
			WebElement element=driver.findElement(by);
			js.executeScript("arguments[0].style.border='3px solid "+color+"'", element);
			logger.info("Applied border for element "+getElementDescription(by));
		} catch (Exception e) {
			logger.error("Unable to apply border to element:"+getElementDescription(by),e);

		}		
	}
}
