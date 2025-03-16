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
    release_date date not null
);

create table if not exists song_artists (
    song_id integer,
    artist_id varchar(36),
    PRIMARY KEY (song_id, artist_id),
    foreign key (song_id) references songs(id)
);