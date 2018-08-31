package com.example.ndt.sabletid.Models.SubletPost;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class SubletPost {
    @PrimaryKey
    @NonNull
    private String id;

    private String userId;
    private String description;
    private String startDate;
    private String endDate;
    private String city;
    private String photo;
    private int price;
    private double latitude;
    private double longitude;

    public SubletPost() {

    }

    @Ignore
    public SubletPost(String userId, String description, String startDate, String endDate, String city, String photo,
                      int price, double latitude, double longitude) {
        this.userId = userId;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.city = city;
        this.photo = photo;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Ignore
    public SubletPost(@NonNull String id, String userId, String description, String startDate, String endDate, String city, String photo,
                      int price, double latitude, double longitude) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.city = city;
        this.photo = photo;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
