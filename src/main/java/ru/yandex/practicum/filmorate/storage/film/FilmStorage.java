package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);

    List<Film> findAll();

    Film getFilmById(Integer filmId);

    Film create(Film film);

    Film update(Film film);

    void validate(Film film);
}
