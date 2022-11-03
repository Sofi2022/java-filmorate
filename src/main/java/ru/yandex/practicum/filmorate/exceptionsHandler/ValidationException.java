package ru.yandex.practicum.filmorate.exceptionsHandler;
public class ValidationException extends RuntimeException {
    public ValidationException(String message){
        super(message);
    }
}
