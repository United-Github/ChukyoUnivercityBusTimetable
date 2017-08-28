package com.support.android.designlibdemo.TimeManager;

import android.content.Context;

import com.support.android.designlibdemo.TimetableList.model.TimeItemModel;
import com.support.android.designlibdemo.TimetableList.model.TimeMinutesListItemModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 裕亮 on 2017/08/20.
 */


class DayOverflowException extends Exception{
    DayOverflowException(){
        super("これ以上はありません");
    }
}

class DayUnderflowException extends Exception{
    DayUnderflowException(){
        super("これ以下はありません");
    }
}

class NoScheduleException extends Exception{
    NoScheduleException(){
        super("バススケジュールがありません");
    }
}

public class TimeManager {
    private Context context;
    private JSONObject month;
    private JSONObject schedule_a;
    private JSONObject schedule_b;
    private JSONObject schedule_c;
    private JSONObject schedule_t;
    private JSONObject schedule_ad;

    TimeManager(Context _context){
        context=_context;
        try{
            parseJson();
        }catch(JSONException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
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
        month=parseJsonSub("calender.json");
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
            String tt=month.getJSONObject(String.valueOf(a)).getString(String.valueOf(b));
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
            JSONObject item = month.getJSONObject(String.valueOf(_month));
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


    public List<TimeItemModel> getBusSchedule(int _month, int _day, String _from) throws NoScheduleException{
        List<TimeItemModel> timeList=new ArrayList<TimeItemModel>();
        try{
            JSONObject schedule_temp=classifySchedule(_month,_day);
            for(int i=8;schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(i))!=null;++i) {
                TimeItemModel time_temp=new TimeItemModel();
                for(int j=0;j<schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(i)).length();j++)
                {
                    time_temp.minutesList.add(new TimeMinutesListItemModel(schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(i)).getInt(j),false));
                }
                timeList.add(time_temp);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return timeList;
    }

    private JSONObject classifySchedule (int _month,int _day) throws NoScheduleException,JSONException{
        String scheduleType = month.getJSONObject(String.valueOf(_month)).getString(String.valueOf(_day));
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
            case "S":
                schedule_temp = schedule_ad;
                break;
            default:
                throw new NoScheduleException();
        }
        return schedule_temp;
    }

    public int[] nearBusTime(int _month,int _day,int _hour,int _minutes,String _from) throws NoScheduleException,DayOverflowException{
        int loHou=_hour;int loMin=_minutes;
        try{
            JSONObject schedule_temp=classifySchedule(_month,_day);
            for(int i=0;i<=schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).length();++i){
                if(i==schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).length()) {
                    if(loHou==21){
                        throw new DayOverflowException();
                    }else if(schedule_temp==schedule_c&&loHou==19){
                        throw new DayOverflowException();
                    }else{
                        loHou++;
                        loMin=schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).getInt(0);
                        break;
                    }
                }else if(schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).getInt(i)>_minutes) {
                    loMin = schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).getInt(i);
                    break;
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        int[] hAndM={loHou,loMin};
        return hAndM;
    }

    public int[] afterBusTime(int _month,int _day,int _hour,int _minutes,String _from) throws NoScheduleException,DayOverflowException{
        int loHou=_hour;int loMin=_minutes;
        try{
            JSONObject schedule_temp=classifySchedule(_month,_day);
            for(int i=0;i<schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).length();++i){
                if(schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).getInt(i)==_minutes){
                    if(i+1==schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).length()){
                        if(loHou==21){
                            throw new DayOverflowException();
                        }else if(schedule_temp==schedule_c&&loHou==19){
                            throw new DayOverflowException();
                        }else{
                            loHou++;
                            loMin=schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).getInt(0);
                            break;
                        }
                    }else{
                        loMin=schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).getInt(i+1);
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

    public int[] beforeBusTime(int _month,int _day,int _hour,int _minutes,String _from) throws NoScheduleException,DayUnderflowException{
        int loHou=_hour;int loMin=_minutes;
        try{
            JSONObject schedule_temp=classifySchedule(_month,_day);
            for(int i=schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).length()-1;i>=0;--i){
                if(schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).getInt(i)==_minutes){
                    if(i==0){
                        if(loHou==8){
                            throw new DayUnderflowException();
                        }else{
                            loHou--;
                            loMin=schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).
                                    getInt(schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).length()-1);
                            break;
                        }
                    }else{
                        loMin=schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).getInt(i-1);
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

}
