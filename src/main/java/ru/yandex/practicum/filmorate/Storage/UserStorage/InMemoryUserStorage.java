package ru.yandex.practicum.filmorate.Storage.UserStorage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptionsHandler.NotFoundException;
import ru.yandex.practicum.filmorate.exceptionsHandler.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component("MemoryStorage")
public class InMemoryUserStorage implements UserStorage {

    private Integer userId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    public Set<Integer> getUsersIds() {
        return users.keySet();
    }

    @Override
    public User addUser(User user) {
        if (users.containsKey(user.getId())) {
            return updateUser(user);
        } else {
            //validateUser(user);
            user.setId(++userId);
            users.put(user.getId(), user);
            return user;
        }
    }

    @Override
    public User updateUser(User user) {
        if(!(users.containsKey(user.getId()))){
            throw new NotFoundException("Добавьте изменения пользователя");
        }
            //validateUser(user);
            users.put(user.getId(), user);
            return user;
    }

    @Override
    public User getUserById(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Такого пользователя нет");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    @Override
    public void validateUser(User user) {
        if (user == null) {
            throw new ValidationException("Объект не может быть пустым");
        }
        if (user.getBirthday().isAfter(LocalDate.of(2022, 9, 13))) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if ((user.getName() == null || user.getName().length() == 0)) {
            user.setName(user.getLogin());
        }
    }
}

