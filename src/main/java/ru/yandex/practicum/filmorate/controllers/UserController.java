package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private Integer userId = 0;
    private Map<Integer, User> users = new HashMap<>();

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        validateUser(user);
        if ((user.getName() == null || user.getName().length() == 0)) {
            user.setName(user.getLogin());
        }
        user.setId(++userId);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            validateUser(user);
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Такого id нет");
        }
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return List.copyOf(users.values());
    }

    void validateUser(User user) throws ValidationException {
        if (user.getBirthday().isAfter(LocalDate.of(2022, 9, 13))) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}

