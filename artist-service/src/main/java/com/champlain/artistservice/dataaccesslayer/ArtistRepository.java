package com.champlain.artistservice.dataaccesslayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    Artist findArtistByIdentifier_ArtistId(String artistId);
}
