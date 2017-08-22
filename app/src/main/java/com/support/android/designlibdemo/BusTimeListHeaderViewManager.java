package com.support.android.designlibdemo;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ragro on 2017/08/22.
 */

public class BusTimeListHeaderViewManager {

    private TextView mMinutesText;
    private TextView mSecondsText;
    private TextView mScheduleTypeText;
    private TextView mDepartTimeText;
    private Button mNextButton;
    private Button mPreviousButton;
    private Context mContext;

    public BusTimeListHeaderViewManager(View rootView){
        bind(rootView);
        mContext = rootView.getContext();
    }

    private void bind(View rootView){
        mMinutesText = (TextView)rootView.findViewById(R.id.time_header_remaining_minutes);
        mSecondsText = (TextView)rootView.findViewById(R.id.time_header_remaining_seconds);
        mScheduleTypeText = (TextView)rootView.findViewById(R.id.time_header_schedule_type);
        mDepartTimeText = (TextView)rootView.findViewById(R.id.time_header_depart);
        mNextButton = (Button)rootView.findViewById(R.id.time_header_next);
        mPreviousButton = (Button)rootView.findViewById(R.id.time_header_previous);
    }
    // バス発車時刻の設定
    public void setCurrentTime(int hour, int minutes){
        mDepartTimeText.setText(mContext.getString(R.string.time_list_header_depart, hour, minutes));
        mMinutesText.setText(Integer.toString(hour));
        mSecondsText.setText(Integer.toString(minutes));
    }
}
