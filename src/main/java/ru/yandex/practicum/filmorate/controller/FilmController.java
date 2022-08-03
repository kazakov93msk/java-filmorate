package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utility.IdentifierGenerator;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
public class FilmController {
    private final Set<Film> films = new HashSet<>();
    private final IdentifierGenerator idGen = new IdentifierGenerator();

    @GetMapping(("/films"))
    public List<Film> allFilms() {
        return new ArrayList<>(films);
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Получен POST-запрос на добавление " + film.toString());
        validateFilm(film);
        if (films.contains(film)) {
            throw new AlreadyExistException("Фильм с названием " + film.getName() + " уже существует");
        }
        if (film.getId() == null || idGen.isBusy(film.getId())) {
            film.setId(idGen.getNextId());
        } else {
            idGen.toBusyIdList(film.getId());
        }
        films.add(film);
        return film;
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Получен PUT-запрос на обновление " + film.toString());
        validateFilm(film);
        if (film.getId() == null) {
            film.setId(idGen.getNextId());
        } else if (!films.contains(film) && film.getId() != null) {
            idGen.toBusyIdList(film.getId());
        }
        films.removeIf(film::equals);
        films.add(film);
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Длина описания не должна быть более 200 символов (обнаружено "
                    + film.getDescription().length() + ").");
        } else if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(
                LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата резила не может быть раньше 28.12.1895г. (обнаружено "
                    + film.getReleaseDate() + ")");
        } else if (film.getDuration() != null && film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма не может быть отрицательной (обнаружено "
                    + film.getDuration() + " минут)");
        } else if (film.getId() != null && film.getId() <= 0) {
            throw new ValidationException("ID сущности не может быть равно 0 или меньше 0");
        }
    }
}
