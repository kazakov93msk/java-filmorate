package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdentifierException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utility.IdentifierGenerator;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private final IdentifierGenerator idGen;

    @Autowired
    public InMemoryUserStorage(IdentifierGenerator idGen) {
        this.idGen = idGen;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        if (userId <= 0) {
            throw new IncorrectIdentifierException("ID пользователя не может быть меньше или равен 0.");
        }
        return Optional.of(users.get(userId));
    }

    @Override
    public User create(User user) {
        validate(user);
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

    @Override
    public User update(User user) {
        validate(user);
        if (user.getId() == null) {
            user.setId(idGen.getNextId());
            users.remove(user.getId());
        } else if (!users.containsValue(user) && user.getId() != null) {
            idGen.toBusyIdList(user.getId());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void validate(User user) {
        if (user.getId() != null && user.getId() <= 0) {
            throw new IncorrectIdentifierException("ID пользователя не может быть меньше или равен 0.");
        } else if (user.getLogin().contains(" ")) {
            throw new ValidationException("Login пользователя не может содержать пробельных символов");
        } else if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
