package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.RatingMpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingMpaService {
    private final RatingMpaDao ratingMpaDao;

    public List<RatingMpa> findAllRatingMpa() {
        return ratingMpaDao.findAllRatingsMpa();
    }

    public RatingMpa findRatingMpaById(Integer ratingId) {
        return ratingMpaDao.findRatingMpaById(ratingId).orElseThrow(
                () -> new NotFoundException(String.format("Рейтинга MPA с ID %d не найдено.", ratingId))
        );
    }

}
