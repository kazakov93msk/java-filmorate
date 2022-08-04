package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utility.IdentifierGenerator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController(new IdentifierGenerator());
    }

    @Test
    public void shouldReturnValidationExceptionWhenFilmInvalid() {
        Film filmLongDescr = new Film("filmWithLongDescr", "a".repeat(201),
                100, LocalDate.of(2000, 1, 1));
        Film filmFrom1890 = new Film("Very Old Film", "Cinema not was invented!",
                100, LocalDate.of(1800, 1, 1));
        Film filmWithDurationBelowZero = new Film("Below Zero", "It's already over!",
                -100, LocalDate.of(2000, 1, 1));
        Film filmWithInvalidId = new Film("Invalid Identifier", "Id Invalid",
                100, LocalDate.of(2000, 1, 1));
        filmWithInvalidId.setId(-1);

        assertThrows(ValidationException.class, () -> filmController.create(filmLongDescr),
                "Ожидалось ValidationException, возвращено некорректное исключение");
        assertThrows(ValidationException.class, () -> filmController.create(filmFrom1890),
                "Ожидалось ValidationException, возвращено некорректное исключение");
        assertThrows(ValidationException.class, () -> filmController.create(filmWithInvalidId),
                "Ожидалось ValidationException, возвращено некорректное исключение");
    }

    @Test
    public void shouldReturnFilmAfterAdded() {
        Film validFilm = new Film("Film", "Descr",
                120, LocalDate.of(2000, 1, 1));
        Film returnedFilm = filmController.create(validFilm);
        validFilm.setId(1);
        assertEquals(validFilm, returnedFilm,
                "Добавленный фильм не совпадает с изначальным");
    }

}
