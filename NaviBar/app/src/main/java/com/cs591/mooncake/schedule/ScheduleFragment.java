package com.cs591.mooncake.schedule;


import android.app.Fragment;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cs591.mooncake.R;
import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {


    public ScheduleFragment() {
        // Required empty public constructor
    }


    RecyclerView recyclerView;
    ArrayList<ModelSchedule> scheduleslist;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        recyclerView = view.findViewById(R.id.rv);
        scheduleslist = new ArrayList<>();

        scheduleslist.add(new ModelSchedule("Jupiter & Okwess","It is a world music festival."));
        scheduleslist.add(new ModelSchedule("Lamada","Ladama is a multinational ensemble of women building community through sound."));
        scheduleslist.add(new ModelSchedule( "Orquesta El Macabeo","Orquesta el Macabeo its a breakthrough on the \"Salda Gorda\", a genre inside tropical music."));




        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager rvlayoutManager = layoutManager;

        recyclerView.setLayoutManager(rvlayoutManager);

        scheduleAdapter adapter = new scheduleAdapter(getContext(), scheduleslist);
        recyclerView.setAdapter(adapter);
        return view;
    }

}
