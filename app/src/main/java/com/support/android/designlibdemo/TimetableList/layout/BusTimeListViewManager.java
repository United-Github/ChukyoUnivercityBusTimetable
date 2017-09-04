package com.support.android.designlibdemo.TimetableList.layout;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.TimetableList.model.TimeItemModel;

/**
 * Created by ragro on 2017/08/21.
 */

public class BusTimeListViewManager {
    private Context mContext;
    private View mRootView;
    private ScrollView mScrollView;
    private ListView mListView;
    private TimeListCustomAdapter adapter;
    private OnBusTimeItemClickListener listener;

    public BusTimeListViewManager(View rootView){
        mContext = rootView.getContext();
        mRootView = rootView;
        mListView = (ListView)mRootView.findViewById(R.id.time_list);
        adapter = new TimeListCustomAdapter(mContext);
        mListView.setAdapter(adapter);
        listener = null;
    }

    public void addTimeItemModel(TimeItemModel itemModel){
        adapter.add(itemModel);
    }
    public void setRemainingTime(int hour, int minutes, int remainingTime){
        adapter.setRemainingTime(hour, minutes, remainingTime);
    }
    public void clearRemainingTime(int hour, int minutes){
        adapter.clearRemainingTime(hour, minutes);
    }
    public void setHighLight(int hour, int minutes){
        adapter.setHightLight(hour, minutes);
    }
    public void clearHighLight(){
        adapter.clearHightLight();
    }
    public void setScrollTo(int hour, int minutes){
        final int y = adapter.getViewY(hour, minutes);
        if (y != -1){
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mScrollView.smoothScrollTo(0, y);
                }
            });
        }
    }
    // クリックリスナーをセット
    public void setBusTimeClickListener(OnBusTimeItemClickListener listener){
        this.listener = listener;
        adapter.setAllItemClickListener(listener);
    }
    // 指定した時間までUIを無効表示に
    public void setUntilDisable(int hour, int minutes){
        adapter.setUntilState(hour, minutes, false);
    }
    // 指定した時間
    public void setCurrentTime(int hour, int minutes){
        adapter.setAllItemState();
        adapter.clearHightLight();
        setUntilDisable(hour, minutes -1);
        setHighLight(hour, minutes);
    }
}
