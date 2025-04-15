package com.champlain.playlistservice.dataaccesslayer.playlist;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Embeddable
@Getter
@Setter
public class PlaylistIdentifier {
    private String playlistId;

    public PlaylistIdentifier(String playlistId) {
        this.playlistId = playlistId;
    }

    public PlaylistIdentifier() {
        this.playlistId = UUID.randomUUID().toString();
    }
}
