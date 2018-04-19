package com.cs591.mooncake.profile;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.cs591.mooncake.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    Button settings, feedback, invite, about, ticket;
    Button logout;
    Intent i;
    ListView mListView;
    CustomAdapter adapter;
    String[] Names = {"Website", "Invite Friends", "Feedback", "About", "Ticket"};
    int[] Icons = {R.drawable.website,R.drawable.invite_friend,R.drawable.feedback,R.drawable.about,R.drawable.ticket};
    Class[] classes = {WebsitePage.class, InvitePage.class, FeedbackPage.class,AboutPage.class,TicketPage.class};






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mListView = (ListView) view.findViewById(R.id.item_menu);
        CustomAdapter customAdapter = new CustomAdapter(getContext(), Icons, Names);
        mListView.setAdapter(customAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(),classes[position]);
                startActivity(i);

            }
        });


//        settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                i = new Intent(getContext(), SettingsPage.class);
//                startActivity(i);
//            }
//        });
//
//        feedback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                i = new Intent(getContext(), FeedbackPage.class);
//                startActivity(i);
//            }
//        });
//
//        invite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                i = new Intent(getContext(), InvitePage.class);
//                startActivity(i);
//            }
//        });
//
//        about.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                i = new Intent(getContext(), AboutPage.class);
//                startActivity(i);
//            }
//        });
//
//        ticket.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                i = new Intent(getContext(), TicketPage.class);
//                startActivity(i);
//            }
//        });
//
//
//        return view;
        return view;

    }


}
