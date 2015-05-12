package com.example.evan.travellogger;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;


public class NewTrip extends AppCompatActivity {

    EditText tripName;
    EditText tripDescription;
    PopupWindow popWind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);
        Log.e("HI", "IT IS STARTING!!!!!!!!!!!!!!!!!!");
        popWind = new PopupWindow(this);

        tripName = (EditText) findViewById(R.id.trip_name_field);
        tripDescription = (EditText) findViewById(R.id.trip_description_field);

        MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());

        Log.e("new trip", "this is a test to see log output");
        Trip trip = new Trip("myTitle", "Mydescription", 0);
        db.addTrip(trip);
        Trip retrieval = db.getTrip(trip.id);
        Log.e("retrieal", retrieval.title);
        //Trip retrieve =
        //db.addTrip("test");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_trip, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

    public void newTripButtonAction(View view) {
        Log.e("thing", "trip button was pressed");
        String tripNameString = tripName.getText().toString();
        String tripDescriptionString = tripDescription.getText().toString();
        Trip trip = new Trip(tripNameString, tripDescriptionString);
        Log.e("trip", trip.description);
        (new MySQLiteHelper(getApplicationContext())).addTrip(trip);
        tripName.setText(Integer.toString(trip.id));
        Trip.setCurrentTrip(trip);
        Intent intent = new Intent(this, NewPost.class);
        startActivity(intent);
    }

}
