package com.kumaran.app.androidfirebaseapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by kumaran on 27-10-2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<String> arrayList;
    public RecyclerAdapter(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_event_child,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.event_icon.setText(arrayList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView event_icon;
        public ViewHolder(View itemView) {
            super(itemView);
            event_icon = (TextView)itemView.findViewById(R.id.event_icon_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            TextView event_icon = (TextView)v.findViewById(R.id.event_icon_text);
            Toast.makeText(v.getContext(),event_icon.getText(),Toast.LENGTH_LONG).show();
            String event_icon_string = event_icon.getText().toString();
            if(event_icon_string.equals("Event Lists For You")) {
                Activity activity = (Activity)v.getContext();
                intent = new Intent(activity, event_list_you_list.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            else if(event_icon_string.equals("Custom-Search Events List"))
            {
                Activity activity = (Activity)v.getContext();
                intent = new Intent(v.getContext(), customsearch_eventsList.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            else if(event_icon_string.equals("User Profile"))
            {
                Activity activity = (Activity)v.getContext();
                intent = new Intent(v.getContext(), UserProfile.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            else if(event_icon_string.equals("Friends List & Messages"))
            {
                Activity activity = (Activity)v.getContext();
                intent = new Intent(v.getContext(), friends_list.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            else if(event_icon_string.equals("Heat Map"))
            {
                Activity activity = (Activity)v.getContext();
                intent = new Intent(v.getContext(), HeatMapsActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

        }
    }
}
