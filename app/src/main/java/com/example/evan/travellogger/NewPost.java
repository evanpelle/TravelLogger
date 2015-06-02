package com.example.evan.travellogger;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.app.Fragment;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.evan.travellogger.interfaces.GPSListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NewPost extends AppCompatActivity implements GPSListener{

    private static final String TAG = NewPost.class.getName();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int SELECT_PHOTO = 100;


    private int test = 0;
    private ImageButton cameraButton;
    private ImageButton galleryButton;


    private EditText titleField;
    private EditText descriptionField;

    private Toolbar toolbar;

    private ProgressBar gpsLoading;
    private TextView gpsLoadingTextView;
    private ImageView checkboxGreen;

    private Location location;

    private ImageView photoView;

    private String mCurrentPhotoPath;

    GPSService gpsService;
    boolean isBound = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        cameraButton = (ImageButton) findViewById(R.id.camera_button);
        galleryButton = (ImageButton) findViewById(R.id.gallery_button);

        titleField = (EditText) findViewById(R.id.post_title_field);
        descriptionField = (EditText) findViewById(R.id.post_description_field);

        gpsLoading = (ProgressBar) findViewById(R.id.gps_loading_spinner);
        gpsLoadingTextView = (TextView) findViewById(R.id.gps_loading_text_view);
        checkboxGreen = (ImageView) findViewById(R.id.checkbox_green_image_view);
        checkboxGreen.setVisibility(View.INVISIBLE);

        photoView = (ImageView) findViewById(R.id.photo_image_view);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(!isMyServiceRunning(GPSService.class)) {
            startService(new Intent(getBaseContext(), GPSService.class));
        }
        Intent intent = new Intent(this, GPSService.class);
        isBound = getApplicationContext().bindService(
                intent, myConnection, Context.BIND_AUTO_CREATE);

        Log.i(TAG + " oncreate", String.valueOf(cameraButton.getWidth()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void cameraButtonListener(View view) {
        Log.i(TAG, "camera button was pressed");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i(TAG, "IOException when creating imagefile");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void galleryButtonListener(View view) {
        Log.i(TAG, "gallery button pressed" + mCurrentPhotoPath);
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile));
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
    }

    public void newPostButtonAction(View view) {
        String title = titleField.getText().toString();
        String description = descriptionField.getText().toString();
        Post post;
        if(location != null) {
            post = new Post(title, description, -1, mCurrentPhotoPath,
                    location.getLatitude(), location.getLongitude());
        } else {
            post = new Post(title, description, -1, mCurrentPhotoPath,
                    0, 0);
        }
        MySQLiteHelper mslh = new MySQLiteHelper(this);
        Log.i(TAG, "inserting post: " + post.toString());
        //(new MySQLiteHelper(this)).insertPost(post);
        mslh.insertPost(post);
        Post ret = mslh.getPost(post.id);
        Log.i(TAG, "getting post: " + post.toString());

    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i(TAG, mCurrentPhotoPath);
        return image;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //setPic();
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch(requestCode) {
                case REQUEST_IMAGE_CAPTURE:

                    Bitmap bitMap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                    Log.i(TAG, "requesting image capture, bitmap: " + mCurrentPhotoPath);
                    //Log.i(TAG, String.valueOf(bitMap.getWidth()));
                    if(bitMap != null) {
                        Bitmap scaledBitMap = Bitmap.createScaledBitmap(
                                bitMap, photoView.getWidth(), photoView.getHeight(), false);
                        scaledBitMap = ImageHelper.getRoundedCornerBitmap(scaledBitMap, 40);
                        //scaledBitMap = ImageHelper.getRoundedCornerBitmap(scaledBitMap, 40);
                        photoView.setImageBitmap(scaledBitMap);
                        /*photoView.setImageBitmap(Bitmap.createScaledBitmap(
                                bitMap, photoView.getWidth(),
                                photoView.getHeight(), false));*/
                        bitMap.recycle();
                        //bitMap = ImageHelper.getRoundedCornerBitmap(bitMap, 10);
                        //photoView.setImageBitmap(bitMap);


                    }
                    break;
                case SELECT_PHOTO:
                    Log.i("result", mCurrentPhotoPath);
                    Uri selectedImage = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bm = BitmapFactory.decodeStream(imageStream);
                    Log.i("bitmap size:", String.valueOf(bm.getByteCount()));
                    Bitmap newBitMap = Bitmap.createScaledBitmap(
                            bm, photoView.getWidth(), photoView.getHeight(), false);
                    newBitMap = ImageHelper.getRoundedCornerBitmap(newBitMap, 40);
                    photoView.setImageBitmap(newBitMap);
                    /*photoView.setImageBitmap(Bitmap.createScaledBitmap(
                            bm, photoView.getWidth(), photoView.getHeight(), false));*/
                    try {
                        File file = createImageFile();
                        FileOutputStream out = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        bm.recycle();
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }



    @Override
    public void locationUpdate(Location location) {
        this.location = location;
        gpsLoading.setVisibility(View.GONE);
        gpsLoadingTextView.setText("GPS Location Found");
        checkboxGreen.setVisibility(View.VISIBLE);
    }

    public void onDestroy() {
        Log.i(TAG, "on destroy being called");
        if(isBound) {
            //gpsService.stopSelf();
            Intent intent = new Intent(this, GPSService.class);
            //stopService(intent);
            getApplicationContext().unbindService(myConnection);
            Log.i(TAG, "Just stopped the service");
            stopService(new Intent(NewPost.this, GPSService.class));
        }
        isBound = false;
        super.onDestroy();
    }



    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service :
                manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private ServiceConnection myConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            GPSService.LocalBinder binder = (GPSService.LocalBinder) service;
            gpsService = binder.getService();
            isBound = true;
            gpsService.addGPSListener(NewPost.this);
        }
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;

        }
    };
}
