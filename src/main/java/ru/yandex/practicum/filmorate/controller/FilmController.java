package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Component
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;
    private final UserStorage userStorage;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.userStorage = userStorage;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable(name = "id") Integer id) {
        return filmStorage.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> findPopular(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        return filmService.getPopular(count);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmStorage.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(
            @PathVariable(name = "id") Integer id,
            @PathVariable(name = "userId") Integer userId
    ) {
        return filmService.addLike(filmStorage.getFilmById(id), userStorage.getUserById(userId));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(
            @PathVariable(name = "id") Integer id,
            @PathVariable(name = "userId") Integer userId
    ) {
        return filmService.removeLike(filmStorage.getFilmById(id), userStorage.getUserById(userId));
    }
}
