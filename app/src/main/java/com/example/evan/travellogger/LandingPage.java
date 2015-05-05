package com.example.evan.travellogger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


public class LandingPage extends Activity {
    private static final String TAG = LandingPage.class.getName();

    private Button newPostButton;
    private Button newTripButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        final RelativeLayout landingPage = (RelativeLayout) findViewById(R.id.landing_page);
        newPostButton = (Button) findViewById(R.id.new_post_button);
        newTripButton = (Button) findViewById(R.id.new_trip_button);
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

    public void newPostButtonAction(View view) {
        startNewPostActivity(view);
    }

    public void newTripButtonAction(View view) {
        startNewTripActivity(view);
    }

    public void startNewPostActivity(View view) {
        Intent intent = new Intent(this, NewPost.class);
        startActivity(intent);
    }

    public void startNewTripActivity(View view) {
        Intent intent = new Intent(this, NewTrip.class);
        startActivity(intent);
    }
}
