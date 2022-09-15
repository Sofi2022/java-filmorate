package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Data
@NoArgsConstructor
public class Film {

    private Integer id;

	@NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;

    private LocalDate releaseDate;
    @Min(1)
    private int duration;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
