package com.cs591.mooncake.profile;

/**
 * Created by LinLi on 4/17/18.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs591.mooncake.R;
import com.cs591.mooncake.explore.ArtistActivity;
import com.cs591.mooncake.explore.ShareUtil;


public class CustomAdapter extends ArrayAdapter<String> {

    //  field for initialization
    String [] names;
    int [] icons;
    Context mContext;
    private int lastPosition = -1;

    //  initialize adapter for profile page
    public CustomAdapter(Context context, int[] icons, String[] names){
        super(context,R.layout.customlist);
        this.icons = icons;
        this.names = names;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // utilize the share function in ShareUtil under explore folder
        final ShareUtil share = new ShareUtil(mContext);
        //  called to get view when click on item in the ListView
        ViewHolder mViewHolder = new ViewHolder();
        if(convertView == null) {
            //  Inflate the layout and set layout
            LayoutInflater mInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflator.inflate(R.layout.customlist, parent, false);

            //  switch cases when click on specific item in Listview
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(position) {
                        case 0:
                            //  called when click on Website and lead to BU global Music Festival website
                            Intent websiteIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(mContext.getString(R.string.gmfUrl)));
                            getContext().startActivity(websiteIntent);
                            break;
                        case 1:
                            //  called when click on Invite Friend and share app to other social media
                            share.inviteFriend();
                            break;
                        case 2:
                            //  called when click on Feedback and open BU global festival survey link
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mContext.getString(R.string.surveymonkey)));
                            getContext().startActivity(browserIntent);
                            break;
                        case 3:
                            //  called when click on About and see the information about Global Music Festival
                            Intent aboutIntent = new Intent(getContext(),AboutPage.class);
                            getContext().startActivity(aboutIntent);
                            break;
                        case 4:
                            //  called when click on Ticket and buy ticket in TicketMaster website
                            Intent browserTic = new Intent(Intent.ACTION_VIEW, Uri.parse(mContext.getString(R.string.concert)));
                            getContext().startActivity(browserTic);
                            break;

                        default:
                            Log.i("onClick", "");
                            break;
                    }
                }
            });
            //  initialize and set viewHolder
            mViewHolder.mIcon = (ImageView) convertView.findViewById(R.id.item_icon);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.item_title);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
            //  set holder's information with specific position icons and names
            mViewHolder.mIcon.setImageResource(icons[position]);
            mViewHolder.mName.setText(names[position]);

            //  set animation
        setAnimation(convertView, position);
        return convertView;
    }

    static class ViewHolder{
        ImageView mIcon;
        TextView mName;
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            //  the ListView slide in from left side one by one from top to down
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            animation.setDuration(400 + position * 130);
            lastPosition = position;
        }
    }

}
