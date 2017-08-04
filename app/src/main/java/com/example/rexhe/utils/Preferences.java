package com.example.rexhe.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.example.rexhe.application.MyApplication;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mahedi on 8/4/17.
 */

public class Preferences {

    private static final String MY_PREFS_COLOR = "my_color";
    private static Preferences preferences;
    private final Context mContext = MyApplication.getAppContext();
    private final SharedPreferences pref;

    private Preferences() {
        pref = mContext.getSharedPreferences(MY_PREFS_COLOR, MODE_PRIVATE);
    }

    public static Preferences getInstance(){
        if(preferences == null){
            preferences = new Preferences();
        }
        return preferences;
    }

    public int getToolbarColor(){
        return pref.getInt(MY_PREFS_COLOR, Color.parseColor("#1E824C"));
    }

    public void setToolbarColor(int toolbarColor) {
        pref.edit().putInt(MY_PREFS_COLOR, toolbarColor).commit();
    }
}
