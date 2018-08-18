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
    private String placeId;

    public SubletPost() {

    }

    @Ignore
    public SubletPost(String userId, String description, String startDate, String endDate, String city, String placeId, String photo) {
        this.userId = userId;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.city = city;
        this.placeId = placeId;
    }

    @Ignore
    public SubletPost(String id, String userId, String description, String startDate, String endDate, String city, String placeId, String photo) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.city = city;
        this.placeId = placeId;
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

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
