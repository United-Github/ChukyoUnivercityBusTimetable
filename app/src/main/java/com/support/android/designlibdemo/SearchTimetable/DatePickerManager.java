package com.support.android.designlibdemo.SearchTimetable;

import com.support.android.designlibdemo.MainActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ragro on 2017/08/27.
 */

public class DatePickerManager {
    private DatePickerDialog datePickerDialog;
    private Context mContext;

    public DatePickerManager(Context context){
        Calendar now = Calendar.getInstance();
        mContext = context;
        datePickerDialog = DatePickerDialog.newInstance(
                new SelectOK(),
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DATE));
        Calendar max = Calendar.getInstance();
        max.set(2017, 11, 31);
        datePickerDialog.setMaxDate(max);
        Calendar min = Calendar.getInstance();
        min.set(2017, 0, 1);
        datePickerDialog.setMinDate(min);
        datePickerDialog.dismissOnPause(true);
    }
    public void show(FragmentManager manager) {
        datePickerDialog.show(manager, "Datepickerdialog");
    }
    public void setDisableDate(ArrayList<Calendar> calendars){
        Calendar disableDays[] = calendars.toArray(new Calendar[calendars.size()]);
        datePickerDialog.setDisabledDays(disableDays);
    }
    private class SelectOK implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            Intent intent = new Intent(mContext, SimpleTimeTableListActivity.class);
            intent.putExtra(SimpleTimeTableListActivity.EXTRA_KEY_MONTH, monthOfYear);
            intent.putExtra(SimpleTimeTableListActivity.EXTRA_KEY_DAY, dayOfMonth);
            mContext.startActivity(intent);
        }
    }

}
