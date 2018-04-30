package com.cs591.mooncake.schedule;



import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.cs591.mooncake.SQLite.SingleEvent;
import com.cs591.mooncake.SQLite.SingleUser;



/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    //  field for initialization
    private OnScheduledEventClikedListener OSCL;

    //  field for interface of OSCL
    public interface OnScheduledEventClikedListener{
        void openScheduleEvent(int id);
    }

    public ScheduleFragment() {
        // Required empty public constructor
    }

    //  Field for initialization
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

        // Reference to object in xml
        recyclerView = view.findViewById(R.id.rv);
        recyclerView2 = view.findViewById(R.id.rv2);
        menubtn = view.findViewById(R.id.menubtn);

        //  Initiate the schedulelists
        scheduleslist = new ArrayList<>();
        scheduleslist2 = new ArrayList<>();
        scheduleslist3 = new ArrayList<>();
        scheduleslist4 = new ArrayList<>();

        //  Reference to RecyclerView layout of rv1
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager rvlayoutManager = layoutManager;
        recyclerView.setLayoutManager(rvlayoutManager);

        //  Reference to RecyclerView layout of rv2
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager rvlayoutManager2 = layoutManager2;
        recyclerView2.setLayoutManager(rvlayoutManager2);

        //  Check whether it is all schedule or my schedule and make function calls
        if (currentPage == ALL_SCHEDULE)
            refreshAllschedulePage(true);
        else
            refreshMySchedulePage(true);

        //  set adapter to both schedulelists of recyclerView
        scheduleAdapter adapter = new scheduleAdapter(getActivity(), scheduleslist,OSCL, true);
        recyclerView.setAdapter(adapter);
        scheduleAdapter adapter2 = new scheduleAdapter(getActivity(), scheduleslist2,OSCL, true);
        recyclerView2.setAdapter(adapter2);

        //  called when user clicks on menubtn on top right corner
        menubtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.menubtn){
                    //  when clicked on it and it is my schedule page, change layout to all schedule page
                    if (currentPage == MY_SCHEDULE) {
                        currentPage = ALL_SCHEDULE;
                        v.setBackgroundResource(R.drawable.myschedule);
                        refreshAllschedulePage(true);
                    }else{

                        //  when clicked on it and it is all schedule page, change layout to my schedule page
                        currentPage = MY_SCHEDULE;
                        v.setBackgroundResource(R.drawable.allschedule);
                        refreshMySchedulePage(true);
                    }
                }
            }
        });



        return view;
    }


    //  called when change on schedule
    public void scheduleChangedHandler() {
        if (currentPage == ALL_SCHEDULE) {
            refreshAllschedulePage(false);
        } else {
            refreshMySchedulePage(false);
        }
    }


    //  Called when user allow app to add their schedule to the built-in Calendar app in their phone
    public static void addToCalenderHandler(int eventID, Context context) {
        MySQLiteHelper myDb;

        //  check for the existance of WRITE_CALENDAR and Read Calendar permission
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_CALENDAR)
                + ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED)  {
        } else {


            try {

                //  give reference to database
                myDb = new MySQLiteHelper(context);

                //  get singleEvent information in database
            SingleEvent singleEvent = myDb.getEvent(eventID);
            int actualdate = singleEvent.getDate();
            String st = singleEvent.getStart();
            String et = singleEvent.getEnd();

            // Inserts a row into a table at the given URL.
            // Content_URI: The content:// style URL for interacting with events.
            ContentResolver cr = context.getContentResolver();

            // Creates an empty set to store a set of values that the ContentResolver can process
            ContentValues calEvent = new ContentValues();

            // Convert time to millisecond from format of 03:00 pm
            starttime = fieldToTimestamp(actualdate,st);
            endtime = fieldToTimestamp(actualdate,et);

            //  Get current timeZone of user
            TimeZone timeZone = TimeZone.getDefault();

            //  Set Calendar_id for Calendar input
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    calEvent.put(CalendarContract.Events.CALENDAR_ID, 1);
                }else{
                    calEvent.put(CalendarContract.Events.CALENDAR_ID, 3);
                }

            // Creating a Event table which contains details for individual events like calendar ID,
                //calEvent.put(CalendarContract.Events._ID,singleEvent.getID());
            calEvent.put(CalendarContract.Events.TITLE, singleEvent.getName());             // Name of Event
            calEvent.put(CalendarContract.Events.DTSTART, starttime);          // Get the current time for starting time
            calEvent.put(CalendarContract.Events.DTEND, endtime);    // Get the ending time as 1 hr after starting time
            calEvent.put(CalendarContract.Events.HAS_ALARM, 1);                             // Whether the event has an alarm or not.
            calEvent.put(CalendarContract.Events.EVENT_LOCATION, singleEvent.getAddress());     // Give the location of Event
            calEvent.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());    //Timezone for the Event
            calEvent.put(CalendarContract.Events.DESCRIPTION,Integer.toString(singleEvent.getID()));    //  Set an unique description of the event
            Log.i("MyDelete", "My add: "+ cr.insert(CalendarContract.Events.CONTENT_URI, calEvent));
            Toast.makeText(context, "Added to Google calendar.", Toast.LENGTH_SHORT).show();


            //  called when catch exception
        } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Exception: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    //  Called when user allow app to delete their schedule from the built-in Calendar app in their phone
    public static void removeCalenderHandler(int eventID, Context context) {
        MySQLiteHelper myDb = new MySQLiteHelper(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_CALENDAR)!= PackageManager.PERMISSION_GRANTED) {
        } else {
        SingleEvent singleEvent = myDb.getEvent(eventID);


        Uri uri = CalendarContract.Events.CONTENT_URI;
        String mSelectionClause = CalendarContract.Events.DESCRIPTION + " = ?";
        String[] mSelectionArgs = {Integer.toString(singleEvent.getID())};
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

    //  function calls when track object events to List<Object> schedulelist for RecyclerView on AllSchedulePage
    private void refreshAllschedulePage(boolean anim) {
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
                //  check for the date of event, if is it 5, add to schedulelist which appears on top of the layout
                case 5:
                    scheduleslist.add(singleEvent);
                    break;
                //  check for the date of event, if is it 5, add to schedulelist2 which appears on top of the layout
                case 6:
                    scheduleslist2.add(singleEvent);
                    break;
            }
        }

        //  Set adapters for recyclerView
        scheduleAdapter adapter = new scheduleAdapter(getActivity(), scheduleslist, OSCL, anim);
        recyclerView.setAdapter(adapter);
        scheduleAdapter adapter2 = new scheduleAdapter(getActivity(), scheduleslist2, OSCL, anim);
        recyclerView2.setAdapter(adapter2);
    }

    //  function calls when track object events to List<Object> schedulelist for RecyclerView on MySchedulePage
    private void refreshMySchedulePage(boolean anim) {
        if (scheduleslist3 == null) return;
        scheduleslist3.clear();
        scheduleslist4.clear();
        SingleUser singleUser = myDb.getProfile();
        for (Integer j : singleUser.getScheduled()){
            SingleEvent singleSchedule = myDb.getEvent(j);
            switch (singleSchedule.getDate()){
                case 5:
                    //  check for the date of event, if is it 5, add to schedulelist3 which appears on top of the layout
                    scheduleslist3.add(singleSchedule);
                    break;
                case 6:
                    //  check for the date of event, if is it 5, add to schedulelist4 which appears on top of the layout
                    scheduleslist4.add(singleSchedule);
                    break;
            }
        }

        //  Set adapters for recyclerView
        scheduleAdapter adapter3 = new scheduleAdapter(getActivity(), scheduleslist3,OSCL, anim);
        recyclerView.setAdapter(adapter3);
        scheduleAdapter adapter4 = new scheduleAdapter(getActivity(), scheduleslist4,OSCL, anim);
        recyclerView2.setAdapter(adapter4);
    }

    //  Called change string of time of format 04:00 pm to string of time of format 16:00
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

        }
        return TimeString;
    }

    //  called when changing string of time to milliseconds
    private static long fieldToTimestamp(int day, String date) {

        String [] stringSTime =  getTimeString(date).split(":");
        int[] time = {Integer.parseInt(stringSTime[0]),Integer.parseInt(stringSTime[1])};
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018,Calendar.OCTOBER,day,time[0],time[1]);
        return calendar.getTimeInMillis();
    }





}
