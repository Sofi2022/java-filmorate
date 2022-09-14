package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

class FilmControllerTest {

    @Test
    void validateFilmTest() throws ValidationException {
        final FilmController controller = new FilmController();
        Film film = new Film("", "description", LocalDate.of(1990, 2, 2), 3);
        ValidationException nameException = Assertions.assertThrows(ValidationException.class, () -> {
            controller.validateFilm(film);
        }, "ValidationException was expected");
        Assertions.assertEquals("Введите название фильма", nameException.getMessage());

        film.setName("name");
        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят " +
                "разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, " +
                "который за время «своего отсутствия», стал кандидатом Коломбани.");
        ValidationException descriptionException = Assertions.assertThrows(ValidationException.class, () -> {
            controller.validateFilm(film);
        }, "ValidationException was expected");
        Assertions.assertEquals("Описание превышает 200 символов", descriptionException.getMessage());

        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1800, 2, 2));
        ValidationException releaseException = Assertions.assertThrows(ValidationException.class, () -> {
            controller.validateFilm(film);
        }, "ValidationException was expected");
        Assertions.assertEquals("Дата релиза раньше 28.12.1895", releaseException.getMessage());

        film.setReleaseDate(LocalDate.of(2000, 2, 2));
        film.setDuration(-1);
        ValidationException durationException = Assertions.assertThrows(ValidationException.class, () -> {
            controller.validateFilm(film);
        }, "ValidationException was expected");
        Assertions.assertEquals("Продолжительность фильма должна быть положительной", durationException.getMessage());
    }
}