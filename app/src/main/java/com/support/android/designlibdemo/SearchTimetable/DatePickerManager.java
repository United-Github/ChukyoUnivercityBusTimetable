package com.support.android.designlibdemo.SearchTimetable;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import android.app.FragmentManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ragro on 2017/08/27.
 */

public class DatePickerManager {
    private DatePickerDialog datePickerDialog;
    public DatePickerManager(@NonNull DatePickerDialog.OnDateSetListener listener){
        Calendar now = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(
                listener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DATE));
        Calendar max = Calendar.getInstance();
        max.set(2017, 11, 31);
        datePickerDialog.setMaxDate(max);
        Calendar min = Calendar.getInstance();
        min.set(2017, 0, 1);
        datePickerDialog.setMinDate(min);
    }
    public void show(FragmentManager manager, String tag) {
        datePickerDialog.show(manager, "Datepickerdialog");
    }
    public void setDisableDate(ArrayList<Calendar> calendars){
        Calendar disableDays[] = calendars.toArray(new Calendar[calendars.size()]);
        datePickerDialog.setDisabledDays(disableDays);
    }


}
