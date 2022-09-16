package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotNull(groups = UserValidator.onUpdateUser.class)
    private Integer id;
    @NotBlank(groups = {UserValidator.OnCreateUser.class, UserValidator.onUpdateUser.class})
    @Email
    private String email;
    @NotBlank(groups = {UserValidator.OnCreateUser.class, UserValidator.onUpdateUser.class})
    private String login;
    @NotNull(groups = {UserValidator.onUpdateUser.class})
    private String name;
    @NotNull(
            message = "Дата рождения не может быть пустой.",
            groups = {UserValidator.OnCreateUser.class, UserValidator.onUpdateUser.class}
    )
    @PastOrPresent
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) || email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
