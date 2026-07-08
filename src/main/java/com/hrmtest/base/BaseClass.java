package com.hrmtest.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v144.emulation.Emulation;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.hrmtest.actiondriver.ActionDriver;
import com.hrmtest.utilities.ExtentManager;
import com.hrmtest.utilities.LoggerManager;

public class BaseClass {
	protected static Properties prop;
	private static ThreadLocal<WebDriver> driver=new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver=new ThreadLocal<>();
	public static final Logger logger=LoggerManager.getLogger(BaseClass.class);
	
	public static ActionDriver getActionDriver() { 
	  if(actionDriver.get()==null) { //
	  logger.info("ActionDriver is not initialized"); 
	  throw new IllegalStateException("ActionDriver is not initialized"); 
	  }  
	  return actionDriver.get();
	  }
	
	public static void setActionDriver(ThreadLocal<ActionDriver> actionDriver) {
		BaseClass.actionDriver = actionDriver;
	}
	 
	public static WebDriver getDriver() {
		if(driver.get()==null) {
			 logger.info("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driver.get();
	}

	public  void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}

	public static Properties getProp() {
		return prop;
	}

	public static void setProp(Properties prop) {
		BaseClass.prop = prop;
	}

@BeforeSuite
public void loadConfig() throws IOException {
	File file=new File(System.getProperty("user.dir")+"/src/main/resources/config.properties");
	prop=new Properties();
	FileInputStream fis=new FileInputStream(file);
	prop.load(fis);
	 logger.info("Configuration is completed");
}


@BeforeMethod
@Parameters("browser")
public synchronized void setup(String browser) {
	System.out.println("Setup running");
	System.out.println("Setting up WebDriver for:" + this.getClass().getSimpleName());

	launchBrowser(browser);
	configBrowser();
	staticWait(3);	
	
	//Initialization of ActionDriver
	
		actionDriver.set(new ActionDriver(getDriver()));
		 logger.info("ActionDriver is initialized"+Thread.currentThread().getId());
}

private synchronized void launchBrowser(String browser) {
	
	boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("seleniumgrid"));
	String gridurl = prop.getProperty("grid_url");
//	String browserName=prop.getProperty("browser");
	
	if(seleniumGrid) {
		try {
			if(browser.equalsIgnoreCase("chrome")) {
				ChromeOptions options=new ChromeOptions();
				options.addArguments("--headless=new","--disable-notifications","--disable-gpu","--window-size=1920,1080","--no-sandbox","--disable-dev-shm-usage");
				driver.set(new RemoteWebDriver(new URL(gridurl),options));
			}else if(browser.equalsIgnoreCase("firefox")) {
				FirefoxOptions options=new FirefoxOptions();
				options.addArguments("--headless=new","--disable-notifications","--disable-gpu","--window-size=1920,1080","--no-sandbox","--disable-dev-shm-usage");
				driver.set(new RemoteWebDriver(new URL(gridurl),options));
			}else if(browser.equalsIgnoreCase("edge")) {
				EdgeOptions options=new EdgeOptions();
				options.addArguments("--headless=new","--disable-notifications","--disable-gpu","--window-size=1920,1080","--no-sandbox","--disable-dev-shm-usage");
				driver.set(new RemoteWebDriver(new URL(gridurl),options));
		}
	} catch (MalformedURLException e) {
		throw new RuntimeException("Invalid Grid URL", e);
	}}
	else {
	if(browser.equalsIgnoreCase("chrome")) {
//		driver=new ChromeDriver();
		ChromeOptions options=new ChromeOptions();
		options.addArguments("--headless=new","--disable-notifications","--disable-gpu","--window-size=1920,1080","--no-sandbox","--disable-dev-shm-usage");
		driver.set(new ChromeDriver(options));
        ExtentManager.registerDriver(getDriver());
		 logger.info("ChromeDriver initialized");
	}else if(browser.equalsIgnoreCase("firefox")) {
//		driver=new FirefoxDriver();
		FirefoxOptions options=new FirefoxOptions();
		options.addArguments("--headless=new","--disable-notifications","--disable-gpu","--window-size=1920,1080","--no-sandbox","--disable-dev-shm-usage");
		driver.set(new FirefoxDriver(options));
		ExtentManager.registerDriver(getDriver());
		logger.info("FirefoxDriver initialized");
		
	}else if(browser.equalsIgnoreCase("edge")) {
//		driver=new EdgeDriver();
		EdgeOptions options=new EdgeOptions();
		options.addArguments("--headless=new","--disable-notifications","--disable-gpu","--window-size=1920,1080","--no-sandbox","--disable-dev-shm-usage");
		driver.set(new EdgeDriver(options));       
        ExtentManager.registerDriver(getDriver());
		logger.info("EdgeDriver initialized");
	}
	else {
		throw new IllegalArgumentException("Browser name is invalid "+browser);
	}		
}
}
private void configBrowser() {
	
	boolean seleniumGrid=Boolean.parseBoolean(System.getProperty("seleniumGrid", prop.getProperty("seleniumgrid")));
	
	int implicit= Integer.parseInt(prop.getProperty("implicitwait"));
	getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicit));
	
	getDriver().manage().window().maximize();
	
	if(seleniumGrid) {
		getDriver().get(prop.getProperty("url_grid"));
	}
	else {
		getDriver().get(prop.getProperty("local_url"));
	} 
}

public void staticWait(int seconds) {
	LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
}


@AfterMethod
public synchronized void tearDown() {
	try {
		if(getDriver()!=null) {
			getDriver().quit();
		}
	} catch (Exception e) {
		logger.error("Driver instance is not closed"+e.getMessage());
	}
	driver.remove();
	actionDriver.remove();
}
}
