package com.support.android.designlibdemo.TimeManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.support.android.designlibdemo.MainActivity;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.TimetableList.model.TimeItemModel;

import java.util.List;

public class TimeManagerTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_manager_test);
        TimeManager timeManager=new TimeManager(TimeManagerTestActivity.this);
        try {
            int[] nt = timeManager.nearBusTime(4, 1, 8, 53, TimeManager.DEPART_JOSUI);
            int[] bt = timeManager.beforeBusTime(4, 1, 9, 1, TimeManager.DEPART_JOSUI);
            int[] at = timeManager.afterBusTime(4, 1, 8, 53, TimeManager.DEPART_JOSUI);
            List<TimeItemModel> bs = timeManager.getBusSchedule(4, 1, TimeManager.DEPART_JOSUI);
            String[] ms = timeManager.getMonthSchedule(4);
            TextView testText=(TextView)findViewById(R.id.ttmt);
            testText.setText(nt[0]+" "+nt[1]+"+"+bt[0]+" "+bt[1]+"+"+at[0]+" "+at[1]+"\n+"+bs.get(0).minutesList.get(0).minutes+"+"+ms[0]);
        }catch(TimeManager.DayOverflowException e){
            e.printStackTrace();
        }catch (TimeManager.NoScheduleException e){
            e.printStackTrace();
        }catch (TimeManager.DayUnderflowException e){
            e.printStackTrace();
        }

    }
}
