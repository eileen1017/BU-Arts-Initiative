package com.cs591.mooncake.map;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cs591.mooncake.R;

public class MapActivity extends AppCompatActivity {
    public MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle extras = getIntent().getExtras();
        int result = extras.getInt("eventId");
        mapFragment = new MapFragment();

        //mapFragment.currentEventID = 5;
//        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.mainFrame, mapFragment);
//        fragmentTransaction.commit();
    }
}
