package ru.yandex.practicum.filmorate.exceptionsHandler;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message){
        super(message);
    }
}
