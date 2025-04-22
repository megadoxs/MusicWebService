package com.champlain.songservice.utils.exceptions;

public class DuplicateSongTitleException extends RuntimeException {

    public DuplicateSongTitleException(String message) {
        super(message);
    }

    public DuplicateSongTitleException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateSongTitleException songTitleAlreadyExists(String songTitle) {
        return new DuplicateSongTitleException("Song title already exists: " + songTitle);
    }

    public DuplicateSongTitleException songTitleAlreadyExists(String songTitle, Throwable cause) {
        return new DuplicateSongTitleException("Song title already exists: " + songTitle, cause);
    }
}
