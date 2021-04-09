package com.allteran.pizzamira.applications;

import android.app.Application;

import com.allteran.pizzamira.services.RealmService;

public class PizzaMiraApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RealmService.initRealm(this);
    }
}
