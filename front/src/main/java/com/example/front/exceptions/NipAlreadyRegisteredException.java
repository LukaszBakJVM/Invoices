package com.example.front.exceptions;

public class NipAlreadyRegisteredException extends RuntimeException {
    public NipAlreadyRegisteredException(String message) {
        super(message);
    }
}
