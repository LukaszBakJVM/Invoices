package org.lukasz.faktury.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserException extends AuthenticationException {
    public UserException(String message) {
        super(message);
    }
}
