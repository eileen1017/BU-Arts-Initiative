package com.cs591.mooncake.profile;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.cs591.mooncake.MainActivity;
import com.cs591.mooncake.R;
import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


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
    ImageView userphoto;
    CustomAdapter adapter;
    String[] Names = {"Website", "Invite Friends", "Feedback", "About", "Ticket"};
    int[] Icons = {R.drawable.website,R.drawable.invite_friend,R.drawable.feedback,R.drawable.about,R.drawable.ticket};
    Class[] classes = {WebsitePage.class, InvitePage.class, FeedbackPage.class,AboutPage.class,TicketPage.class};
    String url = "https://storage.googleapis.com/gweb-uniblog-publish-prod/images/GooglePay_Lockup.max-2800x2800.png";





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        mListView = (ListView) view.findViewById(R.id.item_menu);
        CustomAdapter customAdapter = new CustomAdapter(getActivity(), Icons, Names);
        mListView.setAdapter(customAdapter);



        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MySQLiteHelper(getActivity()).initProfile();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                TwitterCore.getInstance().getSessionManager().clearActiveSession();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(),classes[position]);
                startActivity(i);

            }
        });

        userphoto =  view.findViewById(R.id.profile_image);

        return view;

    }


}
