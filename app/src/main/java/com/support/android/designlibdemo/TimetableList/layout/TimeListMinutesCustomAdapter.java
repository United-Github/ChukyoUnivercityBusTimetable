package com.support.android.designlibdemo.TimetableList.layout;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.TimetableList.model.TimeMinutesListItemModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ragro on 2017/08/20.
 */

public class TimeListMinutesCustomAdapter extends BaseAdapter {
    private List<TimeMinutesItem> mList;
    private LayoutInflater mInflater;
    private Context mContext;
    public TimeListMinutesCustomAdapter(Context context){
        mList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
        mContext = context;


    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean isEnabled(int position) {
        return mList.get(position).enabled;
    }

    public void setMinutesList(List<TimeMinutesListItemModel> list){
        for (TimeMinutesListItemModel item : list) {
            add(item);
        }
    }
    private void add(TimeMinutesListItemModel item){
        TimeMinutesItem timeMinutesItem = new TimeMinutesItem(mContext);
        timeMinutesItem.timeMinutesListItemModel = item;
        mList.add(timeMinutesItem);
        notifyDataSetChanged();
    }
    // 残り時間の表示を変更する
    public void setRemainingTime(int minutes, int remainingTime){
        for (TimeMinutesItem item : mList) {
            if (item.timeMinutesListItemModel.minutes == minutes){
                item.remainingTime = remainingTime;
            }
        }
    }
    // 残り時間の表示を無くす
    public void clearRemainingTime(int minutes){
        for (TimeMinutesItem item : mList) {
            if (item.timeMinutesListItemModel.minutes == minutes){
                item.remainingTime = 0;
            }
        }
    }
    // ハイライトにする
    public void setHightLight(int minutes){
        for (TimeMinutesItem item: mList ) {
            if (item.timeMinutesListItemModel.minutes == minutes){
                item.isHighLight = true;
            }
        }
    }
    // ハイライトを消す
    public void clearHightLight(int minutes){
        for (TimeMinutesItem item: mList ) {
            if (item.timeMinutesListItemModel.minutes == minutes){
                item.isHighLight = false;
            }
        }
    }
    public int getMinutesByPotiision(int i){
        return mList.get(i).timeMinutesListItemModel.minutes;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = mInflater.inflate(R.layout.time_list_minutes, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }
        TimeMinutesItem timeMinutesItem = (TimeMinutesItem)this.getItem(i);
        viewHolder.update(timeMinutesItem);
        return view;
    }

//    public int getViewY(int minutes) {
//        for (TimeMinutesItem item: mList ) {
//            if (item.timeMinutesListItemModel.minutes == minutes && item.holder != null){
//                Log.d("hoge", Integer.toString(item.holder.linearLayout.getTop()));
//                return item.holder.linearLayout.getTop();
//            }
//        }
//        return -1;
//    }

    // 全ての有効、無効を変更
    public void setAllState(boolean enabled){
        for (TimeMinutesItem item : mList) {
            item.enabled = enabled;
        }
    }

    // 指定した分まで無効化
    public void setUntilState(int minutes, boolean enabled){
        for (TimeMinutesItem item : mList){
            if (item.timeMinutesListItemModel.minutes < minutes){
                item.enabled = enabled;
            }
            break;
        }
    }

    static class ViewHolder {
        @BindView(R.id.time_list_minutes_text)
        TextView minutesText;
        @BindView(R.id.time_list_minutes_kaizu)
        TextView kaizuText;
        @BindView(R.id.time_list_minutes_remaining_time)
        TextView remainingTime;
        @BindView(R.id.time_list_minutes_item)
        LinearLayout linearLayout;

        private final int enableTextColor;
        private final int disableTextColor;
        private final int colorNormalBackground;
        private final int colorHighLightBackground;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            final Context context = view.getContext();
            enableTextColor = ContextCompat.getColor(context, R.color.colorBusTimeMinutesTextEnable);
            disableTextColor = ContextCompat.getColor(context, R.color.colorBusTimeMinutesTextDisable);
            colorNormalBackground = ContextCompat.getColor(context, R.color.colorBusTimeListItemBackGroundNormal);
            colorHighLightBackground = ContextCompat.getColor(context, R.color.colorBusTimeListItemBackgroundHighLight);
        }

        public void update(final TimeMinutesItem timeMinutesItem){
            final TimeMinutesListItemModel timeMinutesListItemModel = timeMinutesItem.timeMinutesListItemModel;
            if (timeMinutesListItemModel.viaKaizu) {
                kaizuText.setText("海津経由");
            }

            minutesText.setText(Integer.toString(timeMinutesListItemModel.minutes));

            if (timeMinutesItem.remainingTime == 0) {
                remainingTime.setText("");
            } else {
                remainingTime.setText(Integer.toString(timeMinutesItem.remainingTime));
            }

            if (timeMinutesItem.isHighLight) {
                linearLayout.setBackgroundColor(colorHighLightBackground);

            } else {
                linearLayout.setBackgroundColor(Color.TRANSPARENT);
            }

            if (timeMinutesItem.enabled){
                minutesText.setTextColor(enableTextColor);
            }else {
                minutesText.setTextColor(disableTextColor);
            }
        }
    }
    private class TimeMinutesItem {
        TimeMinutesListItemModel timeMinutesListItemModel;
        private int hour;
        public boolean isHighLight;
        public boolean enabled;
        private int remainingTime;
        private OnBusTimeItemClickListener listener;
        public TimeMinutesItem(Context context){
            isHighLight = false;
            enabled = true;
            remainingTime = 0;
        }
        private void setClickListener(OnBusTimeItemClickListener listener){
            this.listener = listener;
        }
    }
}
