package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectIdentifierException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Component
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable(name = "id") Integer id) {
        if (id <= 0) {
            throw new IncorrectIdentifierException("ID пользователя не может быть меньше или равен нулю.");
        }
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> findUserFriends(@PathVariable(name = "id") Integer id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findFriendIntersection(
            @PathVariable(name = "id") Integer id,
            @PathVariable(name = "otherId") Integer otherId
    ) {
        validateId(id);
        validateId(otherId);
        return userService.getFriendIntersection(id, otherId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(
            @PathVariable(name = "id") Integer id,
            @PathVariable(name = "friendId") Integer friendId
    ) {
        validateId(id);
        validateId(friendId);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(
            @PathVariable(name = "id") Integer id,
            @PathVariable(name = "friendId") Integer friendId
    ) {
        validateId(id);
        validateId(friendId);
        return userService.removeFriend(id, friendId);
    }

    private void validateId(Integer id) {
        if (id <= 0) {
            throw new IncorrectIdentifierException("ID не может быть меньше или равен нулю.");
        }
    }
}
