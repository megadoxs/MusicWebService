CREATE TABLE IF NOT EXISTS users (
    id           INTEGER AUTO_INCREMENT PRIMARY KEY,
    user_id      VARCHAR(36) NOT NULL,
    first_name   VARCHAR(50) NOT NULL,
    last_name    VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    email        VARCHAR(100) UNIQUE NOT NULL,
    username     VARCHAR(75) UNIQUE NOT NULL,
    password     VARCHAR(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS artists (
    id         INTEGER AUTO_INCREMENT PRIMARY KEY,
    artist_id  VARCHAR(36) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    stage_name VARCHAR(50)
    );

CREATE TABLE IF NOT EXISTS songs (
    id           INTEGER AUTO_INCREMENT PRIMARY KEY,
    song_id      VARCHAR(36) NOT NULL UNIQUE,
    title        VARCHAR(50) NOT NULL,
    genre        VARCHAR(50) NOT NULL,
    release_date DATE NOT NULL,
    duration     TIME NOT NULL
    );

CREATE TABLE IF NOT EXISTS song_artists (
    song_id   INTEGER,
    artist_id VARCHAR(36),
    PRIMARY KEY (song_id, artist_id),
    FOREIGN KEY (artist_id) REFERENCES artists(artist_id) ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS playlists (
    id           INTEGER AUTO_INCREMENT PRIMARY KEY,
    playlist_id  VARCHAR(36) NOT NULL UNIQUE,
    "user"       VARCHAR(36) NOT NULL,
    name         VARCHAR(50) NOT NULL,
    duration     TIME NOT NULL
    );

CREATE TABLE IF NOT EXISTS playlist_songs (
    playlist_id INTEGER,
    song_id     VARCHAR(36),
    PRIMARY KEY (playlist_id, song_id),
    FOREIGN KEY (song_id) REFERENCES songs(song_id) ON DELETE CASCADE,
    FOREIGN KEY (playlist_id) REFERENCES playlists(id) ON DELETE CASCADE
    );