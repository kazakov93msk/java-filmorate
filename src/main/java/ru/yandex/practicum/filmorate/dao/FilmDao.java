package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    List<Film> findAllFilms();
    Optional<Film> findFilmById(Integer filmId);
    Film createFilm(Film film);
    Film updateFilm(Film film);
    void deleteFilmById(Integer filmId);
}
