package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserFriendDao {
    List<User> findFriendsByUserId(Integer userId);
    List<User> findFriendsIntersection(Integer userId, Integer otherId);
    void createFriends(Integer userId, Integer friendId);
    void deleteFriends(Integer userId, Integer friendId);
}
