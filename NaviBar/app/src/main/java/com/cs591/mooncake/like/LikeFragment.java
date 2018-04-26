package com.cs591.mooncake.like;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs591.mooncake.R;
import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.cs591.mooncake.SQLite.SingleEvent;
import com.cs591.mooncake.SQLite.SingleUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LikeFragment extends Fragment {


    public LikeFragment() {
        // Required empty public constructor
    }


    TextView txtEmptyLike;
    RecyclerView recyclerView;
    private List<Object> likesList;
    private MySQLiteHelper myDb;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myDb = new MySQLiteHelper(getActivity());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_like, container, false);

        recyclerView = view.findViewById(R.id.rv);
        txtEmptyLike = view.findViewById(R.id.txtEmptyLike);

        likesList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager rvlayoutManager = layoutManager;

        recyclerView.setLayoutManager(rvlayoutManager);

        refreshLikedPage();

        return view;
    }


    public void likeChangedHandler() {

        refreshLikedPage();
    }

    private void refreshLikedPage() {
        if (likesList == null) {
            return;
        }


        likesList.clear();
        SingleUser singleUser = myDb.getProfile();

        if (singleUser.getLiked().isEmpty()){
            txtEmptyLike.setVisibility(View.VISIBLE);
        } else {
            txtEmptyLike.setVisibility(View.INVISIBLE);
        }


        for (Integer i : singleUser.getLiked()){
            SingleEvent singleLiked = myDb.getEvent(i);
            likesList.add(singleLiked);
        }
        LikeAdapter adapter = new LikeAdapter(getActivity(),likesList);
        recyclerView.setAdapter(adapter);
    }

}
