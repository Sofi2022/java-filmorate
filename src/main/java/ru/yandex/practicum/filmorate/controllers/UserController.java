package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private Integer userId = 1;
    private Map<Integer, User> users = new HashMap<>();

    void increaseUserId(Integer userId){
        this.userId++;
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) throws ValidationException {
        increaseUserId(user.getId());
        validateUser(user);
        return user;
        }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (!(users.containsKey(user.getId()))) {
            throw new ValidationException("Такого id нет");
        }
        validateUser(user);
        return user;
    }

//        if(users.containsKey(user.getId())) {
//            validateUser(user);
//        }else{
//            throw new ValidationException("Такого id нет");
//        }
//            return user;

    @GetMapping("/users")
    public List<User> getUsers () {
        return List.copyOf(users.values());
    }

    void validateUser(User user) throws ValidationException {
        if (user.getEmail() == null || !(user.getEmail().contains("@"))) {
            throw new ValidationException("Укажите Email с символом @");
        } else if (user.getLogin().length() == 0 || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        } else if (user.getBirthday().isAfter(LocalDate.of(2022, 9, 13))) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        } else {
            users.put(user.getId(), user);
        }
    }
}

