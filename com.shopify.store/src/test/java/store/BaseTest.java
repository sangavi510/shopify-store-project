package store;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {

	WebDriver driver;
	WebDriverWait wait;
	WebElement addToCartButton;
	WebElement viewCart;
	ExtentReports extent;
	ExtentSparkReporter sparkReporter;
	public static ExtentTest test;
	SoftAssert soft = new SoftAssert();

	@BeforeSuite
	public void setUp() {
		sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir")+"/reports/"+"extentReports.html");
		extent=new ExtentReports();
		extent.attachReporter(sparkReporter);

		WebDriverManager.chromedriver().setup();
		driver= new ChromeDriver();
		wait=new WebDriverWait(driver,Duration.ofSeconds(10));

	}

	@Test(priority=-1)
	public void login() {
		driver.get("https://2x1i0c-wy.myshopify.com/");
		driver.manage().window().maximize();
		WebElement passwordLink = driver.findElement(By.xpath("//div[contains(@class,'password-link')]"));
		passwordLink.click();
		WebElement passwordTextBox =driver.findElement(By.xpath("//input[@id='Password']"));
		passwordTextBox.clear();
		passwordTextBox.sendKeys("Amazon123Password*");
		WebElement submitButton = driver.findElement(By.xpath("//button[contains(@class,'password-button')]"));
		submitButton.click();

	}

	@Test(priority=0)
	public void  ThreePairComboOfferCheck() {

		test=extent.createTest("ThreePairComboOfferCheck","ThreePairComboOfferCheck");
		//verify home page title
		soft.assertEquals(driver.getTitle(), "TestAssignmentStore");
		test.log(Status.INFO, "Navigated to Home page");
		WebElement shopNowButton = driver.findElement(By.xpath("//*[contains(text(),'Shop now')]"));
		shopNowButton.click();
		//quantity offer product

		WebElement beachProduct1=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='CardLink-template--23824074047785__main-collection-product-grid-9583948169513']")));
		beachProduct1.click();
		test.log(Status.INFO, "Navigated to beach product page");
		ArrayList<String> newPriceList = new ArrayList<>();
		newPriceList.add("Rs. 171.79");
		newPriceList.add("Rs. 309.23");
		newPriceList.add("Rs. 412.30");
		ArrayList<String> originalPriceList = new ArrayList<>();
		originalPriceList.add("Rs. 257.68");
		originalPriceList.add("Rs. 515.36");
		originalPriceList.add("Rs. 773.04");
		WebElement radioButtonOption,newPrice,originalPrice ;

		for(int j=0;j<3;j++) {
			String locator = "//*[contains(text(),'" +(j+1)+" Pair')]";
			radioButtonOption=driver.findElement(By.xpath(locator));
			String newPriceLocator = "//*[@id='prvw_totalAmount_"+(j)+"']";
			newPrice=driver.findElement(By.xpath(newPriceLocator));
			soft.assertEquals(newPrice.getText(),newPriceList.get(j));
			String originalPriceLocator = "//*[@id='prvw_originalAmount_"+(j)+"']";
			originalPrice=driver.findElement(By.xpath(originalPriceLocator));
			soft.assertEquals(originalPrice.getText(),originalPriceList.get(j));
			if(j==2) {
				radioButtonOption.click();
			}


		}

		//check cart view
		WebElement addToCartButton =wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@id,'ProductSubmitButton')]")));
		addToCartButton.click();
		WebElement viewCart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@id,'cart-notification-button')]")));
		viewCart.click();
		test.log(Status.INFO, "Navigated to Cart View");
		WebElement threePairDiscountTag =wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='discounts__discount']")));
		soft.assertEquals(threePairDiscountTag.getText().trim().replaceAll("\\s+", " "), "3 Pair");
		test.log(Status.PASS, "Verified 3 Pair Discount tag in Cart View");
		WebElement quantity =driver.findElement(By.xpath("//input[@class='quantity__input']"));
		soft.assertEquals(quantity.getAttribute("value"), "3");
		test.log(Status.PASS, "Verified quantity in Cart View");
		WebElement finalPrice =driver.findElement(By.xpath("//*[text()='Beach Valentine Gift Acrylic Pendant']//following::dd[@class='price price--end'][2]"));
		soft.assertEquals(finalPrice.getText().trim().replaceAll("\\s+", " "),"Rs. 412.30");
		test.log(Status.PASS, "Verified Final Discounted Price in Cart View");
	}

	@Test(priority=1)
	public void  TwoPairComboOfferCheck() throws InterruptedException {
		test=extent.createTest("TwoPairComboOfferCheck","TwoPairComboOfferCheck");
		//All S size and Black color
		WebElement continueShopping = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Continue shopping']")));
		continueShopping.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
		soft.assertEquals(driver.getTitle(), "Products – TestAssignmentStore");
		WebElement petBagProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='CardLink-template--23824074047785__main-collection-product-grid-9583948267817']")));
		petBagProduct.click();
		test.log(Status.INFO, "Navigated to pet bag product page");
		WebElement twoPairComboRadio = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='prvw__radio__botton_2']")));
		twoPairComboRadio.click();
		WebElement discountedPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[@id='prvw_totalAmount_1']")));
		soft.assertEquals(discountedPrice.getText(), "Rs. 1,221.41");
		WebElement originalPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='prvw_originalAmount_1']")));
		soft.assertEquals(originalPrice.getText(), "Rs. 2,035.70");
		List<String> expectedSizeOptions = new ArrayList<>();
		expectedSizeOptions.add("S");
		expectedSizeOptions.add("M");
		List<String> expectedColorOptions = new ArrayList<>();
		expectedColorOptions.add("Black");
		expectedColorOptions.add("Green");
		expectedColorOptions.add("Blue");
		expectedColorOptions.add("Purple");
		expectedColorOptions.add("Orange");

		List<String> actualSizeOptions = new ArrayList<>();
		List<String> actualColorOptions = new ArrayList<>();
		List<WebElement> actualSizeElements = new ArrayList<>();
		List<WebElement> actualColorElements = new ArrayList<>();
		for(int i=1;i<3;i++) {
			String sizeLocator = "//select[@name='prvw_selector_1_"+i+"_Size']";
			String colorLocator = "//select[@name='prvw_selector_1_"+i+"_Color']";
			Select sizeSelect =new Select(driver.findElement(By.xpath(sizeLocator)));
			actualSizeElements= sizeSelect.getOptions();
			for(WebElement option: actualSizeElements) {
				actualSizeOptions.add(option.getText());
			}
			soft.assertEquals(actualSizeOptions, expectedSizeOptions);
			actualSizeElements.clear();
			actualSizeOptions.clear();

			Select colorSelect =new Select(driver.findElement(By.xpath(colorLocator)));
			actualColorElements= colorSelect.getOptions();
			for(WebElement option: actualColorElements) {
				actualColorOptions.add(option.getText());
			}
			soft.assertEquals(actualColorOptions, expectedColorOptions);
			actualColorElements.clear();
			actualColorOptions.clear();
		}


		WebElement addToCartButton =wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@id,'ProductSubmitButton')]")));
		addToCartButton.click();
		WebElement viewCart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@id,'cart-notification-button')]")));
		viewCart.click();
		test.log(Status.INFO, "Navigated to Cart View");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		WebElement sizeCheck = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Carrying Pets Bag')]//following::div[@class='product-option'][2]")));
		soft.assertEquals(sizeCheck.getText(), "Size: S");
		test.log(Status.PASS, "Verified Size S in Cart View");
		WebElement colorCheck = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Carrying Pets Bag')]//following::div[@class='product-option'][3]")));
		soft.assertEquals(colorCheck.getText(), "Color: Black");
		test.log(Status.PASS, "Verified Color Black in Cart View");
		WebElement finalPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Carrying Pets Bag')]//following::span[@class='price price--end'][2]")));
		test.log(Status.INFO, finalPrice.getText());
		soft.assertTrue(finalPrice.getText()=="Rs. 1,221.41");

	}
	//
	/*
	 * */


	@Test(priority=2)
	public void verifyNonOfferProduct() throws InterruptedException {
		test=extent.createTest("verifyNonOfferProduct","verifyNonOfferProduct");

		WebElement continueShopping = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Continue shopping']")));
		continueShopping.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
		soft.assertEquals(driver.getTitle(), "Products – TestAssignmentStore");
		WebElement regularProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='CardLink-template--23824074047785__main-collection-product-grid-9583948333353']")));
		regularProduct.click();
		String textToVerify="Pair";
		soft.assertFalse(driver.getPageSource().contains(textToVerify));
	}

	@AfterMethod (alwaysRun=true)
	 public void AfterMethod() {
		extent.flush();
	}
	@AfterSuite
	public void tearDown(ITestResult result) {
		driver.quit();
		soft.assertAll();
		if(result.getStatus()==ITestResult.SUCCESS)
		{
			String ScreenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getName());
			test.log(Status.PASS,"Test Passed");
			test.addScreenCaptureFromPath(ScreenshotPath);}
		else {
			String ScreenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getName());
			test.log(Status.FAIL, result.getThrowable());
			test.addScreenCaptureFromPath(ScreenshotPath);
		}

	}

}
