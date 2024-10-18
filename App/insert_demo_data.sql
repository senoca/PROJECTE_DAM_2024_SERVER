---- TABLE USERS ----
--  id          serial primary key,
--  alias       varchar(255) not null,
--  pswd        varchar(255) not null,
--  username    varchar(255) not null,
--  surname1    varchar(255) not null,
--  surname2    varchar(255),
--  usertype    varchar(5) not null

INSERT INTO USERS (useralias, userpswd, username, surname1, surname2, usertype) VALUES ('Sergio', '1234', 'senoca', 'Noya', 'Cambeiro', 'ADMIN');
INSERT INTO USERS (useralias, userpswd, username, surname1, surname2, usertype) VALUES ('Son', '4321', 'supersayan', 'Goku', null, 'USER');
INSERT INTO USERS (useralias, userpswd, username, surname1, surname2, usertype) VALUES ('Mortadelo', 'abcde', 'calvorota', 'Mortadela', null, 'USER');
INSERT INTO USERS (useralias, userpswd, username, surname1, surname2, usertype) VALUES ('Antígona', 'abcde', 'dramas', 'Hija de Edípo', null, 'USER');
INSERT INTO USERS (useralias, userpswd, username, surname1, surname2, usertype) VALUES ('Pepa', 'abcde23', 'cerdita', 'Pig', 'Rosita', 'WORKER');