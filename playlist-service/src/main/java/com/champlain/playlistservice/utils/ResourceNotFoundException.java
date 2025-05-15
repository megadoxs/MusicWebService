package com.champlain.playlistservice.utils;

import jakarta.persistence.EntityNotFoundException;

import java.util.UUID;

public class ResourceNotFoundException extends EntityNotFoundException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Not implemented yet
    public static ResourceNotFoundException playlistNotFound(UUID playlistId) {
        return new ResourceNotFoundException("Playlist not found with ID: " + playlistId);
    }
}
