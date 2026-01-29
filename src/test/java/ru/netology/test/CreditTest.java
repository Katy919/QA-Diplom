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

public class CreditTest {

    @AfterAll
    static void tearAll() {
        SelenideLogger.removeListener("allure");
        DataHelper.cleanDatabase();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
    }

    @Test
    void shouldPayCreditWithValidCard() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var validCard = DataHelper.getApprovedCard();

        creditPage.fillCardData(validCard);
        creditPage.submit();

        creditPage.shouldShowSuccessMessage();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
    }

    @Test
    void shouldApproveCreditWithDoubleNameHolder() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.withHolder("Екатерина-Ольга Голубева");
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowSuccessMessage();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
    }

    @Test
    void shouldApproveCreditWithYoInHolderName() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.withHolder("Алёна Голубева");
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowSuccessMessage();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
    }

    @Test
    void shouldApproveCreditWithIInHolderName() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.withHolder("Виталий Голубев");
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowSuccessMessage();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
    }

    @Test
    void shouldApproveCreditWithLatinHolderName() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.withHolder("EKATERINA GOLUBEVA");
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowSuccessMessage();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
    }

    @Test
    void shouldShowCreditErrorIfCardNumberIsEmpty() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.withNumber("");
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowCardNumberError();
    }

    @Test
    void shouldShowCreditErrorIfCardNumberIsShort() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.withNumber("4444 4444 4444");
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowCardNumberError();
    }

    @Test
    void shouldShowCreditErrorIfCardIsExpired() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.withYear("25");
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowYearErrorExpired();
    }

    @Test
    void shouldShowCreditErrorIfYearIsEmpty() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.withYear("");
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowYearErrorInvalidFormat();
    }

    @Test
    void shouldShowCreditErrorIfYearIsTooFarInFuture() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.withYear("32");
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowYearErrorInvalidRange();
    }

    @Test
    void shouldShowCreditErrorIfMonthIsEmpty() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.withMonth("");
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowMonthError();
    }

    @Test
    void shouldShowCreditErrorIfMonthIsZero() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.withMonth("00");
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowMonthErrorInvalidRange();
    }

    @Test
    void shouldShowCreditErrorIfMonthIsThirteen() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.withMonth("13");
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowMonthErrorInvalidRange();
    }

    @Test
    void shouldShowCreditErrorIfCvcIsEmpty() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.withCvc("");
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowCvcError();
    }

    @Test
    void shouldShowCreditErrorIfCvcIsOneDigit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.withCvc("1");
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowCvcError();
    }

    @Test
    void shouldShowCreditErrorIfHolderIsEmpty() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.withHolder("");
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowHolderRequiredField();
    }

    @Test
    void shouldShowCreditErrorIfHolderHasNumbers() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.withHolder("111111");
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowHolderInvalidFormat();
    }

    @Test
    void shouldShowCreditErrorIfHolderHasSpecialCharacters() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.withHolder("-+?*;№;№");
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowHolderInvalidFormat();
    }

    @Test
    void shouldShowCreditErrorIfHolderIsOneChar() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.withHolder("А");
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowHolderInvalidFormat();
    }

    @Test
    void shouldDeclineCreditPaymentWithDeclinedCard() {
        var mainPage = new MainPage();
        var creditPage = mainPage.buyOnCredit();

        var card = DataHelper.getDeclinedCard();
        creditPage.fillCardData(card);
        creditPage.submit();

        creditPage.shouldShowDeclinedMessage();
        assertEquals("DECLINED", SQLHelper.getCreditStatus());
    }
}
