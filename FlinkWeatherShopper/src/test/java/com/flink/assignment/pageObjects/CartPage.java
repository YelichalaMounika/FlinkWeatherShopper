package com.flink.assignment.pageObjects;

import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

public class CartPage {
	private WebDriver driver;
	private By checkOutHeaderLabel = By.tagName("h2");
	private By payWithCardButton = By.xpath("//button[@type='submit']");
	private By emailTextBox = By.id("email");
	private By cardNumber = By.id("card_number");
	private By expDateBox = By.id("cc-exp");
	private By cvcTextBox = By.id("cc-csc");
	private By payButton = By.id("submitButton");
	private By location = By.id("billing-zip");
	private By paymentSuccessLabel = By.tagName("h2");

	public CartPage(WebDriver driver) {
		this.driver = driver;
	}

	public String getCheckoutHeaderLabel() {
		return driver.findElement(checkOutHeaderLabel).getText();
	}

	public void verifyRecentlyAddedProducts(Map<String, Integer> productDetails) {
		int totalPrice =0;
		for (String productName : productDetails.keySet()) {
			int price = productDetails.get(productName);
			Assert.assertTrue(driver.findElement(By.xpath("//td[text()='"+productName+"']")).isDisplayed());
			Assert.assertTrue(driver.findElement(By.xpath("//td[text()='"+productName+"']/following-sibling::td[text()="+price+"]")).isDisplayed());
			totalPrice = price+totalPrice;
		}
		String totalPriceString = driver.findElement(By.id("total")).getText();
		String[] tempTotalPrice = totalPriceString.split(" ");
		int totalPriceApp = Integer.valueOf(tempTotalPrice[tempTotalPrice.length-1]);
		Assert.assertTrue(totalPriceApp ==totalPrice);
	}

	public void clickPayWithCardButton() {
		driver.findElement(payWithCardButton).click();
	}

	public void fillPaymentDetails() throws InterruptedException {
		driver.switchTo().frame("stripe_checkout_app");
		WebElement email = driver.findElement(emailTextBox);
		WebElement cardNum = driver.findElement(cardNumber);
		WebElement expDate = driver.findElement(expDateBox);
		WebElement cvv =  driver.findElement(cvcTextBox);
		WebElement pay = driver.findElement(payButton);
		WebElement locationZipCode = driver.findElement(location);
		Actions action = new Actions(driver);
		Thread.sleep(5000);
		action.sendKeys(email, "abc@gmail.com").build().perform();
		action.sendKeys(cardNum, "4242 4242 4242 4242").build().perform();
		action.sendKeys(expDate, "12/22").build().perform();
		action.sendKeys(cvv, "877").build().perform();
		if(locationZipCode.isDisplayed()) {
			action.sendKeys(locationZipCode, "520001").build().perform();
		}
		pay.click();
		driver.switchTo().defaultContent();
	}
	public String verifyPaymentSuccessLabel() throws InterruptedException {
		Thread.sleep(5000);
		return driver.findElement(paymentSuccessLabel).getText();
	}
	public String verifyPaymentSuccessMessage() {
		return driver.findElement(By.xpath("//p[contains(text(),'Your payment was successful')]")).getText();
	}
}
