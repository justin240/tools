package com.justin.tools.translation;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class SelTest {

	public static void main(String[] args) throws InterruptedException, IOException {

		 /*HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME);
		 driver.get("https://translate.google.com/#auto/ta/world");
		 //Thread.sleep(5000);
		 WebElement srcVal = driver.findElementById("source");
		 srcVal.sendKeys("Hello");
		 System.out.println(srcVal.getAttribute("name"));
		 WebElement transVal = driver.findElementById("result_box");
		 System.out.println(transVal.getAttribute("class"));
		 System.out.println(transVal.getAttribute("outerHTML"));
		 System.out.println("|"+transVal.getText()+"|");
		 //System.out.println(driver.getPageSource());
*/		 
		
		final String link = "https://translate.google.com/#auto/ta/world";
	    final File screenShot = new File("/tmp/screenshot.png").getAbsoluteFile();

	    File pathToBinary = new File("/home/shervin/Documents/app/browser/firefox/firefox");
	    FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
	    FirefoxProfile firefoxProfile = new FirefoxProfile();       
	    WebDriver driver = new FirefoxDriver(ffBinary,firefoxProfile);
	    try {
	      driver.get(link);

	      TimeUnit.SECONDS.sleep(5);

	      final File outputFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	      FileUtils.copyFile(outputFile, screenShot);
	    } finally {
	      driver.close();
	    }

	}

}
