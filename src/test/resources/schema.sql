CREATE TABLE IF NOT EXISTS USERS (
    ID INT AUTO_INCREMENT PRIMARY KEY ,
    LOGIN VARCHAR2(64) NOT NULL UNIQUE ,
    EMAIL VARCHAR2(64) NOT NULL UNIQUE ,
    NAME VARCHAR2(64) NOT NULL ,
    BIRTHDAY DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS USERS_FRIENDS (
    USER_ID INT REFERENCES USERS (ID) ON DELETE CASCADE ,
    FRIEND_ID INT REFERENCES USERS (ID) ON DELETE CASCADE ,
    PRIMARY KEY (USER_ID, FRIEND_ID)
);

CREATE TABLE IF NOT EXISTS GENRES (
    ID INT AUTO_INCREMENT PRIMARY KEY ,
    NAME VARCHAR2(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS RATINGS_MPA (
    ID INT AUTO_INCREMENT PRIMARY KEY ,
    NAME VARCHAR2(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS FILMS (
    ID INT AUTO_INCREMENT PRIMARY KEY ,
    NAME VARCHAR2(64) NOT NULL ,
    DESCRIPTION VARCHAR2(200) NOT NULL ,
    RELEASE_DATE DATE NOT NULL ,
    DURATION INT NOT NULL ,
    MPA INT REFERENCES RATINGS_MPA (ID) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS FILMS_LIKES (
    FILM_ID INT NOT NULL REFERENCES FILMS (ID) ON DELETE CASCADE ,
    USER_ID INT NOT NULL REFERENCES USERS (ID) ON DELETE CASCADE ,
    PRIMARY KEY (FILM_ID, USER_ID)
);

CREATE TABLE IF NOT EXISTS FILMS_GENRES (
    FILM_ID INT NOT NULL REFERENCES FILMS (ID) ON DELETE CASCADE ,
    GENRE_ID INT NOT NULL REFERENCES GENRES (ID) ON DELETE CASCADE ,
    PRIMARY KEY (FILM_ID, GENRE_ID)
);