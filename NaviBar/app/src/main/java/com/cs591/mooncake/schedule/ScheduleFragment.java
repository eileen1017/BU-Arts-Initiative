package com.cs591.mooncake.schedule;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cs591.mooncake.R;
import java.util.ArrayList;
import java.util.List;

import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.cs591.mooncake.SQLite.SingleArtist;
import com.cs591.mooncake.SQLite.SingleEvent;



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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myDb = new MySQLiteHelper(getActivity());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        recyclerView = view.findViewById(R.id.rv);
        recyclerView2 = view.findViewById(R.id.rv2);
        scheduleslist = new ArrayList<>();
        scheduleslist2 = new ArrayList<>();



        List<Integer> eventIDs = myDb.getEventList();

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




        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager rvlayoutManager = layoutManager;

        recyclerView.setLayoutManager(rvlayoutManager);

        scheduleAdapter adapter = new scheduleAdapter(getActivity(), scheduleslist);
        recyclerView.setAdapter(adapter);


        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager rvlayoutManager2 = layoutManager2;

        recyclerView2.setLayoutManager(rvlayoutManager2);

        scheduleAdapter adapter2 = new scheduleAdapter(getActivity(), scheduleslist2);
        recyclerView2.setAdapter(adapter2);
        return view;
    }




}
