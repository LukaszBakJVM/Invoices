package org.lukasz.faktury.exceptions;

public record ResponseError(int status, String message) {
}
