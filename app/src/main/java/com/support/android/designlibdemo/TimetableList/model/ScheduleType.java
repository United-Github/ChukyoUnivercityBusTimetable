package com.support.android.designlibdemo.TimetableList.model;

/**
 * Created by ragro on 2017/08/22.
 */

public enum  ScheduleType {
    A("A", "A"),B("B", "B"),C("C", "C"),Ad("Ad", "A'"),T("T", "臨時"), S("S", "運休日");
    private final String identifier;
    private final String name;
    ScheduleType(String identifier, String name){
        this.identifier = identifier;
        this.name = name;
    }
    @Override
    public String  toString(){
        return identifier;
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
    public String getName(ScheduleType type){
        return type.name;
    }
}
