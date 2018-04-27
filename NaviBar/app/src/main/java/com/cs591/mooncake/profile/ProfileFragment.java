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


    public ProfileFragment() {
        // Required empty public constructor
    }


    Button settings, feedback, invite, about, ticket;
    Button logout;
    Intent i;
    ListView mListView;
    CustomAdapter adapter;
    TextView username;
    String[] Names = {"Website", "Invite Friends", "Feedback", "About", "Ticket"};
    int[] Icons = {R.drawable.website,R.drawable.invite_friend,R.drawable.feedback,R.drawable.about,R.drawable.ticket};
    Class[] classes = {WebsitePage.class, InvitePage.class, FeedbackPage.class,AboutPage.class,TicketPage.class};
    CircleImageView userPic;
    private MySQLiteHelper myDb;
    public String url = "https://www.reka.in/pres/1258167031.jpg";





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        userPic = (CircleImageView) view.findViewById(R.id.profile_image);
        username = (TextView) view.findViewById(R.id.profile_name);

        MySQLiteHelper mydb = new MySQLiteHelper(getActivity());
        SingleUser singleUser = mydb.getProfile();

        username.setText(singleUser.getUserName());


        userPic.setImageBitmap(singleUser.getPic());


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

<<<<<<< HEAD
        userphoto =  view.findViewById(R.id.profile_image);
=======
>>>>>>> f0ec4dca2364da52a46f52e0e90c627eaaadcc24

        return view;

    }
    public static Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream stream = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        try {
            stream = getHttpConnection(url);
//            Log.i("bitmap1",""+stream);
//            Log.i("bitmap0",""+url);
            bitmap = BitmapFactory.decodeStream(stream);
//            Log.i("bitmap2",""+bitmap);

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
