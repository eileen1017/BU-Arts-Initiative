package com.cs591.mooncake.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.cs591.mooncake.R;
import com.cs591.mooncake.explore.SingleHorizontal;
import com.cs591.mooncake.explore.SingleVertical;

import java.util.ArrayList;

import static com.cs591.mooncake.explore.ExploreFragment.getHorizontalData;
import static com.cs591.mooncake.explore.ExploreFragment.getVerticalData;


public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Object> items;
    private final int ARTIST = 1;
    private final int PERFORMANCE = 2;
    private final int WORKSHOP = 3;
    private final int BAZAAR = 4;


    public MainAdapter(Context context, ArrayList<Object> items) {
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
            case ARTIST:
                view = inflater.inflate(R.layout.vertical, parent, false);
                holder = new VerticalViewHolder(view);
                break;
            case PERFORMANCE:
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
        if (holder.getItemViewType() == ARTIST)
            verticalView((VerticalViewHolder) holder);
        else if (holder.getItemViewType() == PERFORMANCE)
            horizontalView((HorizontalViewHolder) holder);
    }

    private void verticalView(VerticalViewHolder holder) {
        VerticalAdapter adapter1 = new VerticalAdapter(getVerticalData());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(adapter1);

    }

    private void horizontalView(HorizontalViewHolder holder) {
        HorizontalAdapter adapter = new HorizontalAdapter(context, getHorizontalData());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(items.get(position) instanceof SingleVertical)
            return ARTIST;
        if(items.get(position) instanceof SingleHorizontal)
            return PERFORMANCE;
        return -1;
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        HorizontalViewHolder(View itemView) {
            super(itemView);
            recyclerView =  itemView.findViewById(R.id.inner_recyclerView);
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
