package ru.yandex.practicum.filmorate.Exceptions;

public class AlreadyExists extends RuntimeException{

    public AlreadyExists(String message){
        super(message);
    }
}
