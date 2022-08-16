package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdentifierException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(User user, User friend) {
        validate(user);
        validate(friend);
        if (user.equals(friend)) {
            throw new ValidationException("Невозможно добавить в друзья пользователя самого себя");
        }
        if (user.getFriends().contains(friend.getId())) {
            throw new AlreadyExistException(String.format(
                    "Пользователь %s уже в друзьях у %s",
                    friend.getEmail(), user.getEmail()
            ));
        }
        user.addFriend(friend);
        friend.addFriend(user);
        return user;
    }

    public User removeFriend(User user, User friend) {
        validate(user);
        validate(friend);
        if (!user.getFriends().contains(friend.getId())) {
            throw new ValidationException(String.format(
                    "Пользователя с email %s нет в друзьях у пользователя с email %s",
                    user.getEmail(), friend.getEmail()
            ));
        }
        user.removeFriend(friend);
        friend.removeFriend(user);
        return user;
    }

    public List<User> getFriendIntersection(User user, User other) {
        return user.getFriends().stream()
                .filter(other.getFriends()::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public void validate(User user) {
        if (user.getId() != null && user.getId() <= 0) {
            throw new IncorrectIdentifierException("ID пользователя не может быть меньше или равен нулю.");
        }
        if (!userStorage.findAll().contains(user)) {
            throw new NotFoundException(String.format("Пользователя с email %s не существует", user.getEmail()));
        }
    }
}
