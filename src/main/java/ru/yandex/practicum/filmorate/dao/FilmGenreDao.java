package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmGenreDao {
    List<Genre> findGenresByFilmId(Integer filmId);
    void createFilmGenre(Integer filmId, Integer genreId);
    void deleteFilmGenre(Integer filmId, Integer genreId);
    void deleteFilmGenresByFilmId(Integer filmId);
}
