package org.lukasz.faktury.exceptions;

public class NipNotFoundException extends RuntimeException{
    public NipNotFoundException(String message) {
        super(message);
    }
}
