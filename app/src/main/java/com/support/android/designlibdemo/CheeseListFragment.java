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
import com.support.android.designlibdemo.TimetableList.layout.BusTimeListViewManager;
import com.support.android.designlibdemo.TimetableList.layout.OnBusTimeItemClickListener;
import com.support.android.designlibdemo.TimetableList.layout.TimeListCustomAdapter;
import com.support.android.designlibdemo.TimetableList.model.TimeItemModel;
import com.support.android.designlibdemo.TimetableList.model.TimeMinutesListItemModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

public class CheeseListFragment extends Fragment {
    private BusTimeListViewManager busTimeListViewManager;
    private TimeListCustomAdapter adapter;
    @BindView(R.id.time_list_scrollview)
    ScrollView scrollView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rv = inflater.inflate(R.layout.fragment_cheese_list, container, false);
        busTimeListViewManager = new BusTimeListViewManager(rv.findViewById(R.id.time_list_scrollview));
        adapter = new TimeListCustomAdapter(getContext());
        setTestData();
        return rv;
    }

    private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
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
        busTimeListViewManager.setCurrentTime(3, 1);
    }
}
