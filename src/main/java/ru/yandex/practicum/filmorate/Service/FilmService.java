package ru.yandex.practicum.filmorate.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.Storage.FilmStorage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.Storage.UserStorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {

//    private final InMemoryFilmStorage filmStorage;
//    private final InMemoryUserStorage userStorage;
//
//    @Autowired
//    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage){
//        this.filmStorage = filmStorage;
//        this.userStorage = userStorage;
//    }

    private final FilmDbStorage filmDbStorage;

    public FilmService(FilmDbStorage filmDbStorage){
        this.filmDbStorage = filmDbStorage;
    }
    public void addLike(int filmId, int userId){
        filmDbStorage.addLike(filmId, userId);
       // Film film = filmStorage.getFilmById(filmId);
        //film.addLike(userId);
    }

    public void deleteLike(int filmId, int userId) {
        filmDbStorage.removeLike(filmId, userId);
//        if (filmStorage.getFilms().containsKey(filmId) && userStorage.getUsersIds().contains(userId)){
//                Film film = filmStorage.getFilmById(filmId);
//                film.deleteLike(userId);
//            } else{
//            throw new NotFoundException("Такого id нет");
//        }
    }

   public List<Film> getMostPopularFilms(int count) {
        //final List<Film>
        return filmDbStorage.getMostPopularFilms(count);
//        Map<Integer, Film> allFilms = filmStorage.getFilms();
//        Comparator<Film> comparator = Comparator.comparingInt(film -> film.getLikes().size());
//        return allFilms.values().stream()
//                .sorted(comparator.reversed())
//                .limit(count)
//                .collect(Collectors.toList());
    }

    public Film addFilm(Film film){
        return filmDbStorage.addFilm(film);
    }

    public Film updateFilm(Film film){
        return filmDbStorage.updateFilm(film);
    }

    public Film getFilmById(int id){
        return filmDbStorage.getFilmById(id);
    }

    public List<Film> getAllFilms(){
        return filmDbStorage.getAllFilms();
    }
}
