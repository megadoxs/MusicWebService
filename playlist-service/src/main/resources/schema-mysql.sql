CREATE TABLE IF NOT EXISTS playlists (
    id           INTEGER AUTO_INCREMENT PRIMARY KEY,
    playlist_id  VARCHAR(36) NOT NULL UNIQUE,
    `user`       VARCHAR(36) NOT NULL,
    name         VARCHAR(50) NOT NULL,
    duration     TIME NOT NULL
);

CREATE TABLE IF NOT EXISTS playlist_songs (
    playlist_id INTEGER,
    song_id     VARCHAR(36),
    PRIMARY KEY (playlist_id, song_id),
    FOREIGN KEY (playlist_id) REFERENCES playlists(id) ON DELETE CASCADE
);