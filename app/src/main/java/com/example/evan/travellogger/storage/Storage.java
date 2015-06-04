package com.example.evan.travellogger.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by evan on 5/13/15.
 */

public class Storage extends Activity {

    private static final Storage instance = new Storage();
    private static final String FILE = "file";

    public static final String CURRENT_TRIP_ID_KEY = "current_trip";


    private Storage() {

    }

    public static void saveString(String fileName, String name, String value, Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        prefs.edit().putString(name, value).commit();
    }

    public static String loadString(String fileName, String name, Context context) {
        SharedPreferences values = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return values.getString(name, null);
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
