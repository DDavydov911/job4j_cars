package ru.job4j.cars.model;

import javax.persistence.*;

@Entity
@Table(name = "photos")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private byte[] photo;

    public Photo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] bytes) {
        this.photo = bytes;
    }
}
