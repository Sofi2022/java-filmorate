package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptionsHandler.NotFoundException;
import ru.yandex.practicum.filmorate.exceptionsHandler.ValidationException;
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

    private final FilmGenresDbStorage filmGenresDbStorage;
    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmGenresDbStorage filmGenresDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmGenresDbStorage = filmGenresDbStorage;
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
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setInt(6, film.getMpa().getId());
            log.info("mpaId {}", film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        log.info("filmId= {}", film.getId());
        filmGenresDbStorage.saveGenre(film);
        log.info("???????? ????????????????????");
        return getFilmById(film.getId());
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
            filmGenresDbStorage.saveGenre(film);
            return getFilmById(film.getId());
        }
        throw new NotFoundException("???????????? ???????????????????????? ??????");
    }

    @Override
    public Film getFilmById(int filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE FILM_ID = ? ", filmId);
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS f JOIN MPA m " +
                "ON m.MPA_ID = f.MPA_ID where f.FILM_ID = ?", filmId);
        SqlRowSet genreNamesRows = jdbcTemplate.queryForRowSet("SELECT * from GENRES g JOIN FILM_GENRES fg" +
                " ON g.GENRE_ID = fg.GENRE_ID where fg.FILM_ID = ?", filmId);

        Mpa mpa = new Mpa();
        if (mpaRows.next()) {
            mpa.setId(mpaRows.getInt("MPA_ID"));
            mpa.setName(mpaRows.getString("MPA_NAME"));
        }

        List<Genre> genres = new ArrayList<>();
        while (genreNamesRows.next()) {
            genres.add(new Genre(genreNamesRows.getInt("GENRE_ID"),
                    genreNamesRows.getString("NAME")));

        }

        if (filmRows.next()) {
            LocalDate releaseDate = filmRows.getDate("RELEASE_DATE").toLocalDate();
            int duration = filmRows.getInt("DURATION");
            Film film = new Film(filmRows.getInt("FILM_ID"),
                    filmRows.getString("NAME"),
                    filmRows.getString("DESCRIPTION"),
                    releaseDate, duration, mpa, genres);
            log.info("???????????? ?????????? {}", filmId);
            return film;
        } else {
            throw new NotFoundException("???????????? ???????????? ?????? {}");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * from FILMS f join MPA as m on f.MPA_ID = m.MPA_ID";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        films.forEach(this :: setLikes);
        films.forEach(this :: setGenres);
        return films;
    }

    private void setGenres(Film film){
        String sql = "SELECT FG.GENRE_ID, G.NAME FROM FILM_GENRES FG JOIN GENRES G ON FG.GENRE_ID = G.GENRE_ID " +
                "WHERE FILM_ID = ? GROUP BY FG.GENRE_ID";
        List<Genre> genres = new ArrayList<>(jdbcTemplate.query(sql, this :: makeGenre, film.getId()));
        film.setGenres(genres);
    }

    private void setLikes(Film film){
        String sql = "SELECT COUNT(FILM_ID), USER_ID FROM LIKES L WHERE L.FILM_ID = ? GROUP BY USER_ID;";
        Set<Integer> likes = new HashSet<>(jdbcTemplate.query(sql, this :: makeLikes, film.getId()));
        film.setLikes(likes);
    }

    private int makeLikes(ResultSet resultSet, int RowNum) throws SQLException {
        return resultSet.getInt("USER_ID");
    }

    public Genre makeGenre(ResultSet resultSet, int RowNum) throws SQLException {
        return new Genre(resultSet.getInt("GENRE_ID"), resultSet.getString("NAME"));
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
            throw new NullPointerException("???????????? ???? ?????????? ???????? ????????????");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("???????? ???????????? ???????????? 28.12.1895");
        }
    }

    public List<Film> getMostPopularFilms(int count) {
        final String sqlQuery = "SELECT F.*, MPA.MPA_NAME FROM FILMS AS F LEFT JOIN LIKES L on" +
                " F.FILM_ID = L.FILM_ID LEFT JOIN MPA ON " +
                "F.MPA_ID = MPA.MPA_ID GROUP BY F.FILM_ID ORDER BY COUNT(DISTINCT L.USER_ID) DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), count);
    }
}

