package com.support.android.designlibdemo.TimetableList.layout;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.TimetableList.model.TimeItemModel;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ragro on 2017/08/20.
 */

public class TimeListCustomAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<TimeItem> mDataList ;
    private Context mContext;
    private ListView rootListView;
    private final String TAG = "TimeList_DEBUG";

    public TimeListCustomAdapter(Context context, ListView rootListView){
        mInflater = LayoutInflater.from(context);
        mDataList = new ArrayList<>();
        mContext = context;
        this.rootListView = rootListView;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = mInflater.inflate(R.layout.time_list_hour, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        TimeItem timeItem = (TimeItem)this.getItem(i);
        viewHolder.update(timeItem);
        return view;
    }
    public void add(TimeItemModel timeItemModel){
        TimeItem timeItem = new TimeItem(mContext, timeItemModel);
        mDataList.add(timeItem);
        notifyDataSetChanged();
    }
    // 残り時間を設定する
    public void setRemainingTime(int hour, int minutes, int remainingTime){
        try {
            final TimeItem timeItem = getTimeItemFromHour(hour);
            timeItem.mAdapter.setRemainingTime(minutes, remainingTime);
            timeItem.mAdapter.notifyDataSetChanged();
        } catch (NonHourException e) {
            return;
        }
    }
    // 残り時間を消す
    public void clearRemainingTime(int hour, int minutes){
        try{
            final TimeItem timeItem = getTimeItemFromHour(hour);
            timeItem.mAdapter.clearRemainingTime(minutes);
            timeItem.mAdapter.notifyDataSetChanged();
        } catch (NonHourException e) {
            return;
        }
    }
    private int mHightLightMemoryHour = -1;
    private int mHightLightMemoryMinutes = -1;
    // 前回ハイライトした場所のハイライトを外す
    public void clearHightLight(){
        if (mHightLightMemoryHour == -1 || mHightLightMemoryMinutes == -1){
            return;
        }
        try {
            final TimeItem timeItem = getTimeItemFromHour(mHightLightMemoryHour);
            timeItem.mAdapter.clearHightLight(mHightLightMemoryMinutes);
            timeItem.mAdapter.notifyDataSetChanged();
            mHightLightMemoryHour = -1;
            mHightLightMemoryMinutes = -1;
        } catch (NonHourException e) {
            return;
        }
    }
    // 指定場所をハイライトして、その場所を覚えておく
    public void setHightLight(int hour, int minutes){
        clearHightLight();
        try {
            final TimeItem timeItem = getTimeItemFromHour(hour);
            clearHightLight();
            timeItem.mAdapter.setHightLight(minutes);
            timeItem.mAdapter.notifyDataSetChanged();
            mHightLightMemoryHour = hour;
            mHightLightMemoryMinutes = minutes;
        } catch (NonHourException e) {
            return;
        }
    }
    public int getPotisionFromHour(int hour) {
        int count = 0;
        for (final TimeItem item : mDataList) {
            if (item.timeItemModel.hour == hour) {
                break;
            }
            count ++;
        }
        return  count;
    }
    public int getViewY(int hour, int minutes){
        try {
            // 時と分のViewのTOPを加算して表示
            final TimeItem timeItem = getTimeItemFromHour(hour);
            final int hourY = timeItem.topY;
            final int minutesY = timeItem.mAdapter.getTimeMinutesItemByMinutes(minutes).topY;
            return hourY + minutesY;
        } catch (NonHourException e) {
            return -1;
        }
    }
    public void setAllItemClickListener(OnBusTimeItemClickListener listener){
        for (final TimeItem timeItem : mDataList){
            timeItem.setOnItemListener(listener);
            timeItem.mAdapter.notifyDataSetChanged();
        }
    }
    public void setUntilState(int hour, int minutes, boolean enabled){
        for(final TimeItem item : mDataList){
                if (item.timeItemModel.hour < hour){
                    item.mAdapter.setAllState(enabled);
                    item.mAdapter.notifyDataSetChanged();
                } else if (item.timeItemModel.hour == hour){
                    item.mAdapter.setUntilState(minutes, enabled);
                    item.mAdapter.notifyDataSetChanged();
                } else {
                    return;
                }
        }
    }
    public void setAllItemState(){
        for(final TimeItem item : mDataList) {
            item.mAdapter.setAllState(true);
            item.mAdapter.notifyDataSetChanged();
        }
    }
    private TimeItem getTimeItemFromHour(int hour) throws NonHourException{
        for (final TimeItem item : mDataList) {
            if (item.timeItemModel.hour == hour) {
                return item;
            }
        }
        throw new NonHourException();
    }
    static class ViewHolder {
        @BindView(R.id.time_list_hour_item)
        LinearLayout linearLayout;
        @BindView(R.id.time_list_hour_text)
        TextView hourText;
        @BindView(R.id.time_list_minutes_list)
        ListView minutesList;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void update(TimeItem timeItem){
            hourText.setText(Integer.toString(timeItem.timeItemModel.hour));
            if (timeItem.listener != null) {
                minutesList.setOnItemClickListener(new onTimeItemClickListener(
                        timeItem.listener,
                        timeItem.timeItemModel.hour,
                        timeItem.mAdapter));
            }
            minutesList.setAdapter(timeItem.mAdapter);
            timeItem.topY = linearLayout.getTop();
        }
    }
    private class TimeItem {
        TimeItemModel timeItemModel;
        TimeListMinutesCustomAdapter mAdapter;
        OnBusTimeItemClickListener listener;
        int topY;

        public TimeItem(Context context, TimeItemModel itemModel){
            timeItemModel = itemModel;
            mAdapter = new TimeListMinutesCustomAdapter(context);
            mAdapter.setMinutesList(timeItemModel.minutesList);
            listener = null;
        }

        public void setOnItemListener(OnBusTimeItemClickListener listener){
            this.listener = listener;
        }
    }
    private static class NonHourException extends ExecutionException{
        @Override
        public String getMessage() {
            return "存在しない時刻";
        }
    }
    private static class onTimeItemClickListener implements AdapterView.OnItemClickListener{
        private OnBusTimeItemClickListener listener;
        private TimeListMinutesCustomAdapter adapter;
        private int hour;
        public onTimeItemClickListener(OnBusTimeItemClickListener listener, int hour, TimeListMinutesCustomAdapter adapter){
            this.listener = listener;
            this.hour = hour;
            this.adapter = adapter;
        }
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            int minutes =  adapter.getMinutesByPotiision(i);
            listener.onItemClick(hour, minutes);
        }
    }
}

