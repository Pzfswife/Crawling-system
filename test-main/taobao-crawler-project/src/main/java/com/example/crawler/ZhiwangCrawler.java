package com.example.crawler;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;
import com.example.crawler.PageParser;

public class ZhiwangCrawler {
    private static WebDriver driver;
    private static WebDriverWait wait;

    public static void main(String[] args) {
        // 设置 ChromeDriver 路径，根据实际情况修改
        System.setProperty("webdriver.chrome.driver", "D:\\Python\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        try {
            String keyword = "Python";
            searcher(keyword);
            // 先点击一次下一页按钮，跳过已经加载好的第一页
            WebElement firstNext = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#Page_next_top")));
            firstNext.click();
            for (int i = 0; i < 6; i++) {
                WebElement paresNext = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#Page_next_top")));
                paresNext.click();
                String html = driver.getPageSource();
                PageParser.parsePage(html);
                TimeUnit.SECONDS.sleep(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.close();
            }
            RedisUtils.close();
        }
    }

    public static void searcher(String keyword) {
        driver.get("https://www.cnki.net/");
        driver.manage().window().maximize();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("txt_SearchText")));
        input.sendKeys(keyword);
        WebElement searchButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("search-btn")));
        searchButton.click();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement sortIcon = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[class=\"icon icon-sort\"]")));
        sortIcon.click();
        WebElement pageCount = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".page-show-count ul li"))).get(0);
        pageCount.click();
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".result-table-list tbody tr")));
    }
}
