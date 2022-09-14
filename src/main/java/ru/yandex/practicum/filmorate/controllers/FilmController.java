package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Calendar.DECEMBER;

@RestController
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private Integer filmId = 1;
    private Map<Integer, Film> films = new HashMap<>();

    void increaseFilmId(Integer filmId) {
        this.filmId++;
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        log.debug("Вызван /films");
        increaseFilmId(film.getId());
        validateFilm(film);
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            validateFilm(film);
        } else {
            throw new ValidationException("Такого id нет");
        }
        return film;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return List.copyOf(films.values());
    }

    public void validateFilm(Film film) throws ValidationException {
        if (film.getName().isEmpty() || film.getName() == null) {
            throw new ValidationException("Введите название фильма");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание превышает 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, DECEMBER, 28))) {
            throw new ValidationException("Дата релиза раньше 28.12.1895");
        } else if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        } else {
            films.put(film.getId(), film);
        }
    }
}
