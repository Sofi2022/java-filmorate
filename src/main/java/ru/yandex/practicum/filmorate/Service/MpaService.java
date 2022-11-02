package ru.yandex.practicum.filmorate.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component("Mpa")
public class MpaService {

    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaService(MpaDbStorage mpaDbStorage){
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<Mpa> getAllMpa(){
        return mpaDbStorage.getAllMpa();
    }

    public Mpa getMpaById(int id){
        return mpaDbStorage.getMpaById(id);
    }
}
