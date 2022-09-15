package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {

    @Test
    void validateUserTest() throws ValidationException {
        final UserController controller = new UserController();
        User user = new User("name", "login", "name", LocalDate.of(1990, 2, 2));
        user.setBirthday(LocalDate.of(2025, 2, 2));
        //controller.validateUser(user);
        ValidationException birthdaylException = Assertions.assertThrows(ValidationException.class, () -> {
            controller.validateUser(user);
        }, "ValidationException was expected");
        assertEquals("Дата рождения не может быть в будущем", birthdaylException.getMessage());

        user.setBirthday(LocalDate.of(1990, 2, 2));
        user.setName("");
        controller.addUser(user);
        assertEquals("login", user.getName(), "Пустое имя не поменялось на логин");
    }
}