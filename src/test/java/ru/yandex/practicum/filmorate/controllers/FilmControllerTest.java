package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
	@Test
	void validateFilmTest() throws ValidationException {
		final FilmController controller = new FilmController();
		Film film = new Film("name", "description", LocalDate.of(1895, 12, 27), 3);
		assertThrows(ValidationException.class, () -> controller.validateFilm(film));
	}
}



