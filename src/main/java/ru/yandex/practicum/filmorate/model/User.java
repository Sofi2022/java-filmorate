package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @JsonIgnore
    private Set<Integer> friends = new HashSet<>();


    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFriend(int id){
        friends.add(id);
    }

    public void deleteFriend(int id){
        friends.remove(id);
    }
}
