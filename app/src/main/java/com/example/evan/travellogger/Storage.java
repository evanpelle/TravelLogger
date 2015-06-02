package com.example.evan.travellogger;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by evan on 5/13/15.
 */

public class Storage extends Activity {
    private static final Storage instance = new Storage();
    private static final String FILE = "file";

    public static final String CURRENT_TRIP_ID_KEY = "current_trip";


    private Storage() {

    }

    public static void saveInt(String name, int i, Context context) {
        //SharedPreferences.Editor editor =
        //        activity.getSharedPreferences("hi", Context.MODE_PRIVATE).edit();
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences prefs =
                context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
        prefs.edit().putInt(name, i).commit();
    }

    //returns minvalue if nothing found
    public static int loadInt(String name, Context context) {
        SharedPreferences values = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
        return values.getInt(name, Integer.MIN_VALUE);
    }
}
