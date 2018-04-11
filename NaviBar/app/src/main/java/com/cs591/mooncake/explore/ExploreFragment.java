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
import com.cs591.mooncake.adapter.MainAdapter;

import java.util.ArrayList;


public class ExploreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ArrayList<Object> objects = new ArrayList<>();

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

        RecyclerView recyclerView = v.findViewById(R.id.recycler_View);
        MainAdapter adapter = new MainAdapter(getContext(), getObject());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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

    private ArrayList<Object> getObject() {
        objects.add(getHorizontalData().get(0));
        objects.add(getHorizontalData().get(0));
        objects.add(getHorizontalData().get(0));
        objects.add(getHorizontalData().get(0));
        objects.add(getHorizontalData().get(0));
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

    public static ArrayList<SingleVertical> getVerticalData() {
        ArrayList<SingleVertical> singleVerticals = new ArrayList<>();
        singleVerticals.add(new SingleVertical("Charlie Chaplin",
                "Sir Charles Spencer \"Charlie\" Chaplin, KBE was an。。。。。 ",
                R.drawable.charlie));
        singleVerticals.add(new SingleVertical("Mr. Bean",
                "Mr. Bean is a British sitcom created by Rowan Atkinson and ....",
                R.drawable.mrbean));
        singleVerticals.add(new SingleVertical("Jim Carrey",
                "James Eugene \"Jim\" Carrey is a Canadian-American actor, comedian, impressionist...",
                R.drawable.jim));
        singleVerticals.add(new SingleVertical("Charlie Chaplin",
                "Sir Charles Spencer \"Charlie\" Chaplin, KBE was an。。。。。 ",
                R.drawable.charlie));
        singleVerticals.add(new SingleVertical("Mr. Bean",
                "Mr. Bean is a British sitcom created by Rowan Atkinson and ....",
                R.drawable.mrbean));
        singleVerticals.add(new SingleVertical("Jim Carrey",
                "James Eugene \"Jim\" Carrey is a Canadian-American actor, comedian, impressionist...",
                R.drawable.jim));

        singleVerticals.add(new SingleVertical("Charlie Chaplin",
                "Sir Charles Spencer \"Charlie\" Chaplin, KBE was an。。。。。 ",
                R.drawable.charlie));
        singleVerticals.add(new SingleVertical("Mr. Bean",
                "Mr. Bean is a British sitcom created by Rowan Atkinson and ....",
                R.drawable.mrbean));
        singleVerticals.add(new SingleVertical("Jim Carrey",
                "James Eugene \"Jim\" Carrey is a Canadian-American actor, comedian, impressionist...",
                R.drawable.jim));


        return singleVerticals;
    }

    public static ArrayList<SingleHorizontal> getHorizontalData() {
        ArrayList<SingleHorizontal> singleHorizontals = new ArrayList<>();
        singleHorizontals.add(new SingleHorizontal(R.drawable.charlie,
                "Charlie Chaplin",
                "Sir Charles Spencer \"Charlie\" Chaplin, KBE was an。。。。。 ",
                "2018/10/10"));
        singleHorizontals.add(new SingleHorizontal(R.drawable.mrbean,
                "Mr. Bean",
                "Mr. Bean is a British sitcom created by Rowan Atkinson and ....",
                "2018/10/10"));
        singleHorizontals.add(new SingleHorizontal(R.drawable.jim,
                "Jim Carrey",
                "James Eugene \"Jim\" Carrey is a Canadian-American actor, comedian, impressionist...",
                "2018/10/10"));
        singleHorizontals.add(new SingleHorizontal(R.drawable.charlie,
                "Charlie Chaplin",
                "Sir Charles Spencer \"Charlie\" Chaplin, KBE was an。。。。。 ",
                "2018/10/10"));
        singleHorizontals.add(new SingleHorizontal(R.drawable.mrbean,
                "Mr. Bean",
                "Mr. Bean is a British sitcom created by Rowan Atkinson and ....",
                "2018/10/10"));
        singleHorizontals.add(new SingleHorizontal(R.drawable.jim,
                "Jim Carrey",
                "James Eugene \"Jim\" Carrey is a Canadian-American actor, comedian, impressionist...",
                "2018/10/10"));
        return singleHorizontals;
    }
}
