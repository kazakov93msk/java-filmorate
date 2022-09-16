package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private Integer id;
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;
    @NotBlank(message = "Описание фильма не может быть пустым.")
    @Size(max = 200)
    private String description;
    @Positive
    private int duration;
    @NotNull(message = "Дата релиза не может быть пустой.")
    private LocalDate releaseDate;

    private int rate;
    private RatingMpa mpa;
    private List<Genre> genres;

    public Film(Integer id, String name, String description, int duration, @NonNull LocalDate releaseDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
    }

    public Film(String name, String description, int duration, @NonNull LocalDate releaseDate) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
    }

    public Film(String name, String description, int duration,
                @NonNull LocalDate releaseDate, @Nullable Integer rate,
                @Nullable RatingMpa mpa, @Nullable List<Genre> genres) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.rate = Objects.requireNonNullElse(rate, 0);
        this.mpa = mpa;
        this.genres = genres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return Objects.equals(id, film.id) || name.equals(film.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
