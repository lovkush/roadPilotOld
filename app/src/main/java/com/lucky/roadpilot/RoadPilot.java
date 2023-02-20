package com.lucky.roadpilot;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class RoadPilot extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
