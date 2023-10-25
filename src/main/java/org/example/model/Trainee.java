package org.example.model;

import java.util.Date;

public class Trainee implements Entity {
    private int id;
    private Date dateOfBirth;
    private String address;
    private User user;

    public Trainee() {
    }

    public Trainee(Date dateOfBirth, String address) {
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Trainee(Date dateOfBirth, String address, User user) {
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.user = user;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Trainee setId(int id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public Trainee setAddress(String address) {
        this.address = address;
        return this;
    }


    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "id=" + id +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", user=" + user +
                '}';
    }
}
