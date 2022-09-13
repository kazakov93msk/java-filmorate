package ru.yandex.practicum.filmorate.dao;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql", "/test_data.sql"})
public class RatingMpaDaoImplTest {
    private final RatingMpaDao mpaDao;

    @Test
    void testFindAllRatingsMpa() {
        List<RatingMpa> ratings = mpaDao.findAllRatingsMpa();
        Assertions.assertEquals(5, ratings.size(), "Не возвращены все 5 рейтингов из БД.");
    }

    @Test
    void testFindRatingMpaById() {
        Optional<RatingMpa> mpaOptional = mpaDao.findRatingMpaById(1);

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(rating ->
                        assertThat(rating).hasFieldOrPropertyWithValue("id", 1)
                );
        assertEquals("G", mpaOptional.get().getName(),
                "Возвращен рейтинш (" + mpaOptional.get().getName() + "отличный от ожидаемого (G)"
        );
    }
}
