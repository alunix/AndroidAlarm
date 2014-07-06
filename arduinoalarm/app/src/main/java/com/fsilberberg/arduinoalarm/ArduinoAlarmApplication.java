package com.fsilberberg.arduinoalarm;

import android.app.Application;

import com.fsilberberg.arduinoalarm.db.AlarmDbManager;

/**
 * Created by 333fr_000 on 7/5/14.
 */
public class ArduinoAlarmApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the database
        AlarmDbManager.initialize(this);
    }

    @Override
    public void onTerminate() {
        AlarmDbManager.close();
        super.onTerminate();
    }
}
