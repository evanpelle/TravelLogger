package com.example.evan.travellogger;

import android.content.Context;

import java.util.Map;

/**
 * Created by evan on 5/31/15.
 */
public class SaveablePost extends SavableObject {

    public SaveablePost(Context context) {
        super(context);
    }

    @Override
    protected Map<String, String> getValues() {
        return null;
    }
}
