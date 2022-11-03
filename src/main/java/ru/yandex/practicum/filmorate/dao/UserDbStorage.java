package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptionsHandler.NotFoundException;
import ru.yandex.practicum.filmorate.exceptionsHandler.ValidationException;
import ru.yandex.practicum.filmorate.Storage.UserStorage.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;


@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        validateUser(user);
        String sqlQuery = "insert into USERS(LOGIN, EMAIL, USER_NAME, USER_BIRTHDAY) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "update USERS set EMAIL = ?, LOGIN = ?, USER_NAME = ?, USER_BIRTHDAY = ? " +
                "where USER_ID = ?";
        String sqlQuery2 = "select EMAIL, LOGIN, USER_NAME, USER_BIRTHDAY from USERS where USER_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery2, user.getId());
        if (userRows.next()) {
            jdbcTemplate.update(sqlQuery
                    , user.getEmail()
                    , user.getLogin()
                    , user.getName()
                    , user.getBirthday()
                    , user.getId());
            return user;
        }
        throw new NotFoundException("Такого пользователя нет");
    }

    @Override
    public User getUserById(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select USER_ID, LOGIN, EMAIL, USER_NAME, USER_BIRTHDAY " +
                "from USERS where USER_ID = ?", id);
        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("USER_ID"),
                    userRows.getString("EMAIL"),
                    userRows.getString("LOGIN"),
                    userRows.getString("USER_NAME"),
                    userRows.getDate("USER_BIRTHDAY").toLocalDate());
            log.info("Найден пользователь: {} {}", userRows.getString("USER_ID"),
                    userRows.getString("USER_NAME"));
            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Такого пользователя нет");
        }
    }

    User makeUser(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("USER_ID");
        String email = rs.getString("EMAIL");
        String login = rs.getString("LOGIN");
        String name = rs.getString("USER_NAME");
        LocalDate birthday = rs.getDate("USER_BIRTHDAY").toLocalDate();
        return new User(id, email, login, name, birthday);
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "select USER_ID,  LOGIN, EMAIL, USER_NAME, USER_BIRTHDAY from USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
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


