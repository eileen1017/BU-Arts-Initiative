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

import com.cs591.mooncake.FirebaseUtils.FirebaseProfile;
import com.cs591.mooncake.R;
import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.cs591.mooncake.SQLite.SingleEvent;
import com.cs591.mooncake.SQLite.SingleUser;
import com.cs591.mooncake.explore.EventActivity;
import java.util.List;

/**
 * Created by LinLi on 4/8/18.
 */

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder> {

    private Context mContext;
    private List<Object> mList;

    LikeAdapter(Context context, List<Object> list){
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


        final SingleEvent singleEvent = (SingleEvent) mList.get(position);


        holder.item_name.setText(singleEvent.getName());
        holder.item_image.setImageBitmap(singleEvent.getPic());

        // go to the specific event page when click on the liked item
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

                final MySQLiteHelper mydb = new MySQLiteHelper(view.getContext());
               // final SingleUser singleUser = mydb.getProfile();

                String normalText1 = mContext.getString(R.string.unlike_check);
                String boldText = singleEvent.getName();
                String normalText2 = mContext.getString(R.string.question_mark);

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
                                // remove the event from the user database
                                SingleUser singleUser = mydb.getProfile();
                                singleUser.removeLiked(singleEvent.getID());
                                // update the local database
                                mydb.addProfile(singleUser);

                                // update Firebase real time data
                                new FirebaseProfile().updateLikedScheduled(singleUser);
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
