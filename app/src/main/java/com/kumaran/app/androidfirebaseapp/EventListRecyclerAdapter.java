package com.kumaran.app.androidfirebaseapp;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by kumaran on 28-10-2016.
 */
public class EventListRecyclerAdapter extends RecyclerView.Adapter<EventListRecyclerAdapter.ViewHolder> {



    private int[] event_Image = {
            R.drawable.sample_1,
            R.drawable.sample_2,
            R.drawable.sample_3,
            R.drawable.sample_4,
            R.drawable.sample_5,
    };

    HashMap<String,ArrayList<Object>> eventValuesHmForYou = new HashMap<>();
    ArrayList<String> event_key_values = new ArrayList<>();
    ArrayList<Object> event_values = new ArrayList<>();
    ArrayList<String> e_address = new ArrayList<>();
    ArrayList<Long> e_capacity = new ArrayList<>();
    ArrayList<String> e_category = new ArrayList<>();
    ArrayList<String> e_date = new ArrayList<>();
    ArrayList<String> e_image = new ArrayList<>();
    ArrayList<String> e_location = new ArrayList<>();
    ArrayList<String> e_name = new ArrayList<>();
    ArrayList<String> e_price = new ArrayList<>();
    ArrayList<Long> e_rate = new ArrayList<>();
    ArrayList<Long> e_ticket_available = new ArrayList<>();
    ArrayList<Long> e_ticket_sold = new ArrayList<>();
    ArrayList<String> e_time = new ArrayList<>();
    ArrayList<String> e_venue = new ArrayList<>();
    ArrayList<Long> eventID = new ArrayList<>();
    ArrayList<String> e_dummy = new ArrayList<>();


/*    private String[] event_date = {
            "21/10/2016",
            "22/10/2016",
            "23/10/2016",
            "24/10/2016",
            "25/10/2016",
    };*/

 /*   private String[] event_title = {
            "Holi",
            "Diwali",
            "Ramzan",
            "Onam",
            "Christmas"
    };*/
 /*   private String[] event_place_city = {
            "Mumbai",
            "Chennai",
            "Hyderabad",
            "Cochin",
            "Bangalore"
    };*/
/*    private String[] category_name = {
            "Festival",
            "Festival",
            "Festival",
            "Festival",
            "Festival"
    };*/

    class ViewHolder extends RecyclerView.ViewHolder
    {

        public ImageView event_image;
        public TextView event_city;
        public TextView category_name;
        public TextView event_title;
        public TextView event_date;
        public TextView event_ticket_cost;
        public TextView event_ticket_available;
        public TextView event_ID;

        public ViewHolder(View itemView) {

            super(itemView);
            System.out.println("check1");
            System.out.println("ViewHolder");
            event_image = (ImageView)itemView.findViewById(R.id.events_image);
            event_city = (TextView)itemView.findViewById(R.id.e_location);
            category_name = (TextView)itemView.findViewById(R.id.category_name_text);
            event_title = (TextView)itemView.findViewById(R.id.event_title_text);
            event_date = (TextView)itemView.findViewById(R.id.event_date_text);
            event_ticket_cost = (TextView)itemView.findViewById(R.id.ticket_cost_text);
            event_ticket_available = (TextView)itemView.findViewById(R.id.tickets_available_text);
            event_ID = (TextView) itemView.findViewById(R.id.event_ID);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("eventId for registration:"+event_ID.getText().toString());
                    Intent intent;
                    intent = new Intent(v.getContext(), event_description.class);
                    String event_register_id = event_ID.getText().toString();
                    intent.putExtra("event_ID register",event_register_id);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("check2");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.eventlist_cardlayout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        System.out.println("check3");
        HashMap<String,ArrayList<Object>> eventValuesHmForYou = new HashMap<>();
        event_key_values = new ArrayList<>();
        event_values = new ArrayList<>();
        e_address = new ArrayList<>();
        e_capacity = new ArrayList<>();
        e_category = new ArrayList<>();
        e_date = new ArrayList<>();
        e_image = new ArrayList<>();
        e_location = new ArrayList<>();
        e_name = new ArrayList<>();
        e_price = new ArrayList<>();
        e_rate = new ArrayList<>();
        e_ticket_available = new ArrayList<>();
        e_ticket_sold = new ArrayList<>();
        e_time = new ArrayList<>();
        e_venue = new ArrayList<>();
        eventID = new ArrayList<>();
        int i=0;

        FireApp fireApp = (FireApp)getApplicationContext();
        eventValuesHmForYou = fireApp.getEventValuesHmForYou();
        event_key_values = fireApp.getEvent_key_name();

        System.out.println("eventValuesHmForYou:"+eventValuesHmForYou);

        for(Map.Entry<String, ArrayList<Object>> entry: eventValuesHmForYou.entrySet())
        {
            event_values = entry.getValue();
            for(i=0;i<event_key_values.size();i++) {
                if (event_key_values.get(i).equals("e_address"))
                {

                    System.out.println("going");
                    System.out.println(event_key_values.get(i));
                    System.out.println(event_values.get(i));
                    e_address.add((String)event_values.get(i));
                    System.out.println("e_address:"+e_address);
                }
                else if (event_key_values.get(i).equals("e_capacity"))
                {
                    System.out.println("going");
                    System.out.println(event_key_values.get(i));
                    System.out.println(event_values.get(i));
                    e_capacity.add((Long) event_values.get(i));
                    System.out.println("e_capacity:"+e_capacity);
                }
                else if (event_key_values.get(i).equals("e_category"))
                {
                    System.out.println("going");
                    System.out.println(event_key_values.get(i));
                    System.out.println(event_values.get(i));
                    e_category.add((String)event_values.get(i));
                    System.out.println("e_category:"+e_category);
                }
                else if (event_key_values.get(i).equals("e_date"))
                {
                    System.out.println("going");
                    System.out.println(event_key_values.get(i));
                    System.out.println(event_values.get(i));
                    e_date.add((String)event_values.get(i));
                    System.out.println("e_date:"+e_date);
                }
                else if (event_key_values.get(i).equals("e_image"))
                {
                    System.out.println("going");
                    System.out.println(event_key_values.get(i));
                    System.out.println(event_values.get(i));
                    e_image.add((String)event_values.get(i));
                    System.out.println("e_image:"+e_image);
                }
                else if (event_key_values.get(i).equals("e_location"))
                {
                    System.out.println("going");
                    System.out.println(event_key_values.get(i));
                    System.out.println(event_values.get(i));
                    e_location.add((String)event_values.get(i));
                    System.out.println("e_location:"+e_location);
                }
                else if (event_key_values.get(i).equals("e_name"))
                {
                    System.out.println("going");
                    System.out.println(event_key_values.get(i));
                    System.out.println(event_values.get(i));
                    e_name.add((String)event_values.get(i));
                    System.out.println("e_name:"+e_name);
                }
                else if (event_key_values.get(i).equals("e_price"))
                {
                    System.out.println("going");
                    System.out.println(event_key_values.get(i));
                    System.out.println(event_values.get(i));
                    e_price.add((String)event_values.get(i));
                    System.out.println("e_price:"+e_price);
                }
                else if (event_key_values.get(i).equals("e_ticket_available"))
                {
                    System.out.println("going");
                    System.out.println(event_key_values.get(i));
                    System.out.println(event_values.get(i));
                    e_ticket_available.add((Long)event_values.get(i));
                    System.out.println("e_ticket_available:"+e_ticket_available);
                }
                else if (event_key_values.get(i).equals("e_time"))
                {
                    System.out.println("going");
                    System.out.println(event_key_values.get(i));
                    System.out.println(event_values.get(i));
                    e_time.add((String)event_values.get(i));
                    System.out.println("e_time:"+e_time);
                }
                else if (event_key_values.get(i).equals("e_venue"))
                {
                    System.out.println("going");
                    System.out.println(event_key_values.get(i));
                    System.out.println(event_values.get(i));
                    e_venue.add((String)event_values.get(i));
                    System.out.println("e_venue:"+e_venue);
                }
                else if (event_key_values.get(i).equals("eventID"))
                {
                    System.out.println("going");
                    System.out.println(event_key_values.get(i));
                    System.out.println(event_values.get(i));
                    eventID.add((Long)event_values.get(i));
                    System.out.println("eventID:"+eventID);
                }
            }
        }
        System.out.println("e_address:"+e_address);
        System.out.println("event_values:"+event_key_values);
        System.out.println("event_values:"+event_values);

/*        viewHolder.event_image.setImageResource(event_Image[position]);
        viewHolder.event_date.setText(event_date[position]);
        viewHolder.event_title.setText(event_title[position]);
        viewHolder.event_city.setText(event_place_city[position]);
        viewHolder.category_name.setText(category_name[position]);*/

        viewHolder.event_image.setImageResource(event_Image[position]);
        viewHolder.event_date.setText(e_date.get(position));
        viewHolder.event_title.setText("Event Name: "+e_name.get(position));
        viewHolder.event_city.setText("Place: "+e_location.get(position));
        viewHolder.category_name.setText("Category: "+e_category.get(position));
        viewHolder.event_ticket_cost.setText("Price: "+e_price.get(position));
        viewHolder.event_ID.setText("Event ID: "+eventID.get(position).toString());
        viewHolder.event_ticket_available.setText("Tickets Available:"+e_ticket_available.get(position).toString());

    }


    @Override
    public int getItemCount() {
        System.out.println("check4");
        e_location = new ArrayList<>();
        final FireApp fireApp = (FireApp)getApplicationContext();
        e_dummy = fireApp.getEventShortListedForYou();
        System.out.println("e_location.size:"+e_dummy.size());
        return e_dummy.size();
    }
}
