package com.cs591.mooncake.schedule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs591.mooncake.FirebaseUtils.FirebaseProfile;
import com.cs591.mooncake.MainActivity;
import com.cs591.mooncake.R;
import java.util.ArrayList;
import java.util.List;


import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.cs591.mooncake.SQLite.SingleEvent;
import com.cs591.mooncake.SQLite.SingleUser;
import com.cs591.mooncake.explore.EventActivity;



public class scheduleAdapter extends RecyclerView.Adapter<scheduleAdapter.ViewHolder> {

    private Context mContext;
    private List<Object> mList;
    private boolean isButtonClicked = false;
    List<Integer> res = new ArrayList<>();



    public scheduleAdapter(Context context, List<Object> list){
        mContext = context;
        mList = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView item_name;
        TextView item_from_time;
        TextView item_location;
        TextView item_at;
        Button item_status;


        public ViewHolder(View itemView) {
            super(itemView);


            item_name = itemView.findViewById(R.id.item_name);
            item_from_time = itemView.findViewById(R.id.item_from_time);
            item_location = itemView.findViewById(R.id.item_location);
            item_at = itemView.findViewById(R.id.item_at);
            item_status = itemView.findViewById(R.id.item_status);


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
            final SingleEvent singleEvent = (SingleEvent) mList.get(position);
            holder.item_name.setText(singleEvent.getName());
            holder.item_from_time.setText(singleEvent.getStart());
            holder.item_location.setText(singleEvent.getAddress());
            holder.item_at.setText("@");

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
                        v.setBackgroundResource(R.drawable.add);
                    } else {
                        v.setBackgroundResource(R.drawable.ischecked);
                        singleUser.addScheduled(singleEvent.getID());
                    }
                    new FirebaseProfile().updateLikedScheduled(singleUser);
                    mydb.addProfile(singleUser);

                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,EventActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }







    }

    @Override
    public int getItemCount() {
        return mList.size();
    }




}
