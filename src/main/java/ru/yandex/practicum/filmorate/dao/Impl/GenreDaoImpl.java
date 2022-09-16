package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.rowmapper.GenreMapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    @Override
    public List<Genre> findAllGenres() {
        return jdbcTemplate.query("SELECT * FROM genres ORDER BY id", genreMapper);
    }

    @Override
    public Optional<Genre> findGenreById(Integer genreId) {
        String sql = "SELECT * FROM genres WHERE id = ?";
        return jdbcTemplate.query(sql, genreMapper, genreId).stream().findFirst();
    }

    @Override
    public Genre createGenre(Genre genre) {
        String sql = "INSERT INTO genres (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, genre.getName());
            return stmt;
        }, keyHolder);
        genre.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return genre;
    }

    @Override
    public Genre updateGenre(Genre genre) {
        String sql = "UPDATE genres SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, genre.getName(), genre.getId());
        return genre;
    }

    @Override
    public void deleteGenre(Integer genreId) {
        jdbcTemplate.update("DELETE FROM genres WHERE id = ?", genreId);
    }
}
