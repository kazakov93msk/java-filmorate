package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utility.IdentifierGenerator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    UserController userController;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController(new IdentifierGenerator());
    }

    @Test
    public void shouldReturnValidationExceptionWhenUserInvalid() {
        User loginWithSpaces = new User("mail@mail.ru", "space here",
                null, LocalDate.of(2000, 1, 1));
        User userWithInvalidId = new User("InvalidId", "IdInvalid",
            "some name", LocalDate.of(2000, 1, 1));
        userWithInvalidId.setId(-1);

        assertThrows(ValidationException.class, () -> userController.create(loginWithSpaces),
                "Ожидалось ValidationException, возвращено некорректное исключение");
        assertThrows(ValidationException.class, () -> userController.create(userWithInvalidId),
                "Ожидалось ValidationException, возвращено некорректное исключение");
    }

    @Test
    public void shouldReturnUserAfterAdded() {
        User validUser = new User("user@test.ru", "UserLogin",
                "UserName", LocalDate.of(1990, 1, 1));
        User returnedUser = userController.create(validUser);
        validUser.setId(1);
        assertEquals(validUser, returnedUser,
                "Добавленный пользователь не совпадает с изначальным");
    }

    @Test
    public void shouldSetNameAsLoginWhenNameIsUndefined() {
        User nameless = new User("user@test.ru", "UserLogin", null, LocalDate.of(1990, 1, 1));
        userController.create(nameless);
        assertEquals(nameless.getLogin(), nameless.getName(),
                "Пустому имени не было присоено значение логина пользователя");
    }
}
