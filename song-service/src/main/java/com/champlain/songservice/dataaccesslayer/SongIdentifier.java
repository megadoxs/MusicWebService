package com.champlain.songservice.dataaccesslayer;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Embeddable
@Getter
@Setter
public class SongIdentifier {
    private String songId;

    public SongIdentifier(String songId) {
        this.songId = songId;
    }

    public SongIdentifier() {
        this.songId = UUID.randomUUID().toString();
    }
}
