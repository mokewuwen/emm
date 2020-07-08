package com.adtest.fb;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HelperUtils.init(getApplicationContext());
    }
}
