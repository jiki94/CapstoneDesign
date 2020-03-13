package com.example.expirationdateapp;

import android.app.Application;

// AppContainer 사용 위해서
public class MyApplication extends Application {
    public AppContainer appContainer;
    private static MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();

        appContainer = new AppContainer(this);
        application = this;
    }

    // Context 없을 경우에도 접근 가능하게
    public static MyApplication getInstance(){
        return application;
    }
}
