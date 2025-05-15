package com.champlain.playlistservice.utils.exceptions;

public class DuplicateUserPlaylist extends RuntimeException {
    public DuplicateUserPlaylist(String message) {
        super(message);
    }
}
