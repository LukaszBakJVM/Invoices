package com.example.front.exceptions;

public class NipNotFoundException extends RuntimeException{
    public NipNotFoundException(String message) {
        super(message);
    }
}
