package com.champlain.music.playlistsubdomain.dataaccesslayer.playlist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    Playlist findByIdentifier_PlaylistId(String identifier);
}
