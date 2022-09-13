package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "classpath:scripts/test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class FilmDaoImplTest {
    private final FilmDao filmDao;
    private final UserDao userDao;
    private final FilmLikeDao likeDao;
    private final GenreDao genreDao;
    private final FilmGenreDao filmGenreDao;
    private final RatingMpaDao mpaDao;

    @Test
    void testFindFilmById() {
        Optional<Film> filmOptional = filmDao.findFilmById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void findGenresByFilmId() {
        List<Genre> genres = filmGenreDao.findGenresByFilmId(1);

        assertEquals(2, genres.size(), "Не возвращены жанры фильма ID=1");
        assertEquals("Комедия", genres.get(0).getName(), "Возвращен некорректный жанр");
    }

    @Test
    void shouldReturnUserAfterHeAddLikeToFilm() {
        likeDao.createLike(1, 2);

        Optional<User> likeOptional = likeDao.findLikesByFilmId(1).stream().findFirst();

        assertThat(likeOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 2)
                );
        assertEquals(userDao.findUserById(2), likeOptional,
                "Возравщенный пользователь не соответствует ожидаемому"
        );
    }

    @Test
    void shouldReturnRatingMpaByFilm() {
        Optional<RatingMpa> mpaOptional = mpaDao.findRatingMpaByFilmId(2);

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(ratingMpa ->
                        assertThat(ratingMpa).hasFieldOrPropertyWithValue("id", 2)
                );
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(ratingMpa ->
                        assertThat(ratingMpa).hasFieldOrPropertyWithValue("name", "PG")
                );
    }

}
