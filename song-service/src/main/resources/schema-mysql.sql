DROP TABLE IF EXISTS song_artists;
DROP TABLE IF EXISTS songs;
CREATE TABLE IF NOT EXISTS songs (
     id INTEGER AUTO_INCREMENT PRIMARY KEY,
     song_id VARCHAR(36) NOT NULL UNIQUE,
    title VARCHAR(50) NOT NULL,
    genre VARCHAR(50) NOT NULL,
    release_date DATE NOT NULL,
    duration TIME NOT NULL
    );

CREATE TABLE IF NOT EXISTS song_artists (
                                            song_id INTEGER,
                                            artist_id VARCHAR(36),
    PRIMARY KEY (song_id, artist_id),
    FOREIGN KEY (song_id) REFERENCES songs (id) ON DELETE CASCADE
    );