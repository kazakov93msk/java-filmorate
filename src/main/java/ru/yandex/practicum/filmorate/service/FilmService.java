package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdentifierException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmService {

    private final LocalDate FIRST_FILM = LocalDate.of(1895, 12, 28);
    private final FilmDao filmDao;
    private final FilmLikeDao likeDao;
    private final RatingMpaDao mpaDao;
    private final FilmGenreDao filmGenreDao;
    private final UserService userService;


    public List<Film> findAllFilms() {
        return filmDao.findAllFilms();
    }

    public Film findFilmById(Integer filmId) {
        Film returnedFilm = filmDao.findFilmById(filmId).orElseThrow(
                () -> new NotFoundException(String.format("Фильм с ID %d не найден.", filmId))
        );
        return fillFilmData(returnedFilm);
    }

    public Film createFilm(Film film) {
        validate(film);
        List<Genre> filmGenres = film.getGenres();
        Film returnedFilm = fillFilmData(filmDao.createFilm(film));
        if (filmGenres != null && !filmGenres.isEmpty()) {
            filmGenreDao.createFilmGenreBatch(returnedFilm.getId(), new HashSet<>(filmGenres));
        }
        return fillFilmData(returnedFilm);
    }

    public Film updateFilm(Film film) {
        validate(film);
        List<Genre> filmGenres = film.getGenres();
        Film returnedFilm = fillFilmData(filmDao.updateFilm(film));
        filmGenreDao.deleteFilmGenresByFilmId(film.getId());
        if (filmGenres != null && !filmGenres.isEmpty()) {
            filmGenreDao.createFilmGenreBatch(returnedFilm.getId(), new HashSet<>(filmGenres));
        }
        return fillFilmData(returnedFilm);
    }

    public Film addLike(Integer filmId, Integer userId) {
        Film film = findFilmById(filmId);
        User user = userService.findUserById(userId);
        if (likeDao.findLikesByFilmId(filmId).contains(user)) {
            throw new AlreadyExistException(String.format("Лайк на фильме %s от пользователя %s уже стоит.",
                    film.getName(), user.getEmail()
            ));
        }
        likeDao.createLike(filmId, userId);
        film.setRate(film.getRate() + 1);
        return fillFilmData(film);
    }

    public Film removeLike(Integer filmId, Integer userId) {
        Film film = findFilmById(filmId);
        User user = userService.findUserById(userId);
        if (!likeDao.findLikesByFilmId(filmId).contains(user)) {
            throw new NotFoundException(String.format("Нет лайка на фильме %s от пользователя %s.",
                    film.getName(), user.getName()
            ));
        }
        likeDao.deleteLike(filmId, userId);
        return fillFilmData(film);
    }

    public List<Film> getPopular(Integer count) {
        return likeDao.findPopular(count);
    }

    private Film fillFilmData(Film film) {
        film.setGenres(filmGenreDao.findGenresByFilmId(film.getId()));
        return film;
    }

    private void validate(Film film) {
        if (film.getId() != null && film.getId() <= 0) {
            throw new IncorrectIdentifierException("ID фильма не может быть меньше или равен нулю.");
        }
        if (film.getReleaseDate().isBefore(FIRST_FILM)) {
            throw new ValidationException("Дата релиза фильма не может быть меньше, чем " + FIRST_FILM + ".");
        }
    }
}
