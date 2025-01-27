import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebTest {


    @Test
    public void testSearchField() {

        WebDriverManager.chromedriver().setup(); // Автоматичне завантаження ChromeDriver
        WebDriver driver = new ChromeDriver();

        try {
            // Відкрити веб-сайт
            driver.get("https://rozetka.com.ua/");

            // Максимізувати вікно браузера
            driver.manage().window().maximize();

            // Знайти пошукове поле за допомогою не прямого XPath (використання функції contains)
            WebElement searchField = driver.findElement(By.xpath("//input[@type='text' and @placeholder='Я шукаю...']"));

            // Введення даних у поле
            String searchText = "Ноутбук";
            searchField.sendKeys(searchText);

            // Перевірка, що дані введені в поле
            Assert.assertEquals("Дані у полі не відповідають очікуваним", searchText, searchField.getAttribute("value"));

            // Клік по кнопці пошуку (використання унікального ідентифікатора)
            WebElement searchButton = driver.findElement(By.cssSelector("button.search-form__submit"));
            searchButton.click();

            // Очікування результатів пошуку
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            WebElement searchResults = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                    "/html/body/rz-app-root/div/div/rz-category/div/main/rz-catalog/div/rz-catalog-settings/div/rz-selected-filters/div/p")));

            // Перевірка наявності результатів пошуку
            Assert.assertTrue("Результати пошуку не відображаються", searchResults.isDisplayed());

        } finally {
            // Закрити браузер
            driver.quit();
        }
    }
}
