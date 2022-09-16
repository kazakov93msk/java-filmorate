package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    List<Genre> findAllGenres();
    Optional<Genre> findGenreById(Integer genreId);
    Genre createGenre(Genre genre);
    Genre updateGenre(Genre genre);
    void deleteGenre(Integer genreId);
}
