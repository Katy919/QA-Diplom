package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentTest {

    @AfterAll
    static void tearAll() {
        SelenideLogger.removeListener("allure");
        DataHelper.cleanDatabase();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        System.setProperty("db", "mysql");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
    }

    @Test
    void shouldPayWithValidCard() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var validCard = DataHelper.getApprovedCard();

        paymentPage.fillCardData(validCard);
        paymentPage.submit();

        paymentPage.shouldShowSuccessMessage();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    void shouldApprovePaymentWithDoubleNameHolder() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.withHolder("Екатерина-Ольга Голубева");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowSuccessMessage();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    void shouldApprovePaymentWithYoInHolderName() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.withHolder("Алёна Голубева");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowSuccessMessage();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    void shouldApprovePaymentWithIInHolderName() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.withHolder("Виталий Голубев");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowSuccessMessage();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    void shouldApprovePaymentWithLatinHolderName() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.withHolder("EKATERINA GOLUBEVA");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowSuccessMessage();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    void shouldShowErrorIfCardNumberIsEmpty() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.withNumber("");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowCardNumberError();
    }

    @Test
    void shouldShowErrorIfCardNumberIsShort() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.withNumber("4444 4444 4444");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowCardNumberError();
    }

    @Test
    void shouldShowErrorIfCardIsExpired() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.withYear("25");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowYearErrorExpired();
    }

    @Test
    void shouldShowErrorIfYearIsEmpty() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.withYear("");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowYearErrorInvalidFormat();
    }

    @Test
    void shouldShowErrorIfYearIsTooFarInFuture() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.withYear("32");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowYearErrorInvalidRange();
    }

    @Test
    void shouldShowErrorIfYearHasOneDigit() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.withYear("3");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowYearErrorInvalidFormat();
    }

    @Test
    void shouldShowErrorIfMonthIsEmpty() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.withMonth("");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowMonthError();
    }

    @Test
    void shouldShowErrorIfMonthHasOneDigit() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.withMonth("1");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowMonthError();
    }

    @Test
    void shouldShowErrorIfMonthIsZero() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.withMonth("00");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowMonthErrorInvalidRange();
    }

    @Test
    void shouldShowErrorIfMonthIsThirteen() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.withMonth("13");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowMonthErrorInvalidRange();
    }

    @Test
    void shouldShowErrorIfCvcIsEmpty() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();
        var card = DataHelper.withCvc("");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowCvcError();
    }

    @Test
    void shouldShowErrorIfCvcIsOneDigit() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();
        var card = DataHelper.withCvc("1");
        paymentPage.fillCardData(card);
        paymentPage.submit();
        paymentPage.shouldShowCvcError();
    }

    @Test
    void shouldShowErrorIfHolderIsEmpty() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.withHolder("");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowHolderRequiredField();
    }

    @Test
    void shouldShowErrorIfHolderHasNumbers() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.withHolder("111111");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowHolderInvalidFormat();
    }

    @Test
    void shouldShowErrorIfHolderHasSpecialCharacters() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.withHolder("-+?*;№;№");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowHolderInvalidFormat();
    }

    @Test
    void shouldShowErrorIfHolderIsOneChar() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.withHolder("А");
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowHolderInvalidFormat();
    }

    @Test
    void shouldDeclinePaymentWithDeclinedCard() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.buy();

        var card = DataHelper.getDeclinedCard();
        paymentPage.fillCardData(card);
        paymentPage.submit();

        paymentPage.shouldShowDeclinedMessage();
        assertEquals("DECLINED", SQLHelper.getPaymentStatus());
    }
}
