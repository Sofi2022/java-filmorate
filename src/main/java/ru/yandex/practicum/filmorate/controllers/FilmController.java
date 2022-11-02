package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Service.FilmService;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class FilmController {


    private final Logger log = LoggerFactory.getLogger(FilmController.class);

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService){
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void putLikeForFilm(@PathVariable ("id")int filmId, @PathVariable int userId){
       filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLikeForFilm(@PathVariable("id")int filmId, @PathVariable int userId) {
      filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getMostPopularFilm(@RequestParam(defaultValue = "10") int count){
        return filmService.getMostPopularFilms(count);
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable int id){
        return filmService.getFilmById(id);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmService.getAllFilms();

    }
}
