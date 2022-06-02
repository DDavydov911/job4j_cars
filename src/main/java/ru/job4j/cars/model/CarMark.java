package ru.job4j.cars.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "carmarks")
public class CarMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CarMark carMark = (CarMark) o;
        return id == carMark.id;
    }

    @Override
    public String toString() {
        return "CarMark{"
                + "id=" + id
                + ", name='" + name + '\''
                + '}';
    }
}
