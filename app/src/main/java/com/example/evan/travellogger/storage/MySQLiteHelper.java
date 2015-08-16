package com.example.evan.travellogger.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * use this class to access, store, and create sql tables
 * only supports Strings, doubles, and ints for now
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "test.db";
    private static final int DATABASE_VERSION = 1;
    public static final String DOUBLE_TYPE = "FLOAT";
    public static final String INTEGER_TYPE = "INTEGER";
    public static final String STRING_TYPE = "TEXT";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) { }

    /**
     * creates a new table in the database
     * @param tableName name of the table to be created
     * @param values basically a map of strings where the key is the
     *               name of the column and the value is the type
     */
    public void createTable(String tableName, ContentValues values) {
        Map<String, String> tableMap = new HashMap<>();
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

    public boolean hasTable(String tableName) {
        return listTables().contains(tableName);
    }

    /**
     *
     * @return a list of all the table names in the database
     */
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
        db.close();
        return tableList;
    }


    public void destroy(Context context) {
        context.deleteDatabase(this.DATABASE_NAME);
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
            try {
                String value = cursor.getString(cursor.getColumnIndex(columnName));
                entries.put(columnName, value);
            } catch (Exception e) {
                Log.e("helperException", e.toString());
            }
        }
        cursor.close();
        db.close();
        return entries;
    }

    public List<ContentValues> retrieve(String table, String column, String value) {
        List<ContentValues> entries = new LinkedList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor columnCursor = db.query(table, null, null, null, null, null, null);
        String[] columns = columnCursor.getColumnNames();
        Cursor cursor =
                db.query(table, // a. table
                        columns, // b. column names
                        column + " = ?", // c. selections
                        new String[] { value }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ContentValues entry = new ContentValues();
                    for(String columnName : columns) {
                        try {
                            String values = cursor.getString(cursor.getColumnIndex(columnName));
                            entry.put(columnName, value);
                        } catch (Exception e) {
                            Log.e("helperException", e.toString());
                        }
                    }
                    entries.add(entry);
                } while (cursor.moveToNext());
            }
        }


        cursor.close();
        db.close();
        return entries;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        onCreate(db);
    }
} 