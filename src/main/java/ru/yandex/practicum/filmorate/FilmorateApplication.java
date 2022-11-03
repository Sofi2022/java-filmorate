package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.Service.UserService;
import ru.yandex.practicum.filmorate.Storage.UserStorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.controllers.UserController;

@SpringBootApplication
public class FilmorateApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmorateApplication.class, args);
	}
}
