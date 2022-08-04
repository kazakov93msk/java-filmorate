package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utility.IdentifierGenerator;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private final IdentifierGenerator idGen;

    public UserController(IdentifierGenerator idGen) {
        this.idGen = idGen;
    }

    @GetMapping("/users")
    public List<User> allUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        validateUser(user);
        if (users.containsValue(user)) {
            throw new AlreadyExistException("Пользователь с почтой " + user.getEmail() + " уже существует.");
        }
        if (user.getId() == null || idGen.isBusy(user.getId())) {
            user.setId(idGen.getNextId());
        } else {
            idGen.toBusyIdList(user.getId());
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        validateUser(user);
        if (user.getId() == null) {
            user.setId(idGen.getNextId());
            users.remove(user.getId());
        } else if (!users.containsValue(user) && user.getId() != null) {
            idGen.toBusyIdList(user.getId());
        }
        users.put(user.getId(), user);
        return user;
    }

    private void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Login пользователя не может содержать пробельных символов");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть больше сегодняшней даты (обнаружено "
                    + user.getBirthday() + ")");
        } else if (user.getId() != null && user.getId() <= 0) {
            throw new ValidationException("ID сущности не может быть равно 0 или меньше 0");
        } else if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
