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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.support.android.designlibdemo.TimeManager.TimeManager;
import com.support.android.designlibdemo.TimetableList.layout.BusTimeListViewManager;
import com.support.android.designlibdemo.TimetableList.layout.OnBusTimeItemClickListener;
import com.support.android.designlibdemo.TimetableList.layout.TimeListCustomAdapter;
import com.support.android.designlibdemo.TimetableList.model.TimeItemModel;
import com.support.android.designlibdemo.TimetableList.model.TimeMinutesListItemModel;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
@SuppressLint("ValidFragment")
public class TimetableFragment extends Fragment {
    private BusTimeListViewManager busTimeListViewManager;
    private TimeListCustomAdapter adapter;
    private final boolean isDepartJosui;
    private final int depart;
    private TimeManager timeManager;
    public TimetableFragment(boolean isDepartJosui){
        super();
        this.isDepartJosui = isDepartJosui;
        this.depart = (isDepartJosui)? TimeManager.DEPART_JOSUI : TimeManager.DEPART_UNIVERCITY;
    }
    @BindView(R.id.time_list_scrollview)
    ScrollView scrollView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rv = inflater.inflate(R.layout.fragment_cheese_list, container, false);
        busTimeListViewManager = new BusTimeListViewManager(rv.findViewById(R.id.time_list_scrollview));
        adapter = new TimeListCustomAdapter(getContext());
        timeManager = ((BusTimerApplication)getActivity().getApplication()).getInstanceTimeManager();
        setTestData();
        return rv;
    }


    private void setTestData(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        try {
            List<TimeItemModel> itemModels = timeManager.getBusSchedule(month, date, depart);
            for (final TimeItemModel item : itemModels){
                busTimeListViewManager.addTimeItemModel(item);
            }
        } catch (TimeManager.NoScheduleException e) {
            e.printStackTrace();
        }
    }
}
