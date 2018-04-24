package com.cs591.mooncake.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.cs591.mooncake.R;
import com.cs591.mooncake.explore.SingleHorizontal;
import com.cs591.mooncake.explore.SingleVertical;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;



public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<List<Object>> items;
    private final int ARTIST = 1;
    private final int PERFORMANCE = 2;
    private final int WORKSHOP = 3;
    private final int BAZAAR = 4;

    private final String[] types = new String[]{"Artists", "Performances", "Workshops", "Bazaar"};


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
        horizontalView((HorizontalViewHolder) holder, position);
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
