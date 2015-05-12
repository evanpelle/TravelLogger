package com.example.evan.travellogger;

import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * Created by evan on 5/6/15.
 */
public class Trip {
    int id;
    int parent_id;
    String title;
    String description;
    Calendar startDatetime;
    public static final String PREFS_NAME = "myPrefs";

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

    public static Trip getCurrentTrip() {
        return null;
    }

    public static void setCurrentTrip(Trip trip) {


    }

}
