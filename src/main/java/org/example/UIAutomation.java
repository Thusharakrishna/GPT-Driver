package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UIAutomation {
    private WebDriver driver;

    @BeforeClass
    public void setup() {
        // Set up WebDriver
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test(priority = 1)
    public void testStandardUser() {
        openURL("https://www.saucedemo.com");
        enterCredentials("standard_user", "secret_sauce");
        clickOnLogin();
        openProduct("Sauce Labs Backpack");
        addToCart();
        clickOnCart();
        checkout();
        enterShippingDetails("Kaishu", "Thushu", "400001");
        clickOnContinue();
        clickOnFinish();
        assertThankYouMessageDisplayed();
        sleep(2000); // 2-second delay
    }

    @Test(priority = 2)
    public void testProblemUser() {
        openURL("https://www.saucedemo.com");
        enterCredentials("problem_user", "secret_sauce");
        clickOnLogin();
        openProduct("Sauce Labs Backpack");
        addToCart();
        clickOnCart();
        checkout();
        enterShippingDetails("Kaishu", "Thushu", "400001");
        clickOnContinue();
        clickOnFinish();
        assertThankYouMessageDisplayed();
        sleep(2000); // 2-second delay
    }

    private void openURL(String url) {
        driver.get(url);
        sleep(2000);
    }

    private void enterCredentials(String username, String password) {
        WebElement usernameField = waitForElement(By.id("user-name"));
        usernameField.sendKeys(username);

        WebElement passwordField = waitForElement(By.id("password"));
        passwordField.sendKeys(password);
        sleep(2000);
    }

    private void clickOnLogin() {
        WebElement loginButton = waitForElement(By.id("login-button"));
        loginButton.click();
        sleep(2000);
    }

    private void openProduct(String productName) {
        WebElement productLink = waitForElement(By.linkText(productName));
        productLink.click();
        sleep(2000);
    }

    private void addToCart() {
        WebElement addToCartButton = waitForElement(By.xpath("//button[text()='Add to cart']"));
        addToCartButton.click();
        sleep(2000);
    }

    private void clickOnCart() {
        WebElement cartIcon = waitForElement(By.className("shopping_cart_link"));
        cartIcon.click();
        sleep(2000);
    }

    private void checkout() {
        WebElement checkoutButton = waitForElement(By.id("checkout"));
        checkoutButton.click();
        sleep(2000);
    }

    private void enterShippingDetails(String firstName, String lastName, String zip) {
        WebElement firstNameField = waitForElement(By.id("first-name"));
        firstNameField.sendKeys(firstName);

        WebElement lastNameField = waitForElement(By.id("last-name"));
        lastNameField.sendKeys(lastName);

        WebElement zipField = waitForElement(By.id("postal-code"));
        zipField.sendKeys(zip);
        sleep(2000);
    }

    private void clickOnContinue() {
        WebElement continueButton = waitForElement(By.id("continue"));
        continueButton.click();
        sleep(2000);
    }

    private void clickOnFinish() {
        WebElement finishButton = waitForElement(By.id("finish"));
        finishButton.click();
        sleep(2000);
    }

    private void assertThankYouMessageDisplayed() {
        WebElement thankYouMessage = waitForElement(By.xpath("//h2[text()='Thank you for your order!']"));
        Assert.assertTrue(thankYouMessage.isDisplayed(), "Thank you message is not displayed.");
        sleep(2000);
    }

    private WebElement waitForElement(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // Handle the exception if needed
        }
    }

    @AfterClass
    public void teardown() {
        driver.quit();
    }
}
