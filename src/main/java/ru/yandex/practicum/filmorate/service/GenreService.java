package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreService {
    private final GenreDao genreDao;

    public List<Genre> findAllGenres() {
        return genreDao.findAllGenres();
    }

    public Genre findGenreById(Integer genreId) {
        return genreDao.findGenreById(genreId).orElseThrow(
                () -> new NotFoundException(String.format("Жанра с ID %d не найдено.", genreId))
        );
    }

}
