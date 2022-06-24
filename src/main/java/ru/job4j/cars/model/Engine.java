package ru.job4j.cars.model;

import javax.persistence.*;

@Entity
@Table(name = "engines")
public class Engine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int horsePower;

    public Engine() {
    }

    public Engine(int horsePower) {
        this.horsePower = horsePower;
    }

    public int getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(int horsePower) {
        this.horsePower = horsePower;
    }

    @Override
    public String toString() {
        return "Engine{"
                + "id=" + id
                + ", horsePower=" + horsePower
                + '}';
    }
}
