package com.support.android.designlibdemo.TimetableList.layout;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.TimetableList.model.ScheduleType;

/**
 * Created by ragro on 2017/08/22.
 */

public class BusTimeListHeaderViewManager {

    private TextView mMinutesText;
    private TextView mSecondsText;
    private TextView mScheduleTypeText;
    private TextView mDepartTimeText;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private Context mContext;
    private ScheduleType mScheduleType;
    private LinearLayout mRootView;
    private LinearLayout mHeaderInfo;
    private TextView mNonInfo;

    public BusTimeListHeaderViewManager(View rootView){
        bind(rootView);
        mContext = rootView.getContext();
        mScheduleType = ScheduleType.A;
    }

    private void bind(View rootView){
        mMinutesText = (TextView)rootView.findViewById(R.id.time_header_remaining_minutes);
        mSecondsText = (TextView)rootView.findViewById(R.id.time_header_remaining_seconds);
        mScheduleTypeText = (TextView)rootView.findViewById(R.id.time_header_schedule_type);
        mDepartTimeText = (TextView)rootView.findViewById(R.id.time_header_depart);
        mNextButton = (ImageButton) rootView.findViewById(R.id.time_header_next);
        mPreviousButton = (ImageButton) rootView.findViewById(R.id.time_header_previous);
        mRootView = (LinearLayout)rootView.findViewById(R.id.time_header_root);
        mHeaderInfo = (LinearLayout)rootView.findViewById(R.id.time_header_info);
        mNonInfo = (TextView) rootView.findViewById(R.id.time_header_non);
    }
    // バス発車時刻の設定
    public void setDepartTime(int hour, int minutes){
        mDepartTimeText.setText(mContext.getString(R.string.time_list_header_depart, hour, minutes));
    }
    // 残り時間を設定
    public void setRemainingTime(int hour, int minutes){
        mMinutesText.setText(mContext.getString(R.string.time_list_header_time, hour));
        mSecondsText.setText(mContext.getString(R.string.time_list_header_time, minutes));
    }
    // バスが運休のとき
    public void setSuspended(){
        mRootView.setVisibility(View.GONE);
    }
    // バスの時刻が存在しないとき
    public void setNonBusTime(){
        mNextButton.setVisibility(View.GONE);
        mPreviousButton.setVisibility(View.GONE);
        mHeaderInfo.setVisibility(View.GONE);
        mNonInfo.setVisibility(View.VISIBLE);
        mNonInfo.setText(mContext.getString(R.string.time_list_header_finished));
    }
    // ダイヤを設定
    public void setScheduleType(ScheduleType type) {
        mScheduleTypeText.setText(type.toString());
        mScheduleType = type;
    }
    // 前を選択した時のクリックリスナーをセット
    public void setOnPreviousClickListener(View.OnClickListener listener){
        mPreviousButton.setOnClickListener(listener);
    }
    // 次を選択した時のクリックリスナーをセット
    public void setOnNextClickListener(View.OnClickListener listener){
        mNextButton.setOnClickListener(listener);
    }
}
