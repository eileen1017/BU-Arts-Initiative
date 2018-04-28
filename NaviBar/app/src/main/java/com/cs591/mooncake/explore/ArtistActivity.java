package com.cs591.mooncake.explore;

import android.content.Intent;
import android.media.Image;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.cs591.mooncake.R;
import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.cs591.mooncake.SQLite.SingleArtist;
import com.cs591.mooncake.SQLite.SingleEvent;

import java.util.ArrayList;
import java.util.List;

public class ArtistActivity extends AppCompatActivity {

    public static final String DATE_5 = "5th, Friday";
    public static final String DATE_6 = "6th, Saturday";
    private boolean expanded;
    MySQLiteHelper myDb;
    SingleArtist singleArtist;
    private int highlight = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        myDb = new MySQLiteHelper(this);
        expanded = false;

        int artistID = extras.getInt("artistID");
        if (artistID != -1) {
            Log.i("Workshop artist id", "" + artistID);
            singleArtist = myDb.getArtist(artistID);
        } else {
            String artistName = extras.getString("artistName");
            Log.i("Workshop artist name", artistName);
            for (int i : myDb.getArtistList()) {
                if (myDb.getArtist(i).getName().equals(artistName)) {
                    singleArtist = myDb.getArtist(i);
                    break;
                }
            }
            highlight = extras.getInt("highlight");
        }

        boolean workshopAtTop = extras.getBoolean("workshopOnTop");

        generateContent(workshopAtTop);
    }

    private void generateContent(boolean workshopAtTop) {

        ((ImageView)(findViewById(R.id.artist_page_image))).setImageBitmap(singleArtist.getPic());
        ((Toolbar)(findViewById(R.id.artist_page_title))).setTitle(singleArtist.getName());
        View highlightView = null;
        // generate bios
        final View biosView = LayoutInflater.from(this).inflate(R.layout.description_card, null);
        final String content = singleArtist.getBios();
        if (content.length() < 500) {
            ((biosView.findViewById(R.id.description_card_collapse_layout))).setVisibility(View.INVISIBLE);
            ((TextView)(biosView.findViewById(R.id.description_card_text))).setText(content);
        } else {
            ((TextView)(biosView.findViewById(R.id.description_card_text))).setText(content.substring(0, 500) + " ...");
            (biosView.findViewById(R.id.description_card_button)).setBackgroundResource(R.drawable.ic_description_card_expand);
            (biosView.findViewById(R.id.description_card_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changingBiosContent(biosView, content);
                }
            });
            ((biosView.findViewById(R.id.description_card_collapse_layout))).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changingBiosContent(biosView, content);
                }
            });
        }


        // generate Performances and workshops
        List<View> performanceViews = new ArrayList<>();
        List<View> workshopViews = new ArrayList<>();

        List<Integer> events = myDb.getEventList();

        for (Integer event : events) {
            SingleEvent singleEvent = myDb.getEvent(event);
            if (singleEvent.getArtist().equals(singleArtist.getName())) {
                View currentView = LayoutInflater.from(this).inflate(R.layout.event_card, null);
                ((TextView)(currentView.findViewById(R.id.event_card_type))).setText(singleEvent.getType());
                String date;
                if (singleEvent.getDate() == 5) {
                    date = DATE_5;
                } else {
                    date = DATE_6;
                }

                ((TextView) (currentView.findViewById(R.id.event_card_date))).setText(date);
                String time = singleEvent.getStart();
                if (singleEvent.getEnd() != null) time += (" : " + singleEvent.getEnd());
                ((TextView) (currentView.findViewById(R.id.event_card_time))).setText(time);
                ((TextView) (currentView.findViewById(R.id.event_card_type))).setText(singleEvent.getType());
                if (singleEvent.getID() == highlight) {
                    highlightView = currentView;
                    ((currentView.findViewById(R.id.event_card_highlight)))
                            .setVisibility(View.VISIBLE);
                } else {
                    if (singleEvent.getType().equals("Workshop")) {
                        //((TextView) (workshopView.findViewById(R.id.event_card_description))).setText();
                        workshopViews.add(currentView);
                    } else {
                        ((TextView) (currentView.findViewById(R.id.event_card_description))).setText("");
                        ((TextView) (currentView.findViewById(R.id.event_card_description))).setVisibility(View.GONE);
                        performanceViews.add(currentView);
                    }
                }

            }
        }

        LinearLayout llArtistEvents = findViewById(R.id.llArtistEvents);

        if (highlight != -1) {
            llArtistEvents.addView(highlightView);
        }
        if (workshopAtTop) {
            for (View v : workshopViews) {
                llArtistEvents.addView(v);
            }
            llArtistEvents.addView(biosView);
            for (View v : performanceViews) {
                llArtistEvents.addView(v);
            }
        } else {
            llArtistEvents.addView(biosView);
            for (View v : performanceViews) {
                llArtistEvents.addView(v);
            }
            for (View v : workshopViews) {
                llArtistEvents.addView(v);
            }
        }

    }

    public void changingBiosContent(View view, String content) {

        if (expanded) {
            view.findViewById(R.id.description_card_button).setBackgroundResource(R.drawable.ic_description_card_expand);
            ((TextView)(view.findViewById(R.id.description_card_text))).setText(content.substring(0, 500) + " ...");
            expanded = false;
        } else {
            view.findViewById(R.id.description_card_button).setBackgroundResource(R.drawable.ic_description_card_collapse);
            ((TextView)(view.findViewById(R.id.description_card_text))).setText(content);
            expanded = true;
        }
    }

}
