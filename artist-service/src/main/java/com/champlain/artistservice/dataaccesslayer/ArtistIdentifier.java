package com.champlain.artistservice.dataaccesslayer;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ArtistIdentifier {
    private String artistId;

    public ArtistIdentifier(String artistId) {
        this.artistId = artistId;
    }

    public ArtistIdentifier() {
        this.artistId = UUID.randomUUID().toString();
    }
}
