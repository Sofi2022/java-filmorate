package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
@Data
@NoArgsConstructor
//@AllArgsConstructor
public class Film {

    private Integer id;

	@NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;

    @NotNull
    private LocalDate releaseDate;
    @Min(1)
    private int duration;

    private int rate;

    private Mpa mpa;

    Set<Genre> genres = new HashSet<>();
    private  Set<Integer> likes = new HashSet<>();

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, int rate, Mpa mpa,
                Set<Genre> genres, Set<Integer> likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = genres;
        this.likes = likes;
    }

    public Film(Integer id, String name, String description, LocalDate releaseDate, int duration, int rate, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
    }

    public void addLike(int userId){
        likes.add(userId);
        System.out.println("likes: " + likes);
        rate = likes.size();
    }

    public void deleteLike(int userId){
        likes.remove(userId);
    }

    public Set<Integer> getLikes() {
        return likes;
    }

    //public int getRate() {
     //   return rate;
    //}

    public Mpa getMpa(){
        return mpa;
    }

    public Set<Genre> getGenres(){
        return genres;
    }
}
