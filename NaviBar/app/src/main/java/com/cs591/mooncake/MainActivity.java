package com.cs591.mooncake;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

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

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements FirebaseProfile.profile{


    private FrameLayout mainFrame;
    private BottomNavigationView navigation;

    private ExploreFragment exploreFragment;
    private LikeFragment likeFragment;
    private ScheduleFragment scheduleFragment;
    private MapFragment mapFragment;
    private ProfileFragment profileFragment;
    private FirebaseProfile firebaseProfile;

    public MySQLiteHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        copyDataBaseToPhone();
        myDb = new MySQLiteHelper(this);

        AdView mAdView = (AdView) findViewById(R.id.adView_bottom);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

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

        AdListener listener = new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.i("TAG", "onAdClosed");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
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

    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        fragmentTransaction.commit();

    }








    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseInitialize.Initialize(this);
    }

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

    @Override
    public void onLikedChangedListener() {
        if (likeFragment!=null && likeFragment.isVisible())
            likeFragment.likeChangedHandler();
    }
}
