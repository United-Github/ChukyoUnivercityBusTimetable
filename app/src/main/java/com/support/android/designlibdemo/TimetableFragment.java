/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.support.android.designlibdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.support.android.designlibdemo.TimeManager.TimeManager;
import com.support.android.designlibdemo.TimetableList.layout.BusTimeListHeaderViewManager;
import com.support.android.designlibdemo.TimetableList.layout.BusTimeListViewManager;
import com.support.android.designlibdemo.TimetableList.layout.TimeListCustomAdapter;
import com.support.android.designlibdemo.TimetableList.model.TimeItemModel;
import com.support.android.designlibdemo.TimetableList.model.TimeMinutesListItemModel;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class TimetableFragment extends Fragment {
    private BusTimeListViewManager listViewManager;
    private BusTimeListHeaderViewManager headerViewManager;
    private TimeListCustomAdapter adapter;
    private final boolean isDepartJosui;
    private final int depart;
    private TimeManager timeManager;
    private int currentBusTimeMonth;
    private int currentBusTimeDate;
    private int currentBusTimeHour;
    private int currentBusTimeMinutes;
    private Calendar currentBusTimeCalendar;
    private long remainingMillis;
    private Handler handler = new Handler();
    private final static int updateInterval = 100;
    private Timer timer;

    public TimetableFragment(boolean isDepartJosui){
        super();
        this.isDepartJosui = isDepartJosui;
        this.depart = (isDepartJosui)? TimeManager.DEPART_JOSUI : TimeManager.DEPART_UNIVERCITY;
    }

//    @BindView(R.id.time_list_scrollview)
//    ScrollView scrollView;
    @BindView(R.id.bus_time_suspended)
    TextView mSuspendedText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rv = inflater.inflate(R.layout.fragment_cheese_list, container, false);
        ButterKnife.bind(this, rv);
        listViewManager = new BusTimeListViewManager(rv.findViewById(R.id.time_list));
        headerViewManager = new BusTimeListHeaderViewManager(rv.findViewById(R.id.time_header_root));
        headerViewManager.setOnNextClickListener(new onClickeHeaderViewButtonListener(true));
        headerViewManager.setOnPreviousClickListener(new onClickeHeaderViewButtonListener(false));
        adapter = new TimeListCustomAdapter(getContext());
        timeManager = ((BusTimerApplication)getActivity().getApplication()).getInstanceTimeManager();
        timer = new Timer();
        setTestData();
//        setCurrentDay();
        return rv;
    }

    private void setCurrentDay(){
        Calendar calendar = Calendar.getInstance();
        currentBusTimeMonth = calendar.get(Calendar.MONTH) + 1;
        currentBusTimeDate = calendar.get(Calendar.DATE);
        try {
            List<TimeItemModel> itemModels = timeManager.getBusSchedule(currentBusTimeMonth, currentBusTimeDate, depart);
            for (final TimeItemModel item : itemModels){
                listViewManager.addTimeItemModel(item);
            }
            timer.schedule(new updateRemainingTask(), 0, updateInterval);
        } catch (TimeManager.NoScheduleException e) {
//            headerViewManager.setNonBusTime();
            headerViewManager.setSuspended();
//            scrollView.setVisibility(View.GONE);
            mSuspendedText.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }

    private void setHeaderViewButtonLitener(){
    }

    private void updateCurrentBusTime(Calendar current){
        current = Calendar.getInstance();
        try {
            int currentBusTime[] = timeManager.nearBusTime(
                    currentBusTimeMonth,
                    currentBusTimeDate,
                    current.get(Calendar.HOUR_OF_DAY),
                    current.get(Calendar.MINUTE),
                    depart);
            currentBusTimeHour = currentBusTime[0];
            currentBusTimeMinutes = currentBusTime[1];
            currentBusTimeCalendar = Calendar.getInstance();
            currentBusTimeCalendar.set(timeManager.YEAR, currentBusTimeMonth -1, currentBusTimeDate, currentBusTimeHour, currentBusTimeMinutes);
            remainingMillis = currentBusTimeCalendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
            headerViewManager.setDepartTime(currentBusTimeHour, currentBusTimeMinutes);
        } catch (TimeManager.NoScheduleException | TimeManager.DayOverflowException e) {
            e.printStackTrace();
        }

    }
    class updateRemainingTask extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    remainingMillis -= updateInterval;
                    if (remainingMillis <= 0){
                        updateCurrentBusTime(Calendar.getInstance());
                    } else {
                        long remainingSeconds = remainingMillis / 1000;
                        int minutes = (int)(remainingSeconds / 60);
                        int seconds = (int)(remainingSeconds % 60);
                        headerViewManager.setRemainingTime(minutes, seconds);;
                        listViewManager.setCurrentTime(currentBusTimeHour, currentBusTimeMinutes);
                    }
                }
            });
        }
    }

    private class onClickeHeaderViewButtonListener implements View.OnClickListener{
        private boolean next;
        onClickeHeaderViewButtonListener(boolean next){
            this.next = next;
        }
        @Override
        public void onClick(View view) {
            try {
                int busTime[];
                if (next){
                    busTime = timeManager.afterBusTime(
                            currentBusTimeMonth,
                            currentBusTimeDate,
                            currentBusTimeHour,
                            currentBusTimeMinutes,
                            depart);
                } else {
                    busTime = timeManager.beforeBusTime(
                            currentBusTimeMonth,
                            currentBusTimeDate,
                            currentBusTimeHour,
                            currentBusTimeMinutes,
                            depart);
                }
                Calendar current = Calendar.getInstance();
                current.set(
                        timeManager.YEAR,
                        currentBusTimeMonth,
                        currentBusTimeDate,
                        busTime[0],
                        busTime[1]);
                updateCurrentBusTime(current);
            } catch (TimeManager.NoScheduleException e) {
                e.printStackTrace();
            } catch (TimeManager.DayOverflowException e) {
                e.printStackTrace();
            } catch (TimeManager.DayUnderflowException e) {
                e.printStackTrace();
            }
        }
    }
    private void setTestData(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 9, 4);
        currentBusTimeMonth = calendar.get(Calendar.MONTH) + 1;
        currentBusTimeDate = calendar.get(Calendar.DATE);
        try {
            List<TimeItemModel> itemModels = timeManager.getBusSchedule(currentBusTimeMonth, currentBusTimeDate, depart);
            for (final TimeItemModel item : itemModels){
                listViewManager.addTimeItemModel(item);
            }
        } catch (TimeManager.NoScheduleException e) {
        }
        listViewManager.setCurrentTime(9, 6);
    }
}
