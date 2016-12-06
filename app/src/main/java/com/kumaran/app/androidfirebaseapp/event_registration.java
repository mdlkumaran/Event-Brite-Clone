package com.kumaran.app.androidfirebaseapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class event_registration extends AppCompatActivity {
    FireApp fireApp;
    TextView ticket_price;
    NumberPicker qty_picker = null;
    EditText last_name;
    EditText first_name;
    Button btn_register_tickets;
    ArrayList<Object> event_details;
    ArrayList<String> event_key_name;
    String first_registred_name = "";
    String last_registerd_name = "";
    int qty_chosen = -1;
    DatabaseReference dRef;
    private String username;
    String event_id;
    int ticket_sold;
    int max_qty;
    String event_tic_date;
    String  event_tic_location;
    String event_tic_time;
    String  event_tic_name;
    String event_tic_venue;
    String event_tic_eventID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_registration);
        fireApp = (FireApp)getApplicationContext();
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_custom_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("Event Registration");
        my_toolbar.setBackgroundColor(Color.parseColor("#fff17a0a"));
        event_key_name = fireApp.getEvent_key_name();
        ticket_price =(TextView)findViewById(R.id.event_ticket_price);
        qty_picker = (NumberPicker) findViewById(R.id.qty_number_picker);
        first_name = (EditText)findViewById(R.id.event_regiter_first_name);
        last_name = (EditText)findViewById(R.id.event_register_lastname);
        btn_register_tickets = (Button)findViewById(R.id.registerTicketsButton);

        event_details = fireApp.getEvent_details();
        max_qty = -1;
        String price = "default";
        for(int i=0; i<event_details.size();i++)
        {
            if(event_key_name.get(i).equals("e_ticket_available"))
            {
                String max_qty_string = (String.valueOf(event_details.get(i)));
                max_qty = Integer.valueOf(max_qty_string);
            }
            else if(event_key_name.get(i).equals("e_price"))
            {
                price = (String.valueOf(event_details.get(i)));
            }
            else if(event_key_name.get(i).equals("eventID"))
            {
                event_id = (String.valueOf(event_details.get(i)));
            }
            else if(event_key_name.get(i).equals("e_ticket_sold"))
            {
                String ticket_sold_string = (String.valueOf(event_details.get(i)));
                ticket_sold = Integer.valueOf(ticket_sold_string);
            }
        }
        qty_picker.setMinValue(1);
        qty_picker.setMaxValue(max_qty);
        ticket_price.setText("Ticket Price: ($)"+price);


        btn_register_tickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_registred_name = first_name.getText().toString();
                last_registerd_name = last_name.getText().toString();
                qty_chosen = qty_picker.getValue();
                username = fireApp.getUsername();
                System.out.println("first_registred_name: "+first_registred_name);
                System.out.println("first_registred_name:"+last_registerd_name);
                System.out.println("qty_chosen:"+qty_chosen);

                int new_ticket_sold = ticket_sold + qty_chosen;
                int new_ticket_available = max_qty - qty_chosen;

                dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/event/event"+event_id);
                dRef.child("e_ticket_available").setValue(new_ticket_available);
                dRef.child("e_ticket_sold").setValue(new_ticket_sold);

                dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/Users/"+username + "/tickets");
                dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int ticketCount = (int)dataSnapshot.getChildrenCount()+1;
                        for(int i=0; i<event_details.size();i++)
                        {
                            if(event_key_name.get(i).equals("e_date"))
                            {
                                event_tic_date = (String.valueOf(event_details.get(i)));
                                System.out.println(event_tic_date);
                            }
                            else if(event_key_name.get(i).equals("e_location"))
                            {
                                event_tic_location = (String.valueOf(event_details.get(i)));
                                System.out.println(event_tic_location);
                            }
                            else if(event_key_name.get(i).equals("e_name"))
                            {
                                event_tic_name = (String.valueOf(event_details.get(i)));
                                System.out.println(event_tic_name);
                            }
                            else if(event_key_name.get(i).equals("e_time"))
                            {
                                event_tic_time = (String.valueOf(event_details.get(i)));
                                System.out.println(event_tic_time);
                            }
                            else if(event_key_name.get(i).equals("e_venue"))
                            {
                                event_tic_venue = (String.valueOf(event_details.get(i)));
                                System.out.println(event_tic_venue);
                            }
                            else if(event_key_name.get(i).equals("eventID"))
                            {
                                event_tic_eventID = (String.valueOf(event_details.get(i)));
                                System.out.println(event_tic_eventID);
                            }
                        }
                        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        Date today_date = new Date();
                        dRef=dRef.child("ticket"+ticketCount);
                        dRef.child("e_date").setValue(event_tic_date);
                        dRef.child("eventID").setValue(event_tic_eventID);
                        dRef.child("e_location").setValue(event_tic_location);
                        dRef.child("e_name").setValue(event_tic_name);
                        dRef.child("e_time").setValue(event_tic_time);
                        dRef.child("e_time").setValue(event_tic_time);
                        dRef.child("Ticket purchased").setValue(String.valueOf(qty_chosen));
                        dRef.child("Ticket purchased_date").setValue(dateFormat.format(today_date));
                        dRef.child("e_venue").setValue(event_tic_venue).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Intent intent = new Intent(getBaseContext(),Tickets.class);
                                    intent.putExtra("qty_chosen", qty_chosen);
                                    intent.putExtra("event_name", event_tic_name);
                                    intent.putExtra("event_time_date" , event_tic_time + " " + event_tic_date);
                                    intent.putExtra("event_location" , event_tic_location);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        menu.add(0, 0, 0, "Change Location");
        menu.add(0, 1, 0, "Personalize");
        menu.add(0, 2, 0, "DashBoard");
        menu.add(0, 3, 0, "Event List For You");
        menu.add(0, 4, 0, "User Profile");
        menu.add(0, 5, 0, "LogOut");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                startActivity(new Intent(getBaseContext(), location_selection.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case 1:
                startActivity(new Intent(getBaseContext(), personalize_interest.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case 2:
                startActivity(new Intent(getBaseContext(), EventRecyclerView.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case 3:
                startActivity(new Intent(getBaseContext(), event_list_you_list.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case 4:
                startActivity(new Intent(getBaseContext(), UserProfile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case 5:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                            }
                        });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
