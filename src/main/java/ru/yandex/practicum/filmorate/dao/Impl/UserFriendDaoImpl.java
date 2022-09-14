package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserFriendDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.rowmapper.UserMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserFriendDaoImpl implements UserFriendDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public List<User> findFriendsByUserId(Integer userId) {
        String sql = "SELECT u.* FROM USERS_FRIENDS uf JOIN users u ON u.id = uf.friend_id WHERE uf.user_id = ?";
        return jdbcTemplate.query(sql, userMapper, userId);
    }

    @Override
    public List<User> findFriendsIntersection(Integer userId, Integer otherId) {
        String sql = "SELECT * FROM users "
                + "WHERE id IN (SELECT friend_id FROM users_friends WHERE user_id = ?) "
                + "AND id IN (SELECT friend_id FROM users_friends WHERE user_id = ?)";
        return jdbcTemplate.query(sql, userMapper, userId, otherId);

    }

    @Override
    public void createFriends(Integer userId, Integer friendId) {
        String sql = "INSERT INTO USERS_FRIENDS VALUES (?,?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void deleteFriends(Integer userId, Integer friendId) {
        String sql = "DELETE FROM USERS_FRIENDS WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }


}
