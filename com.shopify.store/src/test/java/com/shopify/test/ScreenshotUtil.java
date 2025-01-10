package com.shopify.test;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil {
	public static String captureScreenshot(WebDriver driver, String screenshotName) {

	String destination = System.getProperty("user.dir")+"/screenshots/"+ screenshotName +".png";
	File source = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	try {
		FileUtils.copyFile(source, new File(destination));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return destination;
}
}
