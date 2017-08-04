package com.example.rexhe.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by mahedi on 8/4/17.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext(){
        return context;
    }
}
