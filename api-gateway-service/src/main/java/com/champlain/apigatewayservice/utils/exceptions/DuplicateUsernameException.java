package com.champlain.apigatewayservice.utils.exceptions;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
