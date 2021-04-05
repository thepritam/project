package com.pritam.project;

public class User {
    public String Name;
    public String Email;
    public String Phone;
    public String Category;
    public String IsAvailable;
    public double Latitiude;
    public double Longitude;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String Category, String Email, String IsAvailable, String Name , String Phone,double Latitude,double Longitude) {
        this.Category = Category;
        this.Email = Email;
        this.IsAvailable = IsAvailable;
        this.Name = Name;
        this.Phone = Phone;
        this.Latitiude=Latitude;
        this.Longitude=Longitude;
    }
}
