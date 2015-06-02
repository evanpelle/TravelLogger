package com.example.evan.travellogger;

import android.content.ContentValues;
import android.content.Context;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Created by evan on 5/31/15.
 *
 * Make a class the extends this if you want to save that object,
 * only use Integers,Strings, and Doubles as object fields.
 * just envoke the save() method on the object to save it.
 */
public abstract class SavableObject {
    public static final String LAST_ID_KEY = "last_id_key";

    private Map<String, String> map = new HashMap<String,String>();
    private int id;
    private static int LAST_ID = 0;
    private final String TABLE_NAME;


    public SavableObject(Context context) {
        TABLE_NAME = this.getClass().toString();
        LAST_ID = Storage.loadInt(LAST_ID_KEY, context);
        if(LAST_ID < 0) { //if LAST_ID was not previously stored
            LAST_ID = 1;
        }else {
            LAST_ID += 1;
        }
        this.id = LAST_ID;
        Storage.saveInt(LAST_ID_KEY, LAST_ID, context);
        Field f = null;

    }

    //I think this function is cool
    //im saving all the fields of my object
    public void save(Context context) {
        ContentValues values = new ContentValues();
        Set<String> fieldNames = new HashSet<String>();
        Field[] saveFields = SavableObject.class.getDeclaredFields();
        for(Field field : saveFields) {
            fieldNames.add(field.getName());
        }
        for(Field field : this.getClass().getDeclaredFields()) {
            String name = field.getName();
            if(!fieldNames.contains(name)) { //don't want to save SavableObject fields
                Object objectValue = null;

                try {
                    objectValue = field.get(this);
                    Class objectClass = objectValue.getClass();
                    if(objectClass == String.class) {
                        values.put(name, (String) objectValue);
                    } else if (objectClass == Double.class) {
                        values.put(name, (Double) objectValue);
                    } else if (objectClass == Integer.class) {
                        values.put(name, (Integer) objectValue);
                    }
                } catch (Exception e) {}
            }
        }
        (new MySQLiteHelper(context)).insert(TABLE_NAME, values);
    }

    protected abstract Map<String, String> getValues();



    public void retrieveWhere(String columnName, String columnValue) {

    }
}
