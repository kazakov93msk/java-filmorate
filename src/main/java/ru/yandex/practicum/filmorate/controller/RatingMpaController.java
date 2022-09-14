package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.RatingMpaService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@Component
@Validated
@RequestMapping("/mpa")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingMpaController {
    private final RatingMpaService mpaService;

    @GetMapping
    public List<RatingMpa> findAllGenres() {
        return mpaService.findAllRatingMpa();
    }

    @GetMapping("/{id}")
    public RatingMpa findGenreById(@PathVariable(name = "id") @Positive Integer ratingId) {
        return mpaService.findRatingMpaById(ratingId);
    }
}
