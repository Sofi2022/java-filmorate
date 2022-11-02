package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public class GenreDbStorage {

    private final Logger log = LoggerFactory.getLogger(GenreDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getAllGenres() {
        log.info("Вызван метод getAllGenres");
        String sql = "SELECT * FROM GENRES";
        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) ->
                new Genre(rs.getInt("GENRE_ID"), rs.getString("NAME")));
        return genres;
    }

    public Genre getGenreById(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES WHERE GENRE_ID = ?", id);
        if (genreRows.next()) {
            Genre genre = new Genre(genreRows.getInt("GENRE_ID"), genreRows.getString("NAME"));
            log.info("Найден Genre с id{}", id);
            return genre;
        }
        throw new NotFoundException("Такого Genre нет {}");
    }
}
