package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.RatingMpaDao;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.rowmapper.RatingMpaMapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingMpaDaoImpl implements RatingMpaDao {

    private final JdbcTemplate jdbcTemplate;
    private final RatingMpaMapper mpaMapper;

    @Override
    public List<RatingMpa> findAllRatingsMpa() {
        return jdbcTemplate.query("SELECT * FROM ratings_mpa", mpaMapper);
    }

    @Override
    public Optional<RatingMpa> findRatingMpaById(Integer ratingId) {
        String sql = "SELECT * FROM ratings_mpa WHERE id = ?";
        return jdbcTemplate.query(sql, mpaMapper, ratingId).stream().findFirst();
    }

    @Override
    public Optional<RatingMpa> findRatingMpaByFilmId(Integer filmId) {
        String sql = "SELECT r.* FROM ratings_mpa r JOIN films f ON f.mpa = r.id WHERE f.id = ?";
        return jdbcTemplate.query(sql, mpaMapper, filmId).stream().findFirst();
    }

    @Override
    public RatingMpa createRatingMpa(RatingMpa ratingMpa) {
        String sql = "INSERT INTO ratings_mpa (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, ratingMpa.getName());
            return stmt;
        }, keyHolder);
        ratingMpa.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return ratingMpa;
    }

    @Override
    public RatingMpa updateRatingMpa(RatingMpa ratingMpa) {
        String sql = "UPDATE ratings_mpa SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, ratingMpa.getName(), ratingMpa.getId());
        return ratingMpa;
    }

    @Override
    public void deleteRatingMpa(Integer ratingId) {
        jdbcTemplate.update("DELETE FROM ratings_mpa WHERE id = ?", ratingId);
    }
}
