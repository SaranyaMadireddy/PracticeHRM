package com.hrmtest.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.hrmtest.base.BaseClass;

public class DummyTest extends BaseClass{

	
	@Test
	public void testTitle() {
		Assert.assertEquals(getDriver().getTitle(), "OrangeHRM");
		logger.info("Title matched");
	}
}
