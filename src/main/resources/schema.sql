-- auto-generated definition
DROP TABLE IF EXISTS USERS CASCADE;
create table if not exists USERS
(
    USER_ID       INTEGER auto_increment,
    LOGIN         CHARACTER VARYING(50)  not null,
    EMAIL         CHARACTER VARYING(200) not null,
    USER_NAME     CHARACTER VARYING(100) not null,
    USER_BIRTHDAY DATE                   not null,
    constraint USER_ID
        primary key (USER_ID)
);

create unique index if not exists USERS_LOGIN_UNIQUE
    on USERS (EMAIL);

DROP TABLE IF EXISTS MPA CASCADE;
create table if not exists MPA
(
    MPA_ID   INTEGER not null primary key,
    MPA_NAME CHARACTER VARYING not null
);

DROP TABLE IF EXISTS FILMS CASCADE;
create table if not exists FILMS
(
    FILM_ID      INTEGER auto_increment
        primary key,
    NAME         CHARACTER VARYING      not null,
    DESCRIPTION  CHARACTER VARYING(200) not null,
    RELEASE_DATE DATE                   not null,
    DURATION     INTEGER                not null,
    MPA_ID       INTEGER                not null,
    RATE         INTEGER,
    constraint FILMS_MPA_MPA_ID_FK
        foreign key (MPA_ID) references MPA
);

DROP TABLE IF EXISTS LIKES CASCADE;
create table if not exists LIKES
(
    USER_ID int not null references USERS(USER_ID),
    FILM_ID int not null references FILMS(FILM_ID),
    PRIMARY KEY(USER_ID, FILM_ID)
    );

DROP TABLE IF EXISTS FRIENDS CASCADE;
create table if not exists FRIENDS
(
    USER_ID   INTEGER references USERS(USER_ID),
    FRIEND_ID INTEGER references USERS(USER_ID),
        PRIMARY KEY(USER_ID, FRIEND_ID)
);

DROP TABLE IF EXISTS GENRES CASCADE;
create table if not exists GENRES(
    GENRE_ID int not null primary key auto_increment,
    NAME varchar(200)

);

DROP TABLE IF EXISTS FILM_GENRES CASCADE;
create table if not exists FILM_GENRES(
    FILM_ID int references FILMS(FILM_ID),
    GENRE_ID int references GENRES(GENRE_ID),
    PRIMARY KEY(FILM_ID, GENRE_ID)
);








