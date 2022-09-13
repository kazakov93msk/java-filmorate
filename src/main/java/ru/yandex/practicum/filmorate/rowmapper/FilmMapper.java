package ru.yandex.practicum.filmorate.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Film(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("duration"),
                rs.getDate("release_date").toLocalDate(),
                null,
                new RatingMpa(rs.getInt("mpa"), null),
                null
        );
    }
}
