package com.flink.assignment.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
	private WebDriver driver;
	private By temperatureLabel = By.id("temperature");
	private By temperatureHeaderLabel = By.tagName("h2");

	public HomePage(WebDriver driver) {
		this.driver = driver;
	}

	public void clickBuyButton(String productType) {
		driver.findElement(By.xpath("//button[text()='Buy "+productType.toLowerCase()+"']")).click();
	}

	public int getCurrrentTemp() {
		String currentTemp = driver.findElement(temperatureLabel).getText();
		return Integer.valueOf(currentTemp.split(" ")[0]);
	}

	public String getCurrentTempHeaderLabel() {
		return driver.findElement(temperatureHeaderLabel).getText();
	}
}
