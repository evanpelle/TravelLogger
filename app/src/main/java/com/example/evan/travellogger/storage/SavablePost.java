package com.example.evan.travellogger.storage;

import android.content.Context;

import com.example.evan.travellogger.storage.SavableObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by evan on 5/31/15.
 */
public class SavablePost extends SavableObject {

    public String title;
    public String description;
    public String pictureFile;
    public String timeStamp;
    public String city;
    public String country;
    public int parentTripId;
    public double longitude;
    public double latitude;

    public SavablePost(Context context) {
        super(context);
        this.timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    public SavablePost(String title, String description, Context context) {
        super(context);
        this.title = title;
        this.description = description;
    }

    public String toString() {
        return "title: " + this.title + ", description: " + description +
                "id: " + String.valueOf(this.getId()) + ", parent id: " + this.parentTripId +
                ", timestamp: " + this.timeStamp + ", picture: " + this.pictureFile +
                ", latitude: " + this.latitude + ", longitude: " + this.longitude;
    }
}
