package ru.yandex.practicum.filmorate.Exceptions;

import java.io.IOException;

public class ValidationException extends IOException {
    public ValidationException(String message){
        super(message);
    }
}
