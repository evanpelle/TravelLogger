package com.example.evan.travellogger;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class NewPost extends AppCompatActivity {

    private static final String TAG = NewPost.class.getName();

    private ImageButton cameraButton;
    private ImageButton galleryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        cameraButton = (ImageButton) findViewById(R.id.camera_button);
        galleryButton = (ImageButton) findViewById(R.id.gallery_button);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_post, menu);
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

    public void cameraButtonListener(View view) {
        Log.i(TAG, "camera button was pressed");
    }

    public void galleryButtonListener(View view) {
        Log.i(TAG, "gallery button was pressed");
    }
}
