package com.example.evan.travellogger.storage;

import android.content.Context;

import com.example.evan.travellogger.storage.SavableObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by evan on 6/3/15.
 */
public class SavableTrip extends SavableObject {

    public static final String CURRENT_TRIP_ID_KEY = "current_trip";

    public int parentId;
    public String title;
    public String description;
    String timeStamp;

    public SavableTrip(Context context) {
        super(context);
        this.timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    public SavableTrip(String title, String description, Context context) {
        this(context);
        this.title = title;
        this.description = description;
    }

    public String toString() {
        return "title: " + title + " description: " + description;
    }
}
