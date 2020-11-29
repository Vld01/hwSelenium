import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class RgsDmsTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void before() {
        System.setProperty("webdriver.chrome.driver", "webdriver/chromedriver.exe");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

        driver = new ChromeDriver(chromeOptions);
        System.out.println("Экзмепляр драйвера создан");

        driver.manage().window().maximize();
        System.out.println("Максимальный размер экрана задан");

        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wait = new WebDriverWait(driver, 10, 1000);

        String baseUrl = "http://www.rgs.ru";

        //1. Переход по ссылке http://www.rgs.ru
        System.out.println("Переход по ссылке http://www.rgs.ru");
        driver.get(baseUrl);
        System.out.println("Переход по ссылке http://www.rgs.ru осуществлен");
    }

    @Test
    public void voluntaryMedicalInsuranceRGS() {

        //2. Выбор меню
        System.out.println("Открытие Меню");

        String menuButtonXPath = "//a[contains(text(),'Меню') and @class='hidden-xs']";
        WebElement menuButton = driver.findElement(By.xpath(menuButtonXPath));
        waitUtilElementToBeClickable(menuButton);
        menuButton.click();

        //3. Выбор категории ДМС
        System.out.println("Выбор категории ДМС");

        String voluntaryMedicalInsuranceButtonXPath = "//a[contains(text(),'ДМС')]";
        WebElement voluntaryMedicalInsuranceButton = driver.findElement(By.xpath(voluntaryMedicalInsuranceButtonXPath));
        waitUtilElementToBeClickable(voluntaryMedicalInsuranceButton);
        voluntaryMedicalInsuranceButton.click();

        //4. Проверка актуальности заголовка ДМС страницы
        System.out.println("Проверка актуальности заголовка ДМС страницы");

        String voluntaryMedicalInsuranceTitleXPath4 = "//title[contains(text(),'ДМС')]";
        WebElement voluntaryMedicalInsuranceTitle = driver.findElement(By.xpath(voluntaryMedicalInsuranceTitleXPath4));
        Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
                "ДМС 2020 | Рассчитать стоимость добровольного медицинского страхования и оформить ДМС в Росгосстрах", driver.getTitle());

        //5. Нажатие кнопки "Отправить заявку"
        System.out.println("Нажатие кнопки \"Отправить заявку\"");

        String checkoutSendRequestXPath = "//a[contains(text(),'Отправить заявку')]";
        WebElement checkoutSendRequestButton = driver.findElement(By.xpath(checkoutSendRequestXPath));
        waitUtilElementToBeClickable(checkoutSendRequestButton);
        checkoutSendRequestButton.click();

        //6.Проверка открытия всплывающего окна заявки
        System.out.println("Проверка открытия всплывающего окна заявки");

        String rgsOptionsTitleXPath = "//h4[@class='modal-title']/b";
        WebElement rgsOptionsTitle = driver.findElement(By.xpath(rgsOptionsTitleXPath));
        waitUtilElementToBeClickable(rgsOptionsTitle);
        scrollToElementJs(rgsOptionsTitle);
        Assert.assertEquals("Заголовок всплывающего окна отсутствует/не соответствует требуемому",
                "Заявка на добровольное медицинское страхование", rgsOptionsTitle.getText());

        //7. Заполнение полей данными
        System.out.println("Заполнение полей данными");

        String fieldXPath = "//input[@name='%s']";
        String fieldXPath2 = "//input[contains(@data-bind,'%s')]";
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "LastName"))), "Иванов");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "FirstName"))), "Степан");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "MiddleName"))), "Степанович");
        driver.findElement(By.xpath(String.format(fieldXPath2, "Phone"))).click();
        driver.findElement(By.xpath(String.format(fieldXPath2, "Phone"))).sendKeys("9999999999");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath2, "Email"))), "qwertyqwerty");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath2, "ContactDate"))), "01.01.2021");
        fillInputField(driver.findElement(By.xpath("//textarea[@name='Comment']")), "Я согласен на обработку");

        //Нажатие кнопки "Москва"
        String checkoutCityXPath = "//option[@value='77']";
        WebElement checkoutCityButton = driver.findElement(By.xpath(checkoutCityXPath));
        scrollToElementJs(checkoutCityButton);
        waitUtilElementToBeClickable(checkoutCityButton);
        checkoutCityButton.click();

        //Нажатие кнопки "Согласен на обработку моих персональных данных"
        String checkoutCityXPath2 = "//input[@type='checkbox']";
        WebElement checkoutCityButton2 = driver.findElement(By.xpath(checkoutCityXPath2));
        checkoutCityButton2.click();

        //8.Проверка, что все поля заполнены введенными значениями
        System.out.println("Проверка, что все поля заполнены введенными значениями");

        String actualLastName = driver.findElement(By.xpath(String.format(fieldXPath, "LastName"))).getAttribute("value");
        Assert.assertEquals("Фамилия не совпадает", "Иванов", actualLastName);
        String actualFirstName = driver.findElement(By.xpath(String.format(fieldXPath, "FirstName"))).getAttribute("value");
        Assert.assertEquals("Имя не совпадает", "Степан", actualFirstName);
        String actualMiddleName = driver.findElement(By.xpath(String.format(fieldXPath, "MiddleName"))).getAttribute("value");
        Assert.assertEquals("Отчество не совпадает", "Степанович", actualMiddleName);
        String actualPhone = driver.findElement(By.xpath(String.format(fieldXPath2, "Phone"))).getAttribute("value");
        Assert.assertEquals("Телефон не совпадает", "+7 (999) 999-99-99", actualPhone);
        String actualEmail = driver.findElement(By.xpath(String.format(fieldXPath2, "Email"))).getAttribute("value");
        Assert.assertEquals("Почта не совпадает", "qwertyqwerty", actualEmail);
        String actualContactDate = driver.findElement(By.xpath(String.format(fieldXPath2, "ContactDate"))).getAttribute("value");
        Assert.assertEquals("Дата для связи не совпадает", "01.01.2021", actualContactDate);
        String actualComment = driver.findElement(By.xpath("//textarea[@name='Comment']")).getAttribute("value");
        Assert.assertEquals("Комментарий не совпадает", "Я согласен на обработку", actualComment);
        String actualCity = driver.findElement(By.xpath("//select[@name='Region']")).getAttribute("value");
        Assert.assertEquals("Город не совпадает", "77", actualCity);
        String actualCheckBox = driver.findElement(By.xpath("//input[@type='checkbox']")).getAttribute("value");
        Assert.assertEquals("Не дано согласие на обработку данных", "on", actualCheckBox);


        //9.Кликнуть по кнопке "Отправить"
        System.out.println("Кликнуть по кнопке \"Отправить\"");

        String sendButtonXPath = "//button[contains(text(),'Отправить')]";
        WebElement sendButton = driver.findElement(By.xpath(sendButtonXPath));
        scrollToElementJs(sendButton);
        waitUtilElementToBeClickable(sendButton);
        sendButton.click();

        //10. Проверить, что у Поля - Эл. почта присутствует сообщение об ошибке
        System.out.println("Проверить, что у Поля - Эл. почта присутствует сообщение об ошибке");

        String errorAlertXPath = "//input[@name='Email']/..//span[@class='validation-error-text']";
        WebElement errorAlert = driver.findElement(By.xpath(errorAlertXPath));
        scrollToElementJs(errorAlert);
        waitUtilElementToBeVisible(errorAlert);
        Assert.assertEquals("Проверка ошибки у почты не была пройдена",
                "Введите адрес электронной почты", errorAlert.getText());
    }

    @After
    public void after(){
        System.out.println("Конец теста");
        driver.quit();
        System.out.println("Выход из драйвера");
    }

    private void scrollToElementJs(WebElement element) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private void waitUtilElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    private void waitUtilElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    private void fillInputField(WebElement element, String value) {
        scrollToElementJs(element);
        waitUtilElementToBeClickable(element);
        element.click();
        element.sendKeys(value);
        Assert.assertEquals("Поле было заполнено некорректно",
                value, element.getAttribute("value"));
    }
}