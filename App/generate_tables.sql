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

DROP TABLE if exists AUTHORS;
CREATE TABLE AUTHORS (
    authorid        serial primary key,
    authorname      varchar(255) not null,
    surname1        varchar(255),
    surname2        varchar(255),
    biography       varchar(255) not null,
    nationality     varchar(255) not null,
    yearbirth       integer not null
);
GRANT ALL PRIVILEGES ON AUTHORS TO library_app_admin;

DROP TABLE if exists MEDIA;
CREATE TABLE MEDIA (
    workid                  serial primary key,
    title                   varchar(255) not null unique,
    yearpublication         integer not null,
    mediatype               varchar(255) not null,
    media_description       varchar(255) not null,

    CONSTRAINT ck_mediatype_is_valid CHECK (
        (upper(mediatype) = 'BOOK' )
        OR (upper(mediatype) = 'MAGAZINE' )
        OR (upper(mediatype) = 'MULTIMEDIA' )
    )
);
GRANT ALL PRIVILEGES ON MEDIA TO library_app_admin;

DROP TABLE if exists MEDIA_CREATORS;
CREATE TABLE MEDIA_CREATORS (
    workid                  integer,
    creatorid              integer,
	PRIMARY KEY (workid, creatorid)
);
GRANT ALL PRIVILEGES ON MEDIA_CREATORS TO library_app_admin;