package com.example.rexhe.watermeter;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class About extends AppCompatActivity {


    private ActionBar actionBar;
    public static final String MY_PREFS_COLOR = "mycolor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Changing ActionBar Color
        actionBar=getSupportActionBar();
        SharedPreferences shared = getSharedPreferences(MY_PREFS_COLOR, MODE_PRIVATE);
        if (shared!=null) {
            int getColor = (shared.getInt("position", 0));
            actionBar.setBackgroundDrawable(new ColorDrawable(getColor));
        }
    }
}
