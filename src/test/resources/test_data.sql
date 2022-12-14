DELETE FROM USERS_FRIENDS;
DELETE FROM USERS;
DELETE FROM FILMS_GENRES;
DELETE FROM FILMS_LIKES;
DELETE FROM FILMS;

COMMIT;

ALTER TABLE FILMS ALTER COLUMN id RESTART WITH 1;
ALTER TABLE USERS ALTER COLUMN id RESTART WITH 1;


INSERT INTO TEST_FILMORATE.PUBLIC.USERS (LOGIN, EMAIL, NAME, BIRTHDAY)
VALUES ('loginname', 'email@email.ru', 'username', PARSEDATETIME('01.01.1990', 'dd.MM.yyyy'));

INSERT INTO TEST_FILMORATE.PUBLIC.USERS (LOGIN, EMAIL, NAME, BIRTHDAY)
VALUES ('userlogin', 'mail@mail.ru', 'username2', PARSEDATETIME('01.01.2000', 'dd.MM.yyyy'));

INSERT INTO TEST_FILMORATE.PUBLIC.FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA)
VALUES ('oldFilm', 'Old Drama Comedy Film', PARSEDATETIME('01.01.1900', 'dd.MM.yyyy'), 160, 1);

INSERT INTO TEST_FILMORATE.PUBLIC.FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA)
VALUES ('newFilm', 'It is new Action Film', PARSEDATETIME('01.01.2000', 'dd.MM.yyyy'), 120, 2);

INSERT INTO TEST_FILMORATE.PUBLIC.FILMS_GENRES (FILM_ID, GENRE_ID)
VALUES (1, 1);

INSERT INTO TEST_FILMORATE.PUBLIC.FILMS_GENRES (FILM_ID, GENRE_ID)
VALUES (1, 2);

INSERT INTO TEST_FILMORATE.PUBLIC.FILMS_GENRES (FILM_ID, GENRE_ID)
VALUES (2, 6);





