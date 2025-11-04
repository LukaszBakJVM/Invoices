package com.example.front.exceptions;

public class ItemExistException extends RuntimeException{
    public ItemExistException(String message) {
        super(message);
    }
}
