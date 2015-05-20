package com.example.evan.travellogger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by evan on 5/10/15.
 */
public class Post
{
    String title;
    String description;
    String pictureFile;
    String timeStamp;
    String city;
    String country;
    int id;
    int parentTripId;
    double longitude;
    double latitude;


    public Post(String title, String description, int parentTripId,
               String pictureFile, double latitude, double longitude) {
        this.title = title;
        this.description = description;
        this.parentTripId = parentTripId;
        this.pictureFile = pictureFile;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = (int) (Integer.MAX_VALUE * Math.random());
        this.timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    public String toString() {
        return "title: " + this.title + ", description: " + description +
                "id: " + String.valueOf(this.id) + ", parent id: " + this.parentTripId +
                ", timestamp: " + this.timeStamp + ", picture: " + this.pictureFile +
                ", latitude: " + this.latitude + ", longitude: " + this.longitude;
    }
}
