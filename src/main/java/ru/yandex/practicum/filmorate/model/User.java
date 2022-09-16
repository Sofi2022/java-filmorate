package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {


    private Integer id;
    @NotBlank
    @Email
    @NotNull
    private String email;
    @Pattern(regexp = "^\\S*$")
    @NotBlank
    @NotNull
    private String login;

    private String name;
    @NotNull
    private LocalDate birthday;


    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
