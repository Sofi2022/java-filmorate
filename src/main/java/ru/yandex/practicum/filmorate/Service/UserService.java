package ru.yandex.practicum.filmorate.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Storage.UserStorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;

import java.util.List;

@Service
public class UserService {

    private final UserDbStorage userDbStorage;
    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserDbStorage userDbStorage,
                       @Qualifier("MemoryStorage") InMemoryUserStorage userStorage){
        this.userDbStorage = userDbStorage;
        this.userStorage = userStorage;
    }


    public void addFriend(int userId, int friendId){
//        User user = userStorage.getUserById(userId);
//        User newFriend = userStorage.getUserById(friendId);
//        user.addFriend(friendId);
//        newFriend.addFriend(userId);
        userDbStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId){
//        User user = userStorage.getUserById(userId);
//        User newFriend = userStorage.getUserById(friendId);
//        user.deleteFriend(friendId);
//        newFriend.deleteFriend(userId);
        userDbStorage.deleteFriend(userId, friendId);
    }

    public List<User> findCommonFriends(int userId, int friendId){
//        User user = userStorage.getUserById(userId);
//        User newFriend = userStorage.getUserById(friendId);
//
//        return user.getFriends().stream().
//                filter(newFriend.getFriends() ::contains).
//                map(id -> userStorage.getUserById(id)).
//                collect(Collectors.toList());
        return userDbStorage.findCommonFriends(userId, friendId);
    }

    public User addUser(User user){
        return userDbStorage.addUser(user);
    }


    public User updateUser(User user){
       return userDbStorage.updateUser(user);
    }

    public User getUser(int id){
        return userDbStorage.getUserById(id);
    }

    public List<User> getAllUsers(){
        return userDbStorage.getAllUsers();
    }

    public List<User> getUserFriends(int userId){
//        User user = userStorage.getUserById(userId);
//        List<Integer> ids = List.copyOf(user.getFriends());
//        return ids.stream()
//                .map(id -> userStorage.getUserById(id)).
//                collect(Collectors.toList());
        return userDbStorage.getUserFriends(userId);

    }
}

