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
public class TimetableFragment extends Fragment {
    private BusTimeListViewManager busTimeListViewManager;
    private TimeListCustomAdapter adapter;
    private final boolean isDepartJosui;
    public TimetableFragment(boolean isDepartJosui){
        super();
        this.isDepartJosui = isDepartJosui;
    }
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

    }
}
