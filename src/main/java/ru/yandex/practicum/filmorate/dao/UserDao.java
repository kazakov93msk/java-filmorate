package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> findAllUsers();
    Optional<User> findUserById(Integer userId);
    User createUser(User user);
    User updateUser(User user);
    void deleteUserById(Integer userId);
}
