package ru.job4j.cars.model;

import javax.persistence.*;

@Entity
@Table(name = "engines")
public class Engine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int horsePower;

    @Override
    public String toString() {
        return "Engine{"
                + "id=" + id
                + ", horsePower=" + horsePower
                + '}';
    }
}
