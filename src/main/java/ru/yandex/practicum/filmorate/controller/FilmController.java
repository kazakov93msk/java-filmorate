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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {
    public static LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private final IdentifierGenerator idGen;

    public FilmController(IdentifierGenerator idGen) {
        this.idGen = idGen;
    }

    @GetMapping(("/films"))
    public List<Film> allFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Получен POST-запрос на добавление " + film.toString());
        validateFilm(film);
        if (films.containsValue(film)) {
            throw new AlreadyExistException("Фильм с названием " + film.getName() + " уже существует");
        }
        if (film.getId() == null || idGen.isBusy(film.getId())) {
            film.setId(idGen.getNextId());
        } else {
            idGen.toBusyIdList(film.getId());
        }
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Получен PUT-запрос на обновление " + film.toString());
        validateFilm(film);
        if (film.getId() == null) {
            film.setId(idGen.getNextId());
            films.remove(film.getId());
        } else if (!films.containsValue(film) && film.getId() != null) {
            idGen.toBusyIdList(film.getId());
        }
        films.put(film.getId(), film);
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Длина описания не должна быть более 200 символов (обнаружено "
                    + film.getDescription().length() + ").");
        } else if (film.getReleaseDate().isBefore(FIRST_FILM_DATE)) {
            throw new ValidationException("Дата резила не может быть раньше 28.12.1895г. (обнаружено "
                    + film.getReleaseDate() + ")");
        } else if (film.getId() != null && film.getId() <= 0) {
            throw new ValidationException("ID сущности не может быть равно 0 или меньше 0");
        }
    }
}
