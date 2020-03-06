package com.example.expirationdateapp;

import android.app.Application;

public class MyApplication extends Application {
    public AppContainer appContainer;
    private static MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();

        appContainer = new AppContainer(this);
        application = this;
    }

    public static MyApplication getInstance(){
        return application;
    }
}
