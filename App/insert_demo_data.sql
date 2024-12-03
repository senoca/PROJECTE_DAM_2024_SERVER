---- TABLE USERS ----
--  id          serial primary key,
--  username       varchar(255) not null,
--  pswd        varchar(255) not null,
--  realname    varchar(255) not null,
--  surname1    varchar(255) not null,
--  surname2    varchar(255),
--  usertype    varchar(5) not null

INSERT INTO USERS (username, userpswd, realname, surname1, surname2, usertype) VALUES ('senoca', '1234', 'Sergio', 'Noya', 'Cambeiro', 'ADMIN');

INSERT INTO AUTHORS (authorname, surname1, surname2, biography, nationality, yearbirth) values ('Sófocles', null, null, 'Dramaturgo ateniense del siglo III aC.', 'Grecia', -405);
INSERT INTO AUTHORS (authorname, surname1, surname2, biography, nationality, yearbirth) values ('Virgilio', null, null, 'Poeta romano del siglo I.', 'Roma', -70);
INSERT INTO AUTHORS (authorname, surname1, surname2, biography, nationality, yearbirth) values ('John Ronald', 'Tolkien', null, 'Escritor de fantasía.', 'Reino Unido', 1892);
INSERT INTO AUTHORS (authorname, surname1, surname2, biography, nationality, yearbirth) values ('Lope', 'De Vega', null, 'Dramaturgo español del siglo del Oro.', 'España', 1562);

INSERT INTO MEDIA (title, yearpublication, mediatype, media_description) values ('Édipo Rey', -429, 'BOOK', 'Drama griego');
INSERT INTO MEDIA (title, yearpublication, mediatype, media_description) values ('Antígona', -429, 'BOOK', 'Drama griego');
INSERT INTO MEDIA (title, yearpublication, mediatype, media_description) values ('El Señor de los Anillos', 1954, 'BOOK', 'Fantasía');
INSERT INTO MEDIA (title, yearpublication, mediatype, media_description) values ('Fuenteovejuna', 1619, 'BOOK', 'Obra de teatro');

insert into media_creators (workid, creatorid) values (1, 1);
insert into media_creators (workid, creatorid) values (2, 1);
insert into media_creators (workid, creatorid) values (3, 3);
insert into media_creators (workid, creatorid) values (4, 4);

