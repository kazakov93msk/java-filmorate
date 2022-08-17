package ru.yandex.practicum.filmorate.exception;

public class IncorrectIdentifierException extends RuntimeException {
    public IncorrectIdentifierException(String message) {
        super(message);
    }
}
