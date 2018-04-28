package com.cs591.mooncake.explore;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.cs591.mooncake.R;
import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.cs591.mooncake.SQLite.SingleEvent;
import com.cs591.mooncake.adapter.MainAdapter;

import java.util.ArrayList;
import java.util.List;


public class ExploreFragment extends Fragment {


    private static final String TYPE_PERFORMANCE = "Performance";
    private static final String TYPE_WORKSHOP = "Workshop";
    private static final String TYPE_BAZAAR = "Bazaar";


    private ExploreInteractionListener EIL;
    private MySQLiteHelper myDb;
    private List<List<Object>> objects;

    public ExploreFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ExploreInteractionListener) {
            EIL = (ExploreInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ExploreInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EIL = null;
    }

    private List<List<Object>> getObject() {
        List<Integer> eventIDs = myDb.getEventList();
        List<Integer> artistIDs = myDb.getArtistList();

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
    public interface ExploreInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
