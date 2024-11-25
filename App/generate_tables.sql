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
	PRIMARY KEY (workid, creatorid),
    CONSTRAINT fk_workid FOREIGN KEY (workid) REFERENCES MEDIA(workid),
    CONSTRAINT fk_creatorid FOREIGN KEY (creatorid) REFERENCES AUTHORS(authorid)
);
GRANT ALL PRIVILEGES ON MEDIA_CREATORS TO library_app_admin;

DROP TABLE if exists LOANS;
CREATE TABLE LOANS (
    workid              integer primary key,
    userid              integer not null,
    returned            boolean not null,
    date_start_loan     date not null,
    date_end_loan       date not null,
    CONSTRAINT fk_workid FOREIGN KEY (workid) REFERENCES MEDIA(workid),
    CONSTRAINT fk_userid FOREIGN KEY (userid) REFERENCES users(userid)
);
GRANT ALL PRIVILEGES ON MEDIA_CREATORS TO library_app_admin;

GRANT USAGE, SELECT ON SEQUENCE users_userid_seq TO library_app_admin;
GRANT USAGE, SELECT ON SEQUENCE authors_authorid_seq TO library_app_admin;
