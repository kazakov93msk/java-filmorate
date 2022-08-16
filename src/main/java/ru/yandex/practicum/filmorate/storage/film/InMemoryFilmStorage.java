package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdentifierException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utility.IdentifierGenerator;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private final IdentifierGenerator idGen;

    @Autowired
    public InMemoryFilmStorage(IdentifierGenerator idGen) {
        this.idGen = idGen;
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Integer filmId) {
        if (filmId <= 0) {
            throw new IncorrectIdentifierException("ID фильма не может быть меньше или равно нулю.");
        }
        return films.get(filmId);
    }

    @Override
    public Film create(Film film) {
        validate(film);
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

    @Override
    public Film update(Film film) {
        validate(film);
        if (film.getId() == null) {
            film.setId(idGen.getNextId());
            films.remove(film.getId());
        } else if (!films.containsValue(film) && film.getId() != null) {
            idGen.toBusyIdList(film.getId());
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(FIRST_FILM_DATE)) {
            throw new ValidationException("Дата резила не может быть раньше 28.12.1895г. (обнаружено "
                    + film.getReleaseDate() + ")");
        } else if (film.getId() != null && film.getId() <= 0) {
            throw new NotFoundException("ID сущности не может быть равно 0 или меньше 0");
        }
    }
}
