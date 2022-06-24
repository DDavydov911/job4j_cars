package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.store.CarMarkStore;
import ru.job4j.cars.store.CarsRepositoryDB;

import java.util.List;

@Service
public class CarService {
    private final CarMarkStore carMarkStore;
    private final CarsRepositoryDB carsRepositoryDB;

    public CarService(CarMarkStore carMarkStore, CarsRepositoryDB carsRepositoryDB) {
        this.carMarkStore = carMarkStore;
        this.carsRepositoryDB = carsRepositoryDB;
    }

    public List<String> getCarMarks() {
        return carMarkStore.getCarMarks();
    }

    public Car getCarById(int id) {
        return carsRepositoryDB.findCarById(id);
    }
}
