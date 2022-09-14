package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@Component
@Validated
@RequestMapping("/films")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAllFilms();
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable(name = "id") @Positive Integer id) {
        return filmService.findFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> findPopular(@RequestParam(name = "count", defaultValue = "10") @Positive Integer count) {
        return filmService.getPopular(count);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(
            @PathVariable(name = "id") @Positive Integer id,
            @PathVariable(name = "userId") @Positive Integer userId
    ) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(
            @PathVariable(name = "id") @Positive Integer id,
            @PathVariable(name = "userId") @Positive Integer userId
    ) {
        return filmService.removeLike(id, userId);
    }
}
