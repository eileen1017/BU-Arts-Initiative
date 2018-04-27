package com.cs591.mooncake.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.cs591.mooncake.R;
import com.cs591.mooncake.SQLite.SingleArtist;
import com.cs591.mooncake.SQLite.SingleEvent;

import java.util.List;



public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<List<Object>> items;
    private final int BAZAAR = 1;
    private final int OTHER = 2;
    private final int WORKSHOP = 3;

    private final String[] types = new String[]{"Artists", "Workshops", "Bazaar"};


    public MainAdapter(Context context, List<List<Object>> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case BAZAAR:
                view = inflater.inflate(R.layout.bazaar, parent, false);
                holder = new VerticalViewHolder(view);
                break;
            case OTHER:
                view = inflater.inflate(R.layout.horizontal, parent, false);
                holder = new HorizontalViewHolder(view);
                break;
            default:
                view = inflater.inflate(R.layout.horizontal, parent, false);
                holder = new HorizontalViewHolder(view);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == BAZAAR) {
            singleCardView(holder, position);
        } else
            horizontalView((HorizontalViewHolder) holder, position);
    }

    private void singleCardView(RecyclerView.ViewHolder holder, int position) {
        View itemView = holder.itemView;

        SingleEvent singleEvent = (SingleEvent) items.get(position).get(0);

        ((ImageView)itemView.findViewById(R.id.image_view)).setImageBitmap(singleEvent.getPic());
        ((TextView)itemView.findViewById(R.id.description)).setText(singleEvent.getAddress());
        ((TextView)itemView.findViewById(R.id.title)).setText(singleEvent.getName());
        ((TextView)itemView.findViewById(R.id.published_date)).setText(singleEvent.getStart());
        ((TextView)itemView.findViewById(R.id.type)).setText(types[position]);
    }

    private void horizontalView(HorizontalViewHolder holder, int position) {
        HorizontalAdapter adapter = new HorizontalAdapter(context, items.get(position));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
        holder.type.setText(types[position]);
        //holder.btnShowAll.setText("Show All " + types[position]);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).get(0) instanceof SingleArtist) {
            return 2;
        }
        if (items.get(position).get(0) instanceof SingleEvent) {

            if (((SingleEvent)(items.get(position).get(0))).getType().equals("Bazaar")) {
                return 1;
            } else {
                return 2;
            }
        }
        return 2;
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView type;
        Button btnShowAll;

        HorizontalViewHolder(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
            recyclerView =  itemView.findViewById(R.id.inner_recyclerView);
            //btnShowAll = itemView.findViewById(R.id.btnShowAll);
        }
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        VerticalViewHolder(View itemView) {
            super(itemView);
            recyclerView =  itemView.findViewById(R.id.inner_recyclerView);
        }
    }


}
