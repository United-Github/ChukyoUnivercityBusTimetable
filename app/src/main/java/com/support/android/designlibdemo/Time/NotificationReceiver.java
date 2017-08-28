package com.support.android.designlibdemo.Time;

import android.content.BroadcastReceiver;
import com.support.android.designlibdemo.R;
import android.content.Context;
import android.content.Intent;


public class NotificationReceiver extends BroadcastReceiver{

    //MainActivity側からも参照されるのでpublic
    public static final String DELETE_NOTIFICATION = "delete_notification";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (action.equals(DELETE_NOTIFICATION)) {
            TimeSet set=new TimeSet();
            set.ClearAlarm();
        }
    }
}