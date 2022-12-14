package ru.yandex.practicum.filmorate.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptionsHandler.NotFoundException;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.LikesDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public class FilmService {

    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    private final LikesDbStorage likesDbStorage;

    @Autowired
    public FilmService(FilmDbStorage filmDbStorage, UserDbStorage userDbStorage, LikesDbStorage likesDbStorage) {
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
        this.likesDbStorage = likesDbStorage;
    }

    public void addLike(int filmId, int userId) {
        Film film = filmDbStorage.getFilmById(filmId);
        User user = userDbStorage.getUserById(userId);
        List<Film> films = filmDbStorage.getAllFilms();
        List<User> users = userDbStorage.getAllUsers();
        if (!(films.contains(film)) && (!(users.contains(user)))) {
            throw new NotFoundException("Такого фильма и пользователя нет");
        } else {
            likesDbStorage.addLike(filmId, userId);
        }
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmDbStorage.getFilmById(filmId);
        User user = userDbStorage.getUserById(userId);
        List<Film> films = filmDbStorage.getAllFilms();
        List<User> users = userDbStorage.getAllUsers();
        if (!(films.contains(film)) && (!(users.contains(user)))) {
            throw new NotFoundException("Такого фильма и пользователя нет");
        } else {
            likesDbStorage.removeLike(filmId, userId);
        }
    }

    public List<Film> getMostPopularFilms(int count) {
        return filmDbStorage.getMostPopularFilms(count);
    }

    public Film addFilm(Film film) {
        return filmDbStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmDbStorage.updateFilm(film);
    }

    public Film getFilmById(int id) {
        return filmDbStorage.getFilmById(id);
    }

    public List<Film> getAllFilms() {
        return filmDbStorage.getAllFilms();
    }
}
