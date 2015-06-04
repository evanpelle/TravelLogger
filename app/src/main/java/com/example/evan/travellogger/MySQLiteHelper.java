package com.example.evan.travellogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MySQLiteHelper extends SQLiteOpenHelper {

    //java double is sql float
    public static final String DOUBLE_TYPE = "FLOAT";
    public static final String INTEGER_TYPE = "INTEGER";
    public static final String STRING_TYPE = "TEXT";

    public static final String TABLE_TRIPS = "trips";
    public static final String TABLE_POSTS = "posts";

    //THESE ARE THE TRIP COLUMNS
    public static final String TRIP_KEY_ID = "id";
    public static final String TRIP_KEY_TITLE = "title";
    public static final String TRIP_KEY_DESCRIPTION = "description";
    public static final String TRIP_KEY_PARENT_ID = "parent_id";
    private static final String[] TRIP_COLUMNS =
            {TRIP_KEY_ID,TRIP_KEY_TITLE,TRIP_KEY_DESCRIPTION, TRIP_KEY_PARENT_ID};
    //public static final String

    private static final String DATABASE_NAME = "test.db";
    private static final int DATABASE_VERSION = 1;

    //these are the post columns
    private static final String POST_KEY_ID = "id";
    private static final String POST_KEY_TITLE = "title";
    private static final String POST_KEY_DESCRIPTION = "description";
    private static final String POST_KEY_PARENT_ID = "parent_id";
    private static final String POST_KEY_TIMESTAMP = "time_stamp";
    private static final String POST_KEY_CITY = "city";
    private static final String POST_KEY_COUNTRY = "country";
    private static final String POST_KEY_PICTURE_FILE = "picture_file";
    private static final String POST_KEY_LATITUDE = "latitude";
    private static final String POST_KEY_LONGITUDE = "longitude";

    private static final String[] POST_COLUMNS =
            {POST_KEY_ID,POST_KEY_TITLE,POST_KEY_DESCRIPTION,POST_KEY_PARENT_ID,
            POST_KEY_TIMESTAMP, POST_KEY_CITY, POST_KEY_COUNTRY, POST_KEY_PICTURE_FILE,
            POST_KEY_LATITUDE, POST_KEY_LONGITUDE};



    //TRIPS: ID, Name

    // Database creation sql statement
    private static final String CREATE_TRIP_TABLE = "CREATE TABLE trips ( " +
            "id INTEGER PRIMARY KEY, " +
            "title TEXT, " +
            "description TEXT, " +
            "parent_id INTEGER)";

    private static final String CREATE_POST_TABLE = "CREATE TABLE posts ( " +
            POST_KEY_ID           + " INTEGER PRIMARY KEY, " +
            POST_KEY_TITLE        + " TEXT, "                +
            POST_KEY_DESCRIPTION  + " TEXT, "                +
            POST_KEY_PARENT_ID    + " INTEGER, "             +
            POST_KEY_TIMESTAMP    + " TEXT, "                +
            POST_KEY_CITY         + " TEXT, "                +
            POST_KEY_COUNTRY      + " TEXT, "                +
            POST_KEY_PICTURE_FILE + " TEXT, "                +
            POST_KEY_LATITUDE     + " FLOAT, "               +
            POST_KEY_LONGITUDE    + " FLOAT "                +
            ")";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TRIP_TABLE);
        database.execSQL(CREATE_POST_TABLE);
    }

    public void createTable(String tableName, ContentValues values, Context context) {
        Map<String, String> tableMap = new HashMap<String,String>();
        for(String key : values.keySet()) {
            String keyType = "";
            Class c = values.get(key).getClass();
            if(c == String.class) {
                keyType = MySQLiteHelper.STRING_TYPE;
            }else if(c == Double.class) {
                keyType = MySQLiteHelper.DOUBLE_TYPE;
            } else if(c == Integer.class) {
                keyType = MySQLiteHelper.INTEGER_TYPE;
            } else {continue;}
            tableMap.put(key, keyType);
        }
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        String createTable = "CREATE TABLE " + tableName + " ( ";
        for(Map.Entry<String,String> entry : tableMap.entrySet()) {
            createTable += entry.getKey() + " " + entry.getValue() + ", ";
        }
        //need to remove the comma at the end of the string
        createTable = createTable.substring(0, createTable.length()-2);
        createTable += " )";
        db.execSQL(createTable);
        db.close();
    }

    //need to test this
    public boolean hasTable(String tableName) {
        return listTables().contains(tableName);
    }

    public List<String> listTables()
    {
        ArrayList<String> tableList = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL_GET_ALL_TABLES = "SELECT name FROM " +
                "sqlite_master WHERE type='table' ORDER BY name";
        Cursor cursor = db.rawQuery(SQL_GET_ALL_TABLES, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                tableList.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return tableList;
    }


    public void destroy(Context context) {
        context.deleteDatabase(this.DATABASE_NAME);
    }

    public void insertTrip(Trip trip) {
        Log.e("adding trip", trip.title);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", trip.id);
        values.put("title", trip.title); // get title
        values.put("description", trip.description); // get author
        values.put("parent_id", trip.parent_id);
        db.insert("trips",
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public void insert(String table, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(table, null, values);
        db.close();
    }

    public ContentValues retrieve(String table, int id) {
        ContentValues entries = new ContentValues();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor columnCursor = db.query(table, null, null, null, null, null, null);
        String[] columns = columnCursor.getColumnNames();
        Cursor cursor =
                db.query(table, // a. table
                        columns, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        cursor.moveToFirst();
        for(String columnName : columns) {
            Log.e("column from database: ",columnName);
            String key = columnName;
            try {
                //TODO cursor doesn't have anything stored (probably) columns if doing aight tho
                Log.e("helper", columnName + " " +
                        cursor.getString(0));
                String value = cursor.getString(cursor.getColumnIndex(columnName));
                entries.put(key, value);
            } catch (Exception e) {
                Log.e("helperException", e.toString());
            }
        }
        cursor.close();
        return entries;
    }


    public void insertPost(Post post) {
        Log.e("adding post", post.title);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(POST_KEY_ID, post.id);
        values.put(POST_KEY_TITLE, post.title); // get title
        values.put(POST_KEY_DESCRIPTION, post.description); // get description
        values.put(POST_KEY_PARENT_ID, post.parentTripId);
        db.insert("posts",
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }
    public Trip getTrip(int id){
        if(id == -1) return null;

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
        int parent_id = -1;
        try{
            parent_id = Integer.parseInt(cursor.getString(3));
        } catch(Exception e) {

        }
        Log.i("loading_trip", String.valueOf(parent_id));


        // 4. build book object
        Trip trip = new Trip(cursor.getString(1), cursor.getString(2),
               parent_id);
        trip.id = (Integer.parseInt(cursor.getString(0)));


        //log
        //Log.d("getBook("+id+")", trip.title);

        // 5. return book
        return trip;
    }

    /*public Post getPost(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_POSTS, // a. table
                        POST_COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build post object
        Post post = new Post(cursor.getString(1), cursor.getString(2), -1, null, 0, 0);
        post.id = cursor.getInt(0);//(Integer.parseInt(cursor.getString(0)));
        post.parentTripId = cursor.getInt(3);//(Integer.parseInt(cursor.getString(3)));
        post.timeStamp = cursor.getString(4);
        post.city = cursor.getString(5);
        post.country = cursor.getString(6);
        post.pictureFile = cursor.getString(7);
        try {
            post.latitude = Double.parseDouble(cursor.getString(8));
            post.longitude = Double.parseDouble(cursor.getString(9));
        } catch (Exception e) {}

        cursor.close();
        db.close();
        //log
        //Log.d("getPost("+id+")", post.title);

        // 5. return book
        return post;
    } */

    public Post[] getChildPost(Trip parent) {
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_POSTS, // a. table
                        POST_COLUMNS, // b. column names
                        " parent_id = ?", // c. selections
                        new String[] { String.valueOf(parent.id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        return null;
    }

    public Trip[] getChildTrip(Trip parent) {
        return null;
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