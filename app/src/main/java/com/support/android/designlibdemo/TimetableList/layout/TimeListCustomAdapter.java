package com.support.android.designlibdemo.TimetableList.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
    private List<TimeItemView> mDataList ;
    private Context mContext;

    public TimeListCustomAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        mDataList = new ArrayList<>();
        mContext = context;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        TimeItemView timeItemView ;
        if (view != null) {
            timeItemView = (TimeItemView) view.getTag();
        } else {
            timeItemView = mDataList.get(i);
            view = mInflater.inflate(R.layout.time_list_hour, parent, false);
            timeItemView.holder = new ViewHolder(view);
            view.setTag(timeItemView);
            timeItemView.setAdapter();
        }
        timeItemView.setDataToView();
        return view;
    }
    public void add(TimeItemModel timeItemModel){
        TimeItemView timeItemView = new TimeItemView(mContext, timeItemModel);
        mDataList.add(timeItemView);
        notifyDataSetChanged();
    }
    // 残り時間を設定する
    public void setRemainingTime(int hour, int minutes, int remainingTime){
        try {
            final TimeItemView itemView = getTimeItemViewFromHour(hour);
            itemView.mAdapter.setRemainingTime(minutes, remainingTime);
        } catch (NonHourException e) {
            return;
        }
    }
    // 残り時間を消す
    public void clearRemainingTime(int hour, int minutes){
        try{
            final TimeItemView itemView = getTimeItemViewFromHour(hour);
            itemView.mAdapter.clearRemainingTime(minutes);
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
            final TimeItemView itemView = getTimeItemViewFromHour(mHightLightMemoryHour);
            itemView.mAdapter.clearHightLight(mHightLightMemoryMinutes);
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
            final TimeItemView itemView = getTimeItemViewFromHour(hour);
            clearHightLight();
            itemView.mAdapter.setHightLight(minutes);
            mHightLightMemoryHour = hour;
            mHightLightMemoryMinutes = minutes;

        } catch (NonHourException e) {
            return;
        }
    }

    public int getViewY(int hour, int minutes){
        try {
            final TimeItemView itemView = getTimeItemViewFromHour(hour);
            final int y = itemView.mAdapter.getViewY(minutes);
            if (y != -1){
                return itemView.holder.linearLayout.getTop() + itemView.mAdapter.getViewY(minutes);
            } else {
                return -1;
            }
        } catch (NonHourException e) {
            return -1;
        }
    }
    public void setAllItemClickListener(OnBusTimeItemClickListener listener){
        for (final TimeItemView itemView : mDataList){
            itemView.setOnItemListener(listener);
        }
    }
    public void setUntilState(int hour, int minutes, boolean enabled){
        for(final TimeItemView item : mDataList){
            if (item.timeItemModel.hour < hour){
                item.mAdapter.setAllState(enabled);
            } else if (item.timeItemModel.hour == hour){
                item.mAdapter.setUntilState(minutes, enabled);
            } else {
                return;
            }
        }
    }
    public void setAllItemState(){
        for(final TimeItemView item : mDataList) {
            item.mAdapter.setAllState(true);
        }
    }
    private TimeItemView getTimeItemViewFromHour(int hour) throws NonHourException{
        for (final TimeItemView item : mDataList) {
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
        NonScrollListView minutesList;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    private class TimeItemView {
        ViewHolder holder;
        TimeItemModel timeItemModel;
        TimeListMinutesCustomAdapter mAdapter;
        OnBusTimeItemClickListener listener;

        public TimeItemView(Context context, TimeItemModel itemModel){
            timeItemModel = itemModel;
            mAdapter = new TimeListMinutesCustomAdapter(context);
            mAdapter.setMinutesList(timeItemModel.minutesList);
            listener = null;
        }

        public void setDataToView(){
            holder.hourText.setText(Integer.toString(timeItemModel.hour));
            if (listener != null) {
                holder.minutesList.setOnItemClickListener(new onTimeItemClickListener(listener, timeItemModel.hour));
            }
        }
        public void setAdapter(){
            holder.minutesList.setAdapter(mAdapter);
        }

        public void setOnItemListener(OnBusTimeItemClickListener listener){
            this.listener = listener;
            if (holder != null) {
                holder.minutesList.setOnItemClickListener(new onTimeItemClickListener(listener, timeItemModel.hour));
            }
        }

        private class onTimeItemClickListener implements AdapterView.OnItemClickListener{
            private OnBusTimeItemClickListener listener;
            private int hour;
            public onTimeItemClickListener(OnBusTimeItemClickListener listener, int hour){
                this.listener = listener;
                this.hour = hour;
            }
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int minutes =  mAdapter.getMinutesByPotiision(i);
                listener.onItemClick(hour, minutes);
            }
        }
    }
    private static class NonHourException extends ExecutionException{
        @Override
        public String getMessage() {
            return "存在しない時刻";
        }
    }
}

