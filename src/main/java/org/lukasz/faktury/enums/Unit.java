package org.lukasz.faktury.enums;

public enum Unit {
        PIECE("szt"),HOUR("godz"),DAYS("dni"),MONTH("m-c"),KILOMETER("km"),KILOGRAM("kg"),ANOTHER("inna");

        private final String value;

    Unit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
