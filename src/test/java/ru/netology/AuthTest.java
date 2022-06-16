package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import static com.codeborne.selenide.Configuration.holdBrowserOpen;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.DataGenerator.Registration.getUser;
import static ru.netology.DataGenerator.getRandomLogin;
import static ru.netology.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        holdBrowserOpen = true;
        var registeredUser = getUser("active");
        DataGenerator.sendRequest(registeredUser);
        $x("//input[@name='login']").val(registeredUser.getLogin());
        $x("//input[@name='password']").val(registeredUser.getPassword());
        $(withText("Продолжить")).click();
        $x("//*[@class='App_appContainer__3jRx1']").shouldHave(Condition.text("Личный кабинет"));
        // TODO: добавить логику теста, в рамках которого будет выполнена попытка входа в личный кабинет с учётными
        //  данными зарегистрированного активного пользователя, для заполнения полей формы используйте
        //  пользователя registeredUser
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $x("//input[@name='login']").val(notRegisteredUser.getLogin());
        $x("//input[@name='password']").val(notRegisteredUser.getPassword());
        $(withText("Продолжить")).click();
        $x("//*[@class='App_appContainer__3jRx1']").shouldHave(Condition.text("Личный кабинет"));
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет
        //  незарегистрированного пользователя, для заполнения полей формы используйте пользователя notRegisteredUser
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        holdBrowserOpen = true;
        var blockedUser = getRegisteredUser("blocked");
        DataGenerator.sendRequest(blockedUser);
        $x("//input[@name='login']").val(blockedUser.getLogin());
        $x("//input[@name='password']").val(blockedUser.getPassword());
        $(withText("Продолжить")).click();
        $x("//*[@class='notification__content']").shouldHave(Condition.text("Пользователь заблокирован"));
    }
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет,
        //  заблокированного пользователя, для заполнения полей формы используйте пользователя blockedUser


    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        holdBrowserOpen = true;
        var registeredUser = getRegisteredUser("active");
        DataGenerator.sendRequest(registeredUser);
        var wrongLogin = getRandomLogin();
        $x("//input[@name='login']").val(wrongLogin);
        $x("//input[@name='password']").val(registeredUser.getPassword());
        $(withText("Продолжить")).click();
        $x("//*[@class='notification__content']").shouldHave(Condition.text("Неверно указан логин или пароль"));
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет с неверным
        //  логином, для заполнения поля формы "Логин" используйте переменную wrongLogin,
        //  "Пароль" - пользователя registeredUser
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        holdBrowserOpen = true;
        var registeredUser = getRegisteredUser("active");
        DataGenerator.sendRequest(registeredUser);
        var wrongPassword = getRandomPassword();
        $x("//input[@name='login']").val(registeredUser.getLogin());
        $x("//input[@name='password']").val(wrongPassword);
        $(withText("Продолжить")).click();
        $x("//*[@class='notification__content']").shouldHave(Condition.text("Неверно указан логин или пароль"));
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет с неверным
        //  паролем, для заполнения поля формы "Логин" используйте пользователя registeredUser,
        //  "Пароль" - переменную wrongPassword
    }
}