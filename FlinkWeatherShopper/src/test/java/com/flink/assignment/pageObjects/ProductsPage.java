package com.flink.assignment.pageObjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProductsPage {
	private WebDriver driver;
	private By cartBtn = By.xpath("//span[@id='cart']/parent::button");
	private By productTypeHeaderLabel = By.tagName("h2");

	public ProductsPage(WebDriver driver) {
		this.driver = driver;
	}

	public String getproductTypeHeaderLabel() {
		return driver.findElement(productTypeHeaderLabel).getText();
	}

	public void clickCartButton() {
		driver.findElement(cartBtn).click();
	}

	public Map<String, Integer> addLowPricedProductsToCart(String[] productNames) {
		Map<String, Integer> map = new HashMap<String, Integer>();

		for (String productName : productNames) {
			int leastPrice = 0;
			WebElement leastPricedProduct = null;
			List<WebElement> products = driver.findElements(By.xpath("//p[contains(text(),'"+productName+"') or contains(text(),'"+productName.toLowerCase()+"')]/following-sibling::p"));
			for (int i=0; i<products.size();i++) {
				WebElement product = products.get(i);
				String[] tempPrice = product.getText().split(" ");
				int price = Integer.valueOf(tempPrice[tempPrice.length-1]);
				if(i == 0) {
					leastPrice = price;
					leastPricedProduct = product.findElement(By.xpath("./preceding-sibling::p[1]"));
				}
				else if(leastPrice > price) {
					leastPrice = price;
					leastPricedProduct = product.findElement(By.xpath("./preceding-sibling::p[1]"));
				}
			}
			leastPricedProduct.findElement(By.xpath("./following-sibling::button[1]")).click();
			map.put(leastPricedProduct.getText(), leastPrice);
		}

		return map;
	}
}
