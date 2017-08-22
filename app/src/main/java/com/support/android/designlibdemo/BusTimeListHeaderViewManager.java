package com.support.android.designlibdemo;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.support.android.designlibdemo.TimetableList.model.ScheduleType;

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
    private ScheduleType mScheduleType;

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
        mNextButton = (Button)rootView.findViewById(R.id.time_header_next);
        mPreviousButton = (Button)rootView.findViewById(R.id.time_header_previous);
    }
    // バス発車時刻の設定
    public void setDepartTime(int hour, int minutes){
        mDepartTimeText.setText(mContext.getString(R.string.time_list_header_depart, hour, minutes));
        mMinutesText.setText(Integer.toString(hour));
        mSecondsText.setText(Integer.toString(minutes));
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
