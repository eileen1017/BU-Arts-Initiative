package com.cs591.mooncake;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.cs591.mooncake.explore.ExploreFragment;
import com.cs591.mooncake.like.LikeFragment;
import com.cs591.mooncake.map.MapFragment;
import com.cs591.mooncake.schedule.ScheduleFragment;
import com.cs591.mooncake.profile.ProfileFragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.HttpURLConnection;


public class MainActivity extends AppCompatActivity implements FirebaseProfile.profile{


    private FrameLayout mainFrame;
    private BottomNavigationView navigation;

    private ExploreFragment exploreFragment;
    private LikeFragment likeFragment;
    private ScheduleFragment scheduleFragment;
    private MapFragment mapFragment;
    private ProfileFragment profileFragment;
    private FirebaseProfile firebaseProfile;
    private AdView mAdView;
    private AdRequest adRequest;

    private Button closeAds;

    public MySQLiteHelper myDb;
    ImageView imageView;
    String url = "https://www.reka.in/pres/1258167031.jpg";



    final private int REQUEST_CODE_ASK_PERMISSIONS = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        copyDataBaseToPhone();
        myDb = new MySQLiteHelper(this);


        mainFrame = findViewById(R.id.mainFrame);
        navigation = findViewById(R.id.navigation);

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

        findViewById(R.id.closeAds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdView.setVisibility(View.GONE);
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
        findViewById(R.id.adView_bottom).setVisibility(mAdView.GONE);
        mAdView.removeAllViews();
        mAdView.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        insertDummyContactsWrapper();
        MobileAds.initialize(this,
                "ca-app-pub-6996605839799649~8321977534");
        mAdView = (AdView) findViewById(R.id.adView_bottom);
        adRequest = new AdRequest.Builder().addTestDevice("DAE593B5C72B2D6265E20F227970A023")
                .build();
        AdListener listener = new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                mAdView.setVisibility(View.GONE);
                Log.i("TAG", "onAdClosed");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
//                mAdView.loadAd(adRequest);
                Log.i("TAG", "onAdFailedToLoad");
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.i("TAG", "onAdLeftApplication");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.i("TAG", "onAdOpened");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.i("TAG", "onAdLoaded");
            }
        };

        mAdView.setAdListener(listener);
        mAdView.loadAd(adRequest);

//        imageView = (ImageView) findViewById(R.id.profile_image);
//
//
//        loadImageFromUrl(url);



    }
//
//    public Bitmap getBitmapFromURL(String src){
//        java.net.URL url = new java.net.URL(src);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setDoInput(true);
//        connection
//    }
//
//    private void loadImageFromUrl(String url) {
//
//
//        // add to database
//        SingleUser singleUser = myDb.getProfile();
//
//        // download image as Bitmap object as "userPic"
//        singleUser.setPic(userPic);
//
//        myDb.addProfile(singleUser);
//
//
//        // load image
//        SingleUser singleUser1 = myDb.getProfile();
//        Bitmap userPic = singleUser1.getPic();
//
//
//        Picasso.with(this).load(url).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(imageView,new com.squareup.picasso.Callback(){
//
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });

    private void copyDataBaseToPhone() {
        DataBaseUtil util = new DataBaseUtil(this);

        boolean dbExist = util.checkDataBase();

        if (dbExist) {
            Log.i("tag", "The database is exist.");
        } else {
            try {
                util.copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }



    @Override
    public void onScheduledChangedListener() {
        if (scheduleFragment!=null && scheduleFragment.isVisible())
            scheduleFragment.scheduleChangedHandler();
    }




    //  Check for Permission Request
    private void insertDummyContactsWrapper(){
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
}
