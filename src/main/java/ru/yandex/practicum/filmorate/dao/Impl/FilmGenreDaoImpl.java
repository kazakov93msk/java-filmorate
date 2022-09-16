package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.rowmapper.GenreMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmGenreDaoImpl implements FilmGenreDao {
    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    @Override
    public List<Genre> findGenresByFilmId(Integer filmId) {
        String sql = "SELECT g.* FROM genres g JOIN films_genres fg ON g.id = fg.genre_id WHERE fg.film_id = ?";
        return jdbcTemplate.query(sql, genreMapper, filmId);
    }

    @Override
    public void createFilmGenreBatch(Integer filmId, Set<Genre> genres) {
        List<Object[]> batch = new ArrayList<>();
        for (Genre genre : genres) {
            Integer[] values = new Integer[] {filmId, genre.getId()};
            batch.add(values);
        }
        jdbcTemplate.batchUpdate("INSERT INTO films_genres (film_id, genre_id) VALUES (?,?)", batch);
    }

    @Override
    public void createFilmGenre(Integer filmId, Integer genreId) {
        String sql = "INSERT INTO films_genres (film_id, genre_id) VALUES (?,?)";
        jdbcTemplate.update(sql, filmId, genreId);
    }

    @Override
    public void deleteFilmGenre(Integer filmId, Integer genreId) {
        String sql = "DELETE FROM films_genres WHERE film_id = ? AND genre_id = ?";
        jdbcTemplate.update(sql, filmId, genreId);
    }

    @Override
    public void deleteFilmGenresByFilmId(Integer filmId) {
        String sql = "DELETE FROM films_genres WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }
}
