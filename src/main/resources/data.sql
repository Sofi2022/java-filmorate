merge into GENRES (GENRE_ID, NAME)
values(1, 'Комедия');
merge into GENRES (GENRE_ID, NAME)
values(2, 'Драма');
merge into GENRES (GENRE_ID, NAME)
values(3, 'Мультфильм');
merge into GENRES (GENRE_ID, NAME)
values(4, 'Триллер');
merge into GENRES (GENRE_ID, NAME)
values(5, 'Документальный');
merge into GENRES (GENRE_ID, NAME)
values(6, 'Боевик');

merge into MPA(MPA_ID, MPA_NAME)
values(1, 'G'), (2, 'PG'), (3, 'PG-13'), (4, 'R'), (5, 'NC-17');

insert into LIKES (FILM_ID, USER_ID) VALUES (2, 2);
update FILMS as f set rate = (select count (l.USER_ID) from LIKES as l
where l.FILM_ID = f.FILM_ID) where f.FILM_ID = 2;
