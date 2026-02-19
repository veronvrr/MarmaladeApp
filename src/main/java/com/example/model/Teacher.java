package com.example.model;

import jakarta.persistence.*;

@Entity
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String language;
    private String level;
    private Double price;

    private String photo;
    // новое поле
    @Column(length = 2000) // можно ограничить длину
    private String bio;

    // геттеры/сеттеры
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
    public Teacher() {}

    public Teacher(String fullName, String language, String level, Double price, String photo, String bio) {
        this.fullName = fullName;
        this.language = language;
        this.level = level;
        this.price = price;
        this.photo=photo;
        this.bio=bio;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }
}
