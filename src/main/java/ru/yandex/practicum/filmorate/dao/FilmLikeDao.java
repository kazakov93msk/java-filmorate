package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmLikeDao {
    List<User> findLikesByFilmId(Integer filmId);
    void createLike(Integer filmId, Integer userId);
    void deleteLike(Integer filmId, Integer userId);
}
