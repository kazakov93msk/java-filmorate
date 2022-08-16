package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Component
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;
    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable(name = "id") Integer id) {
        return userStorage.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> findUserFriends(@PathVariable(name = "id") Integer id) {
        return userStorage.getUserById(id).getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findFriendIntersection(
            @PathVariable(name = "id") Integer id,
            @PathVariable(name = "otherId") Integer otherId
    ) {
        return userService.getFriendIntersection(userStorage.getUserById(id), userStorage.getUserById(otherId));
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userStorage.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(
            @PathVariable(name = "id") Integer id,
            @PathVariable(name = "friendId") Integer friendId
    ) {
        return userService.addFriend(userStorage.getUserById(id), userStorage.getUserById(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(
            @PathVariable(name = "id") Integer id,
            @PathVariable(name = "friendId") Integer friendId
    ) {
        return userService.removeFriend(userStorage.getUserById(id), userStorage.getUserById(friendId));
    }
}
