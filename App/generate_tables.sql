DROP TABLE if exists USERS;

CREATE TABLE USERS (
    userid          serial primary key,
    useralias       varchar(255) not null unique,
    userpswd        varchar(255) not null,
    username    varchar(255) not null,
    surname1    varchar(255) not null,
    surname2    varchar(255),
    usertype    varchar(10) not null,

    CONSTRAINT ck_usertype_is_valid CHECK (
        (upper(usertype) = 'ADMIN' )
        OR (upper(usertype) = 'USER' )
        OR (upper(usertype) = 'WORKER' )
    )
);