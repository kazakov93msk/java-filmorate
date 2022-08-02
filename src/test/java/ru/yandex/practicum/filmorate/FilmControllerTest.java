package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    public void shouldReturnValidationExceptionWhenFilmInvalid() {
        Film filmLongDescr = new Film("filmWithLongDescr", "a".repeat(201));
        Film filmFrom1890 = new Film("Very Old Film", "Cinema not was invented!");
        filmFrom1890.setReleaseDate(LocalDate.of(1890, 1, 1));
        Film filmWithDurationBelowZero = new Film("Below Zero", "It's already over!");
        filmWithDurationBelowZero.setDuration(-100);
        Film filmWithInvalidId = new Film("Invalid Identifier", "Id Invalid");
        filmWithInvalidId.setId(-1);

        assertThrows(ValidationException.class, () -> filmController.create(filmLongDescr),
                "Ожидалось ValidationException, возвращено некорректное исключение");
        assertThrows(ValidationException.class, () -> filmController.create(filmFrom1890),
                "Ожидалось ValidationException, возвращено некорректное исключение");
        assertThrows(ValidationException.class, () -> filmController.create(filmWithDurationBelowZero),
                "Ожидалось ValidationException, возвращено некорректное исключение");
        assertThrows(ValidationException.class, () -> filmController.create(filmWithInvalidId),
                "Ожидалось ValidationException, возвращено некорректное исключение");
    }

    @Test
    public void shouldReturnFilmAfterAdded() {
        Film validFilm = new Film("Film", "Descr");
        validFilm.setDuration(120);
        validFilm.setReleaseDate(LocalDate.of(2022, 1, 1));
        Film returnedFilm = filmController.create(validFilm);
        validFilm.setId(1);
        assertEquals(validFilm, returnedFilm,
                "Добавленный фильм не совпадает с изначальным");
    }

}
