package ru.yandex.practicum.filmorate.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserFriendDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.rowmapper.UserMapper;

import java.util.List;

@Component
public class UserFriendDaoImpl implements UserFriendDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserFriendDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findFriendsByUserId(Integer userId) {
        String sql = "SELECT u.* FROM USERs_FRIENDS uf JOIN users u ON u.id = uf.friend_id WHERE uf.user_id = ?";
        return jdbcTemplate.query(sql, new UserMapper(), userId);
    }

    @Override
    public void createFriends(Integer userId, Integer friendId) {
        String sql = "INSERT INTO USERs_FRIENDS VALUES (?,?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void deleteFriends(Integer userId, Integer friendId) {
        String sql = "DELETE FROM USERs_FRIENDS WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }
}
