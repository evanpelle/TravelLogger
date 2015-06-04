package com.example.evan.travellogger;

import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * Created by evan on 5/6/15.
 */
public class Trip {
    public int id;
    public int parent_id;
    public String title;
    public String description;
    public Calendar startDatetime;

    public static final String PREFS_NAME = "myPrefs";
    private static Trip current_trip;
    public static final String CURRENT_TRIP_ID_KEY = "current_trip";

    public Trip(String title, String description, int parent_id) {
        this.title = title;
        this.description = description;
        this.parent_id = parent_id;
        id = (int) (Integer.MAX_VALUE*Math.random());
        startDatetime = Calendar.getInstance();
    }

    public Trip(String title, String description) {
        this(title, description, -1);
    }

    public Trip(String title, String description, Trip parent) {
        this(title, description, parent.id);
    }

    public String toString() {
        return "title: " + title + ", description: " + description +
                ", id: " + this.id;
    }

}
