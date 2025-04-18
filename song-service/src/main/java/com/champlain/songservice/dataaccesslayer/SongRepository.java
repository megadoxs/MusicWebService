package com.champlain.songservice.dataaccesslayer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Integer> {
    Song findSongByIdentifier_SongId(String songId);
    List<Song> findAllByArtistsContains(String artistId);
}
