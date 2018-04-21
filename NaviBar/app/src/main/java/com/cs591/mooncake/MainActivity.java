package com.cs591.mooncake;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.cs591.mooncake.SQLite.DataBaseUtil;
import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.cs591.mooncake.explore.ExploreFragment;
import com.cs591.mooncake.like.LikeFragment;
import com.cs591.mooncake.login.LoginActivity;
import com.cs591.mooncake.map.MapFragment;
import com.cs591.mooncake.schedule.ScheduleFragment;
import com.cs591.mooncake.profile.ProfileFragment;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.twitter.sdk.android.core.TwitterCore;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private FrameLayout mainFrame;
    private BottomNavigationView navigation;

    private ExploreFragment exploreFragment;
    private LikeFragment likeFragment;
    private ScheduleFragment scheduleFragment;
    private MapFragment mapFragment;
    private ProfileFragment profileFragment;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public MySQLiteHelper myDb;

    private FirebaseAuth mAuth;

    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        firebaseAuthInitialize();

        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                TwitterCore.getInstance().getSessionManager().clearActiveSession();
            }
        });

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

    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        fragmentTransaction.commit();

    }

    private void firebaseAuthInitialize() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Toast.makeText(MainActivity.this, "Logout!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
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


}
