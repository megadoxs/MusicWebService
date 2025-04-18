package com.champlain.playlistservice.dataaccesslayer.playlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    Playlist findByIdentifier_PlaylistId(String identifier);
    List<Playlist> findAllByUser(String userId);
    void deleteAllByUser(String userId);
    List<Playlist> findAllBySongsContains(String songId);
}
