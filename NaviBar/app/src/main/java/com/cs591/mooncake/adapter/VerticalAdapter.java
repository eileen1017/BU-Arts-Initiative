package com.cs591.mooncake.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs591.mooncake.R;
import com.cs591.mooncake.SQLite.SingleArtist;
import com.cs591.mooncake.SQLite.SingleEvent;
import com.cs591.mooncake.explore.ArtistActivity;
import com.cs591.mooncake.explore.SingleVertical;

import java.util.ArrayList;
import java.util.List;

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.MyViewHolder> {

    public static final String TAG = "Explore";
    private List<Object> data;
    Context context;
    private int lastPosition = -1;

    public VerticalAdapter(Context context, List<Object> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_single_row,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (data.get(position) instanceof SingleEvent) {
            final SingleEvent singleEvent = (SingleEvent)data.get(position);
            holder.description.setText(singleEvent.getAddress());
            holder.title.setText(singleEvent.getName());
            holder.image.setImageBitmap(singleEvent.getPic());
            if (singleEvent.getType().equals("Workshop")) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ArtistActivity.class);
                        intent.putExtra("workshopOnTop", true);
                        intent.putExtra("artistID", -1);
                        intent.putExtra("highlight", singleEvent.getID());
                        intent.putExtra("artistName", singleEvent.getArtist());
                        context.startActivity(intent);
                    }
                });
            }
        } else if (data.get(position) instanceof SingleArtist){
            final SingleArtist singleArtist = (SingleArtist)data.get(position);
            holder.description.setText(singleArtist.getCountry());
            holder.title.setText(singleArtist.getName());
            holder.image.setImageBitmap(singleArtist.getPic());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ArtistActivity.class);
                    intent.putExtra("artistID", singleArtist.getId());
                    intent.putExtra("highlight", -1);
                    context.startActivity(intent);
                }
            });
        }
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, description;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            Log.i(TAG, ""+title);
        }
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


}
