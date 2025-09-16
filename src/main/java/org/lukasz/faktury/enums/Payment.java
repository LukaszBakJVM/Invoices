package org.lukasz.faktury.enums;

public enum Payment {
    BANK_TRANSFER("Przelew"),CARD("Karta"),CASH("Gotówka");
    private final String PAYMENT_TYPE;

    Payment(String PAYMENT_TYPE) {
        this.PAYMENT_TYPE = PAYMENT_TYPE;
    }

    public String getPAYMENT_TYPE() {
        return PAYMENT_TYPE;
    }


}
