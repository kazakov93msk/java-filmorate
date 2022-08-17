package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectIdentifierException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Component
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable(name = "id") Integer id) {
        validateId(id);
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> findPopular(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        return filmService.getPopular(count);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(
            @PathVariable(name = "id") Integer id,
            @PathVariable(name = "userId") Integer userId
    ) {
        validateId(id);
        validateId(userId);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(
            @PathVariable(name = "id") Integer id,
            @PathVariable(name = "userId") Integer userId
    ) {
        validateId(id);
        validateId(userId);
        return filmService.removeLike(id, userId);
    }
    
    private void validateId(Integer id) {
        if (id <= 0) {
            throw new IncorrectIdentifierException("ID не может быть меньше или равен нулю.");
        }
    }
}
