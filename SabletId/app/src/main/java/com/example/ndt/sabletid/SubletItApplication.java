package com.example.ndt.sabletid;

import android.app.Application;
import android.content.Context;

public class SubletItApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
