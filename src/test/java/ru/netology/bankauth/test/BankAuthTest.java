package ru.netology.bankauth.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.netology.bankauth.data.DataHelper;
import ru.netology.bankauth.data.SQLHelper;
import ru.netology.bankauth.page.LoginPage;


import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.bankauth.data.SQLHelper.cleanAuthCode;
import static ru.netology.bankauth.data.SQLHelper.cleanDataBase;

public class BankAuthTest {
    LoginPage loginPage;

    @AfterEach
    void tearDown1() {
        cleanAuthCode();
    }

    @AfterAll
    static void tearDown2() {
        cleanDataBase();
    }

    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    void successLogin() {
        var authInfo = DataHelper.getAuthInfoWithTest();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verificationPage();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    void RandomUser() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.verifyErrorNotification("Ошибка! \nНеверно указан логин или пароль");
    }

    @Test
    void errorNotificationIfLoginWithExistUserAndRandomVerificationCode() {
        var authInfo = DataHelper.getAuthInfoWithTest();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verificationPage();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! \nНеверно указан код! Попробуйте ещё раз.");
    }
}
