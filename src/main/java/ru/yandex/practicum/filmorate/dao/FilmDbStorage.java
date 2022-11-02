package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.Storage.FilmStorage.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Component
public class FilmDbStorage implements FilmStorage {

    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        validateFilm(film);
        String sqlQuery = "insert into FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_ID) " +
                "values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setLong(5, film.getRate());
            stmt.setLong(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        saveGenre(film);
        return getFilmById(film.getId());
    }


    private void saveGenre(Film film) {
        final int filmId = film.getId();
        jdbcTemplate.update("delete from FILM_GENRES where FILM_ID = ?", filmId);
        final Set<Genre> genres = film.getGenres();
        System.out.println("genres : " + genres.toString());
        if (genres == null || genres.isEmpty()) {
            return;
        }
        final ArrayList<Genre> genreList = new ArrayList<>(genres);
        jdbcTemplate.batchUpdate(
                "merge into FILM_GENRES (FILM_ID, GENRE_ID) values (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, genreList.get(i).getId());
                    }

                    public int getBatchSize() {
                        return genreList.size();
                    }
                });
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update films set name = ?, description = ?, release_date = ?, duration = ?, rate = ?, " +
                "mpa_id = ? where FILM_ID = ?";
        String sqlQuery2 = "select * from FILMS where FILM_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery2, film.getId());
        if (userRows.next()) {
            jdbcTemplate.update(sqlQuery
                    , film.getName()
                    , film.getDescription()
                    , film.getReleaseDate()
                    , film.getDuration()
                    , film.getRate()
                    , film.getMpa().getId()
                    , film.getId());
            saveGenre(film);
            return getFilmById(film.getId());
        }
        throw new NotFoundException("Такого пользователя нет");
    }

    @Override
    public Film getFilmById(int filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE FILM_ID = ? ", filmId);
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS f JOIN MPA m " +
                "ON m.MPA_ID = f.MPA_ID where f.FILM_ID = ?", filmId);
//        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * from FILMS f JOIN FILM_GENRES fg " +
//                "on f.FILM_ID = fg.FILM_ID where f.FILM_ID = ?", id);
        SqlRowSet genreNamesRows = jdbcTemplate.queryForRowSet("SELECT * from GENRES g JOIN FILM_GENRES fg" +
                " ON g.GENRE_ID = fg.GENRE_ID where fg.FILM_ID = ?", filmId);
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("SELECT * FROM LIKES where FILM_ID = ?", filmId);

        Set<Integer> likes = new HashSet<>();
        if(likesRows.next()){
            likes.add(likesRows.getInt("USER_ID"));
        } else{
            log.info("У данного фильма лайокв нет {}", filmId);
        }

        Mpa mpa = new Mpa();
        if(mpaRows.next()){
            mpa.setId(mpaRows.getInt("MPA_ID"));
            mpa.setName(mpaRows.getString("MPA_NAME"));
        }

        Set<Genre> genres = new HashSet<>();
        while(genreNamesRows.next()){
            genres.add(new Genre(genreNamesRows.getInt("GENRE_ID"),
                    genreNamesRows.getString("NAME")));

        }

        if(filmRows.next()){
            LocalDate releaseDate = filmRows.getDate("RELEASE_DATE").toLocalDate();
            int duration = filmRows.getInt("DURATION");
            Film film =  new Film(filmRows.getInt("FILM_ID"),
                    filmRows.getString("NAME"),
                    filmRows.getString("DESCRIPTION"),
                    releaseDate, duration, filmRows.getInt("RATE"), mpa,genres, likes);
            log.info("Найден фильм {}", filmId);
            return film;
        }else{
            throw new NotFoundException("Такого фильма нет {}");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * from FILMS f join MPA as m on f.MPA_ID = m.MPA_ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
//        return jdbcTemplate.query("SELECT * from FILMS f join MPA as m on f.MPA_ID = m.MPA_ID",
//                FilmDbStorage :: makeFilm);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        LocalDate releaseDate = rs.getDate("RELEASE_DATE").toLocalDate();
        int duration = rs.getInt("DURATION");
        Film film =  new Film(rs.getInt("FILM_ID")
                , rs.getString("NAME")
                , rs.getString("DESCRIPTION")
                , releaseDate, duration, rs.getShort("RATE")
                , new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")));
        return film;
    }

    @Override
    public void validateFilm(Film film) {
        if (film == null) {
            throw new NullPointerException("Объект не может быть пустым");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза раньше 28.12.1895");
        }
    }

    public List<Film> getMostPopularFilms(int count) {
        final String sqlQuery = "SELECT FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, f.MPA_ID, RATE," +
                " m.MPA_NAME from FILMS as f join MPA as m on f.MPA_ID = m.MPA_ID order by RATE desc limit ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), count);
    }

    public void addLike(int filmId, int userId) {
        String sqlQuery = "merge into LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        updateRate(filmId);
    }

    private void updateRate(int filmId) {
        String sqlQuery = "update FILMS as f set rate = (select count (l.USER_ID) from LIKES as l " +
                "where l.FILM_ID = f.FILM_ID) where f.FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    public void removeLike(int filmId, int userId) {
            String sqlQuery = "delete from LIKES where FILM_ID = ? and USER_ID = ?";
            jdbcTemplate.update(sqlQuery, filmId, userId);
            updateRate(filmId);
        }
}

