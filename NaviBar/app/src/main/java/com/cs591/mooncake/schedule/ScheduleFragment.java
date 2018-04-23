package com.cs591.mooncake.schedule;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import com.cs591.mooncake.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.cs591.mooncake.SQLite.SingleArtist;
import com.cs591.mooncake.SQLite.SingleEvent;
import com.cs591.mooncake.SQLite.SingleUser;
import com.cs591.mooncake.schedule.scheduleAdapter;



/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {


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
    private boolean isButtonClicked = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myDb = new MySQLiteHelper(getActivity());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        recyclerView = view.findViewById(R.id.rv);
        recyclerView2 = view.findViewById(R.id.rv2);
        menubtn = view.findViewById(R.id.menubtn);
        scheduleslist = new ArrayList<>();
        scheduleslist2 = new ArrayList<>();
        scheduleslist3 = new ArrayList<>();
        scheduleslist4 = new ArrayList<>();


        List<Integer> eventIDs = myDb.getEventList();
        SingleUser singleUser = myDb.getProfile();



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
        myDb.addProfile(singleUser);



        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager rvlayoutManager = layoutManager;

        recyclerView.setLayoutManager(rvlayoutManager);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager rvlayoutManager2 = layoutManager2;

        recyclerView2.setLayoutManager(rvlayoutManager2);


        scheduleAdapter adapter = new scheduleAdapter(getActivity(), scheduleslist);
        recyclerView.setAdapter(adapter);
        scheduleAdapter adapter2 = new scheduleAdapter(getActivity(), scheduleslist2);
        recyclerView2.setAdapter(adapter2);


        menubtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.menubtn){
                    isButtonClicked = !isButtonClicked;
                    if (isButtonClicked) {
                        v.setBackgroundResource(R.drawable.myschedule);
                        scheduleAdapter adapter = new scheduleAdapter(getActivity(), scheduleslist);
                        recyclerView.setAdapter(adapter);
                        scheduleAdapter adapter2 = new scheduleAdapter(getActivity(), scheduleslist2);
                        recyclerView2.setAdapter(adapter2);


                    }else{
                        v.setBackgroundResource(R.drawable.allschedule);
                        scheduleAdapter adapter3 = new scheduleAdapter(getActivity(), scheduleslist3);
                        recyclerView.setAdapter(adapter3);
                        scheduleAdapter adapter4 = new scheduleAdapter(getActivity(), scheduleslist4);
                        recyclerView2.setAdapter(adapter4);
                    }
                }
            }
        });



        return view;
    }




}
