package com.allteran.pizzamira.applications;

import android.app.Application;

import com.allteran.pizzamira.services.RealmService;

import io.realm.Realm;

public class PizzaMiraApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
