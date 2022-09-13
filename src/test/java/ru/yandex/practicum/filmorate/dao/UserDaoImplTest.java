package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "classpath:scripts/test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserDaoImplTest {
    private final UserDao userDao;
    private final UserFriendDao friendDao;

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = userDao.findUserById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void shouldReturnListWithTwoUsers() {
        List<User> users = userDao.findAllUsers();

        assertEquals(2, users.size(), "Не было возвращено два пользователя из БД.");
    }

    @Test
    public void shouldReturnFriendByUserAfterAddFriends() {
        friendDao.createFriends(1, 2);
        Optional<User> friendOptional = friendDao.findFriendsByUserId(1).stream().findFirst();
        assertThat(friendOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", 2));
        assertEquals(userDao.findUserById(2), friendOptional, "Друг пользователя ID=1 не был возвращен.");
    }
}
