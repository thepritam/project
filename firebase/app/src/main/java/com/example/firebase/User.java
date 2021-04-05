package com.example.firebase;

public class User {
    public String category;
    public double latitude;
    public double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public User(){}
    public User(String category, double lat , double long1){
        this.category=category;
        this.latitude=lat;
        this.longitude=long1;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

