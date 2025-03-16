package com.champlain.music.songsubdomain.dataaccesslayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Integer> {
    Song findSongByIdentifier_SongId(String songId);
}
