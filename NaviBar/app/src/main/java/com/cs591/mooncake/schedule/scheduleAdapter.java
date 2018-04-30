package com.cs591.mooncake.schedule;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs591.mooncake.FirebaseUtils.FirebaseProfile;
import com.cs591.mooncake.R;
import java.util.ArrayList;
import java.util.List;


import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.cs591.mooncake.SQLite.SingleEvent;
import com.cs591.mooncake.SQLite.SingleUser;


public class scheduleAdapter extends RecyclerView.Adapter<scheduleAdapter.ViewHolder> {

    //  field for initialization
    private Context mContext;
    private List<Object> mList;
    private int lastPosition = -1;
    ScheduleFragment.OnScheduledEventClikedListener oscl;
    private boolean anim = true;


    //  initialization of scheduleAdaptor
    public scheduleAdapter(Context context, List<Object> list, ScheduleFragment.OnScheduledEventClikedListener oscl, boolean anim){
        this.anim = anim;
        this.oscl = oscl;
        mContext = context;
        mList = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        // fields for initialization on rv_schedule_items.xml
        TextView item_name;
        TextView item_from_time;
        TextView item_location;
        TextView item_at;
        Button item_status;
        TextView item_to_time;
        ImageView item_type;


        public ViewHolder(View itemView) {
            super(itemView);

            //  give the references for TextViews, Button and ImageView
            item_name = itemView.findViewById(R.id.item_name);
            item_from_time = itemView.findViewById(R.id.item_from_time);
            item_location = itemView.findViewById(R.id.item_location);
            item_at = itemView.findViewById(R.id.item_at);
            item_status = itemView.findViewById(R.id.item_status);
            item_to_time = itemView.findViewById(R.id.item_to_time);
            item_type = itemView.findViewById(R.id.item_type);


        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //  Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
        //  Set up viewHolder
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View view = layoutInflater.inflate(R.layout.rv_schedule_items,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        //  Called to display the data at the specified position in RecyclerView.
        //  This method should update the contents of the itemView to reflect the item at the given position.

        if (mList.get(position) instanceof  SingleEvent) {

            //  Connection to SingleEvent database
            final SingleEvent singleEvent = (SingleEvent) mList.get(position);

            //  Set text from database to cardView holder
            holder.item_name.setText(singleEvent.getName());
            holder.item_from_time.setText(singleEvent.getStart());
            holder.item_location.setText(singleEvent.getVenue()+", "+singleEvent.getBuilding()+" "+singleEvent.getLevel());
            holder.item_at.setText("To");
            holder.item_to_time.setText(singleEvent.getEnd());

            //  Looking for the type of the event
            String currentType = singleEvent.getType();

            // Determine which type of the event is and set the image of item_type holder
            switch(currentType){
                case "Workshop":
                    holder.item_type.setImageResource(R.drawable.workshop);
                    break;
                case "Bazaar":
                    holder.item_type.setImageResource(R.drawable.bazzar);
                    break;
                case "Performance":
                    holder.item_type.setImageResource(R.drawable.performance);
                    break;

            }

            //  get reference to SQLite DB
            final MySQLiteHelper myDb = new MySQLiteHelper(mContext);

            //  get profile information of a singleUser database
            SingleUser singleUser = myDb.getProfile();

            // processed if the user has added event to their own schedule
            if (singleUser.getScheduled().contains(singleEvent.getID())) {
               holder.item_status.setBackgroundResource(R.drawable.ischecked);                  //  change the icon of current holder.item_status
            } else {
                holder.item_status.setBackgroundResource(R.drawable.add);                       //  Else, set the icon as R.drawable.add
            }

            //  Called when user clicks on item_status
            holder.item_status.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //  get reference to SQLite DB
                    MySQLiteHelper mydb = new MySQLiteHelper(v.getContext());

                    //  get profile information of a singleUser database
                    SingleUser singleUser = mydb.getProfile();

                    // processed if the user has added event to their own schedule
                    if (singleUser.getScheduled().contains(singleEvent.getID())) {
                        //  remove the scheduled event from database, calendar and set icon back to R.drawable.add
                        singleUser.removeScheduled(singleEvent.getID());
                        ScheduleFragment.removeCalenderHandler(singleEvent.getID(), mContext);
                        v.setBackgroundResource(R.drawable.add);
                    } else {

                        //  Else, add the scheduled event to database, calendar and set icon to R.drawable.ischecked
                        v.setBackgroundResource(R.drawable.ischecked);
                        singleUser.addScheduled(singleEvent.getID());
                        ScheduleFragment.addToCalenderHandler(singleEvent.getID(),mContext);
                    }

                    //  update Firebase Profile
                    new FirebaseProfile().updateLikedScheduled(singleUser);

                    // update SQLite Profile
                    mydb.addProfile(singleUser);

                }
            });

            //  called when user clicks on the holder, it will start event activity
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oscl.openScheduleEvent(singleEvent.getID());
                }
            });
        }
        if (anim)
            //  called animation
            setAnimation(holder.itemView, position);

    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            //  the cardView slide in from left and one by one from top to down
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            animation.setDuration(400 + position * 130);
            lastPosition = position;
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }




}
