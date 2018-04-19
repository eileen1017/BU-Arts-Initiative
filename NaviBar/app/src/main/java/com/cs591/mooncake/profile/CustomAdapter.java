package com.cs591.mooncake.profile;

/**
 * Created by LinLi on 4/17/18.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs591.mooncake.R;


public class CustomAdapter extends ArrayAdapter<String> {

    String [] names;
    int [] icons;
    Context mContext;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if(convertView == null) {
            LayoutInflater mInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflator.inflate(R.layout.customlist, parent, false);
            mViewHolder.mIcon = (ImageView) convertView.findViewById(R.id.item_icon);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.item_title);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

            mViewHolder.mIcon.setImageResource(icons[position]);
            mViewHolder.mName.setText(names[position]);

        return convertView;
    }

    static class ViewHolder{
        ImageView mIcon;
        TextView mName;
    }
}
