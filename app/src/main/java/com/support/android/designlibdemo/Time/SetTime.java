package com.support.android.designlibdemo.Time;
import com.support.android.designlibdemo.R;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.support.android.designlibdemo.R;

import java.util.Calendar;

public class SetTime extends AppCompatActivity {

    private static final int bid1 = 1;
    private static final int bid2 = 2;

    private Button button3;
    private TextView textView;
    private int year, month, date, hour, minute, second, msecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);


        textView = (TextView)findViewById(R.id.text_view);
        // 協定世界時 (UTC)です適宜設定してください


        // 日時を指定したアラーム
        button3 = (Button)this.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
                TimeSet time= new TimeSet(getApplicationContext(),month,date,hour,minute);
                time.Alart();

                Toast.makeText(getApplicationContext(), "ALARM 2", Toast.LENGTH_SHORT).show();

                String setTime = "設定時間(UTC)："+year+"/"+(month+1)+"/"+date+" "+hour+":"+minute+":"+second+"."+msecond;
                textView.setText(setTime);
            }
        });
    }

    private void close(){
        finish();
    }
    private void setData(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis());
        year=calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);// 4=>5月
        date = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND)+3;
        msecond = calendar.get(Calendar.MILLISECOND);
//ローカルプッシュして停止できるようにする
    }
}