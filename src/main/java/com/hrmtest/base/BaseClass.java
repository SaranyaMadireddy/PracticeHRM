package com.hrmtest.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;


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
public synchronized void setup() {
	launchBrowser();
	configBrowser();
	staticWait(3);	
	
	//Initialization of ActionDrivers
	
		actionDriver.set(new ActionDriver(getDriver()));
		 logger.info("ActionDriver is initialized"+Thread.currentThread().getId());
}

private synchronized void launchBrowser() {
	
	String browserName=prop.getProperty("browser");
	
	if(browserName.equalsIgnoreCase("chrome")) {
//		driver=new ChromeDriver();
		ChromeOptions options=new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--disable-gpu");
		options.addArguments("--disable-notifications");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--window-size=1920,1080");
		driver.set(new ChromeDriver(options));
	/*	DevTools devTools = ((ChromeDriver) getDriver()).getDevTools();
        devTools.createSession();

        // Set device metrics (width, height, scale)
        devTools.send(Emulation.setDeviceMetricsOverride(
                1920,   // width
                1080,   // height
                100,    // device scale factor
                false,  // mobile
                Optional.empty(), Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty()
        ));*/
        ExtentManager.registerDriver(getDriver());
		 logger.info("ChromeDriver initialized");
	}else if(browserName.equalsIgnoreCase("firefox")) {
//		driver=new FirefoxDriver();
		FirefoxOptions options=new FirefoxOptions();
		options.addArguments("--headless");
		options.addArguments("--disable-gpu");
		options.addArguments("--disable-notifications");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--window-size=1920,1080");
		driver.set(new FirefoxDriver(options));
		ExtentManager.registerDriver(getDriver());
		logger.info("FirefoxDriver initialized");
		
	}else if(browserName.equalsIgnoreCase("edge")) {
//		driver=new EdgeDriver();
		EdgeOptions options=new EdgeOptions();
		options.addArguments("--headless");
		options.addArguments("--disable-gpu");
		options.addArguments("--disable-notifications");
		options.addArguments("--nosandbox");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--window-size=1920,1080");
		driver.set(new EdgeDriver(options));
		/*	DevTools devTools = ((ChromeDriver) getDriver()).getDevTools();
        devTools.createSession();

		// Set device metrics (width, height, scale)
        devTools.send(Emulation.setDeviceMetricsOverride(
                1920,   // width
                1080,   // height
                100,    // device scale factor
                false,  // mobile
                Optional.empty(), Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty()
        ));*/
        
        ExtentManager.registerDriver(getDriver());
		logger.info("EdgeDriver initialized");
	}else {
		throw new IllegalArgumentException("Browser name is invalid "+browserName);
	}		
}

private void configBrowser() {
	
	int implicit= Integer.parseInt(prop.getProperty("implicitwait"));
	getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicit));
	
	getDriver().manage().window().maximize();
	
	try {
		getDriver().get(prop.getProperty("local_url"));
	} catch (Exception e) {
		logger.error("URL is not valid"+e.getMessage());
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
