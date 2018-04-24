package com.cs591.mooncake.explore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.cs591.mooncake.R;
import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.cs591.mooncake.SQLite.SingleEvent;
import com.cs591.mooncake.SQLite.SingleUser;

public class EventActivity extends AppCompatActivity {

    MySQLiteHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        myDb = new MySQLiteHelper(this);

        Intent extras = getIntent();
        Bundle bundle = extras.getExtras();
        final int eventID = bundle.getInt("eventID");
        populateUI(myDb.getEvent(eventID));
        findViewById(R.id.btnAddEventSchedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingleUser singleUser = myDb.getProfile();
                if (singleUser.getScheduled().contains(eventID)) {
                    singleUser.addScheduled(eventID);
                } else {
                    singleUser.removeScheduled(eventID);
                }
                myDb.addProfile(singleUser);
            }
        });
    }

    private void populateUI(SingleEvent singleEvent) {
        ImageView ivEvent = findViewById(R.id.ivEvent);
        TextView tvEventType = findViewById(R.id.tvEventType);
        TextView tvEventTime = findViewById(R.id.tvEventTime);
        TextView tvEventArtist = findViewById(R.id.tvEventArtist);
        Toolbar tbEventTitle = findViewById(R.id.tbEventTitle);

        tbEventTitle.setTitle(singleEvent.getName());
        tvEventType.setText(singleEvent.getType());
        ivEvent.setImageBitmap(singleEvent.getPic());
        ivEvent.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String date;
        if (singleEvent.getDate() == 5) {
            date = "10/5 Fri. ";
        } else {
            date = "10/6 Sat. ";
        }

        tvEventTime.setText(date + singleEvent.getStart());
        tvEventArtist.setText(singleEvent.getArtist());

    }






}
