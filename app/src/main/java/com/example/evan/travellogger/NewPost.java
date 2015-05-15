package com.example.evan.travellogger;

import android.app.Activity;
import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


public class NewPost extends AppCompatActivity {

    private static final String TAG = NewPost.class.getName();

    private int test = 0;
    private ImageButton cameraButton;
    private ImageButton galleryButton;

    private EditText titleField;
    private EditText descriptionField;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        cameraButton = (ImageButton) findViewById(R.id.camera_button);
        galleryButton = (ImageButton) findViewById(R.id.gallery_button);

        titleField = (EditText) findViewById(R.id.post_title_field);
        descriptionField = (EditText) findViewById(R.id.post_description_field);
    }

    public void onCreateView() {
        
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_new_post, menu);
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

    public void newPostButtonAction(View view) {

        //Log.e(TAG, "new post button was pressed");
        String title = titleField.getText().toString();
        String description = descriptionField.getText().toString();
        Post post = new Post(title, description, -1);
        //titleField.setText(Integer.toString(post.id));
        //MySQLiteHelper db = new MySQLiteHelper(this);
        //db.destroy(this);
        //db.addPost(post);
        //Post retrieve = db.getPost(post.id);
        Log.e("got the post", post.title);
    }
}
