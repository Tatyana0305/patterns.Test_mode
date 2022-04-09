import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import domain.DataGenerator;
import domain.RegistrationInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static domain.DataGenerator.Registration.getRandomLogin;
import static domain.DataGenerator.Registration.getRandomPassword;


public class AuthTest {

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }


    @Test
    void shouldLoginActiveUser() {
        //Активный пользователь
        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("active");
        $x("//input[@type='text']").val(user.getLogin());
        $x("//input[@type='password']").val(user.getPassword());
        $x("//span[@class='button__text']").click();
        $x("//*[contains(text(), 'Личный кабинет')]").should(Condition.visible, Duration.ofSeconds(10));
    }


    @Test
    void shouldUserActiveLoginNot() {
        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("active");
        $("[data-test-id='password'] input").val(user.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='login'] .input__sub").shouldBe(Condition.visible).shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldUserActivePasswordNot() {
        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("active");
        $("[data-test-id='login'] input").val(user.getLogin());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='password'] .input__sub").shouldBe(Condition.visible).shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldLoginBlockedUser() {
        //Заблокированный пользователь
        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        $x("//input[@type='text']").val(user.getLogin());
        $x("//input[@type='password']").val(user.getPassword());
        $x("//span[@class='button__text']").click();
        $x("//div[@class='notification__content']").should(Condition.visible, Duration.ofSeconds(10));

    }

    @Test
    void shouldWrongLogin() {
        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        $x("//input[@type='text']").val(getRandomLogin()); //Введен рандомный логин
        $x("//input[@type='password']").val(user.getPassword());
        $x("//span[@class='button__text']").click();
        $x("//div[@class='notification__content']")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));

    }

    @Test
    void shouldWrongPassword() {
        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        $x("//input[@type='text']").val(user.getLogin());
        $x("//input[@type='password']").val(getRandomPassword());  //Введен рандомный паспорт
        $x("//span[@class='button__text']").click();
        $x("//div[@class='notification__content']")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));


    }

    @Test
    void shouldNoPassword() {

        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        $x("//input[@type='text']").val(user.getLogin());
        // воод паспорта отсутствует
        $x("//span[@class='button__text']").click();
        $("[data-test-id='password'].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"), Duration.ofSeconds(10));

    }

    @Test
    void shouldNoLogin() {

        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        //Ввод логина отсутствует
        $x("//input[@type='password']").val(user.getPassword());
        $x("//span[@class='button__text']").click();
        $("[data-test-id='login'].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"), Duration.ofSeconds(10));
    }
}
