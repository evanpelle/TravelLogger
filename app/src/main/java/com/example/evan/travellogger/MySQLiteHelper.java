package com.example.evan.travellogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_TRIPS = "trips";
    public static final String TABLE_POSTS = "posts";
    public static final String TRIP_ID = "_id";
    public static final String TRIP_TITLE = "name";
    //public static final String

    private static final String DATABASE_NAME = "test.db";
    private static final int DATABASE_VERSION = 1;

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    private static final String[] TRIP_COLUMNS = {KEY_ID,KEY_TITLE,KEY_DESCRIPTION};
    private static final String[] TABLE_COLUMNS = {KEY_ID,KEY_TITLE,KEY_DESCRIPTION};



    //TRIPS: ID, Name

    // Database creation sql statement
    private static final String CREATE_TRIP_TABLE = "CREATE TABLE trips ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "title TEXT, "+
            "description TEXT )";

    private static final String CREATE_POST_TABLE = "CREATE TABLE posts ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "title TEXT, "+
            "description TEXT )";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TRIP_TABLE);
        database.execSQL(CREATE_POST_TABLE);
    }

    public void destroy(Context context) {
        context.deleteDatabase(this.DATABASE_NAME);
    }

    public void addTrip(Trip trip) {
        Log.e("adding trip", trip.title);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", trip.id);
        values.put("title", trip.title); // get title
        values.put("description", trip.description); // get author
        db.insert("trips",
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }


    public void addPost(Post post) {
        Log.e("adding post", post.title);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", post.id);
        values.put("title", post.title); // get title
        values.put("description", post.description); // get author
        db.insert("posts",
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }
    public Trip getTrip(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_TRIPS, // a. table
                        TRIP_COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        Trip trip = new Trip(cursor.getString(1), cursor.getString(2), 0);
        trip.id = (Integer.parseInt(cursor.getString(0)));


        //log
        //Log.d("getBook("+id+")", trip.title);

        // 5. return book
        return trip;
    }

    public Post getPost(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_POSTS, // a. table
                        TABLE_COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        Post post = new Post(cursor.getString(1), cursor.getString(2), 0);
        post.id = (Integer.parseInt(cursor.getString(0)));


        //log
        //Log.d("getPost("+id+")", post.title);

        // 5. return book
        return post;
    }




    public void getAllTrips() {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        onCreate(db);
    }

} 