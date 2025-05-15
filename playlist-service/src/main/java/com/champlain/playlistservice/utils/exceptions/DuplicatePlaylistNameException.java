package com.champlain.playlistservice.utils.exceptions;

public class DuplicatePlaylistNameException extends RuntimeException {

    public DuplicatePlaylistNameException() {
    }

    public DuplicatePlaylistNameException(String message) {
        super(message);
    }

    public DuplicatePlaylistNameException(Throwable cause) {
        super(cause);
    }

    public DuplicatePlaylistNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
