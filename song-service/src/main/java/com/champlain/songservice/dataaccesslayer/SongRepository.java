package com.champlain.songservice.dataaccesslayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Integer> {
    Song findSongByIdentifier_SongId(String songId);
}
