package com.example.evan.travellogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.evan.travellogger.storage.MySQLiteHelper;
import com.example.evan.travellogger.storage.SavableTrip;
import com.example.evan.travellogger.storage.Storage;


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

        /*Log.e("new trip", "this is a test to see log output");
        Trip trip = new Trip("myTitle", "Mydescription", 0);
        db.insertTrip(trip);
        Trip retrieval = db.getTrip(trip.id);
        Log.e("retrieal", retrieval.title);
        //Trip retrieve =
        //db.addTrip("test"); */

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

        SavableTrip st = new SavableTrip(tripNameString, tripDescriptionString, this);
        st.parentId = Storage.loadInt(Storage.CURRENT_TRIP_ID_KEY, this);
        Storage.saveInt(Storage.CURRENT_TRIP_ID_KEY, st.getId(), this);
        st.save(this);

        /*

        Trip trip = new Trip(tripNameString, tripDescriptionString,
                Storage.loadInt(Trip.CURRENT_TRIP_ID_KEY, this));

        //Log.e("newTripbuttonaction", trip.toString());
        (new MySQLiteHelper(getApplicationContext())).insertTrip(trip);
        Storage.saveInt(Trip.CURRENT_TRIP_ID_KEY, trip.id, this);

        Log.e("newtripbuttonaction", trip.title + " " + trip.parent_id);

        //Log.e("newTripbuttonAction", Trip.getCurrentTrip().toString());
        */


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
