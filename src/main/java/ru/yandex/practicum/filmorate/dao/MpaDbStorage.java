package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
public class MpaDbStorage {

    private final Logger log = LoggerFactory.getLogger(MpaDbStorage.class);

    private final JdbcTemplate jdbcTemplate;


    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> getAllMpa(){
        String sql = "SELECT * FROM MPA";
        List<Mpa> mpas = jdbcTemplate.query(sql, (rs, rowNum) ->
                new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")));
        return mpas;
    }

    public Mpa getMpaById(int id){
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM MPA WHERE MPA_ID = ?", id);
        if(mpaRows.next()){
            Mpa mpa = new Mpa(mpaRows.getInt("MPA_ID"), mpaRows.getString("MPA_NAME"));
            log.info("Найден Mpa с id{}", id);
            return mpa;
        }
        throw new NotFoundException("Такого Mpa нет");
    }
}
