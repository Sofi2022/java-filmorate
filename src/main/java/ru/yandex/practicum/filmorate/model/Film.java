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
@AllArgsConstructor
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

    private  Set<Integer> likes = new HashSet<>();

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void addLike(int userId){
        likes.add(userId);
        System.out.println("likes: " + likes);
        //rate = likes.size();
    }

    public void deleteLike(int userId){
        likes.remove(userId);
    }

    public Set<Integer> getLikes() {
        return likes;
    }
}
