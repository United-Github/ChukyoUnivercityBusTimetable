package com.support.android.designlibdemo.TimetableList.layout;

import android.content.Context;
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
    private List<TimeMinutesItemView> mList;
    private LayoutInflater mInflater;
    private Context mContext;
    private int colorNormalBackground;
    private int colorHighLightBackground;
    public TimeListMinutesCustomAdapter(Context context){
        mList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
        mContext = context;

        colorNormalBackground = ContextCompat.getColor(mContext, R.color.colorBusTimeListItemBackGroundNormal);
        colorNormalBackground = ContextCompat.getColor(mContext, R.color.colorBusTimeListItemBackgroundHighLight);
    }
    @Override
    public int getCount() {
        return mList.size();
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
    public boolean isEnabled(int position) {
        return mList.get(position).enabled;
    }

    public void setMinutesList(List<TimeMinutesListItemModel> list){
        for (TimeMinutesListItemModel item : list) {
            add(item);
        }
    }
    private void add(TimeMinutesListItemModel item){
        TimeMinutesItemView listView = new TimeMinutesItemView(mContext);
        listView.timeMinutesListItemModel = item;
        mList.add(listView);
        notifyDataSetChanged();
    }
    // 残り時間の表示を変更する
    public void setRemainingTime(int minutes, int remainingTime){
        for (TimeMinutesItemView item : mList) {
            if (item.timeMinutesListItemModel.minutes == minutes){
                item.timeMinutesListItemModel.remainingTime = remainingTime;
                if (item.holder != null){
                    item.holder.minutesText.setText(Integer.toString(minutes));
                }
            }
        }
    }
    // 残り時間の表示を無くす
    public void clearRemainingTime(int minutes){
        for (TimeMinutesItemView item : mList) {
            if (item.timeMinutesListItemModel.minutes == minutes){
                item.timeMinutesListItemModel.remainingTime = 0;
                if (item.holder != null) {
                    item.holder.minutesText.setText("");
                }
            }
        }
    }
    // ハイライトにする
    public void setHightLight(int minutes){
        for (TimeMinutesItemView item: mList ) {
            if (item.timeMinutesListItemModel.minutes == minutes){
                item.setBackgroundColor(true);
            }
        }
    }
    // ハイライトを消す
    public void clearHightLight(int minutes){
        for (TimeMinutesItemView item: mList ) {
            if (item.timeMinutesListItemModel.minutes == minutes){
                item.setBackgroundColor(false);
            }
        }
    }
    public int getMinutesByPotiision(int i){
        return mList.get(i).timeMinutesListItemModel.minutes;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        TimeMinutesItemView itemView;
        if (view != null) {
            itemView = (TimeMinutesItemView) view.getTag();
        } else {
            itemView = mList.get(i);
            view = mInflater.inflate(R.layout.time_list_minutes, parent, false);
            itemView.holder = new ViewHolder(view);
            view.setTag(itemView);
        }
        itemView.setDataToView();
        return view;
    }
    public int getViewY(int minutes) {
        for (TimeMinutesItemView item: mList ) {
            if (item.timeMinutesListItemModel.minutes == minutes && item.holder != null){
                Log.d("hoge", Integer.toString(item.holder.linearLayout.getTop()));
                return item.holder.linearLayout.getTop();
            }
        }
        return -1;
    }

    // 全ての有効、無効を変更
    public void setAllState(boolean enabled){
        for (TimeMinutesItemView item : mList) {
            item.setState(enabled);
        }
    }

    // 指定した分まで無効化
    public void setUntilState(int minutes, boolean enabled){
        for (TimeMinutesItemView item : mList){
            if (item.timeMinutesListItemModel.minutes <= minutes){
                item.setState(enabled);
            }
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
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    private class TimeMinutesItemView {
        ViewHolder holder;
        TimeMinutesListItemModel timeMinutesListItemModel;
        private int hour;
        private boolean isHighLight;
        private boolean enabled;
        private int enableTextColor;
        private int disableTextColor;
        private OnBusTimeItemClickListener listener;
        public TimeMinutesItemView(Context context){
            isHighLight = false;
            enabled = true;
            enableTextColor = ContextCompat.getColor(context, R.color.colorBusTimeMinutesTextEnable);
            disableTextColor = ContextCompat.getColor(context, R.color.colorBusTimeMinutesTextDisable);
        }
        private void setClickListener(OnBusTimeItemClickListener listener){
            this.listener = listener;
        }
        private void setDataToView() {
            if (timeMinutesListItemModel.viaKaizu) {
                holder.kaizuText.setText("海津経由");
            }

            holder.minutesText.setText(Integer.toString(timeMinutesListItemModel.minutes));

            if (timeMinutesListItemModel.remainingTime == 0) {
                holder.remainingTime.setText("");
            } else {
                holder.remainingTime.setText(Integer.toString(timeMinutesListItemModel.remainingTime));
            }
            setBackgroundColor(isHighLight);
            if (enabled){
                holder.minutesText.setTextColor(enableTextColor);
            }else {
                holder.minutesText.setTextColor(disableTextColor);
            }
        }
        private void setBackgroundColor(boolean isHighLight){
            this.isHighLight = isHighLight;

            if (holder == null){
                return;
            }

            if (isHighLight) {
                holder.linearLayout.setBackgroundColor(colorNormalBackground);
            } else {
                holder.linearLayout.setBackgroundColor(colorHighLightBackground);
            }
        }
        public void setState(boolean enabled){
            this.enabled = enabled;
            if (holder != null){
                if (enabled){
                    holder.minutesText.setTextColor(enableTextColor);
                }else {
                    holder.minutesText.setTextColor(disableTextColor);
                }
            }
        }
    }
}
