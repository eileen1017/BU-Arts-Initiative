package com.cs591.mooncake.FirebaseUtils;

import android.content.Context;
import android.util.Log;

import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.cs591.mooncake.SQLite.SingleUser;
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

public class FirebaseProfile {


    private  DatabaseReference referenceProfile;
    private  final String REF_PROFILE =  "Profile";
    private  final String REF_SCHEDULED = "Scheduled";
    private  final String REF_LIKED = "Liked";
    private  final String REF_USERNAME = "User Name";
    private  final String REF_EMAIL = "Email";
    private  final String REF_PHONE_NUMBER = "Phone Number";
    private  FirebaseAuth mAuth;
    private  MySQLiteHelper myDb;


    private profile OCL;
    public interface profile {
        void onScheduledChangedListener();
        void onLikedChangedListener();
    }

    public FirebaseProfile(){
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseProfile(Context context) {
        OCL = (profile)context;
        mAuth = FirebaseAuth.getInstance();
    }



    public void fetchProfile(final Context context) {
        myDb = new MySQLiteHelper(context);

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
                                OCL.onScheduledChangedListener();
                            } else if (dataSnapshot.getKey().equals(REF_LIKED)) {
                                SingleUser singleUser = myDb.getProfile();
                                singleUser.setLikedByString(dataSnapshot.getValue().toString());
                                myDb.addProfile(singleUser);
                                OCL.onLikedChangedListener();
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
                                OCL.onScheduledChangedListener();
                            } else if (dataSnapshot.getKey().equals(REF_LIKED)) {
                                SingleUser singleUser = myDb.getProfile();
                                singleUser.setLikedByString(dataSnapshot.getValue().toString());
                                myDb.addProfile(singleUser);
                                OCL.onLikedChangedListener();
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



    public void logout(Context context){
        myDb.initProfile();
        mAuth.signOut();
        myDb = new MySQLiteHelper(context);
        LoginManager.getInstance().logOut();
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
    }



}
