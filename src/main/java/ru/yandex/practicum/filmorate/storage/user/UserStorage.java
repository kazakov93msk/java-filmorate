package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<User> getAllUsers();

    Optional<User> getUserById(Integer userId);

    User create(User user);

    User update(User user);

    void validate(User user);
}
