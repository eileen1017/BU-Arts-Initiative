package com.cs591.mooncake.like;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.cs591.mooncake.R;
import com.cs591.mooncake.explore.EventActivity;

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EventActivity.class);
                mContext.startActivity(intent);
            }
        });


        holder.likebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                ModelLike itemLabel = mList.get(position);

                String normalText1 = "Are you sure you want to remove ";
                String boldText = itemLabel.getName();
                String normalText2 = "?";

                SpannableString str = new SpannableString(normalText1 + boldText + normalText2);
                str.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), normalText1.length(), normalText1.length() + boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                // Create a dialog box for user to decide whether to unlike an event or not.
                AlertDialog.Builder altUnlike = new AlertDialog.Builder(mContext);
                // The dialog box cannot be canceled by clicking the back button.
                // Set the "Yes" button and then remove the item from the Like page
                altUnlike.setMessage(str).setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,mList.size());

                            }
                        })
                        // close the dialog box when click on "No"
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = altUnlike.create();
                alert.setTitle("");
                alert.show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



}
