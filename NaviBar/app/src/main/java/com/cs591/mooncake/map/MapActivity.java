package com.cs591.mooncake.map;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cs591.mooncake.R;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class MapActivity extends SwipeBackActivity{
    public MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle extras = getIntent().getExtras();
        int result = extras.getInt("eventId");

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);
        mapFragment.receiveMes(result);
    }

}
