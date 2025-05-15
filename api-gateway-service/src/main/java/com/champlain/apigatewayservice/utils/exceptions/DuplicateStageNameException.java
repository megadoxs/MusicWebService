package com.champlain.apigatewayservice.utils.exceptions;


public class DuplicateStageNameException extends RuntimeException {
    public DuplicateStageNameException(String message) {
        super(message);
    }
}
