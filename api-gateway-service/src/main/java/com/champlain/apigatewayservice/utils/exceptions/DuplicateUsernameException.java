package com.champlain.apigatewayservice.utils.exceptions;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String message) {
        super(message);
    }

    public DuplicateUsernameException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateUsernameException usernameAlreadyExists(String username) {
        return new DuplicateUsernameException("Username already exists: " + username);
    }

    public DuplicateUsernameException usernameAlreadyExists(String username, Throwable cause) {
        return new DuplicateUsernameException("Username already exists: " + username, cause);
    }
}
