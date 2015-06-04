package com.example.evan.travellogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Created by evan on 5/31/15.
 *
 * Make a class that subclasses this if you want to save that object,
 * only use Integers,Strings, and Doubles as object fields.
 * just evoke the save() method on the object to save it.
 * evoke SavableObject.load() to load the object back up.
 * I'm sure there's an API that does this way better, but this is more fun
 */
public abstract class SavableObject {
    public static final String LAST_ID_KEY = "last_id_key";
    private static final String CLASS_NAME_FILE = "CLASS_FILE";
    private int id;
    private static int LAST_ID = 0;
    private final String TABLE_NAME;

    /**
     * creates unique id, and saves the className of the object
     * @param context the context calling the function
     */
    public SavableObject(Context context) {
        TABLE_NAME = getTableName();
        LAST_ID = Storage.loadInt(LAST_ID_KEY, context);
        if(LAST_ID < 0) { //if LAST_ID was not previously stored
            LAST_ID = 1;
        }else {
            LAST_ID += 1;
        }
        this.id = LAST_ID;
        //need to save last id
        Storage.saveInt(LAST_ID_KEY, LAST_ID, context);
        //saving className so I know it when I load it back up
        Storage.saveString(CLASS_NAME_FILE, String.valueOf(id),
                this.getClass().getName(), context);
    }

    public String getTableName()
    {
        return this.getClass().getSimpleName().toLowerCase();
    }

    //im saving all the fields of my object
    //returning unique id so I can load the object later on
    public int save(Context context) {
        ContentValues values = this.getValues();
        MySQLiteHelper helper = new MySQLiteHelper(context);
        if(!helper.hasTable(this.getTableName())) {//make sure table exists
            helper.createTable(this.getTableName(), this.getValues(), context);
        }
        helper.insert(TABLE_NAME, values);
        return this.id;
    }

    /**
     * @param id the id of the object to be loaded
     * @param context the context calling the function
     * @return the SavableObject that has the id
     * rv should be casted to correct subclass
     */
    public static SavableObject load(int id, Context context) {
        if(id < 0) { // all valid ids must be > 0
            return null;
        }
        String className = Storage.loadString(CLASS_NAME_FILE, String.valueOf(id), context);
        Class soClass = null;
        try {
            soClass = Class.forName(className);
        } catch (Exception e) {
            throw new RuntimeException("couldn't load class from name: " + className);
        }
        SavableObject so = null;
        MySQLiteHelper helper = new MySQLiteHelper(context);
        String tableName = soClass.getSimpleName();

        try {
            so = (SavableObject)
                    soClass.getDeclaredConstructor(Context.class).newInstance(context);
        } catch(Exception e) {
            Log.e("SavableObject", e.toString());
        }
        ContentValues values = helper.retrieve(tableName, id);

        Log.e("the size: ", String.valueOf(values.size()));
        for(String v : values.keySet()) {
            Log.e("from loading values", v + " " + values.get(v).toString());
        }
        for(Field field : soClass.getDeclaredFields()) {
            String fieldName = field.getName();
            String value = (String) values.get(fieldName);
            try {
                Class fieldClass = field.get(so).getClass();
                if (fieldClass == String.class) {
                    field.set(so, value);
                } else if (fieldClass == Double.class) {
                    field.setDouble(so, Double.parseDouble(value));
                } else if (fieldClass == Integer.class) {
                    field.setInt(so, Integer.parseInt(value));
                }
            } catch (Exception e) {}
        }
        return so;
    }

    /**
     * I think this function is cool
     * returns a contentvalues object where the key is the field name and the value
     * is the value of that field.
     */
    protected ContentValues getValues() {
        ContentValues values = new ContentValues();
        Set<String> fieldNames = new HashSet<String>();
        Field[] saveFields = SavableObject.class.getDeclaredFields();
        for(Field field : saveFields) {
            fieldNames.add(field.getName());
        }
        for(Field field : this.getClass().getDeclaredFields()) {
            String name = field.getName();
            if(!fieldNames.contains(name)) { //don't want to save SavableObject fields
                Object fieldValue = null;
                try {
                    fieldValue = field.get(this);
                    Class fieldClass = fieldValue.getClass();
                    if(fieldClass == String.class) {
                        values.put(name, (String) fieldValue);
                    } else if (fieldClass == Double.class) {
                        values.put(name, (Double) fieldValue);
                    } else if (fieldClass == Integer.class) {
                        values.put(name, (Integer) fieldValue);
                    }
                } catch (Exception e) {}
            }
            values.put("id", this.id); //id field must be saved
        }
        return  values;
    }

    public int getId() {
        return this.id;
    }

    //TODO
    public void retrieveWhere(String columnName, String columnValue) {

    }

}
