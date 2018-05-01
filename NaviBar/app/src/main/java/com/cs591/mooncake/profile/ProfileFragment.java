package com.cs591.mooncake.profile;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cs591.mooncake.MainActivity;
import com.cs591.mooncake.R;
import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.cs591.mooncake.SQLite.SingleUser;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.TwitterCore;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public static final String GUEST = "guest";

    public ProfileFragment() {
        // Required empty public constructor
    }

    //  field for initialization
    ListView mListView;
    CustomAdapter adapter;
    TextView username;
    String[] Names = {"Website", "Invite Friends", "Feedback", "About", "Ticket"};
    int[] Icons = {R.drawable.website,R.drawable.invite_friend,R.drawable.feedback,R.drawable.about,R.drawable.ticket};
    CircleImageView userPic;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //  inflate layout
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //  give reference to the object
        userPic = (CircleImageView) view.findViewById(R.id.profile_image);
        username = (TextView) view.findViewById(R.id.profile_name);

        //  get the database
        MySQLiteHelper mydb = new MySQLiteHelper(getActivity());
        //  get singleUser's profile from database
        SingleUser singleUser = mydb.getProfile();

        //  check if no name in singleUser database
        if (singleUser.getUserName() == null || singleUser.getUserName().isEmpty())
            //  set username as GUEST
            username.setText(GUEST);
        else
            //  Else, set username as what shown in singleUser database
            username.setText(singleUser.getUserName());

        //  check if there is a profile picture in database
        if (singleUser.getPic() != null)
            //  set the profile picture as what shown in singleUser database
            userPic.setImageBitmap(singleUser.getPic());
        else
            //  set profile picture as default picture
            userPic.setImageResource(R.drawable.profilepic);

        //  Give reference to ListView and set adapter to it
        mListView = (ListView) view.findViewById(R.id.item_menu);
        CustomAdapter customAdapter = new CustomAdapter(getActivity(), Icons, Names);
        mListView.setAdapter(customAdapter);


        //  called when user clicks on logout button and log out to login page
        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MySQLiteHelper(getActivity()).initProfile();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                TwitterCore.getInstance().getSessionManager().clearActiveSession();
            }
        });


        return view;

    }
    public static Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream stream = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        try {
            stream = getHttpConnection(url);
            bitmap = BitmapFactory.decodeStream(stream);

            stream.close();
        }
        catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("downloadImage"+ e1.toString());
        }
        return bitmap;
    }

    // Makes HttpURLConnection and returns InputStream

    public static  InputStream getHttpConnection(String urlString)  throws IOException {

        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();

            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("downloadImage" + ex.toString());
        }
        return stream;
    }


}
