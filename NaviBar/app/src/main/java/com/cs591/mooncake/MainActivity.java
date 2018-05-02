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
import com.cs591.mooncake.map.MapActivity;
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


        //  Adding an advertisement banner at the bottom of main page using a Handler to execute runnables
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Create an random int and initialize the sponsors in the arrays
                Random random = new Random();
                int i = random.nextInt(5);
                int[] backg = {R.drawable.bufinearts,R.drawable.buglobalprogram,R.drawable.buhumanities,R.drawable.bupardeelogo,R.drawable.wbur_logo};
                String[] link = {getString(R.string.cfa),getString(R.string.global),getString(R.string.human),getString(R.string.school),getString(R.string.wbur)};
                // Set the image background of the random generated int and parse uri
                adView.setImageResource(backg[i]);
                final Uri uri = Uri.parse(link[i]);
                // called when clicks on the ads
                adView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // lead users to the website by using intent
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                        getApplicationContext().startActivity(browserIntent);
                    }
                });
                //  Ads will be refreshed every 30 seconds
                handler.postDelayed(this,30000);
            }
        });

        //  when press on close button, ads will disappear. Unless user restart the app, the ads will not show again
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



    //  Check for Permission Request
    private void AskPermissions(){
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();

        // Check the permission for Calendar usage
        if (!addPermission(permissionsList, Manifest.permission.WRITE_CALENDAR))
            permissionsNeeded.add(getString(R.string.write));
        if (!addPermission(permissionsList, Manifest.permission.READ_CALENDAR))
            permissionsNeeded.add(getString(R.string.read));

        //  Add an explanation for calendar permission to users
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = getString(R.string.grant) + permissionsNeeded.get(0) + getString(R.string.and) + permissionsNeeded.get(1) + getString(R.string.sync);

                for (int i = 1; i < permissionsNeeded.size(); i++)
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // ask permission
                                    ActivityCompat.requestPermissions(MainActivity.this,permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            });
                return;
            }
            //This is called if user has denied the permission before
            //In this case I am just asking the permission again
            ActivityCompat.requestPermissions(MainActivity.this,permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission){
        if (ContextCompat.checkSelfPermission(MainActivity.this,permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // if permission not granted, ask permissions
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,permission))
                return false;
        }
        return true;
    }


    // Creating a dialogInterface for user for the explanation.
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener){
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton(R.string.OK, okListener)
                .setNegativeButton(R.string.cancel,null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.READ_CALENDAR, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_CALENDAR, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, R.string.some, Toast.LENGTH_SHORT)
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
        if (!singleEvent.getType().equals(getString(R.string.Bazaar))) {
            Intent intent = new Intent(this, ArtistActivity.class);
            intent.putExtra(getString(R.string.artistID), -1);
            intent.putExtra(getString(R.string.artistName), singleEvent.getArtist());
            intent.putExtra(getString(R.string.highlight), id);
            startActivity(intent);
        }
    }

    @Override
    public void openScheduleEvent(int id) {
        SingleEvent singleEvent = myDb.getEvent(id);
        if (!singleEvent.getType().equals(getString(R.string.Bazaar))) {
            Intent intent = new Intent(this, ArtistActivity.class);
            intent.putExtra(getString(R.string.artistID), -1);
            intent.putExtra(getString(R.string.artistName), singleEvent.getArtist());
            intent.putExtra(getString(R.string.highlight), id);
            startActivity(intent);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
