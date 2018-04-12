package com.cs591.mooncake.like;


import android.os.Bundle;
import android.app.Fragment;
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
public class LikeFragment extends Fragment {


    public LikeFragment() {
        // Required empty public constructor
    }


    RecyclerView recyclerView;


    ArrayList<ModelLike> likeslist;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_like, container, false);

        recyclerView = view.findViewById(R.id.rv);


        likeslist = new ArrayList<>();

        likeslist.add(new ModelLike(R.drawable.jupiter_okwess, "Jupiter & Okwess"));
        likeslist.add(new ModelLike(R.drawable.lamada, "Lamada"));
        likeslist.add(new ModelLike(R.drawable.orquesta_el_macabeo, "Orquesta El Macabeo"));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager rvlayoutManager = layoutManager;

        recyclerView.setLayoutManager(rvlayoutManager);

        LikeAdapter adapter = new LikeAdapter(getContext(),likeslist);
        recyclerView.setAdapter(adapter);

        return view;
    }

}
