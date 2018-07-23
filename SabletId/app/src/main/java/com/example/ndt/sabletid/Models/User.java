package com.example.ndt.sabletid.Models;

import android.arch.persistence.room.Entity;

@Entity
public class User {
    private String name, email, phone, avatar;
    private boolean gender;

    public User() {

    }

    public User(String name, String email, String phone, String avatar, boolean gender) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
