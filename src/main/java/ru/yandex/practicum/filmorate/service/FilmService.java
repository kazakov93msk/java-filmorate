package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdentifierException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(Film film, User user) {
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

    public Film removeLike(Film film, User user) {
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
        if (!userStorage.findAll().contains(user)) {
            throw new NotFoundException(String.format("Пользователя с email %s не существует.", user.getEmail()));
        }
        if (user.getId() != null && user.getId() <= 0) {
            throw new IncorrectIdentifierException("ID пользователя не может быть меньше или равен нулю.");
        }
    }
}
