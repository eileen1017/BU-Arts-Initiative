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

    private Context mContext;
    private List<Object> mList;
    private int lastPosition = -1;
    private boolean isButtonClicked = false;
    List<Integer> res = new ArrayList<>();
    ScheduleFragment.OnScheduledEventClikedListener oscl;
    private boolean anim = true;



    public scheduleAdapter(Context context, List<Object> list, ScheduleFragment.OnScheduledEventClikedListener oscl, boolean anim){
        this.anim = anim;
        this.oscl = oscl;
        mContext = context;
        mList = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView item_name;
        TextView item_from_time;
        TextView item_location;
        TextView item_at;
        Button item_status;
        TextView item_to_time;
        ImageView item_type;


        public ViewHolder(View itemView) {
            super(itemView);


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
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View view = layoutInflater.inflate(R.layout.rv_schedule_items,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {


        if (mList.get(position) instanceof  SingleEvent) {
            String[] type = {"Workshop","Bazaar","Performance"};
            final SingleEvent singleEvent = (SingleEvent) mList.get(position);

            holder.item_name.setText(singleEvent.getName());
            holder.item_from_time.setText(singleEvent.getStart());
            holder.item_location.setText(singleEvent.getAddress());
            holder.item_at.setText("To");
            holder.item_to_time.setText(singleEvent.getEnd());

            String currentType = singleEvent.getType();

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

            final MySQLiteHelper myDb = new MySQLiteHelper(mContext);
            SingleUser singleUser = myDb.getProfile();
            if (singleUser.getScheduled().contains(singleEvent.getID())) {
               holder.item_status.setBackgroundResource(R.drawable.ischecked);
            } else {
                holder.item_status.setBackgroundResource(R.drawable.add);
            }

            holder.item_status.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    MySQLiteHelper mydb = new MySQLiteHelper(v.getContext());
                    SingleUser singleUser = mydb.getProfile();
                    if (singleUser.getScheduled().contains(singleEvent.getID())) {
                        singleUser.removeScheduled(singleEvent.getID());
                        ScheduleFragment.removeCalenderHandler(singleEvent.getID(), mContext);
                        v.setBackgroundResource(R.drawable.add);
                    } else {
                        v.setBackgroundResource(R.drawable.ischecked);
                        singleUser.addScheduled(singleEvent.getID());
                        ScheduleFragment.addToCalenderHandler(singleEvent.getID(),mContext);
                    }
                    new FirebaseProfile().updateLikedScheduled(singleUser);
                    mydb.addProfile(singleUser);

                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oscl.openScheduleEvent(singleEvent.getID());
                }
            });
        }
        if (anim)
            setAnimation(holder.itemView, position);

    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
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
