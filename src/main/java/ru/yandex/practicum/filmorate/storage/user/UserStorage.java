package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {

    List<User> findAll();

    User getUserById(Integer userId);

    User create(User user);

    User update(User user);

    void validate(User user);
}
