package ru.yandex.practicum.filmorate.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Storage.UserStorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage){
        this.userStorage = userStorage;
    }


    public void addFriend(int userId, int friendId){
        User user = userStorage.getUserById(userId);
        User newFriend = userStorage.getUserById(friendId);
        user.addFriend(friendId);
        newFriend.addFriend(userId);
    }

    public void deleteFriend(int userId, int friendId){
        User user = userStorage.getUserById(userId);
        User newFriend = userStorage.getUserById(friendId);
        user.deleteFriend(friendId);
        newFriend.deleteFriend(userId);
    }

    public List<User> findCommonFriends(int userId, int friendId){
        User user = userStorage.getUserById(userId);
        User newFriend = userStorage.getUserById(friendId);

        return user.getFriends().stream().
                filter(newFriend.getFriends() ::contains).
                map(id -> userStorage.getUserById(id)).
                collect(Collectors.toList());
    }

    public User addUser(User user){
        return userStorage.addUser(user);
    }


    public User updateUser(User user){
       return userStorage.updateUser(user);
    }

    public User getUser(int id){
        return userStorage.getUserById(id);
    }

    public List<User> getAllUsers(){
        return userStorage.getAllUsers();
    }

    public List<User> getUserFriends(int userId){
        User user = userStorage.getUserById(userId);
        List<Integer> ids = List.copyOf(user.getFriends());
        return ids.stream()
                .map(id -> userStorage.getUserById(id)).
                collect(Collectors.toList());
    }
}

