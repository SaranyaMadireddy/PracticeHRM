package com.hrmtest.listeners;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.ITestAnnotation;

import com.hrmtest.base.BaseClass;
import com.hrmtest.utilities.ExtentManager;
import com.hrmtest.utilities.RetryAnalyzer;

public class Listeners implements ITestListener,IAnnotationTransformer{

	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor,
			Method testMethod) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}

	@Override
	public void onTestStart(ITestResult result) {
		ExtentManager.startTest(result.getMethod().getMethodName());
		ExtentManager.logStep("Test started");
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String testName=result.getMethod().getMethodName();
		ExtentManager.logStepWithScreenshot(BaseClass.getDriver()	, "Test Passed", "Test End:"+testName+" - Test Passed");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String testName=result.getMethod().getMethodName();
		ExtentManager.logFaliure(BaseClass.getDriver(), "Test Failed"	, "Test End: "+testName+" - X Test Failed");
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		String testName=result.getMethod().getMethodName();
		ExtentManager.logSkip("Test is skipped:"+testName);
	}

	@Override
	public void onStart(ITestContext context) {
		ExtentManager.getReporter();
	}

	@Override
	public void onFinish(ITestContext context) {
		ExtentManager.endTest();
		try {
			Desktop.getDesktop().browse(new File("src/test/resources/ExtentReports/ExtentReport.html").toURI());
		} catch (IOException e) {
			System.out.println("Report is not opened in browser directly:"+e.getMessage());
		}
	}

}
