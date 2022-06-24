package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.store.BodyTypeStore;

import java.util.List;

@Service
public class BodyTypeService {
    private final BodyTypeStore bodyTypeStore;

    public BodyTypeService(BodyTypeStore bodyTypeStore) {
        this.bodyTypeStore = bodyTypeStore;
    }

    public List<String> getBodyTypes() {
        return bodyTypeStore.getBodyTypes();
    }
}
