package com.cs591.mooncake.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import com.cs591.mooncake.explore.EventActivity;
import com.cs591.mooncake.explore.SingleHorizontal;


import java.util.ArrayList;
import java.util.List;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

    List<Object> data;
    Context context;
    private int lastPosition = -1;

    public HorizontalAdapter(Context context, List<Object> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizental_single_row,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalAdapter.MyViewHolder holder, int position) {
        //Called by RecyclerView to display the data at the specified position. This method should update the contents of the itemView to reflect the item at the given position.

        if (data.get(position) instanceof SingleEvent) {
            final SingleEvent singleEvent = (SingleEvent)data.get(position);
            holder.description.setText(singleEvent.getVenue() + context.getString(R.string.comma) + singleEvent.getBuilding());
            holder.title.setText(singleEvent.getName());
            holder.pubDate.setText(singleEvent.getStart() + context.getString(R.string.dash) + singleEvent.getEnd());
            holder.image.setImageBitmap(singleEvent.getPic());
            if (singleEvent.getType().equals(context.getString(R.string.Workshop))) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ArtistActivity.class);
                        intent.putExtra(context.getString(R.string.workshopOnTop), true);
                        intent.putExtra(context.getString(R.string.artistID), -1);
                        intent.putExtra(context.getString(R.string.highlight), singleEvent.getID());
                        intent.putExtra(context.getString(R.string.artistName), singleEvent.getArtist());
                        context.startActivity(intent);
                    }
                });
            }
        } else if (data.get(position) instanceof SingleArtist){
            final SingleArtist singleArtist = (SingleArtist)data.get(position);
            holder.description.setText(singleArtist.getCountry());
            holder.title.setText(singleArtist.getName());
            holder.pubDate.setText(R.string.ArtistSingle);
            holder.image.setImageBitmap(singleArtist.getPic());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ArtistActivity.class);
                    intent.putExtra(context.getString(R.string.artistID), singleArtist.getId());
                    intent.putExtra(context.getString(R.string.highlight), -1);
                    context.startActivity(intent);
                }
            });
        }
        setAnimation(holder.itemView, position);

    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
        //Get the size of data that you input

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, pubDate;
        ImageView image;

        public MyViewHolder(View itemView) {
            //Set the view
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            description = itemView.findViewById(R.id.description);
            title = itemView.findViewById(R.id.title);
            pubDate = itemView.findViewById(R.id.published_date);
        }
    }

}
