package com.support.android.designlibdemo.Time;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.support.android.designlibdemo.R;
import android.os.Bundle;
import java.util.Calendar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;


import static android.content.Context.ALARM_SERVICE;

/**
 * Created by takato on 2017/08/28.
 */

public class TimeSet {
        private int year,month, date, hour, minute, second, msecond;
        private static final int bid1 = 1;
        private static final int bid2 = 2;
        private Context mcontext;
        private Intent intent;
        private Intent intent2;
        private AlarmManager am;
        private AlarmManager am2;
        private PendingIntent pending;
        private PendingIntent pendingIntent;

    TimeSet(Context context ,int month, int date, int hour, int minute){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            year = 2017;
            this.month = month;// 4=>5月
            this.date = date;
            this.hour = hour;
            this.minute = minute;
            second = 0;
            msecond = 0;
            mcontext = context;

        }
    TimeSet(){

    }
        public void Alart(){

            //設定時につけるアラーム
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(System.currentTimeMillis());
//
//            intent2 = new Intent(mcontext, AlarmBroadcastReceiver2.class);
//            intent2.putExtra("intentId", 2);
//            PendingIntent pending2 = PendingIntent.getBroadcast(mcontext, bid1, intent2, 0);
//
//            am2 = (AlarmManager)this.mcontext.getSystemService(ALARM_SERVICE);
//            am2.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending2);
            final String TAG ="test";
            Log.i("test","Alart:"+year);
            FirstAlarm();

            //ここから本来のアラーム
            Calendar calendar2 = Calendar.getInstance();
            // 過去の時間は即実行されます
            calendar2.set(Calendar.YEAR, year);
            calendar2.set(Calendar.MONTH, month);
            calendar2.set(Calendar.DATE, date);
            calendar2.set(Calendar.HOUR_OF_DAY, hour);
            calendar2.set(Calendar.MINUTE, minute );
            calendar2.set(Calendar.SECOND, second);
            calendar2.set(Calendar.MILLISECOND, msecond);

            intent = new Intent(mcontext, AlarmBroadcastReceiver.class);
            intent.putExtra("intentId", 2);
            pending = PendingIntent.getBroadcast(mcontext, bid2, intent, 0);

            // アラームをセットする
            am = (AlarmManager)this.mcontext.getSystemService(ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pending);
            Log.d("test","testtest");
        }

        public void FirstAlarm(){
            Log.d("AlarmBroadcastReceiver","onReceive() pid=" + android.os.Process.myPid());
            Intent intent = new Intent(mcontext,SetTime.class);
            pendingIntent =
                    PendingIntent.getActivity(mcontext, bid1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager notificationManager =
                    (NotificationManager)mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(mcontext)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(hour+"時"+minute+"分にタイマーをセットしました")
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("TestAlarm "+bid1)
                    .setContentText(hour+"時"+minute+"分にアラームがセットされています")
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setDeleteIntent(  //通知の削除時のPendingIntent
                            getPendingIntentWithBroadcast(NotificationReceiver.DELETE_NOTIFICATION)
                    )
                    .setContentIntent(pendingIntent)
                    .build();

            notificationManager.cancelAll();
            notificationManager.notify(R.string.app_name, notification);
        }

        private PendingIntent getPendingIntentWithBroadcast(String action) {
            return PendingIntent.getBroadcast(mcontext, 0 , new Intent(action), 0);
        }
        public void ClearAlarm(){
            am.cancel(pending);
            Log.d("test", "ClearAlarm: ");
        }
    }
