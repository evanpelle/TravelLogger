package com.example.evan.travellogger;

import android.content.Context;

import java.util.Map;

/**
 * Created by evan on 5/31/15.
 */
public class SavablePost extends SavableObject {

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

    public SavablePost(Context context) {
        super(context);
    }

    public SavablePost(String title, String description, Context context) {
        super(context);
        this.title = title;
        this.description = description;
    }

    public String toString() {
        return "title: " + this.title + ", description: " + description +
                "id: " + String.valueOf(this.id) + ", parent id: " + this.parentTripId +
                ", timestamp: " + this.timeStamp + ", picture: " + this.pictureFile +
                ", latitude: " + this.latitude + ", longitude: " + this.longitude;
    }
}
