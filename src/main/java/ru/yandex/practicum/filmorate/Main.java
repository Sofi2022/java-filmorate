package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        User user = new User("test@email", "testLogin", "name", LocalDate.of(1998,02,02));
    }
}
