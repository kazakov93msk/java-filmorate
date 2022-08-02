package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    UserController userController;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
    }

    @Test
    public void shouldReturnValidationExceptionWhenUserInvalid() {
        User loginWithSpaces = new User("mail@mail.ru", "space here");
        User bornInFuture = new User("user@future.ru", "BackToPast");
        bornInFuture.setBirthday(LocalDate.now().plusDays(1));
        User userWithInvalidId = new User("InvalidId", "IdInvalid");
        userWithInvalidId.setId(-1);

        assertThrows(ValidationException.class, () -> userController.create(loginWithSpaces),
                "Ожидалось ValidationException, возвращено некорректное исключение");
        assertThrows(ValidationException.class, () -> userController.create(bornInFuture),
                "Ожидалось ValidationException, возвращено некорректное исключение");
        assertThrows(ValidationException.class, () -> userController.create(userWithInvalidId),
                "Ожидалось ValidationException, возвращено некорректное исключение");
    }

    @Test
    public void shouldReturnUserAfterAdded() {
        User validUser = new User("user@test.ru", "UserLogin");
        validUser.setBirthday(LocalDate.of(1990, 1, 1));
        validUser.setName("UserName");
        User returnedUser = userController.create(validUser);
        validUser.setId(1);
        assertEquals(validUser, returnedUser,
                "Добавленный пользователь не совпадает с изначальным");
    }

    @Test
    public void shouldSetNameAsLoginWhenNameIsUndefined() {
        User nameless = new User("user@test.ru", "UserLogin");
        userController.create(nameless);
        assertEquals(nameless.getLogin(), nameless.getName(),
                "Пустому имени не было присоено значение логина пользователя");
    }
}
