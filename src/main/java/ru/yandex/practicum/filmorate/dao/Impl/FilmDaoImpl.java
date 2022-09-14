package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.rowmapper.FilmMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;

    @Override
    public List<Film> findAllFilms() {
        String sql = "SELECT f.*, r.name AS rating, COUNT(user_id) AS likes "
                + "FROM films f JOIN ratings_mpa r ON f.mpa = r.id "
                + "LEFT JOIN films_likes fl ON f.id = fl.film_id "
                + "GROUP BY f.id";
        return jdbcTemplate.query(sql, filmMapper);
    }

    @Override
    public Optional<Film> findFilmById(Integer filmId) {
        String sql = "SELECT f.*, r.name AS rating, COUNT(user_id) AS likes "
                + "FROM films f JOIN ratings_mpa r ON f.mpa = r.id "
                + "LEFT JOIN films_likes fl ON f.id = fl.film_id "
                + "WHERE f.id = ?"
                + "GROUP BY f.id";
        return jdbcTemplate.query(sql, filmMapper, filmId).stream().findFirst();
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa) VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        return film;
    }

    @Override
    public void deleteFilmById(Integer filmId) {
        jdbcTemplate.update("DELETE FROM films WHERE id = ?");
    }
}
