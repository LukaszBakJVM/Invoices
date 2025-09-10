package org.lukasz.faktury.enums;

public enum Tax {
    VAT23(23),VAT8(8),VAT5(5),VAT0(0);
    private final int vat;

    Tax(int vat) {
        this.vat = vat;
    }

    public int getVat() {
        return vat;
    }
}
