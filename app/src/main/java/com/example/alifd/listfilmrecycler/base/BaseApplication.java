package com.example.alifd.listfilmrecycler.base;

import android.app.Application;

import com.example.alifd.listfilmrecycler.BuildConfig;

import timber.log.Timber;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Timber.e("RUNNING");
        }
    }
}
