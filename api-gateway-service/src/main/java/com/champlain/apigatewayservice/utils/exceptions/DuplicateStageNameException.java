package com.champlain.apigatewayservice.utils.exceptions;


public class DuplicateStageNameException extends RuntimeException {
    public DuplicateStageNameException(String message) {
        super(message);
    }

    public DuplicateStageNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateStageNameException stageNameAlreadyExists(String stageName) {
        return new DuplicateStageNameException("Stage name already exists: " + stageName);
    }

    public DuplicateStageNameException stageNameAlreadyExists(String stageName, Throwable cause) {
        return new DuplicateStageNameException("Stage name already exists: " + stageName, cause);
    }
}
