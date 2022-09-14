package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface FilmGenreDao {
    List<Genre> findGenresByFilmId(Integer filmId);
    void createFilmGenreBatch(Integer filmId, Set<Genre> genres);
    void createFilmGenre(Integer filmId, Integer genreId);
    void deleteFilmGenre(Integer filmId, Integer genreId);
    void deleteFilmGenresByFilmId(Integer filmId);
}
