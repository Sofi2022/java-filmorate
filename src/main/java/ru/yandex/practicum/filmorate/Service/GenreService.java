package ru.yandex.practicum.filmorate.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component("Genre")
public class GenreService {

    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage){
        this.genreDbStorage = genreDbStorage;
    }

    public List<Genre> getAllGenres(){
        return genreDbStorage.getAllGenres();
    }

    public Genre getGenreById(int id){
        return genreDbStorage.getGenreById(id);
    }
}
