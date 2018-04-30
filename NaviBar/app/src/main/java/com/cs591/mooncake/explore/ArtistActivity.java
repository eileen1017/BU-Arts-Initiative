package com.cs591.mooncake.explore;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.cs591.mooncake.FirebaseUtils.FirebaseProfile;
import com.cs591.mooncake.MainActivity;
import com.cs591.mooncake.R;
import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.cs591.mooncake.SQLite.SingleArtist;
import com.cs591.mooncake.SQLite.SingleEvent;
import com.cs591.mooncake.SQLite.SingleUser;
import com.cs591.mooncake.map.MapActivity;
import com.cs591.mooncake.schedule.ScheduleFragment;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ArtistActivity extends SwipeBackActivity {

    public static final String DATE_5 = "5";
    public static final String DATE_6 = "6";
    public static final String MONTH = "OCT";
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
        biosView.findViewById(R.id.btnDescriptionCardWebsite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(singleArtist.getWebsite()
                ));
                startActivity(websiteIntent);
            }
        });
        if ((content).length() < 500) {
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
            final SingleEvent singleEvent = myDb.getEvent(event);
            if (singleEvent.getArtist().equals(singleArtist.getName())) {
                View currentView = LayoutInflater.from(this).inflate(R.layout.event_card, null);
                ((TextView)(currentView.findViewById(R.id.event_card_type))).setText(singleEvent.getType());
                String date;
                if (singleEvent.getDate() == 5) {
                    date = DATE_5;
                } else {
                    date = DATE_6;
                }

                // Buttons:
                final SingleUser singleUser = myDb.getProfile();
                ImageButton shareButton = currentView.findViewById(R.id.event_card_share_button);
                ImageButton likeButton = currentView.findViewById(R.id.event_card_like_button);
                ImageButton addButton = currentView.findViewById(R.id.event_card_add_button);
                ImageButton locButton = currentView.findViewById(R.id.event_card_location_button);

                if (singleUser.getScheduled().contains(singleEvent.getID())) {
                    addButton.setImageResource(R.drawable.ic_event_card_added);
                } else {
                    addButton.setImageResource(R.drawable.ic_event_card_add);
                }

                if (singleUser.getLiked().contains(singleEvent.getID())) {
                    likeButton.setImageResource(R.drawable.ic_event_card_liked);
                } else {
                    likeButton.setImageResource(R.drawable.ic_event_card_like);
                }

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SingleUser singleUser = myDb.getProfile();
                        ImageButton b = (ImageButton) view;
                        if (singleUser.getScheduled().contains(singleEvent.getID())) {
                            b.setImageResource(R.drawable.ic_event_card_add);
                            singleUser.removeScheduled(singleEvent.getID());
                            ScheduleFragment.removeCalenderHandler(singleEvent.getID(), ArtistActivity.this);
                        } else {
                            b.setImageResource(R.drawable.ic_event_card_added);
                            singleUser.addScheduled(singleEvent.getID());
                            ScheduleFragment.addToCalenderHandler(singleEvent.getID(),ArtistActivity.this);
                        }
                        myDb.addProfile(singleUser);
                        new FirebaseProfile().updateLikedScheduled(singleUser);
                    }
                });

                shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new ShareUtil(ArtistActivity.this).share(singleEvent.getID());
                    }
                });

                likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SingleUser singleUser = myDb.getProfile();
                        ImageButton b = (ImageButton) view;
                        if (singleUser.getLiked().contains(singleEvent.getID())) {
                            b.setImageResource(R.drawable.ic_event_card_like);
                            singleUser.removeLiked(singleEvent.getID());
                        } else {
                            b.setImageResource(R.drawable.ic_event_card_liked);
                            singleUser.addLiked(singleEvent.getID());
                        }
                        myDb.addProfile(singleUser);
                        new FirebaseProfile().updateLikedScheduled(singleUser);
                    }
                });

                locButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ArtistActivity.this, MapActivity.class);
                        intent.putExtra("eventId", singleEvent.getID());
                        startActivity(intent);
                    }
                });


                // TextViews:
                ((TextView) (currentView.findViewById(R.id.month))).setText(MONTH);
                ((TextView) (currentView.findViewById(R.id.event_card_date))).setText(date);
                String time = singleEvent.getStart();
                if (singleEvent.getEnd() != null) time += (" - " + singleEvent.getEnd());
                ((TextView) (currentView.findViewById(R.id.event_card_time))).setText(time);
                ((TextView) (currentView.findViewById(R.id.event_card_type))).setText(singleEvent.getType());
                ((TextView) (currentView.findViewById(R.id.event_card_location))).setText(singleEvent.getVenue()+", "+singleEvent.getBuilding()+" "+singleEvent.getLevel());
                ((TextView) (currentView.findViewById(R.id.event_card_address))).setText(singleEvent.getAddress());
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

        LinearLayout llArtistEvents = (LinearLayout)findViewById(R.id.llArtistEvents);

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
