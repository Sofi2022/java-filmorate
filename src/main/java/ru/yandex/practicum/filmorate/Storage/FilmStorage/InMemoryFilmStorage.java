package ru.yandex.practicum.filmorate.Storage.FilmStorage;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptionsHandler.NotFoundException;
import ru.yandex.practicum.filmorate.exceptionsHandler.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Data
public class InMemoryFilmStorage implements FilmStorage {

    private Integer filmId = 0;
    private Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        if(film == null){
            throw new NullPointerException("Фильм не может быть пустым");
        }
        if(films.containsKey(film.getId())){
            return updateFilm(film);
        } else {
            validateFilm(film);
            film.setId(++filmId);
            films.put(film.getId(), film);
            return film;
        }
    }

    @Override
    public Film updateFilm(Film film) {
        if(films.containsKey(film.getId())){
            validateFilm(film);
            films.put(film.getId(), film);
            return film;
        } else{
            throw new NotFoundException("Такого фильма нет");
        }
    }

    @Override
    public Film getFilmById(int id) {
        if(films.containsKey(id)){
            return films.get(id);
        } else{
            throw new NotFoundException("Такого фильма нет");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        return List.copyOf(films.values());
    }

    @Override
    public void validateFilm(Film film) {
        if(film == null){
            throw new NullPointerException("Объект не может быть пустым");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза раньше 28.12.1895");
        }
    }
}
