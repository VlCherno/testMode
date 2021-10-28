package test_mode;

import model.UserRegistrationData;
import model.UserRegistrationDataGenerator;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRegistrationTest {

    @Test
    @Order(0)
    @DisplayName("Вход в личный кабинет")
    void rightSignInPositive() {
        UserRegistrationData user = UserRegistrationDataGenerator.generateValidActive();
        open("http://localhost:9999");
        $(By.xpath("//*[@class='input__control' and @name='login']")).setValue(user.getLogin());
        $(By.xpath("//*[@class='input__control' and @name='password']")).setValue(user.getPassword());
        $(By.xpath("//*[@role='button']")).click();
        $(By.xpath("//*[@class='heading heading_size_l heading_theme_alfa-on-white']")).shouldHave(text("Личный кабинет"));
    }


    @Test
    @Order(1)
    @DisplayName("Вход с неверным логином")
    void wrongLoginNegative() {
        UserRegistrationData user = UserRegistrationDataGenerator.generateInvalidLogin();
        open("http://localhost:9999");
        $(By.xpath("//*[@class='input__control' and @name='login']")).setValue(user.getLogin());
        $(By.xpath("//*[@class='input__control' and @name='password']")).setValue(user.getPassword());
        $(By.xpath("//*[@role='button']")).click();
        $(By.xpath("//*[@data-test-id='error-notification']")).shouldHave(text("Ошибка!"))
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    @Order(2)
    @DisplayName("Вход с неверным паролем")
    void wrongPasswordNegative() {
        UserRegistrationData user = UserRegistrationDataGenerator.generateInvalidPassword();
        open("http://localhost:9999");
        $(By.xpath("//*[@class='input__control' and @name='login']")).setValue(user.getLogin());
        $(By.xpath("//*[@class='input__control' and @name='password']")).setValue(user.getPassword());
        $(By.xpath("//*[@role='button']")).click();
        $(By.xpath("//*[@data-test-id='error-notification']")).shouldHave(text("Ошибка!"))
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    @Order(3)
    @DisplayName("Вход без пароля")
    void noPasswordNegative() {
        UserRegistrationData user = UserRegistrationDataGenerator.generateValidActive();
        open("http://localhost:9999");
        $(By.xpath("//*[@class='input__control' and @name='login']")).setValue(user.getLogin());
        $(By.xpath("//*[@role='button']")).click();
        $("[data-test-id=password]").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    @Order(4)
    @DisplayName("Пользователь заблокирован")
    void blockedUserSignInNegative() {
        UserRegistrationData user = UserRegistrationDataGenerator.generateValidBlocked();
        open("http://localhost:9999");
        $(By.xpath("//*[@class='input__control' and @name='login']")).setValue(user.getLogin());
        $(By.xpath("//*[@class='input__control' and @name='password']")).setValue(user.getPassword());
        $(By.xpath("//*[@role='button']")).click();
        $("[data-test-id=error-notification]").shouldHave(text("Пользователь заблокирован"));
    }
}