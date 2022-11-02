package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Genre {
    int id;
    String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
