package ru.job4j.cars.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ads")
public class Ads {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private int price;
    private boolean sold;

    @OneToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @Temporal(TemporalType.DATE)
    private Date created = new Date();

    public Ads() {
    }

    public Ads(int id, String description, Car car, int price) {
        this.id = id;
        this.description = description;
        this.car = car;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ads ads = (Ads) o;
        return id == ads.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Ads{"
                + "id=" + id
                + ", description='" + description + '\''
                + ", car=" + car
                + ", price=" + price
                + ", created=" + created
                + '}';
    }
}
