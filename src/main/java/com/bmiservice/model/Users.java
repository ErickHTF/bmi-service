package com.bmiservice.model;

import jakarta.persistence.*;

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private int age;

    @Column
    private double weight;

    @Column
    private double height;

    public void setId(Long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

}
