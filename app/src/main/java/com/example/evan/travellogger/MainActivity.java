package com.example.evan.travellogger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    private Button newPostButton;
    private Button newTripButton;

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private Toolbar toolbar;



    // Tab titles
    //private String[] tabs = {"Top Rated", "Games", "Movies"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        newPostButton = (Button) findViewById(R.id.new_post_button);
        newTripButton = (Button) findViewById(R.id.new_trip_button);
        Log.e("oncreate", "this is from oncreate");
        this.loadValues();

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        startService(new Intent(getBaseContext(), GPSService.class));


    }

    private void loadValues() {
        int currTripId = Storage.getInstance().loadInt(Trip.CURRENT_TRIP_ID_KEY, this);
        Trip currTrip = null;
        Log.e("loadvalues", String.valueOf(currTripId));
        if(currTripId != Integer.MIN_VALUE) {
            currTrip = (new MySQLiteHelper(this)).getTrip(currTripId);
            Trip.setCurrentTrip(currTrip);
            Log.e("from loadvalues", currTrip.toString());
        } else {
            Log.e("from loadvalues", "no current trip");
        }

    }

    private void saveValues() {
        SharedPreferences.Editor editor = getPreferences(0).edit();
        if(Trip.getCurrentTrip() != null) {
            editor.putInt(Trip.CURRENT_TRIP_ID_KEY, Trip.getCurrentTrip().id);
            Log.e("saving trip: ", Trip.getCurrentTrip().toString());
        } else {
            Log.e("hi", "current trip is null...");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_new_post) {
            startNewPostActivity(this.viewPager);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void newPostButtonAction(View view) {
        startNewPostActivity(view);
    }

    public void newTripButtonAction(View view) {
        startNewTripActivity(view);
    }

    public void newPostToolBarAction(View view) {
        startNewPostActivity(view);
    }

    public void startNewPostActivity(View view) {
        Intent intent = new Intent(this, NewPost.class);
        startActivity(intent);
    }

    public void startNewTripActivity(View view) {
        Intent intent = new Intent(this, NewTrip.class);
        startActivity(intent);
    }

    public void onPause() {
        super.onPause();
        //Log.e("hi", "this is on pause");
    }

    public void onStop() {
        super.onStop();
        Log.e("mainactivity", "onstop is being called here");
        //this.saveValues();

    }

}