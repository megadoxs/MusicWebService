package com.champlain.apigatewayservice.utils.exceptions;

public class DuplicateSongTitleException extends RuntimeException {
    public DuplicateSongTitleException(String message) {
        super(message);
    }
}
