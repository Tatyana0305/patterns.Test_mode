import com.codeborne.selenide.Condition;
import domain.DataGenerator;
import domain.RegistrationInfo;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static domain.DataGenerator.Registration.getRandomLogin;
import static domain.DataGenerator.Registration.getRandomPassword;


public class AuthTest {


    @Test
    void shouldLoginActiveUser() {
        //Активный пользователь
        com.codeborne.selenide.Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        RegistrationInfo user = DataGenerator.Registration.getUser("active");
        $x("//input[@type='text']").val(user.getLogin());
        $x("//input[@type='password']").val(user.getPassword());
        $x("//span[@class='button__text']").click();
        $x("//*[contains(text(), 'Личный кабинет')]").should(com.codeborne.selenide.Condition.visible, Duration.ofSeconds(10));
    }

    @Test
    void shouldLoginBlockedUser() {
        //Заблокированный пользователь
        com.codeborne.selenide.Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        $x("//input[@type='text']").val(user.getLogin());
        $x("//input[@type='password']").val(user.getPassword());
        $x("//span[@class='button__text']").click();
        $x("//div[@class='notification__content']").should(com.codeborne.selenide.Condition.visible, Duration.ofSeconds(10));


    }

    @Test
    void shouldWrongLogin() {
        com.codeborne.selenide.Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        $x("//input[@type='text']").val(getRandomLogin()); //Введен рандомный логин
        $x("//input[@type='password']").val(user.getPassword());
        $x("//span[@class='button__text']").click();
        $x("//div[@class='notification__content']")
                .shouldHave(com.codeborne.selenide.Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));


    }

    @Test
    void shouldWrongPassword() {
        com.codeborne.selenide.Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        $x("//input[@type='text']").val(user.getLogin());
        $x("//input[@type='password']").val(getRandomPassword());  //Введен рандомный паспорт
        $x("//span[@class='button__text']").click();
        $x("//div[@class='notification__content']")
                .shouldHave(com.codeborne.selenide.Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));


    }

    @Test
    void shouldNoPassword() {
        com.codeborne.selenide.Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        $x("//input[@type='text']").val(user.getLogin());
        // воод паспорта отсутствует
        $x("//span[@class='button__text']").click();
        $("[data-test-id='password'].input_invalid .input__sub")
                .shouldHave(com.codeborne.selenide.Condition.text("Поле обязательно для заполнения"), Duration.ofSeconds(10));


    }

    @Test
    void shouldNoLogin() {
        com.codeborne.selenide.Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        //Ввод логина отсутствует
        $x("//input[@type='password']").val(user.getPassword());
        $x("//span[@class='button__text']").click();
        $("[data-test-id='login'].input_invalid .input__sub")
                .shouldHave(Condition.text("Поле обязательно для заполнения"), Duration.ofSeconds(10));
    }
}