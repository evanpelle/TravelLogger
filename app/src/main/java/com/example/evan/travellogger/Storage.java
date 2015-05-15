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

    private Storage() {

    }

    public void saveInt(String name, int i, Activity activity) {
        //SharedPreferences.Editor editor =
        //        activity.getSharedPreferences("hi", Context.MODE_PRIVATE).edit();
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences prefs =
                activity.getSharedPreferences(FILE, Context.MODE_PRIVATE);
        prefs.edit().putInt(name, i).commit();
    }

    //returns minvalue if nothing found
    public int loadInt(String name, Activity activity) {
        SharedPreferences values = activity.getSharedPreferences(FILE, Context.MODE_PRIVATE);
        return values.getInt(name, Integer.MIN_VALUE);
    }

    public static Storage getInstance() {
        return instance;
    }
}
