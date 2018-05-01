package com.cs591.mooncake.like;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
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

    //  field for initialization
    private  OnLikedEventClickedListener OLCL;
    public interface OnLikedEventClickedListener {
        void openLikeEvent(int id);
    }

    //  field for initialization
    TextView txtEmptyLike;
    RecyclerView recyclerView;
    private List<Object> likesList;
    private MySQLiteHelper myDb;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //  give reference to database
        myDb = new MySQLiteHelper(getActivity());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_like, container, false);

        //  Reference to object in xml
        recyclerView = view.findViewById(R.id.rv);
        //  Reference to the textView with the message
        txtEmptyLike = view.findViewById(R.id.txtEmptyLike);

        //  Initiate the likesLists
        likesList = new ArrayList<>();

        //  Reference to RecyclerView layout for like page
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager rvlayoutManager = layoutManager;
        recyclerView.setLayoutManager(rvlayoutManager);

        // make function call to refresh like page
        refreshLikedPage();

        return view;
    }

    //  called when change on liked items
    public void likeChangedHandler() {

        refreshLikedPage();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLikedEventClickedListener) {
            OLCL = (OnLikedEventClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + getString(R.string.exception));
        }
    }

    // function call in onCreateView to clear the page and get the liked info from database
    // to set adapter for recyclerView
    private void refreshLikedPage() {
        if (likesList == null) {
            return;
        }

        // clear the like fragment
        likesList.clear();
        // get profile info from database
        SingleUser singleUser = myDb.getProfile();

        // show the message if user don't have liked item
        if (singleUser.getLiked().isEmpty()){
            txtEmptyLike.setVisibility(View.VISIBLE);
        } else {
            txtEmptyLike.setVisibility(View.INVISIBLE);
        }

        // get the event from database and put them into likesList
        for (Integer i : singleUser.getLiked()){
            SingleEvent singleLiked = myDb.getEvent(i);
            likesList.add(singleLiked);
        }
        // set adapter for like page recyclerView
        LikeAdapter adapter = new LikeAdapter(getActivity(),likesList, OLCL);
        recyclerView.setAdapter(adapter);
    }

}
