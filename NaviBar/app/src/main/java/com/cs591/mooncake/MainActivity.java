package com.cs591.mooncake;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ImageView;

import com.cs591.mooncake.FirebaseUtils.FirebaseInitialize;
import com.cs591.mooncake.FirebaseUtils.FirebaseProfile;
import com.cs591.mooncake.SQLite.DataBaseUtil;
import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.cs591.mooncake.SQLite.SingleEvent;
import com.cs591.mooncake.SQLite.SingleUser;
import com.cs591.mooncake.explore.ArtistActivity;
import com.cs591.mooncake.explore.ExploreFragment;
import com.cs591.mooncake.like.LikeFragment;
import com.cs591.mooncake.map.MapFragment;
import com.cs591.mooncake.schedule.ScheduleFragment;
import com.cs591.mooncake.profile.ProfileFragment;


import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.HttpURLConnection;
import java.util.Random;
import java.util.TimerTask;

import static android.app.PendingIntent.getActivity;
import static com.cs591.mooncake.profile.ProfileFragment.downloadImage;


public class MainActivity extends AppCompatActivity implements FirebaseProfile.profile,
        ScheduleFragment.OnScheduledEventClikedListener,
        LikeFragment.OnLikedEventClickedListener,
        ExploreFragment.ExploreInteractionListener{


    private FrameLayout mainFrame;
    private BottomNavigationView navigation;

    private ExploreFragment exploreFragment;
    private LikeFragment likeFragment;
    private ScheduleFragment scheduleFragment;
    private MapFragment mapFragment;
    private ProfileFragment profileFragment;
    private FirebaseProfile firebaseProfile;
    private Button closeAds;

    public MySQLiteHelper myDb;
    ImageView adView;




    final private int REQUEST_CODE_ASK_PERMISSIONS = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new MySQLiteHelper(this);


        mainFrame = findViewById(R.id.mainFrame);
        navigation = findViewById(R.id.navigation);
        adView = findViewById(R.id.adView);

        exploreFragment = new ExploreFragment();
        likeFragment = new LikeFragment();
        scheduleFragment = new ScheduleFragment();
        mapFragment = new MapFragment();
        profileFragment = new ProfileFragment();

        setFragment(exploreFragment);

        NaviBarHelper.disableShiftMode(navigation);

        FirebaseInitialize.Initialize(this);
        firebaseProfile = new FirebaseProfile(this);
        firebaseProfile.fetchProfile(this);



        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_explore:
                        setFragment(exploreFragment);
                        return true;
                    case R.id.navigation_like:
                        setFragment(likeFragment);
                        return true;
                    case R.id.navigation_schedule:
                        setFragment(scheduleFragment);
                        return true;
                    case R.id.navigation_map:
                        setFragment(mapFragment);
                        return true;
                    case R.id.navigation_profile:
                        setFragment(profileFragment);
                        return true;
                }
                return false;
            }
        });


        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int i = random.nextInt(5);
                int[] backg = {R.drawable.bufinearts,R.drawable.buglobalprogram,R.drawable.buhumanities,R.drawable.bupardeelogo,R.drawable.wbur_logo};
                String[] link = {"http://www.bu.edu/cfa/","http://www.bu.edu/globalprograms/","http://www.bu.edu/humanities/","http://www.bu.edu/pardeeschool/","http://www.wbur.org/"};
                adView.setImageResource(backg[i]);
                final Uri uri = Uri.parse(link[i]);
                adView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                        getApplicationContext().startActivity(browserIntent);
                    }
                });
                handler.postDelayed(this,30000);
            }
        });

        findViewById(R.id.closeAds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adView.setVisibility(View.GONE);
                findViewById(R.id.closeAds).setVisibility(View.GONE);
            }
        });



    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        fragmentTransaction.commit();

    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        AskPermissions();



    }





    @Override
    public void onScheduledChangedListener() {
        if (scheduleFragment!=null && scheduleFragment.isVisible())
            scheduleFragment.scheduleChangedHandler();
    }





//    private void showAds(){
//        Random random = new Random();
//        int i = random.nextInt(5);
//        int[] backg = {R.drawable.bufinearts,R.drawable.buglobalprogram,R.drawable.buhumanities,R.drawable.bupardeelogo,R.drawable.wbur_logo};
//        String[] link = {"http://www.bu.edu/cfa/","http://www.bu.edu/globalprograms/","http://www.bu.edu/humanities/","http://www.bu.edu/pardeeschool/","http://www.wbur.org/"};
//        adView.setImageResource(backg[i]);
//        final Uri uri = Uri.parse(link[i]);
//        adView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
//                getApplicationContext().startActivity(browserIntent);
//            }
//        });
//
//    }



    //  Check for Permission Request
    private void AskPermissions(){
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();


        if (!addPermission(permissionsList, Manifest.permission.WRITE_CALENDAR))
            permissionsNeeded.add("Write Calendars");
        if (!addPermission(permissionsList, Manifest.permission.READ_CALENDAR))
            permissionsNeeded.add("Read Calendars");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = "You need to grant access to " + permissionsNeeded.get(0) + " and " + permissionsNeeded.get(1) + " to Sync your schedule to Calendars.";

                for (int i = 1; i < permissionsNeeded.size(); i++)
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(MainActivity.this,permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            });
                return;
            }
            ActivityCompat.requestPermissions(MainActivity.this,permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission){
        if (ContextCompat.checkSelfPermission(MainActivity.this,permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,permission))
                return false;
        }
        return true;
    }



    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener){
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel",null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_CONTACTS, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onLikedChangedListener() {
        if (likeFragment!=null && likeFragment.isVisible())
            likeFragment.likeChangedHandler();

    }



    @Override
    public void openLikeEvent(int id) {
        SingleEvent singleEvent = myDb.getEvent(id);
        if (!singleEvent.getType().equals("Bazaar")) {
            Intent intent = new Intent(this, ArtistActivity.class);
            intent.putExtra("artistID", -1);
            intent.putExtra("artistName", singleEvent.getArtist());
            intent.putExtra("highlight", id);
            startActivity(intent);
        }
    }

    @Override
    public void openScheduleEvent(int id) {
        SingleEvent singleEvent = myDb.getEvent(id);
        if (!singleEvent.getType().equals("Bazaar")) {
            Intent intent = new Intent(this, ArtistActivity.class);
            intent.putExtra("artistID", -1);
            intent.putExtra("artistName", singleEvent.getArtist());
            intent.putExtra("highlight", id);
            startActivity(intent);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
