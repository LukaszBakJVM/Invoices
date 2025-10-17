package org.lukasz.faktury.exceptions;

public class BuyerNotFoundException extends RuntimeException{
    public BuyerNotFoundException(String message) {
        super(message);
    }
}
