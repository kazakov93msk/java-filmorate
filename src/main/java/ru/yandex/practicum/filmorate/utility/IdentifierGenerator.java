package ru.yandex.practicum.filmorate.utility;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class IdentifierGenerator {
    private Integer nextId = 1;

    private final List<Integer> busyIdList = new ArrayList<>();

    public Integer getNextId() {
        Integer id = nextId;
        while (busyIdList.contains(id)) {
            id = ++nextId;
        }
        nextId++;
        toBusyIdList(id);
        return id;
    }

    public void toBusyIdList(Integer id) {
        busyIdList.add(id);
    }

    public boolean isBusy(Integer id) {
        return busyIdList.contains(id);
    }

    public void removeFromBusyList(Integer id) {
        if (busyIdList.contains(id)) {
            busyIdList.remove(id);
            nextId = id;
        }
    }

    public void resetNextId() {
        busyIdList.clear();
        nextId = 1;
    }
}
