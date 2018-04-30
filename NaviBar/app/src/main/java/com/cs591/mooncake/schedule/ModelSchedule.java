package com.cs591.mooncake.schedule;

import android.util.Log;

import java.util.Calendar;
import java.util.List;

/**
 * Created by LinLi on 4/9/18.
 */

public class ModelSchedule {
    private String name;
    private String starttime;
    private String endtime;
    private String location;
    private int date;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

//    @Override
//    public int compareTo(ModelSchedule modelSchedule) {
//        int compareTo=((ModelSchedule )modelSchedule).fieldgetStarttime();
//        /* For Ascending order*/
//        return this.rating-compareTo;
//
//        /* For Descending order do like this */
//        //return compareage-this.rating;
//    }

    private static long fieldToTimestamp(int day, String date) {

        String [] stringSTime =  getTimeString(date).split(":");
        int[] time = {Integer.parseInt(stringSTime[0]),Integer.parseInt(stringSTime[1])};
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018,Calendar.OCTOBER,day,time[0],time[1]);
        return calendar.getTimeInMillis();
    }

    private static String getTimeString(String string){
        String[] SplitString;
        String[] SplitTime;
        String TimeString;
        SplitString = string.split("\\s+");
        SplitTime = SplitString[0].split(":");
        if (!SplitTime[0].equals("12")) {
            if (SplitString[1].equals("pm")) {

                TimeString = Integer.toString(Integer.parseInt(SplitTime[0]) + 12) + ":" + SplitTime[1];
            } else {
                TimeString = SplitString[0];
            }
        } else {
            if (SplitString[1].equals("am")) {

                TimeString = "00:00";
            } else {
                TimeString = "12:00";
            }
            Log.i("MyAdd", "string is "+string);
            Log.i("MyAdd", "SplitString[1] is " + SplitString[1]);
            Log.i("MyAdd", "SplitTime[0] is "+ SplitTime[0]);
            Log.i("MyAdd", "TimeString is " + TimeString);

        }
        return TimeString;
    }
}
