package com.support.android.designlibdemo.TimetableList.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ragro on 2017/08/20.
 */

public class TimeItemModel {
    public List<TimeMinutesListItemModel> minutesList;
    public int hour;
    public TimeItemModel(){
        minutesList = new ArrayList<>();
    }
}