package com.support.android.designlibdemo.TimetableList.model;

import java.util.concurrent.ExecutionException;

/**
 * Created by ragro on 2017/08/22.
 */

public enum  ScheduleType {
    A("A"),B("B"),C("C"),Ad("Ad"),T("T"), S("S");
    private final String typeName;
    ScheduleType(String string){
        typeName = string;
    }
    @Override
    public String  toString(){
        return typeName;
    }
    public ScheduleType getScheduleTypeByString(String string) throws IllegalArgumentException{
        switch (string){
            case "A":
                return A;
            case "B":
                return B;
            case "C":
                return C;
            case "Ad":
                return Ad;
            case "T":
                return T;
            case "S":
                return S;
            default:
                throw  new IllegalArgumentException();
        }
    }
}
