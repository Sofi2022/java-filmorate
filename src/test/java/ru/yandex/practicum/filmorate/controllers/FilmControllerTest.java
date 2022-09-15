package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

class FilmControllerTest {
    @Test
    void releaseFilmTest() {
        final FilmController controller = new FilmController();
        Film film = new Film("name", "description", LocalDate.of(1999, 2, 2), 3);
        film.setReleaseDate(LocalDate.of(1800, 2, 2));
        ValidationException releaseException = Assertions.assertThrows(ValidationException.class, () -> {
            controller.validateFilm(film);
        }, "ValidationException was expected");
        Assertions.assertEquals("Дата релиза раньше 28.12.1895", releaseException.getMessage());
    }
}