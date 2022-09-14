package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.rowmapper.FilmMapper;
import ru.yandex.practicum.filmorate.rowmapper.UserMapper;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmLikeDaoImpl implements FilmLikeDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;
    private final FilmMapper filmMapper;


    @Override
    public List<User> findLikesByFilmId(Integer filmId) {
        String sql = "SELECT u.* FROM films_likes fl JOIN users u ON u.id = fl.user_id WHERE fl.film_id = ?";
        return jdbcTemplate.query(sql, userMapper, filmId);
    }

    @Override
    public List<Film> findPopular(Integer size) {
        String sql = "SELECT f.*, r.name AS rating, COUNT(user_id) AS likes "
                + "FROM films f JOIN ratings_mpa r ON r.id = f.mpa "
                + "JOIN films_likes fl ON fl.film_id = f.id "
                + "GROUP BY f.id ORDER BY likes DESC LIMIT ?";
        return jdbcTemplate.query(sql, filmMapper, size);
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
