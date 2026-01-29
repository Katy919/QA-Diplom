package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final SelenideElement buyButton = $(byText("Купить"));
    private final SelenideElement buyOnCreditButton = $(byText("Купить в кредит"));

    public PaymentPage buy() {
        buyButton.shouldBe(visible).click();
        return new PaymentPage();
    }

    public CreditPage buyOnCredit() {
        buyOnCreditButton.shouldBe(visible).click();
        return new CreditPage();
    }
}

