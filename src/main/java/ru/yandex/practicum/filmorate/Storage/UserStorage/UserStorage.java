package ru.yandex.practicum.filmorate.Storage.UserStorage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

//методы добавления, удаления, модификации и поиска объектов.

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    User getUserById(int id);

    List<User> getAllUsers();

    void validateUser(User user);
}
