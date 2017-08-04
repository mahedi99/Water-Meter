package com.example.rexhe.watermeter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.rexhe.utils.Preferences;

import petrov.kristiyan.colorpicker.ColorPicker;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout colorLL;
    ImageView circleImage;
//    ActionBar actionBar;
    public static final String MY_PREFS_COLOR = "mycolor";

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        int preferredColor = Preferences.getInstance().getToolbarColor();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(preferredColor);

        colorLL= (LinearLayout) findViewById(R.id.colorLL);
        circleImage= (ImageView) findViewById(R.id.circleImage);
        GradientDrawable bgShape = (GradientDrawable)circleImage.getBackground();
        bgShape.setColor(preferredColor);
        colorLL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v==colorLL)
        {
            final ColorPicker colorPicker = new ColorPicker(SettingsActivity.this);
            colorPicker.setRoundButton(true);
            colorPicker.setFastChooser(new ColorPicker.OnFastChooseColorListener() {
                @Override
                public void setOnFastChooseColorListener(int position, int color) {
                    GradientDrawable bgShape = (GradientDrawable)circleImage.getBackground();
                    bgShape.setColor(color);
                    toolbar.setBackgroundColor(color);
                    changeColor(color);
                    colorPicker.dismissDialog();
                }
            }).setNegativeButton("DEFAULT", new ColorPicker.OnButtonListener() {
                @Override
                public void onClick(View v) {
                    GradientDrawable bgShape = (GradientDrawable)circleImage.getBackground();
                    bgShape.setColor(Color.parseColor("#1E824C"));
                    toolbar.setBackgroundColor(Color.parseColor("#1E824C"));
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
        Preferences.getInstance().setToolbarColor(color);
    }

}
