package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

class UserControllerTest {

    @Test
    void validateUserTest() throws ValidationException {
        final UserController controller = new UserController();
        User user = new User("", "login", "name", LocalDate.of(1990, 2, 2));
        ValidationException emailException = Assertions.assertThrows(ValidationException.class, () -> {
            controller.validateUser(user);
        }, "ValidationException was expected");
        Assertions.assertEquals("Укажите Email с символом @", emailException.getMessage());

        user.setEmail("@validEmail");
        user.setLogin(" ");
        ValidationException loginException = Assertions.assertThrows(ValidationException.class, () -> {
            controller.validateUser(user);
        }, "ValidationException was expected");
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", loginException.getMessage());

        user.setLogin("validLogin");
        user.setBirthday(LocalDate.MAX);
        ValidationException birthdayException = Assertions.assertThrows(ValidationException.class, () -> {
            controller.validateUser(user);
        }, "ValidationException was expected");
        Assertions.assertEquals("Дата рождения не может быть в будущем", birthdayException.getMessage());
        Assertions.assertThrows(ValidationException.class, () -> controller.validateUser(user));

        user.setBirthday(LocalDate.of(1990, 2, 2));
        user.setEmail("");
        Assertions.assertThrows(ValidationException.class, () -> controller.validateUser(user));

        user.setEmail("@validEmail");
        user.setName("");
        controller.validateUser(user);
        assertEquals("validLogin", user.getName());
    }
}