package com.cs591.mooncake.explore;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.cs591.mooncake.R;
import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.cs591.mooncake.SQLite.SingleArtist;
import com.cs591.mooncake.SQLite.SingleEvent;
import com.cs591.mooncake.SQLite.SingleUser;
import com.cs591.mooncake.adapter.MainAdapter;

import java.util.ArrayList;
import java.util.List;


public class ExploreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TYPE_PERFORMANCE = "Performance";
    private static final String TYPE_WORKSHOP = "Workshop";
    private static final String TYPE_BAZAAR = "Bazaar";


    private OnFragmentInteractionListener mListener;
    private MySQLiteHelper myDb;
    private List<List<Object>> objects;

    public ExploreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_explore.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
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
        View v = inflater.inflate(R.layout.fragment_explore, container,
                false);

        myDb = new MySQLiteHelper(getActivity());

        RecyclerView recyclerView = v.findViewById(R.id.recycler_View);
        MainAdapter adapter = new MainAdapter(getActivity(), getObject());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private List<List<Object>> getObject() {
        List<Integer> eventIDs = myDb.getEventList();
        List<Integer> artistIDs = myDb.getArtistList();

        SingleUser singleUser = new SingleUser();
        singleUser.addScheduled(2);
        singleUser.addScheduled(3);

        Log.i("scheduled string", singleUser.getScheduledString(";"));
        myDb.addProfile(singleUser);

        singleUser.removeScheduled(2);
        singleUser.addScheduled(5);
        myDb.addProfile(singleUser);

        SingleUser res = myDb.getProfile();
        Log.i("Size", res.getScheduledString(";"));

        objects = new ArrayList<>();
        List<Object> artists = new ArrayList<>();
        List<Object> performances = new ArrayList<>();
        List<Object> workshops = new ArrayList<>();
        List<Object> bazaar = new ArrayList<>();

        for (Integer i : artistIDs) {
            artists.add(myDb.getArtist(i));
        }

        for (Integer i : eventIDs) {
            SingleEvent singleEvent = myDb.getEvent(i);
            switch (singleEvent.getType()) {
                case TYPE_PERFORMANCE:
                    performances.add(singleEvent);
                    break;
                case TYPE_WORKSHOP:
                    workshops.add(singleEvent);
                    break;
                case TYPE_BAZAAR:
                    bazaar.add(singleEvent);
            }

        }

        objects.add(artists);
        objects.add(performances);
        objects.add(workshops);
        objects.add(bazaar);


        return objects;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
