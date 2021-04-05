package com.example.ServicePortal;

public class User {

    public String Name;
    public String Email;
    public String Phone;
    public String Category;
    public String IsAvailable;
    public String Address;
    public Object Document;
    public String DateBirth;
    public long isValid;
    public long CooldownCat;
    public long timeCooldown;
    public double Latitude;
    public double Longitude;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String Address, String Category,long CooldownCat, String DateBirth, Object Document, String Email,String IsAvailable, double Latitude, double Longitude,String Name ,String Phone, long isValid, long timeCooldown) {
        this.Address = Address;
        this.Category = Category;
        this.CooldownCat = CooldownCat;
        this.DateBirth = DateBirth;
        this.Document = Document;
        this.Email = Email;
        this.IsAvailable = IsAvailable;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Name = Name;
        this.Phone = Phone;
        this.isValid = isValid;
        this.timeCooldown = timeCooldown;
    }

}

