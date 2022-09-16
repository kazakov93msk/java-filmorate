package ru.yandex.practicum.filmorate.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Film(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("duration"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("likes"),
                new RatingMpa(rs.getInt("mpa"), rs.getString("rating")),
                null
        );
    }
}
