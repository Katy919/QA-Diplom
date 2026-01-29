package ru.netology.data;

import lombok.Value;

public class DataHelper {
    private DataHelper() {
    }

    private static final String APPROVED_NUMBER = "4444 4444 4444 4441";
    private static final String DECLINED_NUMBER = "4444 4444 4444 4442";
    private static final String DEFAULT_MONTH = "02";
    private static final String DEFAULT_YEAR = "26";
    private static final String DEFAULT_HOLDER = "Екатерина Голубева";
    private static final String DEFAULT_CVC = "999";

    public static void cleanDatabase() {
        SQLHelper.cleanDatabase();
    }

    @Value
    public static class CardInfo {
        String number;
        String month;
        String year;
        String holder;
        String cvc;
    }

    private static CardInfo approvedCard() {
        return new CardInfo(APPROVED_NUMBER, DEFAULT_MONTH, DEFAULT_YEAR, DEFAULT_HOLDER, DEFAULT_CVC);
    }

    private static CardInfo declinedCard() {
        return new CardInfo(DECLINED_NUMBER, DEFAULT_MONTH, DEFAULT_YEAR, DEFAULT_HOLDER, DEFAULT_CVC);
    }

    public static CardInfo withNumber(String number) {
        return new CardInfo(number, DEFAULT_MONTH, DEFAULT_YEAR, DEFAULT_HOLDER, DEFAULT_CVC);
    }

    public static CardInfo withMonth(String month) {
        return new CardInfo(APPROVED_NUMBER, month, DEFAULT_YEAR, DEFAULT_HOLDER, DEFAULT_CVC);
    }

    public static CardInfo withYear(String year) {
        return new CardInfo(APPROVED_NUMBER, DEFAULT_MONTH, year, DEFAULT_HOLDER, DEFAULT_CVC);
    }

    public static CardInfo withHolder(String holder) {
        return new CardInfo(APPROVED_NUMBER, DEFAULT_MONTH, DEFAULT_YEAR, holder, DEFAULT_CVC);
    }

    public static CardInfo withCvc(String cvc) {
        return new CardInfo(APPROVED_NUMBER, DEFAULT_MONTH, DEFAULT_YEAR, DEFAULT_HOLDER, cvc);
    }

    public static CardInfo getApprovedCard() {
        return approvedCard();
    }

    public static CardInfo getDeclinedCard() {
        return declinedCard();
    }
}

