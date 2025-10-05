package org.lukasz.faktury.exceptions;

public class AccountNumberException extends RuntimeException{
    public AccountNumberException(String message) {
        super(message);
    }
}
