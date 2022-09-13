package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Size;

@Data
public class RatingMpa {
    private Integer id;
    @Size(max = 64)
    private String name;

    public RatingMpa(Integer id, @Nullable String name) {
        this.id = id;
        this.name = name;
    }
}
