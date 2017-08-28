package com.support.android.designlibdemo.TimetableList.model;

/**
 * Created by ragro on 2017/08/20.
 */

public class TimeMinutesListItemModel {
    public int minutes; // 分
    public boolean viaKaizu; // 海津経由であるか否か(海津経由:true)
    public TimeMinutesListItemModel(int minutes, boolean viaKaizu){
        this.minutes = minutes;
        this.viaKaizu = viaKaizu;
    }
}
