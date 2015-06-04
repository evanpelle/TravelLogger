package com.example.evan.travellogger;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.evan.travellogger.storage.MySQLiteHelper;
import com.example.evan.travellogger.storage.SavableObject;
import com.example.evan.travellogger.storage.SavablePost;
import com.example.evan.travellogger.storage.SavableTrip;
import com.example.evan.travellogger.storage.Storage;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    private Button newPostButton;
    private Button newTripButton;
    private Button end_trip_button;

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private Toolbar toolbar;

    private TextView topParentTextView;
    private TextView middleParentTextView;
    private TextView bottomParentTextView;



    // Tab titles
    //private String[] tabs = {"Top Rated", "Games", "Movies"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        newPostButton = (Button) findViewById(R.id.new_post_button);
        newTripButton = (Button) findViewById(R.id.new_trip_button);

        topParentTextView = (TextView) findViewById(R.id.top_parent);
        middleParentTextView = (TextView) findViewById(R.id.middle_parent);
        bottomParentTextView = (TextView) findViewById(R.id.bottom_parent);



        Log.e("oncreate", "this is from oncreate");
        //this.loadValues();
        this.generateParentLabels();
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        startService(new Intent(getBaseContext(), GPSService.class));
        test();
        //Log.i(TAG, this.getClass().getSimpleName());
    }

   public void test() {
       SavableTrip st = new SavableTrip(this);
       st.title = "this should work";
       st.description = "this is the description";
       st.save(this);
       st = (SavableTrip) SavableObject.load(st.getId(), this);
       Log.i(TAG, st.toString());


    }

    private void generateParentLabels() {
        topParentTextView.setVisibility(View.INVISIBLE);
        middleParentTextView.setVisibility(View.INVISIBLE);
        bottomParentTextView.setVisibility(View.INVISIBLE);
        int currentTripId = Storage.loadInt(Storage.CURRENT_TRIP_ID_KEY, this);
        if(currentTripId > 0) {
            //MySQLiteHelper db = new MySQLiteHelper(this);
            /*Trip currentTrip = db.getTrip(currentTripId);
            Trip parentTrip = null;
            Trip grandTrip = null;*/
            SavableTrip currentTrip = (SavableTrip) SavableObject.load(currentTripId, this);
            SavableTrip parentTrip = null;
            SavableTrip grandTrip = null;
            Log.i(TAG, currentTrip.toString());
            if(currentTrip.parentId > 0) {
                //parentTrip = db.getTrip(currentTrip.parent_id);
                parentTrip = (SavableTrip) SavableObject.load(currentTrip.parentId, this);
                if(parentTrip.parentId > 0) {
                    //grandTrip = db.getTrip(parentTrip.parent_id);
                    grandTrip = (SavableTrip) SavableObject.load(parentTrip.parentId, this);
                    topParentTextView.setVisibility(View.VISIBLE);
                    middleParentTextView.setVisibility(View.VISIBLE);
                    bottomParentTextView.setVisibility(View.VISIBLE);
                    topParentTextView.setText(grandTrip.title);
                    middleParentTextView.setText(parentTrip.title);
                    bottomParentTextView.setText(currentTrip.title);
                } else {
                    topParentTextView.setVisibility(View.VISIBLE);
                    middleParentTextView.setVisibility(View.VISIBLE);
                    topParentTextView.setText(parentTrip.title);
                    middleParentTextView.setText(currentTrip.title);
                }
            } else {
                topParentTextView.setVisibility(View.VISIBLE);
                topParentTextView.setText(currentTrip.title);
            }
        } else {
            topParentTextView.setVisibility(View.VISIBLE);
            topParentTextView.setText("NO CURRENT TRIP");
        }
    }

    public void endTripButtonAction(View view) {
        int currentTripId = Storage.loadInt(Storage.CURRENT_TRIP_ID_KEY, this);
        if(currentTripId > 0) {
            //MySQLiteHelper db = new MySQLiteHelper(this);
            //Trip currentTrip = db.getTrip(currentTripId);
            SavableTrip currentTrip = (SavableTrip) SavableObject.load(currentTripId, this);
            int parentTripId = currentTrip.parentId;
            if (parentTripId > 0) {
                Storage.saveInt(Storage.CURRENT_TRIP_ID_KEY, parentTripId, this);
            } else {
                Storage.saveInt(Storage.CURRENT_TRIP_ID_KEY, -1, this);
            }
            this.generateParentLabels();
        }
    }


   /* private void loadValues() {
        int currTripId = Storage.loadInt(Trip.CURRENT_TRIP_ID_KEY, this);
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
    } */

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

    public void viewTripButtonAction(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
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
        Log.i("mainactivity", "onstop is being called here");
        //this.saveValues();

    }

}