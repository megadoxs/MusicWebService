package com.champlain.music.playlistsubdomain.dataaccesslayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    Playlist findByIdentifier_PlaylistIdentifier(String identifier);
}
