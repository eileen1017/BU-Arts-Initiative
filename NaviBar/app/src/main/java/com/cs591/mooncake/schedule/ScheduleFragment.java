package com.cs591.mooncake.schedule;



import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cs591.mooncake.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.cs591.mooncake.SQLite.SingleArtist;
import com.cs591.mooncake.SQLite.SingleEvent;
import com.cs591.mooncake.SQLite.SingleUser;



/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    private OnScheduledEventClikedListener OSCL;

    public interface OnScheduledEventClikedListener{
        void openScheduleEvent(int id);
    }

    public ScheduleFragment() {
        // Required empty public constructor
    }


    RecyclerView recyclerView;
    private List<Object> scheduleslist;
    RecyclerView recyclerView2;
    private List<Object> scheduleslist2;
    private MySQLiteHelper myDb;
    Button menubtn;
    private List<Object> scheduleslist3;
    private List<Object> scheduleslist4;

    private final int ALL_SCHEDULE = 0;
    private final int MY_SCHEDULE = 1;
    private int currentPage = ALL_SCHEDULE;


    static long starttime = 0;
    static long endtime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        recyclerView = view.findViewById(R.id.rv);
        recyclerView2 = view.findViewById(R.id.rv2);
        menubtn = view.findViewById(R.id.menubtn);

        scheduleslist = new ArrayList<>();
        scheduleslist2 = new ArrayList<>();
        scheduleslist3 = new ArrayList<>();
        scheduleslist4 = new ArrayList<>();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager rvlayoutManager = layoutManager;

        recyclerView.setLayoutManager(rvlayoutManager);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager rvlayoutManager2 = layoutManager2;

        recyclerView2.setLayoutManager(rvlayoutManager2);


        if (currentPage == ALL_SCHEDULE)
            refreshAllschedulePage();
        else
            refreshMySchedulePage();


        scheduleAdapter adapter = new scheduleAdapter(getActivity(), scheduleslist,OSCL);
        recyclerView.setAdapter(adapter);
        scheduleAdapter adapter2 = new scheduleAdapter(getActivity(), scheduleslist2,OSCL);
        recyclerView2.setAdapter(adapter2);


        menubtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.menubtn){
                    if (currentPage == MY_SCHEDULE) {
                        currentPage = ALL_SCHEDULE;
                        v.setBackgroundResource(R.drawable.myschedule);
                        refreshAllschedulePage();
                    }else{
                        currentPage = MY_SCHEDULE;
                        v.setBackgroundResource(R.drawable.allschedule);
                        refreshMySchedulePage();
                    }
                }
            }
        });



        return view;
    }

    public void scheduleChangedHandler() {
        if (currentPage == ALL_SCHEDULE) {
            refreshAllschedulePage();
        } else {
            refreshMySchedulePage();
        }
    }

    public static void addToCalenderHandler(int eventID, Context context) {
        MySQLiteHelper myDb;
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_CALENDAR)
                + ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED)  {
        } else {

            try {
                myDb = new MySQLiteHelper(context);
            SingleEvent singleEvent = myDb.getEvent(eventID);
            // Inserts a row into a table at the given URL.
            // Content_URI: The content:// style URL for interacting with events.

            ContentResolver cr = context.getContentResolver();
            // Creates an empty set to store a set of values that the ContentResolver can process
            ContentValues calEvent = new ContentValues();
            int actualdate = singleEvent.getDate();
            String st = singleEvent.getStart();
            String et = singleEvent.getEnd();

            starttime = fieldToTimestamp(actualdate,st);
            endtime = fieldToTimestamp(actualdate,et);


            TimeZone timeZone = TimeZone.getDefault();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    calEvent.put(CalendarContract.Events.CALENDAR_ID, 1);
                }else{
                    calEvent.put(CalendarContract.Events.CALENDAR_ID, 3);
                }

            // Creating a Event table which contains details for individual events like calendar ID,

            calEvent.put(CalendarContract.Events.TITLE, singleEvent.getName());             // Name of Event
            calEvent.put(CalendarContract.Events.DTSTART, starttime);          // Get the current time for starting time
            calEvent.put(CalendarContract.Events.DTEND, endtime);    // Get the ending time as 1 hr after starting time
            calEvent.put(CalendarContract.Events.HAS_ALARM, 1);                             // Whether the event has an alarm or not.
            calEvent.put(CalendarContract.Events.EVENT_LOCATION, singleEvent.getAddress());     // Give the location of Event
            calEvent.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());    //Timezone for the Event

            Log.i("MyDelete", "My add: "+ cr.insert(CalendarContract.Events.CONTENT_URI, calEvent));


//        }
        } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Exception: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    public static void removeCalenderHandler(int eventID, Context context) {
        MySQLiteHelper myDb = new MySQLiteHelper(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_CALENDAR)!= PackageManager.PERMISSION_GRANTED) {
        } else {
        SingleEvent singleEvent = myDb.getEvent(eventID);


//        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
//        // Creates an empty set to store a set of values that the ContentResolver can process
//        ContentValues calEvent = new ContentValues();

        Uri uri = CalendarContract.Events.CONTENT_URI;
        String mSelectionClause = CalendarContract.Events.TITLE + " = ?";
        String[] mSelectionArgs = {singleEvent.getName()};
        context.getContentResolver().delete(uri,mSelectionClause,mSelectionArgs);


    }
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnScheduledEventClikedListener) {
            OSCL = (OnScheduledEventClikedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnScheduledEventClikedListener");
        }
    }

    private void refreshAllschedulePage() {
        if (scheduleslist == null) {
            return;
        }
        myDb = new MySQLiteHelper(getActivity());
        List<Integer> eventIDs = myDb.getEventList();
        scheduleslist.clear();
        scheduleslist2.clear();
        for (Integer i : eventIDs) {
            SingleEvent singleEvent = myDb.getEvent(i);

            switch (singleEvent.getDate()) {
                case 5:
                    scheduleslist.add(singleEvent);
                    break;
                case 6:
                    scheduleslist2.add(singleEvent);
            }
        }

        scheduleAdapter adapter = new scheduleAdapter(getActivity(), scheduleslist, OSCL);
        recyclerView.setAdapter(adapter);
        scheduleAdapter adapter2 = new scheduleAdapter(getActivity(), scheduleslist2, OSCL);
        recyclerView2.setAdapter(adapter2);
    }

    private void refreshMySchedulePage() {
        if (scheduleslist3 == null) return;
        scheduleslist3.clear();
        scheduleslist4.clear();
        SingleUser singleUser = myDb.getProfile();
        for (Integer j : singleUser.getScheduled()){
            SingleEvent singleSchedule = myDb.getEvent(j);
            switch (singleSchedule.getDate()){
                case 5:
                    scheduleslist3.add(singleSchedule);
                    break;
                case 6:
                    scheduleslist4.add(singleSchedule);
            }
        }
        scheduleAdapter adapter3 = new scheduleAdapter(getActivity(), scheduleslist3,OSCL);
        recyclerView.setAdapter(adapter3);
        scheduleAdapter adapter4 = new scheduleAdapter(getActivity(), scheduleslist4,OSCL);
        recyclerView2.setAdapter(adapter4);
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

    private static long fieldToTimestamp(int day, String date) {

        String [] stringSTime =  getTimeString(date).split(":");
        int[] time = {Integer.parseInt(stringSTime[0]),Integer.parseInt(stringSTime[1])};
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018,Calendar.OCTOBER,day,time[0],time[1]);
        return calendar.getTimeInMillis();
    }

//    private static List<Long> getAllTimeStrings(String eventStartTime){
//        List<Long> times = new ArrayList<Long>();
//        times.add(fieldToTimestamp(eventStartTime));
//        return times;
//    }
//
//    public void setEventOrder(int day, List<String> times,List<Object> allscheduleslist) {
//        // sort people into buckets by the first letter of last name
//        long smallest = 0;
//        allscheduleslist.clear();
//        List<Object> currentSchedulelist = null;
//        for (String i : times) {
//            if (fieldToTimestamp(day,i) != smallest) {
//                if (allscheduleslist != null) {
//                    allscheduleslist.add(allscheduleslist);
//                }
//
//                currentSchedulelist = new ArrayList<>();;
//                smallest = fieldToTimestamp(day,i);
//                currentSchedulelist.smallest = String.valueOf(smallest);
//            }
//        }
//
//            if (currentSection != null) {
//                currentSection.people.add(person);
//            }
//        }
//
//        sections.add(currentSection);
//        notifyAllSectionsDataSetChanged();
//    }


}
