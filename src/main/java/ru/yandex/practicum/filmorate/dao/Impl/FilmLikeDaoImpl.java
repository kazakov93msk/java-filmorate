package ru.yandex.practicum.filmorate.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.rowmapper.UserMapper;

import java.util.List;

@Component
public class FilmLikeDaoImpl implements FilmLikeDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmLikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findLikesByFilmId(Integer filmId) {
        String sql = "SELECT u.* FROM films_likes fl JOIN users u ON u.id = fl.user_id WHERE fl.film_id = ?";
        return jdbcTemplate.query(sql, new UserMapper(), filmId);
    }

    @Override
    public void createLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO films_likes VALUES(?,?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }
}
