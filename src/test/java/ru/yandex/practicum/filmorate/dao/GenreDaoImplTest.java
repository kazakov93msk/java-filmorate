package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql", "/scripts/test_data.sql"})
public class GenreDaoImplTest {
    private final GenreDao genreDao;

    @Test
    void testFindAllGenres() {
        List<Genre> genres = genreDao.findAllGenres();

        assertEquals(6, genres.size(), "Не возвращены все 6 жанров из БД");
    }

    @Test
    void testFindGenreById() {
        Optional<Genre> genreOptional = genreDao.findGenreById(1);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 1)
                );
        assertEquals("Комедия", genreOptional.get().getName(),
                "Возвращен жанр (" + genreOptional.get().getName() + "отличный от ожидаемого (Комедия)"
        );
    }
}
