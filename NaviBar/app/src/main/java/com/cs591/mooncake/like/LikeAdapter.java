package com.cs591.mooncake.like;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.cs591.mooncake.R;

import java.util.ArrayList;

/**
 * Created by LinLi on 4/8/18.
 */

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ModelLike> mList;
    LikeAdapter(Context context, ArrayList<ModelLike> list){
        mContext = context;
        mList = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView item_image;
        TextView item_name;
        Button likebtn;

        public ViewHolder(View itemView) {
            super(itemView);

            likebtn = itemView.findViewById(R.id.likebtn);

            item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_name);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View view = layoutInflater.inflate(R.layout.rv_like_items,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {




        ModelLike likeItem = mList.get(position);

        ImageView image = holder.item_image;
        TextView name = holder.item_name;

        image.setImageResource(mList.get(position).getImage());

        name.setText(likeItem.getName());

        holder.likebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ModelLike itemLabel = mList.get(position);
                mList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,mList.size());
                Toast.makeText(mContext, "You have dislike "+ itemLabel.getName(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
