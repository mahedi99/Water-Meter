package com.example.rexhe.watermeter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import petrov.kristiyan.colorpicker.ColorPicker;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    LinearLayout colorLL;
    ImageView circleImage;
    ActionBar actionBar;
    public static final String MY_PREFS_COLOR = "mycolor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Changing ActionBar Color
        actionBar=getSupportActionBar();
        SharedPreferences shared = getSharedPreferences(MY_PREFS_COLOR, MODE_PRIVATE);
        int getColor=0;
        if (shared!=null) {
            getColor = (shared.getInt("position", 0));
            actionBar.setBackgroundDrawable(new ColorDrawable(getColor));
        }

        colorLL= (LinearLayout) findViewById(R.id.colorLL);
        circleImage= (ImageView) findViewById(R.id.circleImage);
        GradientDrawable bgShape = (GradientDrawable)circleImage.getBackground();
        bgShape.setColor(getColor);
        colorLL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v==colorLL)
        {
            final ColorPicker colorPicker = new ColorPicker(Settings.this);
            colorPicker.setRoundButton(true);
            colorPicker.setFastChooser(new ColorPicker.OnFastChooseColorListener() {
                @Override
                public void setOnFastChooseColorListener(int position, int color) {
                    GradientDrawable bgShape = (GradientDrawable)circleImage.getBackground();
                    bgShape.setColor(color);
                    actionBar.setBackgroundDrawable(new ColorDrawable(color));
                    //setTheme(R.style.AppTheme);

                    changeColor(color);
                    colorPicker.dismissDialog();
                }
            }).setNegativeButton("DEFAULT", new ColorPicker.OnButtonListener() {
                @Override
                public void onClick(View v) {
                    GradientDrawable bgShape = (GradientDrawable)circleImage.getBackground();
                    bgShape.setColor(Color.parseColor("#1E824C"));
                    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1E824C")));
                    changeColor(Color.parseColor("#1E824C"));
                }
            }).setPositiveButton("OK", new ColorPicker.OnButtonListener() {
                @Override
                public void onClick(View v) {
                    Log.d("CANCEL", "cancel");
                }
            }).setDefaultColor(Color.parseColor("#FFFFFF")).setColumns(5).setRoundButton(true).show();
        }

    }

    private void changeColor(int color)
    {
        //delete data
        Context context=Settings.this;
        SharedPreferences myPrefs = context.getSharedPreferences(MY_PREFS_COLOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.clear();
        editor.commit();

        //save data
        SharedPreferences shared = getSharedPreferences(MY_PREFS_COLOR, MODE_PRIVATE);
        SharedPreferences.Editor editor2 = shared.edit();
        editor2.putInt("position", color);
        editor2.commit();
    }

}
