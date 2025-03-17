create table if not exists users (
    id integer auto_increment primary key,
    user_identifier varchar(255) not null,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    date_of_birth date not null,
    email varchar(100) unique not null,
    username varchar(75) unique not null,
    password varchar(100) not null
);



create table if not exists artists (
    id integer auto_increment primary key,
    artist_id varchar(36) not null unique,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    stage_name varchar(50)
);

create table if not exists songs (
    id integer auto_increment primary key,
    song_id varchar(36) not null unique,
    title varchar(50) not null,
    genre varchar(50) not null,
    release_date date not null,
    duration Time not null
);

create table if not exists song_artists (
    song_id integer,
    artist_id varchar(36),
    PRIMARY KEY (song_id, artist_id),
    foreign key (artist_id) references artists(artist_id),
    foreign key (song_id) references songs(id)
);

create table if not exists playlists (
    id integer auto_increment primary key,
    playlist_id varchar(36) not null unique,
    "user" varchar(36) not null,
    name varchar(50) not null,
    duration Time not null
);

create table if not exists playlist_songs (
    playlist_id integer,
    song_id varchar(36),
    PRIMARY KEY (playlist_id, song_id),
    foreign key (song_id) references songs(song_id),
    foreign key (playlist_id) references playlists(id)
)