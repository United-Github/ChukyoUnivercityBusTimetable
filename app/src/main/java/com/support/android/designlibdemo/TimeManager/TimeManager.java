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
import java.lang.reflect.Array;
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
//<<<<<<< Updated upstream
//=======
    private JSONObject kaizuData;
    private ArrayList<Calendar> suspendedDays;
    private ArrayList<ScheduleMonth> scheduleYear;
    private ScheduleDate scheduleA;
    private ScheduleDate scheduleB;
    private ScheduleDate scheduleC;
    private ScheduleDate scheduleAd;
    private ScheduleDate scheduleT;
    private ArrayList<ScheduleMonth> monthSchedule;


//>>>>>>> Stashed changes
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
//<<<<<<< Updated upstream
//=======
        suspendedDays = getDateType(ScheduleType.S);
        try {
            scheduleA.setArray(getBusSchedule("schedule_a.json", DEPART_JOSUI), getBusSchedule("schedule_a.json", DEPART_UNIVERCITY));
            scheduleAd.setArray(getBusSchedule("schedule_ad.json", DEPART_JOSUI), getBusSchedule("schedule_ad.json", DEPART_UNIVERCITY));
            scheduleB.setArray(getBusSchedule("schedule_b.json", DEPART_JOSUI), getBusSchedule("schedule_b.json", DEPART_UNIVERCITY));
            scheduleC.setArray(getBusSchedule("schedule_c.json", DEPART_JOSUI), getBusSchedule("schedule_c.json", DEPART_UNIVERCITY));
            scheduleT.setArray(getBusSchedule("schedule_t.json", DEPART_JOSUI), getBusSchedule("schedule_t.json", DEPART_UNIVERCITY));
        }catch(NoScheduleException e){
            e.printStackTrace();
        }
        monthSchedule = getMonthSchedule();
    }

    public ArrayList<Calendar> getSuspendedDays(){
        return suspendedDays;
//>>>>>>> Stashed changes
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
        kaizuData=parseJsonSub("kaizu.json");
    }

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

    private ArrayList<ScheduleMonth> getMonthSchedule(){
        ArrayList<ScheduleMonth> schedule=new ArrayList<ScheduleMonth>();
        try{
            for(int month = 1; month<=12; month++) {
                ScheduleMonth month_temp=new ScheduleMonth();
                month_temp.month=month;
                JSONObject item = jsonMonth.getJSONObject(String.valueOf(month));
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
        }
        return schedule;
    }

    public List<TimeItemModel> getBusSchedule(final int _month, final int _day, final int depart) throws NoScheduleException{
        List<TimeItemModel> timeList=new ArrayList<TimeItemModel>();
        try {
            String scheduleType = jsonMonth.getJSONObject(String.valueOf(_month)).getString(String.valueOf(_day));
            timeList = getBusScheduleSub(classifySchedule(_month,_day), depart,scheduleType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return timeList;
    }

    private ArrayList<TimeItemModel> getBusSchedule(String filename, final int depart) throws NoScheduleException{
        ArrayList<TimeItemModel> timeList=new ArrayList<TimeItemModel>();
        try {
            String kaizuType="A";
            switch(filename){
                case "schedule_a.json":
                    kaizuType="A";
                    break;
                case "schedule_ad.json":
                    kaizuType="Ad";
                    break;
                case "schedule_b.json":
                    kaizuType="B";
                    break;
                case "schedule_c.json":
                    kaizuType="C";
                    break;
                case "schedule_t.json":
                    kaizuType="T";
                    break;
            }
            timeList = getBusScheduleSub(parseJsonSub(filename), depart,kaizuType);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return timeList;
    }

    private ArrayList<TimeItemModel> getBusScheduleSub(final JSONObject _jObj, final int depart,String kaizuType){
        final String departString = DEPART_STRING[depart];
        ArrayList<TimeItemModel> timeList=new ArrayList<TimeItemModel>();
        try{
            JSONObject schedule_temp=_jObj;
            for(int i=8;schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(i))!=null;++i) {
                TimeItemModel time_temp=new TimeItemModel();
                time_temp.hour=i;
                for(int j=0;j<schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(i)).length();j++)
                {
                    JSONObject kaizuData_temp = kaizuData;
                    if(depart==DEPART_JOSUI)
                        time_temp.minutesList.add(new TimeMinutesListItemModel(schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(i)).getInt(j),false));
                    else{
                        boolean kaizuFlag=false;
                        if(kaizuData_temp.getJSONObject(kaizuType).getJSONArray(String.valueOf(i)).getInt(j)==1)
                            kaizuFlag=true;
                        time_temp.minutesList.add(new TimeMinutesListItemModel(schedule_temp.getJSONObject(departString).getJSONArray(String.valueOf(i)).getInt(j),kaizuFlag));
                    }
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

    /*
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
    */

    public int[] nearBusTime(int _month,int _day,int _hour,int _minutes,final int depart) throws NoScheduleException,DayOverflowException{
        int loHou=_hour;int loMin=_minutes;
        ArrayList<ScheduleMonth> schedule_temp=getMonthList();
        ScheduleDate date_temp = getScheduleDate(schedule_temp.get(_month-1).days.get(_day-1));
        ArrayList<TimeItemModel> timeItemList = date_temp.getTimeItem(depart);
        TimeItemModel timeHour = timeItemList.get(_hour-timeItemList.get(0).hour);
        if(_hour<timeItemList.get(0).hour){
            loHou=timeItemList.get(0).hour;
            loMin=timeItemList.get(loHou-timeItemList.get(0).hour).minutesList.get(0).minutes;
            int[] hAndM={loHou,loMin};
            return hAndM;
        }
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

    public int[] beforeBusTime(int _month,int _day,int _hour,int _minutes,final int depart) throws NoScheduleException,DayUnderflowException{
        int loHou=_hour;int loMin=_minutes;
        ArrayList<ScheduleMonth> schedule_temp=getMonthList();
        ScheduleDate date_temp = getScheduleDate(schedule_temp.get(_month-1).days.get(_day-1));
        ArrayList<TimeItemModel> timeItemList = date_temp.getTimeItem(depart);
        TimeItemModel timeHour = timeItemList.get(_hour-timeItemList.get(0).hour);
        for(int i=timeHour.minutesList.size()-1;i<=0;--i) {
            if(timeItemList.get(loHou-timeItemList.get(0).hour).minutesList.get(i).minutes == _minutes){
                if(i==0){
                    if(timeHour.hour==timeItemList.get(0).hour)
                        throw new DayUnderflowException();
                    else{
                        loHou--;
                        loMin=timeItemList.get(loHou-timeItemList.get(0).hour).minutesList
                                .get(timeItemList.get(loHou-timeItemList.get(0).hour).minutesList.size()-1).minutes;
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

//<<<<<<< Updated upstream
//=======
    public static class ScheduleMonth{
        public int month;
        public ArrayList<ScheduleType> days;
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
//>>>>>>> Stashed changes
}
