package com.champlain.apigatewayservice.utils.exceptions;

public class DuplicateUserPlaylist extends RuntimeException {
    public DuplicateUserPlaylist(String message) {
        super(message);
    }
}
