DROP TABLE if exists USERS;

CREATE TABLE USERS (
    userid          serial primary key,
    username        varchar(255) not null unique,
    userpswd        varchar(255) not null,
    realname        varchar(255) not null,
    surname1        varchar(255) not null,
    surname2        varchar(255),
    usertype        varchar(10) not null,

    CONSTRAINT ck_usertype_is_valid CHECK (
        (upper(usertype) = 'ADMIN' )
        OR (upper(usertype) = 'USER' )
        OR (upper(usertype) = 'WORKER' )
    )
);
GRANT ALL PRIVILEGES ON USERS TO library_app_admin;

DROP TABLE if exists AUTHOR;
CREATE TABLE AUTHOR (
    authorid        serial primary key,
    authorname      varchar(255) not null,
    surname1        varchar(255) not null,
    surname2        varchar(255),
    biography       varchar(255),
    nationality     varchar(255),
    yearbirth       integer
)
GRANT ALL PRIVILEGES ON AUTHOR TO library_app_admin;

DROP TABLE if exists MEDIA;
CREATE TABLE MEDIA (
    workid                  serial primary key,
    title                   varchar(255) not null unique,
    yearpublication         integer,
    mediatype               varchar(255),
    media_description       varchar(255),

    CONSTRAINT ck_mediatype_is_valid CHECK (
        (upper(usertype) = 'BOOK' )
        OR (upper(usertype) = 'MAGAZINE' )
        OR (upper(usertype) = 'MULTIMEDIA' )
    )
);
GRANT ALL PRIVILEGES ON MEDIA TO library_app_admin;

DROP TABLE if exists MEDIA_CREATORS;
CREATE TABLE MEDIA_CREATORS (
    workid                  integer primary key,
    creator_id              integer primary key,
);
GRANT ALL PRIVILEGES ON MEDIA_CREATORS TO library_app_admin;