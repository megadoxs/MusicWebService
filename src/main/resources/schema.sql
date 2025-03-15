create table if not exists artists (
    id integer not null auto_increment primary key,
    artist_id varchar(36) not null unique,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    stage_name varchar(50)
);