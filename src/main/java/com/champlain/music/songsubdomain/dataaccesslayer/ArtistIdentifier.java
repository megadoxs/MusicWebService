package com.champlain.music.songsubdomain.dataaccesslayer;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Embeddable
@Getter
@Setter
public class ArtistIdentifier {
    String artistId;
    public ArtistIdentifier(String artistId) {
        this.artistId = artistId;
    }
    public ArtistIdentifier(){
        this.artistId = UUID.randomUUID().toString();
    }
}
