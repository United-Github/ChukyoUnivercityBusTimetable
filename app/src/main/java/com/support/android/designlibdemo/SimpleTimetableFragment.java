package com.support.android.designlibdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.support.android.designlibdemo.TimeManager.TimeManager;
import com.support.android.designlibdemo.TimetableList.layout.BusTimeListViewManager;
import com.support.android.designlibdemo.TimetableList.layout.OnBusTimeItemClickListener;
import com.support.android.designlibdemo.TimetableList.layout.TimeListCustomAdapter;
import com.support.android.designlibdemo.TimetableList.model.TimeItemModel;
import com.support.android.designlibdemo.TimetableList.model.TimeMinutesListItemModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

@SuppressLint("ValidFragment")
public class SimpleTimetableFragment extends Fragment {
    private BusTimeListViewManager busTimeListViewManager;
    private TimeListCustomAdapter adapter;
    private final boolean isDepartJosui;
    private final int depart;
    private final int currentBusTimeMonth;
    private final int currentBusTimeDate;
    private TimeManager timeManager;
    public SimpleTimetableFragment(final int month, final int day, boolean isDepartJosui){
        super();
        this.isDepartJosui = isDepartJosui;
        this.depart = (isDepartJosui)? TimeManager.DEPART_JOSUI : TimeManager.DEPART_UNIVERCITY;
        currentBusTimeMonth = month;
        currentBusTimeDate = day;

    }

    @BindView(R.id.time_list_scrollview)
    ScrollView scrollView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rv = inflater.inflate(R.layout.fragment_simple_timetable, container, false);
        busTimeListViewManager = new BusTimeListViewManager(rv.findViewById(R.id.time_list_scrollview));
        adapter = new TimeListCustomAdapter(getContext());
        setCurrentDate();
        return rv;
    }

    private void setCurrentDate(){
        timeManager = ((BusTimerApplication)getActivity().getApplicationContext()).getInstanceTimeManager();
        try {
            List<TimeItemModel> itemModels = timeManager.getBusSchedule(currentBusTimeMonth, currentBusTimeDate, depart);
            for (final TimeItemModel item : itemModels){
                busTimeListViewManager.addTimeItemModel(item);
            }
        } catch (TimeManager.NoScheduleException e) {
            e.printStackTrace();
        }
    }

}
