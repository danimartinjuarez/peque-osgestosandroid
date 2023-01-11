package com.example.pequenosgestos;

import java.io.Serializable;



class User {
    private int id;
    private String name;
    private String photo;
    private String whatOffer;
    private String whatLike;
    private String location;
    private String contact;
    private String activityType;
    private String title;

    public User(int id, String name, String photo, String whatOffer, String whatLike, String location, String contact, String activityType, String title) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.whatOffer= whatOffer;
        this.whatLike= whatLike;
        this.location= location;
        this.contact= contact;
        this.activityType= activityType;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getWhatOffer() {
        return whatOffer;
    }

    public String getWhatLike() {
        return whatLike;
    }

    public String getLocation() {
        return location;
    }

    public String getContact() {
        return contact;
    }

    public String getActivityType() {
        return activityType;
    }

    public String getTitle() {
        return title;
    }
}
