package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable(name = "id") @Positive Integer id) {
        return userService.findUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> findUserFriends(@PathVariable(name = "id") @Positive Integer userId) {
        return userService.findFriendsByUserId(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findFriendsIntersection(
            @PathVariable(name = "id") @Positive Integer userId,
            @PathVariable(name = "otherId") @Positive Integer otherId
    ) {
        return userService.findFriendsIntersection(userId, otherId);
    }

    @PostMapping
    public User create(@RequestBody @Validated User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@RequestBody @Validated User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(
            @PathVariable(name = "id") @Positive Integer id,
            @PathVariable(name = "friendId") @Positive Integer friendId
    ) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(
            @PathVariable(name = "id") @Positive Integer id,
            @PathVariable(name = "friendId") @Positive Integer friendId
    ) {
        return userService.removeFriend(id, friendId);
    }
}
