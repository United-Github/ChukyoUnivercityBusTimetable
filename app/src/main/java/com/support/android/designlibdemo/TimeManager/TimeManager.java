package com.support.android.designlibdemo.TimeManager;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 裕亮 on 2017/08/20.
 */

public class TimeManager {
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
    private void parseJson() throws JSONException,IOException{
        String[] filename={"calender.json","schedule_a.json","schedule_b.json","schedule_c.json","schedule_t.json","schedule_ad.json"};
        for(int i=0;i<6;i++) {
            InputStream inputStream = context.getAssets().open(filename[i]);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                if (length > 0) {
                    outputStream.write(buffer, 0, length);
                }
            }
            switch (i){
                case 0:
                    month = new JSONObject(new String(outputStream.toByteArray()));
                    break;
                case 1:
                    schedule_a = new JSONObject(new String(outputStream.toByteArray()));
                    break;
                case 2:
                    schedule_b = new JSONObject(new String(outputStream.toByteArray()));
                    break;
                case 3:
                    schedule_c = new JSONObject(new String(outputStream.toByteArray()));
                    break;
                case 4:
                    schedule_t = new JSONObject(new String(outputStream.toByteArray()));
                    break;
                case 5:
                    schedule_ad = new JSONObject(new String(outputStream.toByteArray()));
                    break;
            }
        }
    }

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

    public String[] monthSchedule(int _month){
        List<String> schedule = new ArrayList<String>();
        try {
            JSONObject item = month.getJSONObject(String.valueOf(_month));
            for(int i=1;item.getString(String.valueOf(i))==null;i++)
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


    public List<List<Integer>> getBusSchedule(int _month,int _day,String _from){
        List<List<Integer>> busSchedule=new ArrayList<>();
        try{
            String scheduleType = month.getJSONObject(String.valueOf(_month)).getString(String.valueOf(_day));
            JSONObject schedule_temp=schedule_a;
            switch (scheduleType){
                case "A":
                    schedule_temp=schedule_a;
                    break;
                case "B":
                    schedule_temp=schedule_b;
                    break;
                case "C":
                    schedule_temp=schedule_c;
                    break;
                case "T":
                    schedule_temp=schedule_t;
                    break;
                case "S":
                    schedule_temp=schedule_ad;
                    break;
            }
            for(int i=0;schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(i))==null;i++) {
                List<Integer> tempArray=new ArrayList<Integer>();
                for(int j=0;j<schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(i)).length();j++)
                {
                    tempArray.add(schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(i)).getInt(j));
                }
                busSchedule.add(tempArray);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return busSchedule;
    }

    public int[] nearBusTime(int _month,int _day,int _hour,int _minutes,String _from){
        int loMon=_month;int loDay=_day;int loHou=_hour;int loMin=_minutes;
        try{
            String scheduleType = month.getJSONObject(String.valueOf(_month)).getString(String.valueOf(_day));
            JSONObject schedule_temp=schedule_a;
            boolean cFlag=false;
            switch (scheduleType){
                case "A":
                    schedule_temp=schedule_a;
                    break;
                case "B":
                    schedule_temp=schedule_b;
                    break;
                case "C":
                    schedule_temp=schedule_c;
                    cFlag=true;
                    break;
                case "T":
                    schedule_temp=schedule_t;
                    break;
                case "S":
                    schedule_temp=schedule_ad;
                    break;
                default:
                    int[] boo={-1,-1,-1,-1};
                    return boo;
            }
            boolean flag=false;
            for(int i=0;i<=schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).length();i++){
                if(i<=schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).length()) {
                    i=0;
                    loHou++;
                    continue;
                }
                if(schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).getInt(i)>loMin){
                    flag=true;
                }else if(schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).getInt(i)<loMin){
                    if(flag==true){
                        loMin=schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).getInt(i);
                        int[] hAndM={loMon,loDay,loHou,loMin};
                        return hAndM;
                    }
                    flag=false;
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        int[] boo={-1,-1,-1,-1};
        return boo;
    }

    public int[] afterBusTime(int _month,int _day,int _hour,int _minutes,String _from){
        int loMon=_month;int loDay=_day;int loHou=_hour;int loMin=_minutes;
        try{
            String scheduleType = month.getJSONObject(String.valueOf(_month)).getString(String.valueOf(_day));
            JSONObject schedule_temp=schedule_a;
            Boolean cFlag=false;
            switch (scheduleType){
                case "A":
                    schedule_temp=schedule_a;
                    break;
                case "B":
                    schedule_temp=schedule_b;
                    break;
                case "C":
                    schedule_temp=schedule_c;
                    cFlag=true;
                    break;
                case "T":
                    schedule_temp=schedule_t;
                    break;
                case "S":
                    schedule_temp=schedule_ad;
                    break;
                default:
                    int[] boo={-1,-1,-1,-1};
                    return boo;
            }
            for(int i=0;i<schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).length();i++){
                if(schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).getInt(i)==_minutes){
                    if(i==schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).length()-1) {
                        if (loHou == 21||(cFlag&&loHou==19)) {
                            do {
                                if (loDay == schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).length()-1) {
                                    if (loMon + 1 == 13)
                                        loMon = 1;
                                    else
                                        loMon++;
                                    loDay = 1;
                                } else
                                    loDay++;
                            }
                            while (month.getJSONObject(String.valueOf(loMon)).getString(String.valueOf(loDay)) == " ");
                            String tempString=month.getJSONObject(String.valueOf(loMon)).getString(String.valueOf(loDay));
                            loHou=8;
                        } else {
                            loHou++;
                        }
                        loMin = schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).getInt(0);
                    }else{
                        loMin = schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).getInt(i+1);
                    }
                }
                int hAndM[]={loMon,loDay,loHou,loMin};
                return hAndM;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        int hAndM[]={-1,-1,-1,-1};
        return hAndM;
    }

    public int[] beforeBusTime(int _month,int _day,int _hour,int _minutes,String _from){

        int loMon=_month;int loDay=_day;int loHou=_hour;int loMin=_minutes;
        try{
            String scheduleType = month.getJSONObject(String.valueOf(_month)).getString(String.valueOf(_day));
            JSONObject schedule_temp=schedule_a;
            switch (scheduleType){
                case "A":
                    schedule_temp=schedule_a;
                    break;
                case "B":
                    schedule_temp=schedule_b;
                    break;
                case "C":
                    schedule_temp=schedule_c;
                    break;
                case "T":
                    schedule_temp=schedule_t;
                    break;
                case "S":
                    schedule_temp=schedule_ad;
                    break;
                default:
                    int[] boo={-1,-1,-1,-1};
                    return boo;
            }
            for(int i=0;i<schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).length();i++){
                if(schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).getInt(i)==_minutes){
                    if(i==0) {
                        if (loHou == 8) {
                            do {
                                if (loDay == 1) {
                                    if (loMon - 1 == 0)
                                        loMon = 12;
                                    else
                                        loMon--;
                                    loDay = month.getJSONObject(String.valueOf(loMon)).length();
                                } else
                                    loDay--;
                            }
                            while (month.getJSONObject(String.valueOf(loMon)).getString(String.valueOf(loDay)) == " ");
                            String tempString=month.getJSONObject(String.valueOf(loMon)).getString(String.valueOf(loDay));
                            if (tempString.equals("C")) {
                                loHou = 19;
                            } else {
                                loHou = 21;
                            }
                        } else {
                            loHou--;
                        }
                        loMin = schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).getInt(schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).length() - 1);
                    }else{
                        loMin = schedule_temp.getJSONObject(_from).getJSONArray(String.valueOf(loHou)).getInt(i-1);
                    }
                }
                int hAndM[]={loMon,loDay,loHou,loMin};
                return hAndM;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        int hAndM[]={-1,-1,-1,-1};
        return hAndM;
    }

    private Context context;
    private JSONObject month;
    private JSONObject schedule_a;
    private JSONObject schedule_b;
    private JSONObject schedule_c;
    private JSONObject schedule_t;
    private JSONObject schedule_ad;
}
