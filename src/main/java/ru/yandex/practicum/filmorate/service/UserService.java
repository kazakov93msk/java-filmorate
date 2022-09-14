package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.dao.UserFriendDao;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdentifierException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserService {
    private final UserDao userDao;
    private final UserFriendDao friendDao;

    public List<User> findAllUsers() {
        return userDao.findAllUsers();
    }

    public User findUserById(Integer userId) {
        return userDao.findUserById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь с ID %d не найден.", userId))
        );
    }
    public User createUser(User user) {
        validate(user);
        return userDao.createUser(user);
    }

    public User updateUser(User user) {
        validate(user);
        return userDao.updateUser(user);
    }

    public List<User> findFriendsByUserId(Integer userId) {
        return friendDao.findFriendsByUserId(userId);
    }

    public User addFriend(Integer userId, Integer friendId) {
        User user = findUserById(userId);
        User friend = findUserById(friendId);
        if (user.equals(friend)) {
            throw new ValidationException("Невозможно добавить в друзья пользователя самого себя");
        }
        if (findFriendsByUserId(userId).contains(friend)) {
            throw new AlreadyExistException(String.format(
                    "Пользователь %s уже в друзьях у %s",
                    friend.getEmail(), user.getEmail()
            ));
        }
        friendDao.createFriends(userId, friendId);
        return user;
    }

    public User removeFriend(Integer userId, Integer friendId) {
        User user = findUserById(userId);
        User friend = findUserById(friendId);
        if (!friendDao.findFriendsByUserId(userId).contains(friend)) {
            throw new ValidationException(String.format(
                    "Пользователя с email %s нет в друзьях у пользователя с email %s",
                    user.getEmail(), friend.getEmail()
            ));
        }
        friendDao.deleteFriends(userId, friendId);
        return user;
    }

    public List<User> findFriendsIntersection(Integer userId, Integer otherId) {
        return friendDao.findFriendsIntersection(userId, otherId);
    }

    public void validate(User user) {
        if (user.getId() != null && user.getId() <= 0) {
            throw new IncorrectIdentifierException("ID пользователя не может быть меньше или равен нулю.");
        }
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
    }
}
