package com.cs591.mooncake.schedule;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cs591.mooncake.R;
import java.util.ArrayList;



public class scheduleAdapter extends RecyclerView.Adapter<scheduleAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ModelSchedule> mList;

    scheduleAdapter(Context context, ArrayList<ModelSchedule> list){
        mContext = context;
        mList = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView item_name;
        TextView item_description;

        public ViewHolder(View itemView) {
            super(itemView);


            item_name = itemView.findViewById(R.id.item_name);
            item_description = itemView.findViewById(R.id.item_description);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View view = layoutInflater.inflate(R.layout.rv_schedule_items,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {




        ModelSchedule scheduleItem = mList.get(position);


        TextView name = holder.item_name;
        TextView description = holder.item_description;


        name.setText(scheduleItem.getName());
        description.setText(scheduleItem.getDescription());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
