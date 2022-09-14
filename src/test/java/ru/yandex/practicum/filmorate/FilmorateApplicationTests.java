package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@SpringBootTest
class FilmorateApplicationTests {

	@Test
	void validateFilmTest(){
		final FilmController controller = new FilmController();
		Film film = new Film();
		film.setName("");
		Assertions.assertThrows(ValidationException.class, () -> controller.validateFilm(film));

		film.setName("validName");
		film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят разыскать" +
				" господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, который за" +
				" время «своего отсутствия», стал кандидатом Коломбани.");
		Assertions.assertThrows(ValidationException.class, () -> controller.validateFilm(film));

		film.setDuration(3);
		film.setReleaseDate(LocalDate.of(1800, 2, 2));
		Assertions.assertThrows(ValidationException.class, () -> controller.validateFilm(film));

		film.setDescription("validDescription");
		film.setDuration(-3);
		Assertions.assertThrows(ValidationException.class, () -> controller.validateFilm(film));
	}

}
