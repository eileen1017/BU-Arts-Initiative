package com.cs591.mooncake.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cs591.mooncake.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by yangzhuoshu on 4/12/18.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{
    private Context mContext;

    public CustomInfoWindowAdapter(Context context){
        mContext = context;
        //mWindow = LayoutInflater.from(context).inflate(R.layout.info_window_custom, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_map_info_window, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.place);
        TextView tvSubtitle = (TextView) view.findViewById(R.id.description);

        tvTitle.setText(marker.getTitle());
        tvSubtitle.setText(marker.getSnippet());

        return view;
    }
}
