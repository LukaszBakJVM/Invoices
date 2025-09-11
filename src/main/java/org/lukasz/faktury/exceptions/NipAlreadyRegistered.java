package org.lukasz.faktury.exceptions;

public class NipAlreadyRegistered extends RuntimeException {
    public NipAlreadyRegistered(String message) {
        super(message);
    }
}
