package com.cs591.mooncake.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs591.mooncake.R;
import com.cs591.mooncake.explore.EventActivity;
import com.cs591.mooncake.explore.SingleHorizontal;


import java.util.ArrayList;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

    ArrayList<SingleHorizontal> data;
    Context context;

    public HorizontalAdapter(Context context, ArrayList<SingleHorizontal> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizental_single_row,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalAdapter.MyViewHolder holder, int position) {
        holder.description.setText(data.get(position).getDesc());
        holder.title.setText(data.get(position).getTitle());
        holder.pubDate.setText(data.get(position).getPubDate());
        holder.image.setImageResource(data.get(position).getImagess());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, pubDate;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            description = itemView.findViewById(R.id.description);
            title = itemView.findViewById(R.id.title);
            pubDate = itemView.findViewById(R.id.published_date);
        }
    }
}
