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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.cs591.mooncake.R;
import com.cs591.mooncake.SQLite.SingleArtist;
import com.cs591.mooncake.SQLite.SingleEvent;

import java.util.List;




public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int lastPosition = -1;

    private Context context;
    private List<List<Object>> items;
    private final int BAZAAR = 1;
    private final int OTHER = 2;
    private final int WORKSHOP = 3;

    private boolean[] viewtype;

    private final String[] types = new String[]{"Artists", "Workshops", "Bazaar"};


    public MainAdapter(Context context, List<List<Object>> items) {
        this.viewtype = new boolean[items.size()];
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.

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
        //Called by RecyclerView to display the data at the specified position. This method should update the contents of the itemView to reflect the item at the given position.

        if (holder.getItemViewType() == BAZAAR) {
            singleCardView(holder, position);
        } else
            horizontalView((HorizontalViewHolder) holder, position);
    }

    private void singleCardView(RecyclerView.ViewHolder holder, int position) {
//     CardView uses elevation property on Lollipop for shadows and falls back to a custom emulated shadow implementation on older platforms.
        View itemView = holder.itemView;

        SingleEvent singleEvent = (SingleEvent) items.get(position).get(0);

        ((ImageView)itemView.findViewById(R.id.image_view)).setImageBitmap(singleEvent.getPic());
        ((TextView)itemView.findViewById(R.id.description)).setText(singleEvent.getVenue() + ", " + singleEvent.getBuilding());
        ((TextView)itemView.findViewById(R.id.title)).setText(singleEvent.getName());
        ((TextView)itemView.findViewById(R.id.published_date)).setText(singleEvent.getStart() + " - " + singleEvent.getEnd());
        ((TextView)itemView.findViewById(R.id.type)).setText(types[position]);
    }

    private void verticalView(VerticalViewHolder holder, int position) {
        VerticalAdapter adapter1 = new VerticalAdapter(context, items.get(position));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(adapter1);
        //holder.type.setText(types[position]);

    }


    private void horizontalView(final HorizontalViewHolder holder, final int position) {
//        Layout container for a view hierarchy that can be scrolled by the user, allowing it to be larger than the physical display.
//        A HorizontalScrollView is a FrameLayout, meaning you should place one child in it containing the entire contents to scroll
        HorizontalAdapter adapter = new HorizontalAdapter(context, items.get(position));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
        holder.type.setText(types[position]);
        holder.btnChangeViewType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewtype[position]) {
                    HorizontalAdapter adapter = new HorizontalAdapter(context, items.get(position));
                    holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,
                            LinearLayoutManager.HORIZONTAL, false));
                    holder.recyclerView.setAdapter(adapter);
                    viewtype[position] = false;
                    ImageButton b = (ImageButton)view;
                    b.setImageResource(R.drawable.ic_list_type);
                } else {
                    VerticalAdapter adapter1 = new VerticalAdapter(context, items.get(position));
                    holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    holder.recyclerView.setAdapter(adapter1);
                    viewtype[position] = true;
                    ImageButton b = (ImageButton)view;
                    b.setImageResource(R.drawable.ic_array_type);
                }
            }
        });

        //holder.btnShowAll.setText("Show All " + types[position]);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
//        get the type of an ItemView
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
//        A ViewGroup that shows items in a horizontal scrolling list. The items come from the RecyclerView.Adapter associated with this view.
        RecyclerView recyclerView;
        TextView type;
        Button btnShowAll;
        ImageButton btnChangeViewType;

        HorizontalViewHolder(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
            recyclerView =  itemView.findViewById(R.id.inner_recyclerView);
            btnChangeViewType = itemView.findViewById(R.id.btnChangeViewType);
            //btnShowAll = itemView.findViewById(R.id.btnShowAll);
        }
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder {
//        A ViewGroup that shows items in a Vertical scrolling list. The items come from the RecyclerView.Adapter associated with this view.
        RecyclerView recyclerView;

        VerticalViewHolder(View itemView) {
            super(itemView);
            recyclerView =  itemView.findViewById(R.id.inner_recyclerView);
        }
    }



}
