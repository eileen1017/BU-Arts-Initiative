package com.cs591.mooncake.profile;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs591.mooncake.R;


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
    ImageView image;
    TextView name;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        settings =  (Button) view.findViewById(R.id.settings);
        feedback = (Button) view.findViewById(R.id.feedback);
        invite = (Button) view.findViewById(R.id.invite);
        about = (Button) view.findViewById(R.id.about);
        ticket = (Button) view.findViewById(R.id.ticket);
        logout = (Button) view.findViewById(R.id.logout);
        image = (ImageView) view.findViewById(R.id.profile_image);
        name = (TextView) view.findViewById(R.id.profile_name);

        logout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                image.setImageResource(R.drawable.feedback);
                name.setText("Guest");
                logout.setEnabled(false);
                logout.setVisibility(View.INVISIBLE);

            }
        });


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getActivity(), SettingsPage.class);
                startActivity(i);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getActivity(), FeedbackPage.class);
                startActivity(i);
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getActivity(), InvitePage.class);
                startActivity(i);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getActivity(), AboutPage.class);
                startActivity(i);
            }
        });

        ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getActivity(), TicketPage.class);
                startActivity(i);
            }
        });


        return view;
    }


}
