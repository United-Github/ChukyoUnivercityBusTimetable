package com.support.android.designlibdemo.TimeManager;

import android.content.Context;

import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.TimetableList.model.ScheduleType;
import com.support.android.designlibdemo.TimetableList.model.TimeItemModel;
import com.support.android.designlibdemo.TimetableList.model.TimeMinutesListItemModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 裕亮 on 2017/08/20.
 */




public class TimeManager {
    private Context context;
    private JSONObject jsonMonth;
    private JSONObject schedule_a;
    private JSONObject schedule_b;
    private JSONObject schedule_c;
    private JSONObject schedule_t;
    private JSONObject schedule_ad;
    public static final int DEPART_JOSUI = 0;
    public static final int DEPART_UNIVERCITY = 1;
    private static final String [] DEPART_STRING = new String[]{"J", "T"};
    public final int YEAR;

    public TimeManager(Context _context){
        context=_context;
        YEAR=context.getResources().getInteger(R.integer.year);
        try{
            parseJson();
        }catch(JSONException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public ArrayList<Calendar> getDateType(final ScheduleType _type) {
        ArrayList<Calendar> calender=new ArrayList<Calendar>();
        try{
            for(int month = 1; month<=12; month++) {
                for (int date = 1; jsonMonth.getJSONObject(String.valueOf(month)).getString(String.valueOf(date)) != null; date++) {
                    if(_type.toString().equals(jsonMonth.getJSONObject(String.valueOf(month)).getString(String.valueOf(date)))){
                        Calendar calender_temp=Calendar.getInstance();
                        calender_temp.set(YEAR,month-1,date);
                        calender.add(calender_temp);
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return calender;
    }

    private JSONObject parseJsonSub(String filename) throws JSONException,IOException{
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

    private void parseJson() throws JSONException,IOException{
        jsonMonth =parseJsonSub("calender.json");
        schedule_a=parseJsonSub("schedule_a.json");
        schedule_ad=parseJsonSub("schedule_ad.json");
        schedule_b=parseJsonSub("schedule_b.json");
        schedule_c=parseJsonSub("schedule_c.json");
        schedule_t=parseJsonSub("schedule_t.json");
    }

    /*
    public String testText(){
        try{
            int a=4;int b=1;
            String tt=jsonMonth.getJSONObject(String.valueOf(a)).getString(String.valueOf(b));
            return tt;
        }catch(JSONException e) {
            e.printStackTrace();
        }
        return "boo";
    }
    */

    public String[] getMonthSchedule(int _month){
        List<String> schedule = new ArrayList<String>();
        try {
            JSONObject item = jsonMonth.getJSONObject(String.valueOf(_month));
            for(int i=1;item.getString(String.valueOf(i))!=null;i++)
            {
                String temp=item.getString(String.valueOf(i));
                schedule.add(temp);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }finally {
            return schedule.toArray(new String[schedule.size()]);
        }
    }


    public List<TimeItemModel> getBusSchedule(final int _month, final int _day, final int depart) throws NoScheduleException{
        final String departString = DEPART_STRING[depart];
        List<TimeItemModel> timeList=new ArrayList<TimeItemModel>();
        try{
            JSONObject schedule_temp=classifySchedule(_month,_day);
            for(int i=8;schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(i))!=null;++i) {
                TimeItemModel time_temp=new TimeItemModel();
                time_temp.hour=i;
                for(int j=0;j<schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(i)).length();j++)
                {
                    time_temp.minutesList.add(new TimeMinutesListItemModel(schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(i)).getInt(j),false));
                }
                timeList.add(time_temp);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return timeList;
    }

    private JSONObject classifySchedule (int _month,int _day) throws NoScheduleException,JSONException{
        String scheduleType = jsonMonth.getJSONObject(String.valueOf(_month)).getString(String.valueOf(_day));
        JSONObject schedule_temp=schedule_a;
        switch (scheduleType) {
            case "A":
                schedule_temp = schedule_a;
                break;
            case "B":
                schedule_temp = schedule_b;
                break;
            case "C":
                schedule_temp = schedule_c;
                break;
            case "T":
                schedule_temp = schedule_t;
                break;
            case "Ad":
                schedule_temp = schedule_ad;
                break;
            default:
                throw new NoScheduleException();
        }
        return schedule_temp;
    }

    public int[] nearBusTime(int _month,int _day,int _hour,int _minutes,final int depart) throws NoScheduleException,DayOverflowException{
        final String departString = DEPART_STRING[depart];
        int loHou=_hour;int loMin=_minutes;
        try{
            JSONObject schedule_temp=classifySchedule(_month,_day);
            for(int i=0;i<=schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(loHou)).length();++i){
                if(i==schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(loHou)).length()) {
                    if(loHou==21){
                        throw new DayOverflowException();
                    }else if(schedule_temp==schedule_c&&loHou==19){
                        throw new DayOverflowException();
                    }else{
                        loHou++;
                        loMin=schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(loHou)).getInt(0);
                        break;
                    }
                }else if(schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(loHou)).getInt(i)>_minutes) {
                    loMin = schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(loHou)).getInt(i);
                    break;
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        int[] hAndM={loHou,loMin};
        return hAndM;
    }

    public int[] afterBusTime(int _month,int _day,int _hour,int _minutes,final int depart) throws NoScheduleException,DayOverflowException{
        final String departString = DEPART_STRING[depart];
        int loHou=_hour;int loMin=_minutes;
        try{
            JSONObject schedule_temp=classifySchedule(_month,_day);
            for(int i=0;i<schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(loHou)).length();++i){
                if(schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(loHou)).getInt(i)==_minutes){
                    if(i+1==schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(loHou)).length()){
                        if(loHou==21){
                            throw new DayOverflowException();
                        }else if(schedule_temp==schedule_c&&loHou==19){
                            throw new DayOverflowException();
                        }else{
                            loHou++;
                            loMin=schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(loHou)).getInt(0);
                            break;
                        }
                    }else{
                        loMin=schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(loHou)).getInt(i+1);
                    }
                    break;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        int[] hAndM={loHou,loMin};
        return hAndM;
    }

    public int[] beforeBusTime(int _month,int _day,int _hour,int _minutes,final int depart) throws NoScheduleException,DayUnderflowException{
        final String departString = DEPART_STRING[depart];
        int loHou=_hour;int loMin=_minutes;
        try{
            JSONObject schedule_temp=classifySchedule(_month,_day);
            for(int i=schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(loHou)).length()-1;i>=0;--i){
                if(schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(loHou)).getInt(i)==_minutes){
                    if(i==0){
                        if(loHou==8){
                            throw new DayUnderflowException();
                        }else{
                            loHou--;
                            loMin=schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(loHou)).
                                    getInt(schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(loHou)).length()-1);
                            break;
                        }
                    }else{
                        loMin=schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(loHou)).getInt(i-1);
                    }
                    break;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
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

}
