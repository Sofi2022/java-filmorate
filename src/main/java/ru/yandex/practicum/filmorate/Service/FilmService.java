package ru.yandex.practicum.filmorate.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.Storage.FilmStorage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.Storage.UserStorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage){
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int filmId, int userId){
        Film film = filmStorage.getFilmById(filmId);
        film.addLike(userId);
    }

    public void deleteLike(int filmId, int userId) {
        System.out.println("film " + filmStorage.getFilmById(filmId));
        System.out.println("films " + filmStorage.getFilms());
        System.out.println("users" + userStorage.getUsersIds());
        if (filmStorage.getFilms().containsKey(filmId) && userStorage.getUsersIds().contains(userId)){
                Film film = filmStorage.getFilmById(filmId);
                //System.out.println("id film " + film.getId());
                film.deleteLike(userId);
            } else{
            throw new NotFoundException("Такого id нет");
        }
    }

    public List<Film> getMostPopularFilms(int count) {
        Map<Integer, Film> allFilms = filmStorage.getFilms();
        Comparator<Film> comparator = Comparator.comparingInt(film -> film.getLikes().size());
        return allFilms.values().stream()
                .sorted(comparator.reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film addFilm(Film film){
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film){
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int id){
        return filmStorage.getFilmById(id);
    }

    public List<Film> getAllFilms(){
        return filmStorage.getAllFilms();
    }
}
