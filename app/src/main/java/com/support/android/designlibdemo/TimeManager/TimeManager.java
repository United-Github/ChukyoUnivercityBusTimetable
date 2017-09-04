package com.support.android.designlibdemo.TimeManager;

import android.content.Context;
import android.util.Log;

import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.TimetableList.model.ScheduleType;
import com.support.android.designlibdemo.TimetableList.model.TimeItemModel;
import com.support.android.designlibdemo.TimetableList.model.TimeMinutesListItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 裕亮 on 2017/08/20.
 */




public class TimeManager {
    private Context context;
    private ArrayList<Calendar> suspendedDays;
    private ArrayList<ScheduleMonth> scheduleYear;

    private ScheduleDate scheduleA;
    private ScheduleDate scheduleB;
    private ScheduleDate scheduleC;
    private ScheduleDate scheduleAd;
    private ScheduleDate scheduleT;
    private ArrayList<ScheduleMonth> monthSchedule;
    private JSONObject kaizuData;


    public static final int DEPART_JOSUI = 0;
    public static final int DEPART_UNIVERCITY = 1;
    private static final String [] DEPART_STRING = new String[]{"J", "T"};
    public final int YEAR;

    public TimeManager(Context _context){
        context=_context;
        YEAR=context.getResources().getInteger(R.integer.year);
        initialize();
        monthSchedule = getMonthSchedule();
        suspendedDays = getDateType(ScheduleType.S);
    }
    private void initialize(){
        //JSONObject jsonMonth, schedule_a, schedule_ad, schedule_b, schedule_c, schedule_t;
        try {
            scheduleA=new ScheduleDate();
            scheduleAd=new ScheduleDate();
            scheduleB=new ScheduleDate();
            scheduleC=new ScheduleDate();
            scheduleT=new ScheduleDate();
            kaizuData = parseJson("kaizu.json");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            scheduleA.setArray(getBusSchedule("schedule_a.json", DEPART_JOSUI), getBusSchedule("schedule_a.json", DEPART_UNIVERCITY));
            scheduleAd.setArray(getBusSchedule("schedule_ad.json", DEPART_JOSUI), getBusSchedule("schedule_ad.json", DEPART_UNIVERCITY));
            scheduleB.setArray(getBusSchedule("schedule_b.json", DEPART_JOSUI), getBusSchedule("schedule_b.json", DEPART_UNIVERCITY));
            scheduleC.setArray(getBusSchedule("schedule_c.json", DEPART_JOSUI), getBusSchedule("schedule_c.json", DEPART_UNIVERCITY));
            scheduleT.setArray(getBusSchedule("schedule_t.json", DEPART_JOSUI), getBusSchedule("schedule_t.json", DEPART_UNIVERCITY));
        }catch(NoScheduleException e){
            e.printStackTrace();
        }
    }

    public ArrayList<Calendar> getSuspendedDays(){
        return suspendedDays;
    }

    public ArrayList<ScheduleMonth> getMonthList() { return monthSchedule; }

    public ScheduleDate getScheduleDate(ScheduleType type) {
        ScheduleDate temp;
        switch(type.toString())
        {
            case "A":
                return scheduleA;
            case "B":
                return scheduleB;
            case "C":
                return scheduleC;
            case "Ad":
                return scheduleAd;
            case "T":
                return scheduleT;
            default:
                throw  new IllegalArgumentException();
        }

    }

    public ArrayList<Calendar> getDateType(final ScheduleType _type) {
        ArrayList<Calendar> calender=new ArrayList<Calendar>();
        for(int month=0;monthSchedule.size()>month;++month){
            ArrayList<ScheduleType> daysList = monthSchedule.get(month).days;
            for(int date=0;daysList.size()>date;++date){
                if(_type.toString().equals(daysList.get(date).toString())){
                    Calendar calender_temp=Calendar.getInstance();
                    calender_temp.set(YEAR,month-1,date);
                    calender.add(calender_temp);
                }
            }
        }
        return calender;
    }

    private JSONObject parseJson(String filename) throws JSONException,IOException{
        InputStream inputStream = context.getAssets().open(filename);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            if (length > 0) {
                outputStream.write(buffer, 0, length);
            }
        }
        return new JSONObject(new String(outputStream.toByteArray()));
    }

    public ScheduleMonth getMonthSchedule(int _month){
        return monthSchedule.get(_month-1);
    }

    private ArrayList<ScheduleMonth> getMonthSchedule(){
        ArrayList<ScheduleMonth> schedule=new ArrayList<ScheduleMonth>();
        try{
            JSONObject monthJson=parseJson("calender.json");
            for(int month = 1; month<=12; month++) {
                ScheduleMonth month_temp=new ScheduleMonth();
                month_temp.month=month;
                JSONObject item = monthJson.getJSONObject(String.valueOf(month));
                for (int date = 1; true; date++) {
                    try{
                        String temp=item.getString(String.valueOf(date));
                        ScheduleType scheduleType = ScheduleType.valueOf(temp);
                        month_temp.days.add(scheduleType);
                    }catch (JSONException e){
                        break;
                    }
                }
                schedule.add(month_temp);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return schedule;
    }

    public ArrayList<TimeItemModel> getBusSchedule(final int _month, final int _day, final int depart) throws NoScheduleException{
        return classifySchedule(_month,_day).getTimeItem(depart);
    }

    private ArrayList<TimeItemModel> getBusSchedule(String filename, final int depart) throws NoScheduleException{
        ArrayList<TimeItemModel> timeList=new ArrayList<TimeItemModel>();
        try {
            String kaizuType="A";
            if(filename.equals("schedule_a.json")){
                kaizuType="A";
            }else if(filename.equals("schedule_b.json")){
                kaizuType="B";
            }else if(filename.equals("schedule_c.json")){
                kaizuType="C";
            }else if(filename.equals("schedule_ad.json")){
                kaizuType="Ad";
            }else if(filename.equals("schedule_t.json")){
                kaizuType="T";
            }
            timeList = convertJSONObjToScheduleDate(parseJson(filename), depart,kaizuType);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return timeList;
    }

    // JSONObjectを受取、ScheduleDate型に変換して渡す
    private ArrayList<TimeItemModel> convertJSONObjToScheduleDate(final JSONObject scheduleJsonObject, final int depart,String kaizuType){
        final String departString = DEPART_STRING[depart];
        ArrayList<TimeItemModel> timeList=new ArrayList<TimeItemModel>();
        try{
            // 8時からJSONObj連想配列の要素が無くなるまで
            final JSONObject hourJson = scheduleJsonObject.getJSONObject(departString);
            for(int i = 8; true; ++i) {
                try {
                    TimeItemModel time_temp = new TimeItemModel();
                    time_temp.hour = i;
                    final JSONArray minutesArrayJson = hourJson.getJSONArray(String.valueOf(i));
                    for (int j = 0; j < minutesArrayJson.length(); j++) {
                        boolean kaizuFlag = false;
                        if (depart == DEPART_UNIVERCITY) {
                            if (kaizuData.getJSONObject(kaizuType).getJSONArray(String.valueOf(i)).getInt(j) == 1) {
                                kaizuFlag = true;
                            }
                        }
                        time_temp.minutesList.add(new TimeMinutesListItemModel(minutesArrayJson.getInt(j), kaizuFlag));
                    }
                    timeList.add(time_temp);
                }catch(JSONException e){
                    break;
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return timeList;
    }

    private ScheduleDate classifySchedule (int _month,int _day) throws NoScheduleException{
        String scheduleType = monthSchedule.get(_month-1).days.get(_day-1).toString();
        ScheduleDate schedule_temp=scheduleA;
        switch (scheduleType) {
            case "A":
                schedule_temp = scheduleA;
                break;
            case "B":
                schedule_temp = scheduleB;
                break;
            case "C":
                schedule_temp = scheduleC;
                break;
            case "T":
                schedule_temp = scheduleT;
                break;
            case "Ad":
                schedule_temp = scheduleAd;
                break;
            default:
                throw new NoScheduleException();
        }
        return schedule_temp;
    }

    public int[] nearBusTime(int _month,int _day,int _hour,int _minutes,final int depart) throws NoScheduleException,DayOverflowException{
        int loHou=_hour;int loMin=_minutes;
        ArrayList<ScheduleMonth> schedule_temp=getMonthList();
        ScheduleDate date_temp = getScheduleDate(schedule_temp.get(_month-1).days.get(_day-1));
        ArrayList<TimeItemModel> timeItemList = date_temp.getTimeItem(depart);
        if(_hour<timeItemList.get(0).hour){
            loHou=timeItemList.get(0).hour;
            loMin=timeItemList.get(loHou-timeItemList.get(0).hour).minutesList.get(0).minutes;
            int[] hAndM={loHou,loMin};
            return hAndM;
        }

        TimeItemModel timeHour = timeItemList.get(_hour-timeItemList.get(0).hour);
        for(int i=0;i<=timeHour.minutesList.size();++i) {
            if(timeHour.minutesList.size()==i){
                if(timeHour.hour==(timeItemList.get(0).hour+timeItemList.size()-1))
                    throw new DayOverflowException();
                else{
                    loHou++;
                    loMin=timeItemList.get(loHou-timeItemList.get(0).hour).minutesList.get(0).minutes;
                    break;
                }
            }else if(timeItemList.get(loHou-timeItemList.get(0).hour).minutesList.get(i).minutes > _minutes){
                loMin=timeHour.minutesList.get(i+1).minutes;
                break;
            }
        }
        int[] hAndM={loHou,loMin};
        return hAndM;
    }

    public int[] afterBusTime(int _month,int _day,int _hour,int _minutes,final int depart) throws NoScheduleException,DayOverflowException{
        int loHou=_hour;int loMin=_minutes;
        ArrayList<ScheduleMonth> schedule_temp=getMonthList();
        ScheduleDate date_temp = getScheduleDate(schedule_temp.get(_month-1).days.get(_day-1));
        ArrayList<TimeItemModel> timeItemList = date_temp.getTimeItem(depart);
        TimeItemModel timeHour = timeItemList.get(_hour-timeItemList.get(0).hour);
        for(int i=0;i<timeHour.minutesList.size();++i) {
            if(timeItemList.get(loHou-timeItemList.get(0).hour).minutesList.get(i).minutes == _minutes){
                if(timeItemList.get(loHou-timeItemList.get(0).hour).minutesList.size()==i+1){
                    if(timeHour.hour==(timeItemList.get(0).hour+timeItemList.size()-1))
                        throw new DayOverflowException();
                    else{
                        loHou++;
                        loMin=timeItemList.get(loHou-timeItemList.get(0).hour).minutesList.get(0).minutes;
                        break;
                    }
                }
                else {
                    loMin = timeHour.minutesList.get(i + 1).minutes;
                    break;
                }
            }
        }
        int[] hAndM={loHou,loMin};
        return hAndM;
    }

    public int[] beforeBusTime(final int _month,final int _day,final int _hour,final int _minutes,final int depart) throws NoScheduleException,DayUnderflowException{
        int loHou=_hour;int loMin=_minutes;
        ArrayList<ScheduleMonth> schedule_temp=getMonthList();
        ScheduleDate date_temp = getScheduleDate(schedule_temp.get(_month-1).days.get(_day-1));
        ArrayList<TimeItemModel> timeItemList = date_temp.getTimeItem(depart);
        final int minHour=timeItemList.get(0).hour;
        TimeItemModel timeHour = timeItemList.get(_hour-minHour);
        Log.d("hogehoge",(_hour-minHour)+":"+minHour+"");
        for(int i=timeHour.minutesList.size()-1;i>=0;--i) {
            Log.d("hogehoge",timeHour.minutesList.get(i).minutes+"");
            if(timeHour.minutesList.get(i).minutes == _minutes){
                if(i==0){
                    if(timeHour.hour==minHour)
                        throw new DayUnderflowException();
                    else{
                        loHou--;
                        final int beforeHour=loHou-minHour;
                        loMin=timeItemList.get(beforeHour).minutesList
                                .get(timeItemList.get(beforeHour).minutesList.size()-1).minutes;
                        break;
                    }
                }
                else {
                    loMin = timeHour.minutesList.get(i - 1).minutes;
                    break;
                }
            }
        }
        int[] hAndM={loHou,loMin};
        return hAndM;
    }

    public static class DayOverflowException extends Exception{
        DayOverflowException(){
            super("これ以上はありません");
        }
    }

    public static class DayUnderflowException extends Exception{
        DayUnderflowException(){
            super("これ以下はありません");
        }
    }

    public static class NoScheduleException extends Exception{
        NoScheduleException(){
            super("バススケジュールがありません");
        }
    }

    public static class ScheduleMonth{
        public int month;
        public ArrayList<ScheduleType> days = new ArrayList<ScheduleType>();
    }

    public static class ScheduleDate{
        ArrayList<TimeItemModel> josui;
        ArrayList<TimeItemModel> univercity;
        public ArrayList<TimeItemModel> getTimeItemModels(int type){
            return (type == DEPART_JOSUI)?josui:univercity;
        }
        public void setArray(ArrayList<TimeItemModel> _josui,ArrayList<TimeItemModel> _univercity){
            josui=_josui;
            univercity=_univercity;
        }
        public ArrayList<TimeItemModel> getTimeItem(final int depart){
            if(depart==0){
                return josui;
            }
            else{
                return univercity;
            }
        }
    }
}
