package com.flink.assignment;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.flink.assignment.pageObjects.CartPage;
import com.flink.assignment.pageObjects.HomePage;
import com.flink.assignment.pageObjects.ProductsPage;
import io.github.bonigarcia.wdm.WebDriverManager;

public class FlinkTest {
	private WebDriver driver;
	private HomePage homePage;
	private ProductsPage productsPage;
	private CartPage cartPage;

	@Parameters({"browserName", "url"})
	@Test
	public void Test1(@Optional("chrome") String browserName, String url) throws InterruptedException {
		/*Driver Script*/
		switch (browserName.toLowerCase()) {
		case "chrome":
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;
		case "edge":
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;
		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			break;
		default:
			System.out.println("Browsername is invalid");
			break;
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
		driver.get(url);
		/*instance creation for page object files*/
		homePage = new HomePage(driver);
		cartPage = new CartPage(driver);
		productsPage = new ProductsPage(driver);

		/*CurrentTemeparature Screen script*/
		Assert.assertEquals(homePage.getCurrentTempHeaderLabel(), "Current temperature");
		String productType = "";
		String[] products = null;
		if(homePage.getCurrrentTemp()<19) {
			productType = "Moisturizers";
			products = new String[]{"Aloe","Almond"};
		}
		else if(homePage.getCurrrentTemp()>34) {
			productType = "Sunscreens";
			products = new String[]{"SPF-50","SPF-30"};
		}

		homePage.clickBuyButton(productType);
		/*Adding product to cart script*/
		Assert.assertEquals(productsPage.getproductTypeHeaderLabel(), productType);
		Map<String, Integer> lowPricedProductDetails = productsPage.addLowPricedProductsToCart(products);
		productsPage.clickCartButton();
		/*Payment Screen script*/
		Assert.assertEquals(cartPage.getCheckoutHeaderLabel(), "Checkout");
		cartPage.verifyRecentlyAddedProducts(lowPricedProductDetails);

		cartPage.clickPayWithCardButton();
		driver.manage().timeouts().pageLoadTimeout(140, TimeUnit.SECONDS);
		cartPage.fillPaymentDetails();
		/*payment success screen script*/
		driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
		Assert.assertEquals(cartPage.verifyPaymentSuccessLabel(), "PAYMENT SUCCESS");
		Assert.assertEquals(cartPage.verifyPaymentSuccessMessage(), "Your payment was successful. You should receive a follow-up call from our sales team.");
		driver.quit();
	}
}
