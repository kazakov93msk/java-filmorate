package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
public class Film {
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;
    @Positive
    private int duration;
    @NonNull
    private LocalDate releaseDate;
    private Set<Integer> likes = new HashSet<>();

    public Film(String name, String description, int duration, @NonNull LocalDate releaseDate) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
    }

    public List<Integer> getLikes() {
        return new ArrayList<>(likes);
    }

    public void addLike(User user) {
        likes.add(user.getId());
    }

    public void removeLike(User user) {
        likes.remove(user.getId());
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
