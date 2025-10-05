package org.lukasz.faktury.exceptions;

public class NipAlreadyRegisteredException extends RuntimeException {
    public NipAlreadyRegisteredException(String message) {
        super(message);
    }
}
