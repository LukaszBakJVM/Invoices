package org.lukasz.faktury.exceptions;

public class NipConflictException extends RuntimeException{
    public NipConflictException(String message) {
        super(message);
    }
}
