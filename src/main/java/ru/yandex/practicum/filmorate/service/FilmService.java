package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdentifierException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.findAll();
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id).orElseThrow(
                () -> new NotFoundException(String.format("Фильм с ID %d не найден.", id))
        );
    }

    public Film addLike(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);
        User user = userService.getUserById(userId);
        validateFilm(film);
        validateUser(user);
        if (film.getLikes().contains(user.getId())) {
            throw new AlreadyExistException(String.format("Лайк на фильме %s от пользователя %s уже стоит.",
                    film.getName(), user.getEmail()
            ));
        }
        film.addLike(user);
        return film;
    }

    public Film removeLike(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);
        User user = userService.getUserById(userId);
        validateFilm(film);
        validateUser(user);
        if (!film.getLikes().contains(user.getId())) {
            throw new NotFoundException(String.format("Нет лайка на фильме %s от пользователя %s.",
                    film.getName(), user.getName()
            ));
        }
        film.removeLike(user);
        return film;
    }

    public List<Film> getPopular(Integer count) {
        return filmStorage.findAll().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validateFilm(Film film) {
        if (!filmStorage.findAll().contains(film)) {
            throw new NotFoundException(String.format("Фильма %s не существует.", film.getName()));
        }
        if (film.getId() != null && film.getId() <= 0) {
            throw new IncorrectIdentifierException("ID фильма не может быть меньше или равен нулю.");
        }
    }

    private void validateUser(User user) {
        if (!userService.getAllUsers().contains(user)) {
            throw new NotFoundException(String.format("Пользователя с email %s не существует.", user.getEmail()));
        }
        if (user.getId() != null && user.getId() <= 0) {
            throw new IncorrectIdentifierException("ID пользователя не может быть меньше или равен нулю.");
        }
    }
}
