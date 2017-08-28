package com.support.android.designlibdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.support.android.designlibdemo.TimetableList.layout.BusTimeListViewManager;
import com.support.android.designlibdemo.TimetableList.layout.OnBusTimeItemClickListener;
import com.support.android.designlibdemo.TimetableList.layout.TimeListCustomAdapter;
import com.support.android.designlibdemo.TimetableList.model.TimeItemModel;
import com.support.android.designlibdemo.TimetableList.model.TimeMinutesListItemModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

@SuppressLint("ValidFragment")
public class SimpleTimetableFragment extends Fragment {
    private BusTimeListViewManager busTimeListViewManager;
    private TimeListCustomAdapter adapter;
    private final boolean isDepartJosui;
    public SimpleTimetableFragment(boolean isDepartJosui){
        this.isDepartJosui = isDepartJosui;
    }

    @BindView(R.id.time_list_scrollview)
    ScrollView scrollView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rv = inflater.inflate(R.layout.fragment_simple_timetable, container, false);
        busTimeListViewManager = new BusTimeListViewManager(rv.findViewById(R.id.time_list_scrollview));
        adapter = new TimeListCustomAdapter(getContext());
        setTestData();
        return rv;
    }

    private void setTestData(){
        TimeItemModel timeItemModel = new TimeItemModel();
        timeItemModel.hour = 1;
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(1, true));
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(10, false));
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(20, false));
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(39, false));
        busTimeListViewManager.addTimeItemModel(timeItemModel);
        timeItemModel = new TimeItemModel();
        timeItemModel.hour = 2;
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(1, true));
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(10, false));
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(20, false));
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(39, false));
        busTimeListViewManager.addTimeItemModel(timeItemModel);
        timeItemModel = new TimeItemModel();
        timeItemModel.hour = 3;
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(1, true));
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(10, false));
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(20, false));
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(39, false));
        busTimeListViewManager.addTimeItemModel(timeItemModel);
        timeItemModel = new TimeItemModel();
        timeItemModel.hour = 4;
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(1, true));
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(10, false));
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(20, false));
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(39, false));
        busTimeListViewManager.addTimeItemModel(timeItemModel);
        timeItemModel = new TimeItemModel();
        timeItemModel.hour = 5;
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(1, true));
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(10, false));
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(20, false));
        timeItemModel.minutesList.add(new TimeMinutesListItemModel(39, false));
        busTimeListViewManager.addTimeItemModel(timeItemModel);
        busTimeListViewManager.setRemainingTime(1, 1, 5);
        busTimeListViewManager.setRemainingTime(1, 10, 10);
        busTimeListViewManager.setRemainingTime(1, 20, 2);
        busTimeListViewManager.clearRemainingTime(1, 20);
        busTimeListViewManager.setBusTimeClickListener(new OnBusTimeItemClickListener() {
            @Override
            public void onItemClick(int hour, int minutes) {
                Log.d("hoge", Integer.toString(hour) + " : " + Integer.toString(minutes));
            }
        });
        busTimeListViewManager.setUntilDisable(2, 20);
        busTimeListViewManager.setCurrentTime(3, 20);
    }
}
