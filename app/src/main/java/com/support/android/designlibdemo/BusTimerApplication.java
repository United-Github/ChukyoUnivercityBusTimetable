package com.support.android.designlibdemo;

import android.app.Application;

import com.support.android.designlibdemo.TimeManager.TimeManager;

/**
 * Created by ragro on 2017/08/28.
 */

public class BusTimerApplication extends Application {
    private static TimeManager timeManager;
    @Override
    public void onCreate() {
        super.onCreate();
        timeManager = new TimeManager(BusTimerApplication.this);
    }

    public TimeManager getInstanceTimeManager(){
        return timeManager;
    }
}
