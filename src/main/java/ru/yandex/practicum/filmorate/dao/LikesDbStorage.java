package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("LikesDbStorage")
public class LikesDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLike(int filmId, int userId) {
        String sqlQuery = "merge into LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        updateRate(filmId);
    }

    public void removeLike(int filmId, int userId) {
        String sqlQuery = "delete from LIKES where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        updateRate(filmId);
    }

    private void updateRate(int filmId) {
        String sqlQuery = "update FILMS as f set rate = (select count (l.USER_ID) from LIKES as l " +
                "where l.FILM_ID = f.FILM_ID) where f.FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }
}
