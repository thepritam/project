package com.project.placesproject;



public class User {
    public String Category;
    public String Email;
    public String IsAvailable;
    public String Phone;
    public double lat;
    public double longi;


    public User(){}
    public User(String Category,String Email,String IsAvailable,String Phone ,double lat,double long1){
        this.Category=Category;
        this.lat=lat;
        this.longi=long1;
        this.Email=Email;
        this.IsAvailable=IsAvailable;
        this.Phone=Phone;
    }


}

