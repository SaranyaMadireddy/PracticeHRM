package com.hrmtest.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer{

	public int retryCount=0;
	public static final int maxRetryCount=2;
	
	@Override
	public boolean retry(ITestResult result) {
		if(retryCount<maxRetryCount) {
			retryCount++;
			return true;
		}
		
		return false;
	}
	
	

}
