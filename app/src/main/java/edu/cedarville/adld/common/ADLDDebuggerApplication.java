package edu.cedarville.adld.common;

import android.app.Application;

import timber.log.Timber;

public class ADLDDebuggerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
