package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class PaymentPage {

    private final SelenideElement cardNumberField = $("[placeholder='0000 0000 0000 0000']");
    private final SelenideElement monthField = $("[placeholder='08']");
    private final SelenideElement yearField = $("[placeholder='22']");
    private final SelenideElement holderField = $(byText("Владелец"))
            .closest(".input__inner")
            .$("input");
    private final SelenideElement cvcField = $("[placeholder='999']");
    private final SelenideElement continueButton = $(byText("Продолжить"));
    private final SelenideElement successMessage = $(".notification_status_ok");
    private final SelenideElement errorMessage = $(".notification_status_error");
    private final SelenideElement cardNumberError =
            cardNumberField.closest(".input__inner").$(".input__sub");
    private final SelenideElement monthError =
            monthField.closest(".input__inner").$(".input__sub");
    private final SelenideElement yearError =
            yearField.closest(".input__inner").$(".input__sub");
    private final SelenideElement holderError =
            holderField.closest(".input__inner").$(".input__sub");
    private final SelenideElement cvcError =
            cvcField.closest(".input__inner").$(".input__sub");

    public void fillCardData(DataHelper.CardInfo card) {
        cardNumberField.shouldBe(visible).setValue(card.getNumber());
        monthField.shouldBe(visible).setValue(card.getMonth());
        yearField.shouldBe(visible).setValue(card.getYear());
        holderField.shouldBe(visible).setValue(card.getHolder());
        cvcField.shouldBe(visible).setValue(card.getCvc());
    }

    public void shouldShowCardNumberError() {
        cardNumberError.shouldBe(visible)
                .shouldHave(text("Неверный формат"));
    }

    public void shouldShowMonthError() {
        monthError.shouldBe(visible)
                .shouldHave(text("Неверный формат"));
    }

    public void shouldShowMonthErrorInvalidRange() {
        monthError.shouldBe(visible)
                .shouldHave(text("Неверно указан срок действия карты"));
    }

    public void shouldShowYearErrorInvalidFormat() {
        yearError.shouldBe(visible)
                .shouldHave(text("Неверный формат"));
    }

    public void shouldShowYearErrorExpired() {
        yearError.shouldBe(visible)
                .shouldHave(text("Истёк срок действия карты"));
    }

    public void shouldShowYearErrorInvalidRange() {
        yearError.shouldBe(visible)
                .shouldHave(text("Неверно указан срок действия карты"));
    }

    public void shouldShowHolderRequiredField() {
        holderError.shouldBe(visible)
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    public void shouldShowHolderInvalidFormat() {
        holderError.shouldBe(visible)
                .shouldHave(text("Неверный формат"));
    }

    public void shouldShowCvcError() {
        cvcError.shouldBe(visible)
                .shouldHave(text("Неверный формат"));
    }

    public void submit() {
        continueButton.shouldBe(visible).click();
    }

    public void shouldShowSuccessMessage() {
        successMessage.shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Успешно" +
                        " Операция одобрена Банком."));
    }

    public void shouldShowDeclinedMessage() {
        errorMessage.shouldBe(visible)
                .shouldHave(text("Операция не возможна! Карта отклонена."));
    }
}
