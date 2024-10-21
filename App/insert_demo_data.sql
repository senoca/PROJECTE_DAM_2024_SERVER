---- TABLE USERS ----
--  id          serial primary key,
--  username       varchar(255) not null,
--  pswd        varchar(255) not null,
--  realname    varchar(255) not null,
--  surname1    varchar(255) not null,
--  surname2    varchar(255),
--  usertype    varchar(5) not null

INSERT INTO USERS (username, userpswd, realname, surname1, surname2, usertype) VALUES ('senoca', '1234', 'Sergio', 'Noya', 'Cambeiro', 'ADMIN');
INSERT INTO USERS (username, userpswd, realname, surname1, surname2, usertype) VALUES ('supersayan', '4321', 'Goku', 'Goku', null, 'USER');
INSERT INTO USERS (username, userpswd, realname, surname1, surname2, usertype) VALUES ('calvorota', 'abcde', 'Mortadelo', 'Mortadela', null, 'USER');
INSERT INTO USERS (username, userpswd, realname, surname1, surname2, usertype) VALUES ('dramas', 'abcde', 'Antígona', 'Hija de Edípo', null, 'USER');
INSERT INTO USERS (username, userpswd, realname, surname1, surname2, usertype) VALUES ('cerdita', 'abcde23', 'Pepa', 'Pig', 'Rosita', 'WORKER');