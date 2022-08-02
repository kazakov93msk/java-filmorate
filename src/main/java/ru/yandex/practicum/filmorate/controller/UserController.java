package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utility.IdentifierGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
public class UserController {
    private final Set<User> users = new HashSet<>();

    private final IdentifierGenerator idGen = new IdentifierGenerator();

    @GetMapping("/users")
    public List<User> allUsers() {
        return new ArrayList<>(users);
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        validateUser(user);
        if (users.contains(user)) {
            throw new AlreadyExistException("Пользователь с почтой " + user.getEmail() + " уже существует.");
        }
        if (user.getId() == null || idGen.isBusy(user.getId())) {
            user.setId(idGen.getNextId());
        } else {
            idGen.toBusyIdList(user.getId());
        }
        users.add(user);
        return user;
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        validateUser(user);
        if (user.getId() == null) {
            user.setId(idGen.getNextId());
        } else if (!users.contains(user) && user.getId() != null) {
            idGen.toBusyIdList(user.getId());
        }
        users.removeIf(user::equals);
        users.add(user);
        return user;
    }

    private void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Login пользователя не может содержать пробельных символов");
        } else if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть больше сегодняшней даты (обнаружено "
                    + user.getBirthday() + ")");
        } else if (user.getId() != null && user.getId() <= 0) {
            throw new ValidationException("ID сущности не может быть равно 0 или меньше 0");
        } else if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
