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
import com.cs591.mooncake.SQLite.SingleUser;
import com.cs591.mooncake.explore.ExploreFragment;
import com.cs591.mooncake.like.LikeFragment;
import com.cs591.mooncake.login.LoginActivity;
import com.cs591.mooncake.map.MapFragment;
import com.cs591.mooncake.schedule.ScheduleFragment;
import com.cs591.mooncake.profile.ProfileFragment;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.twitter.sdk.android.core.TwitterCore;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    public static final String REF_PROFILE =  "Profile";
    public static final String REF_SCHEDULED = "Scheduled";
    public static final String REF_LIKED = "Liked";
    public static final String REF_USERNAME = "User Name";
    public static final String REF_EMAIL = "Email";
    public static final String REF_PHONE_NUMBER = "Phone Number";
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
    private DatabaseReference referenceProfile;

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
        btnLogout.setVisibility(View.INVISIBLE);


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

    public void logout(){
        myDb.initProfile();
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
    }


    private void firebaseAuthInitialize() {



        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Log.i("UID", mAuth.getCurrentUser().getUid());
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child(REF_PROFILE);
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Log.i("onDataChange", "Called");
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (snapshot.hasChild(user.getUid())) {
                        Log.i("Firebase User exists", ".");
                        SingleUser singleUser = myDb.getProfile();
                        referenceProfile = FirebaseDatabase.getInstance().
                                getReference().child(REF_PROFILE).child(user.getUid());
                        if (user.getDisplayName() != null)
                            singleUser.setUserName(user.getDisplayName());
                        if (user.getPhotoUrl() != null) {
                            singleUser.setPicUrl(user.getPhotoUrl());
                        }
                        singleUser.setUID(user.getUid());
                        myDb.addProfile(singleUser);
                    } else {
                        createProfile();
                        Log.i("Firebase User not exist", ".");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            FirebaseDatabase.getInstance().getReference()
                    .child(REF_PROFILE)
                    .child(mAuth.getCurrentUser().getUid())
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Log.i("onChildAdded", ".");
                            if (dataSnapshot.getKey().equals(REF_SCHEDULED)) {
                                SingleUser singleUser = myDb.getProfile();
                                singleUser.setScheduledByString(dataSnapshot.getValue().toString());
                                myDb.addProfile(singleUser);
                            } else if (dataSnapshot.getKey().equals(REF_LIKED)) {
                                SingleUser singleUser = myDb.getProfile();
                                singleUser.setLikedByString(dataSnapshot.getValue().toString());
                                myDb.addProfile(singleUser);
                            }

                            Log.i("Scheduled:", myDb.getProfile().getScheduledString());
                            Log.i("Liked", myDb.getProfile().getLikedString());


                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            Log.i("onChildChanged", ".");
                            if (dataSnapshot.getKey().equals(REF_SCHEDULED)) {
                                SingleUser singleUser = myDb.getProfile();
                                singleUser.setScheduledByString(dataSnapshot.getValue().toString());
                                myDb.addProfile(singleUser);
                            } else if (dataSnapshot.getKey().equals(REF_LIKED)) {
                                SingleUser singleUser = myDb.getProfile();
                                singleUser.setLikedByString(dataSnapshot.getValue().toString());
                                myDb.addProfile(singleUser);
                            }

                            Log.i("Scheduled:", myDb.getProfile().getScheduledString());
                            Log.i("Liked", myDb.getProfile().getLikedString());

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }
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

    private void createProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Log.e("CreateFirebaseProfile", "current user doesn't exist");
        } else {
            SingleUser singleUser = myDb.getProfile();
            singleUser.setUID(user.getUid());
            DatabaseReference profile = FirebaseDatabase.getInstance()
                    .getReference().child(REF_PROFILE).child(user.getUid());
            if (user.getDisplayName() != null) {
                singleUser.setUserName(user.getDisplayName());
                profile.child(REF_USERNAME).setValue(user.getDisplayName());
            }
            if (user.getPhotoUrl() != null) {
                singleUser.setPicUrl(user.getPhotoUrl());
            }
            if (user.getEmail() != null) {
                profile.child(REF_EMAIL).setValue(user.getEmail());
            }
            if (user.getPhoneNumber() != null) {
                profile.child(REF_PHONE_NUMBER).setValue(user.getPhoneNumber());
            }

            myDb.addProfile(singleUser);


            updateLikedScheduled(singleUser);

        }
    }

    public void updateLikedScheduled(SingleUser singleUser) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Log.e("CreateFirebaseProfile", "current user doesn't exist");
        } else {
            DatabaseReference profile = FirebaseDatabase.getInstance()
                    .getReference().child(REF_PROFILE).child(user.getUid());
            profile.child(REF_SCHEDULED).setValue(singleUser.getScheduledString());
            profile.child(REF_LIKED).setValue(singleUser.getLikedString());
        }
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
