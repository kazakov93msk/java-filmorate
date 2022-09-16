package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;
import java.util.Optional;

public interface RatingMpaDao {
    List<RatingMpa> findAllRatingsMpa();
    Optional<RatingMpa> findRatingMpaById(Integer ratingId);
    Optional<RatingMpa> findRatingMpaByFilmId(Integer filmId);
    RatingMpa createRatingMpa(RatingMpa ratingMpa);
    RatingMpa updateRatingMpa(RatingMpa ratingMpa);
    void deleteRatingMpa(Integer ratingId);
}
