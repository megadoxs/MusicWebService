package com.champlain.artistservice.dataaccesslayer;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArtistRepository extends MongoRepository<Artist, String> {
    Artist findArtistByIdentifier_ArtistId(String artistId);
}
