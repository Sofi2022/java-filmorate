package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component("FriendsDbStorage")
public class FriendsDbStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    public FriendsDbStorage(JdbcTemplate jdbcTemplate,
                            @Qualifier("UserDbStorage") UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    public void addFriend(int userId, int friendId) {
        userDbStorage.getUserById(userId);
        userDbStorage.getUserById(friendId);
        String sqlQuery = "merge into FRIENDS(USER_ID, FRIEND_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        String sqlQuery = "delete from FRIENDS where USER_ID = ? AND FRIEND_ID = ?";
        String sqlQuery2 = "select EMAIL, LOGIN, USER_NAME, USER_BIRTHDAY from USERS where USER_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery2, userId);
        if (userRows.next()) {
            jdbcTemplate.update(sqlQuery, userId, friendId);
        }
    }

    public List<User> getUserFriends(int id) {
        String sqlQuery = "select * from USERS, FRIENDS where USERS.USER_ID = FRIENDS.FRIEND_ID and FRIENDS.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> userDbStorage.makeUser(rs), id);
    }

    public List<User> findCommonFriends(int userId, int friendId) {
        final String sqlQuery = "select * from users u, friends f, friends o " +
                "where u.USER_ID = f.FRIEND_ID and u.USER_ID = o.FRIEND_ID and f.USER_ID = ? and o.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> userDbStorage.makeUser(rs), userId, friendId);
    }
}
