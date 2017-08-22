package com.support.android.designlibdemo.TimetableList.model;

import java.util.concurrent.ExecutionException;

/**
 * Created by ragro on 2017/08/22.
 */

public enum  ScheduleType {
    A("A"),B("B"),C("C"),Ad("A'"),Td("T'");
    private final String typeName;
    ScheduleType(String string){
        typeName = string;
    }
    @Override
    public String  toString(){
        return typeName;
    }
    public ScheduleType getScheduleTypeByString(String string) {
        switch (string){
            case "A":
                return A;
            case "B":
                return B;
            case "C":
                return C;
            case "A'":
                return Ad;
            case "T'":
                return Td;
            default:
                return null;
        }
    }
}
