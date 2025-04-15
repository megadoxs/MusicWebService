CREATE TABLE IF NOT EXISTS artists (
    id         INTEGER AUTO_INCREMENT PRIMARY KEY,
    artist_id  VARCHAR(36) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    stage_name VARCHAR(50)
);