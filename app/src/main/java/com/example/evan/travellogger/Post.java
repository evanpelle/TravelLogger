package com.example.evan.travellogger;

import java.util.Calendar;

/**
 * Created by evan on 5/10/15.
 */
public class Post
{
    String title;
    String description;
    Calendar timeCreated;
    int id;
    int parentTripId;

    public Post(String title, String description, int parentTripId) {
        this.title = title;
        this.description = description;
        this.parentTripId = parentTripId;
        id = (int) (Integer.MAX_VALUE * Math.random());
    }
}
