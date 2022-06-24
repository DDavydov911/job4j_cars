package ru.job4j.cars.store;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BodyTypeStore {
    private final List<String> bodyTypes;

    public BodyTypeStore() {
        this.bodyTypes = List.of(
                "Седан", "Хэтчбэк", "Универсал", "Пикап"
        );
    }

    public List<String> getBodyTypes() {
        return bodyTypes;
    }
}
