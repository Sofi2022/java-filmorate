package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {

    @Test
    void validateUserTest() throws ValidationException {
        final UserController controller = new UserController();
        User user = new User("name", "login", "name", LocalDate.of(2023, 2, 2));
        assertThrows(ValidationException.class, () -> controller.validateUser(user));

        user.setBirthday(LocalDate.of(1990, 2, 2));
        user.setName("");
        controller.addUser(user);
        assertEquals("login", user.getName(), "Пустое имя не поменялось на логин");
    }
}