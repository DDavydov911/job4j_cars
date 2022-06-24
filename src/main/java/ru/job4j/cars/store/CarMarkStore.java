package ru.job4j.cars.store;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CarMarkStore {
    private final List<String> carMarks;

    public CarMarkStore() {
        this.carMarks = List.of(
                "LADA (ВАЗ)", "Audi", "BMW", "Hyundai", "KIA", "Mazda", "Mercedes-Benz", "Nissan",
                "Renault", "Toyota", "Skoda", "Suzuki", "Volkswagen"
        );
    }

    public List<String> getCarMarks() {
        return carMarks;
    }
}
