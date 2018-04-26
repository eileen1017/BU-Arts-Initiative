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


    RecyclerView recyclerView;
    private List<Object> likesList;
    private MySQLiteHelper myDb;
    private LikeFragment.OnFragmentInteractionListener mListener;

    LikeFragment.OnFragmentInteractionListener OnFragmentInteractionListener;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_explore.
     */
    // TODO: Rename and change types and number of parameters
    public static LikeFragment newInstance(String param1, String param2) {
        LikeFragment fragment = new LikeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        myDb = new MySQLiteHelper(getActivity());
        View view = inflater.inflate(R.layout.fragment_like, container, false);

        recyclerView = view.findViewById(R.id.rv);


        likesList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager rvlayoutManager = layoutManager;

        recyclerView.setLayoutManager(rvlayoutManager);

        refreshLikedPage();


        //LikeAdapter adapter = new LikeAdapter(getActivity(),likesList);
        //recyclerView.setAdapter(adapter);

        return view;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            OnFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } catch (Exception e) {}
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        for (Integer i : singleUser.getLiked()){
            SingleEvent singleLiked = myDb.getEvent(i);
            likesList.add(singleLiked);

            LikeAdapter adapter = new LikeAdapter(getActivity(),likesList);
            recyclerView.setAdapter(adapter);
        }
    }

}
