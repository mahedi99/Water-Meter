package com.example.rexhe.watermeter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private ImageView splashIV;
    private TextView splashTV;
    private Animation animation;
    public static final String MY_PREFS_FILE = "loginInfo";
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splah_screen);

        splashIV= (ImageView) findViewById(R.id.splashIV);
        splashTV= (TextView) findViewById(R.id.splashTV);


        Thread timerThread = new Thread(){
            public void run(){
                try{
                    animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
                    splashIV.startAnimation(animation);
                    SharedPreferences shared = getSharedPreferences(MY_PREFS_FILE, MODE_PRIVATE);
                    if (shared!=null) {
                        email = (shared.getString("email", ""));
                        String password = (shared.getString("password", ""));
                    }
                    sleep(3600);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally {
                    if (!email.equals("")) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        timerThread.start();

/*
        SharedPreferences shared = getSharedPreferences(MY_PREFS_FILE, MODE_PRIVATE);
        if (shared!=null) {
            String email = (shared.getString("email", ""));
            String password = (shared.getString("password",""));
            if (!email.equals(""))
            {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }*/


    }
}
