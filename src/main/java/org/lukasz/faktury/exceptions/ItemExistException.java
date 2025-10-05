package org.lukasz.faktury.exceptions;

public class ItemExistException extends RuntimeException{
    public ItemExistException(String message) {
        super(message);
    }
}
