package ru.yandex.practicum.filmorate.exceptionsHandler;

public class AlreadyExists extends RuntimeException{

    public AlreadyExists(String message){
        super(message);
    }
}
