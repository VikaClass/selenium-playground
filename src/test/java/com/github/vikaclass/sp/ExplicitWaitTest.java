package com.github.vikaclass.sp;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class ExplicitWaitTest {
    WebDriver driver;

    @Before
    public void before() {
        System.setProperty("webdriver.chrome.driver", "C:\\drivers\\chromedriver110\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    @Test
    @Ignore
    public void dataShouldBeCalculatedAfter15Seconds() {
        driver.get("http://uitestingplayground.com/clientdelay");
        WebElement button = driver.findElement(By.id("ajaxButton"));
        button.click();
        try {
            Thread.sleep(15500); // один из способ но лучше так не делать!
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebElement dataCalculated = driver.findElement(By.xpath("//*[contains(text(), 'Data calculated on the client side.')]"));
        Assert.assertTrue(dataCalculated.isDisplayed());
    }

    @Test
    @Ignore
    public void dataShouldBeCalculatedAfter15Seconds2() {
        driver.get("http://uitestingplayground.com/clientdelay");
        WebElement button = driver.findElement(By.id("ajaxButton"));
        button.click();

        //явное ожидание
        WebDriverWait wait = new WebDriverWait(driver, 15);
        By dataCalculatedSelector = By.xpath("//*[contains(text(), 'Data calculated on the client side.')]");
        ExpectedCondition<WebElement> isVisible = ExpectedConditions.visibilityOfElementLocated(dataCalculatedSelector);
        wait.until(isVisible);

        WebElement dataCalculated = driver.findElement(dataCalculatedSelector);
        Assert.assertTrue(dataCalculated.isDisplayed());
    }

    @Test
    public void dataShouldBeCalculatedAfter15Seconds3() {
        driver.get("http://uitestingplayground.com/clientdelay");
        WebElement button = driver.findElement(By.id("ajaxButton"));
        button.click();

        //гибкое ожидание
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver);
        fluentWait.withTimeout(Duration.ofSeconds(15));
        fluentWait.pollingEvery(Duration.ofSeconds(5));
        fluentWait.ignoring(NoSuchElementException.class);

        WebElement dataCalcElement = fluentWait.until(webDriver ->{
            WebElement tmp =
                webDriver.findElement(By.xpath("//*[contains(text(), 'Data calculated on the client side.')]"));
            if(tmp.isDisplayed()){
                return tmp;
            }
            return null;
        });


        Assert.assertTrue(dataCalcElement.isDisplayed());
    }

    @After
    public void after() {
        driver.quit();
    }

}
