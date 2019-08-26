package com.example.alifd.listfilmrecycler.base;

import android.app.Application;

import com.example.alifd.listfilmrecycler.BuildConfig;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Timber.e("RUNNING");
        }

        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("fav_catalogue.db")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}
